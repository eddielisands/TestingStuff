package com.masterofcode.android.magreader;


import java.io.File;
import java.net.URL;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.fragments.IssueDetailsFragment;
import com.masterofcode.android.magreader.fragments.IssueDetailsFragment.OnIssueBuyNowBtnClickedListener;
import com.masterofcode.android.magreader.fragments.ShopGalleryForViewFragment;
import com.masterofcode.android.magreader.fragments.ShopGridForViewFragment;
import com.masterofcode.android.magreader.inapp.BillingHelper;
import com.masterofcode.android.magreader.inapp.Consts;
import com.masterofcode.android.magreader.inapp.PurchasedManager;
import com.masterofcode.android.magreader.inapp.util.IabHelper;
import com.masterofcode.android.magreader.inapp.util.IabResult;
import com.masterofcode.android.magreader.inapp.util.Inventory;
import com.masterofcode.android.magreader.inapp.util.Purchase;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.service.GetIssuesService;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.DownloadManager;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class MainShopActivity extends Activity implements OnIssueBuyNowBtnClickedListener {

	/**
	 * The SharedPreferences key for recording whether we initialized the
	 * database.  If false, then we perform a RestoreTransactions request
	 * to get all the purchases for this user.
	 */
	private static final String AVAIL_CONNECT_TO_BILLING = "cannot_connect_to_billing";
	private static final String BILLING_IS_SUPPORTED = "billing_is_supported";

	private static final String DB_INITIALIZED = "db_initialized";

	private static final String TAG = "Billing";
	private static final int DIALOG_CANNOT_CONNECT_ID = 1;
	public static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
	
	// (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

	private static Context mContext;
	private int mCurrentItemPosition = -1;
	public static boolean isClicedItem = false;
	private View detailsFramePort;
	private View  detailsFrameLand;
	private ActiveRecordBase _db;
	private LinearLayout shoploadingLayout;
	private ShopGridForViewFragment shopGridForViewFragment;

	private boolean	isSubscripted;
	private String	subscriptionName;
	private String	subscriptionPassword;
	
	private IssueItem mIssue;

	static class Self {

		public Self() {
		}
		public GetIssuesService mBoundIssuesService;
		public ServiceConnection updateIssuesConnection;
	}

	Self _self = new Self();

	private Handler mHandler;

	private IabHelper mIabHelper;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_view);
		this.mContext = this;
		ActionBar actionBar = getActionBar();

		shoploadingLayout = (LinearLayout) findViewById(R.id.shoploadingLayout);

		subscriptionName = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
		subscriptionPassword = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, null);
		isSubscripted = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);


		if(!ApplicationUtils.getPreferences(mContext).contains(BILLING_IS_SUPPORTED)){
			ApplicationUtils.setPrefProperty(mContext, BILLING_IS_SUPPORTED, true);
		}

		if(!ApplicationUtils.getPreferences(mContext).contains(AVAIL_CONNECT_TO_BILLING)){
			ApplicationUtils.setPrefProperty(mContext, AVAIL_CONNECT_TO_BILLING, true);
		}

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			actionBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R.array.menu_categories,
					android.R.layout.simple_dropdown_item_1line), navigationListener);
			ActionBarView.setActionBarListView(actionBar, mContext);
			actionBar.setSelectedNavigationItem(2);
		} else {
			actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_feeds).setTabListener(feedsTabListener), false);
			actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_library).setTabListener(libraryTabListener), false);
			actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_shop).setTabListener(shopTabListener), true);
			actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_bookmarks).setTabListener(bookmarkTabListener), false);
			ActionBarView.setActionBarTabView(actionBar, mContext);
		}

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("curPosition")) {
				setmCurrentItemPosition(savedInstanceState.getInt("curPosition"));
			}
			if (savedInstanceState.containsKey("isClicedItem")){
				setClicedItem(savedInstanceState.getBoolean("isClicedItem"));
			}
		}
		mHandler = new Handler();
		setupIabHelper();
		BillingHelper.instantiateHelper(getBaseContext(), mIabHelper);
	}

	private void setupIabHelper() {
		/* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
		 * (that you got from the Google Play developer console). This is not your
		 * developer public key, it's the *app-specific* public key.
		 *
		 * Instead of just storing the entire literal string here embedded in the
		 * program,  construct the key at runtime from pieces or
		 * use bit manipulation (for example, XOR with some other string) to hide
		 * the actual key.  The key itself is not secret information, but we don't
		 * want to make it easy for an attacker to replace the public key with one
		 * of their own and then fake messages from the server.
		 */
		String base64EncodedPublicKey = getString(R.string.app_purchase_id);

		// Some sanity checks to see if the developer (that's you!) really followed the
		// instructions to run this sample (don't put these checks on your app!)
		if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
			throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
		}
		if (getPackageName().startsWith("com.example")) {
			throw new RuntimeException("Please change the sample's package name! See README.");
		}

		// Create the helper, passing it our context and the public key to verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mIabHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mIabHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					//                    complain("Problem setting up in-app billing: " + result);
					if (ApplicationUtils.getPrefProperty(mContext, AVAIL_CONNECT_TO_BILLING))
						showDialog(DIALOG_CANNOT_CONNECT_ID);
					ApplicationUtils.setPrefProperty(mContext, AVAIL_CONNECT_TO_BILLING, false);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mIabHelper == null) return;

				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				//                mHelper.queryInventoryAsync(mGotInventoryListener);
				ApplicationUtils.setPrefProperty(mContext, AVAIL_CONNECT_TO_BILLING, true);
				if(ApplicationUtils.isOnline(MainShopActivity.this)){
					new Thread(new Runnable() {
						public void run() {
							if (ApplicationUtils.checkIssueIsAvail()){
								startIssuesService();
								// set up service
								doInitUpdateConnection();
							} else {
								MainShopActivity.this.runOnUiThread(new Runnable() {
									public void run()
									{
										ApplicationUtils.createNoInternetDialog(MainShopActivity.this).show();
									}
								});
							}

						}
					}).start();

				} else {
					ApplicationUtils.createNoInternetDialog(MainShopActivity.this).show();
				}
			}
		});
	}


	/**
	 * If the database has not been initialized, we send a
	 * RESTORE_TRANSACTIONS request to Android Market to get the list of purchased items
	 * for this user. This happens if the application has just been installed
	 * or the user wiped data. We do not want to do this on every startup, rather, we want to do
	 * only when the database needs to be initialized.
	 */
	private void restoreDatabase() {
		boolean initialized = ApplicationUtils.getPrefProperty(MainShopActivity.this, DB_INITIALIZED);
		if (!initialized) {
			//        	mBillingService.restoreTransactions();
			mIabHelper.queryInventoryAsync(restoreDatabaseListener);
			Toast.makeText(this, R.string.restoring_transactions, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Called when this activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * Called when this activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		doUnbindService();
		//        mBillingService.unbind();
		if (mIabHelper != null) {
			mIabHelper.dispose();
			mIabHelper = null;
		}
		setClicedItem(false);
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		//new DownloadManager(mContext).downloadCoversPurchasedIssues();

		subscriptionName = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
		subscriptionPassword = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, null);
		isSubscripted = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
	}

	protected void onResume()
	{
		super.onResume();
		detailsFrameLand = findViewById(R.id.issue_details_container);
		detailsFramePort = findViewById(R.id.issue_details_container_port);
		if(Constants.Debug)
			Log.d("debug", "Filter was registered");
		if(getmCurrentItemPosition() != -1 && isClicedItem ){
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
				IssueDetailsFragment issueDetailsFragment = (IssueDetailsFragment) getFragmentManager().findFragmentById(R.id.issue_details_fragment);
				issueDetailsFragment = IssueDetailsFragment.newInstance(getmCurrentItemPosition());
				detailsFramePort = findViewById(R.id.issue_details_container_port);
				detailsFramePort.setVisibility(View.VISIBLE);
				ShopGalleryForViewFragment shopGalleryForViewFragment = (ShopGalleryForViewFragment)getFragmentManager().findFragmentById(R.id.issue_details_gallery_fragment);
				shopGalleryForViewFragment = ShopGalleryForViewFragment.newInstance(getmCurrentItemPosition());
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.issue_container_port, shopGalleryForViewFragment);
				ft.replace(R.id.issue_details_container_port, issueDetailsFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				IssueDetailsFragment issueDetailsFragment = (IssueDetailsFragment) getFragmentManager().findFragmentById(R.id.issue_details_fragment);
				issueDetailsFragment = IssueDetailsFragment.newInstance(getmCurrentItemPosition());
				detailsFrameLand = findViewById(R.id.issue_details_container);
				detailsFrameLand.setVisibility(View.VISIBLE);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.issue_details_container, issueDetailsFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && getmCurrentItemPosition() == -1){
			setDefaultView();
		}

		// subscription related
		String newSubscriptionName = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
		String newSubscriptionPassword = ApplicationUtils.getPrefPropertyString(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_PASSWORD, null);
		boolean newIsSubscripted = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);

		boolean isNeedReload = false;

		if(isSubscripted!=newIsSubscripted) // changed status
		{
			isNeedReload = true;
		} else {
			if(isSubscripted)		// changed name or password
			{
				if(!subscriptionName.equals(newSubscriptionName) || !subscriptionPassword.equals(newSubscriptionPassword))
				{
					isNeedReload = true;
				}
			}
		}

		if(isNeedReload)
		{
			finish();
			startActivity(new Intent(this, MainShopActivity.class));
		}
	}

	private void setDefaultView(){
		shopGridForViewFragment = (ShopGridForViewFragment) getFragmentManager().findFragmentById(R.id.shop_grid_for_view_fragment);
		shopGridForViewFragment = new ShopGridForViewFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.issue_container_port, shopGridForViewFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curPosition", getmCurrentItemPosition());
		outState.putBoolean("isClicedItem", isClicedItem);
	}

	public static class BroadcastListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(Constants.UPDATE_ISSUE)) {
				ActiveRecordBase database = ((JtjApplication) ((MainShopActivity) mContext).getApplication()).getDatabase();
				List<IssueItem> mIssues = null;
				try {
					database.open();
					mIssues = database.findAll(IssueItem.class);
				} catch (ActiveRecordException e) {
					e.printStackTrace();
				}
				ShopGridForViewFragment shopGridForViewFragment = (ShopGridForViewFragment) ((MainShopActivity) mContext).getFragmentManager().findFragmentById(R.id.shop_grid_for_view_fragment);
				DownloadManager dm = new DownloadManager(mContext);
				dm.downloadCoversPurchasedIssues();
				if (shopGridForViewFragment != null)
					shopGridForViewFragment.getIssueList();
			} else {
				if (action.equals(Constants.SUBSCRIPTION_PASSWORD_IS_INCORRECT))
				{
					((MainShopActivity) mContext).authFailed();
				} else {
					if (action.equals(Constants.UPDATING_ISSUES_IO_ERROR))
					{
						((MainShopActivity) mContext).authCauseIOError();
					}
				}
			}
		}

	}

	public void authCauseIOError()
	{
		DialogFragment newFragment = SignInErrorDialogFragment.newInstance(getString(R.string.shop_auth_dialog_title_auth_ioerror));
		newFragment.show(getFragmentManager(), "signin_error_dialog");
	}

	public void authFailed()
	{
		DialogFragment newFragment = SignInErrorDialogFragment.newInstance(getString(R.string.shop_auth_dialog_title_auth_failed));
		newFragment.show(getFragmentManager(), "signin_error_dialog");
	}

	void doInitUpdateConnection(){
		if (_self.updateIssuesConnection == null) {
			_self.updateIssuesConnection = new ServiceConnection() {
				public void onServiceConnected(ComponentName className,
						IBinder service) {
					// This is called when the connection with the service has
					// been
					// established, giving us the service object we can use to
					// interact with the service. Because we have bound to a
					// explicit
					// service that we know is running in our own process, we
					// can
					// cast its IBinder to a concrete class and directly access
					// it.
					_self.mBoundIssuesService = ((GetIssuesService.Controller) service).getService();
					// Refresh UI
				}

				public void onServiceDisconnected(ComponentName className) {
					// This is called when the connection with the service has
					// been
					// unexpectedly disconnected -- that is, its process
					// crashed.
					// Because it is running in our same process, we should
					// never
					// see this happen.
					_self.mBoundIssuesService = null;
				}
			};
		}
	}

	void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		if (_self.updateIssuesConnection == null)
			doInitUpdateConnection();
		bindService(new Intent(MainShopActivity.this, GetIssuesService.class), _self.updateIssuesConnection, Context.BIND_AUTO_CREATE);
	}

	void doUnbindService() {
		if (_self.mBoundIssuesService != null) {
			// Detach our existing connection.
			try {
				unbindService(_self.updateIssuesConnection);
			} catch (IllegalArgumentException e) {
				Log.e(Constants.LOG_BS, "unbindService failed!", e);
			}

			_self.mBoundIssuesService = null;
		}
	}

	private void startIssuesService(){
		if (GetIssuesService.getInstance() != null) {
			doBindService();
		} else {
			startService(new Intent(mContext, GetIssuesService.class));
			doBindService();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CANNOT_CONNECT_ID:
			return createDialog(R.string.cannot_connect_title,
					R.string.cannot_connect_message);
		case DIALOG_BILLING_NOT_SUPPORTED_ID:
			return createDialog(R.string.billing_not_supported_title,
					R.string.billing_not_supported_message);
		default:
			return null;
		}
	}

	private Dialog createDialog(int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId)
		.setIcon(android.R.drawable.stat_sys_warning)
		.setMessage(messageId)
		.setCancelable(false)
		.setPositiveButton(android.R.string.ok, null)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder.create();
	}

	TabListener feedsTabListener = new TabListener() {	//for this tab we have empty tabListener
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Intent feedIntent = new Intent();
			feedIntent.setClass(mContext, MainActivity.class);
			startActivity(feedIntent);
			finish();			
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};


	public void downloadIssue(String itemId){
		try {
			_db = ((JtjApplication)MainShopActivity.this.getApplication()).getDatabase();
			_db.open();
			List<IssueItem> issueToBuy = _db.find(IssueItem.class, "GOOGLECHECKOUTID=?", new String[] {itemId} );
			if(!issueToBuy.isEmpty()){
				issueToBuy.get(0).isBuyed = true;
				issueToBuy.get(0).update();
				DownloadManager dm = new DownloadManager(MainShopActivity.this);
				dm.startDownloadCover(issueToBuy.get(0).detailCoverUrl);
				if (Constants.Debug)
					Log.d("shop_progress", "in onPurchaseStateChange() issueToBuy.get(0).downloadUrl: " + issueToBuy.get(0).downloadUrl + " issueToBuy.get(0).detailCoverUrl: " + issueToBuy.get(0).detailCoverUrl);
				String destinationFilePath = LibraryManager.GetInstance().libraryPath(mContext) + File.separator + ApplicationUtils.getEpubFileNameFromUrl(new URL(issueToBuy.get(0).downloadUrl));
				boolean isDownloaded = false;
				String coverDestinationFilePath = LibraryManager.GetInstance().libraryPath(mContext) + File.separator + DownloadManager.getCoverFileNameFromUrl(issueToBuy.get(0).detailCoverUrl);
				String newLibItemsTitle = ApplicationUtils.formatDateForIssueDescr(issueToBuy.get(0).publicationDate);
				if ((issueToBuy.get(0).title != null) && (issueToBuy.get(0).title != "")) {
					newLibItemsTitle = issueToBuy.get(0).title;
				}
				LibraryManager.GetInstance().addNewMagazine(mContext, destinationFilePath, coverDestinationFilePath, newLibItemsTitle, issueToBuy.get(0).downloadUrl, isDownloaded, Constants.MAGAZINE_TYPE_NORMAL, issueToBuy.get(0).issueID, issueToBuy.get(0).googlecheckoutid);
				if(issueToBuy.get(0).downloadable)
					PurchasedManager.GetInstance().insertOrder(mContext,"", issueToBuy.get(0).googlecheckoutid, Consts.PurchaseState.PURCHASED, 0, "");
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					ShopGalleryForViewFragment.getInstance().getIssueAdapter().notifyDataSetChanged();
				} else {
					ShopGridForViewFragment.getInstance().getIssueAdapter().notifyDataSetChanged();
				}
				IssueDetailsFragment.getInstance().setissueBuyNowBtnTitle("DOWNLOAD");
			}
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shop_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_shop_settings) {
			startActivity(new Intent(MainShopActivity.this, SettingsActivity.class));
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	OnNavigationListener navigationListener = new OnNavigationListener() {

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			switch (itemPosition) {
			case 0:
				Intent feedIntent = new Intent();
				feedIntent.setClass(mContext, MainActivity.class);
				startActivity(feedIntent);
				finish();
				break;
			case 1:
				Intent libraryIntent = new Intent();
				libraryIntent.setClass(mContext, MainLibraryActivity.class);
				startActivity(libraryIntent);
				finish();
				break;
			case 3:
				Intent bookmarkIntent = new Intent();
				bookmarkIntent.setClass(mContext, MainBookmarkActivity.class);
				startActivity(bookmarkIntent);
				finish();
				break;
			}
			return false;
		}
	};

	TabListener libraryTabListener = new TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Intent libraryIntent = new Intent();
			libraryIntent.setClass(mContext, MainLibraryActivity.class);
			startActivity(libraryIntent);
			finish();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};

	TabListener shopTabListener = new TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};

	TabListener bookmarkTabListener = new TabListener() {

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Intent bookmarkIntent = new Intent();
			bookmarkIntent.setClass(mContext, MainBookmarkActivity.class);
			startActivity(bookmarkIntent);
			finish();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};

	public int getmCurrentItemPosition() {
		return mCurrentItemPosition;
	}

	public void setmCurrentItemPosition(int mCurrentItemPosition) {
		this.mCurrentItemPosition = mCurrentItemPosition;
	}

	public boolean isClicedItem() {
		return isClicedItem;
	}

	public void setClicedItem(boolean isClicedItem) {
		MainShopActivity.isClicedItem = isClicedItem;
	}

	public static class SignInErrorDialogFragment extends DialogFragment {
		private final static String			TITLE_KEY = "reason_is_ioerror";

		public static SignInErrorDialogFragment newInstance(String title) {
			SignInErrorDialogFragment frag = new SignInErrorDialogFragment();
			Bundle args = new Bundle();
			args.putString(TITLE_KEY, title);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String title = getArguments().getString(TITLE_KEY);

			return new AlertDialog.Builder(getActivity())
			.setTitle(title)
			.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			}
					)
					.create();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mCurrentItemPosition != -1) {
			mCurrentItemPosition = -1;
			if (detailsFrameLand != null){
				detailsFrameLand.setVisibility(View.GONE);
				//detailsFrameLand = null;
			} else if (detailsFramePort != null){
				detailsFramePort.setVisibility(View.GONE);
				//detailsFramePort = null;
				setDefaultView();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener restoreDatabaseListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mIabHelper == null) return;

			// Is it a failure?
			if (result.isFailure()) {
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			if (result.isSuccess()) {
				if (Consts.DEBUG) {
					Log.d(TAG, "completed RestoreTransactions request");
				}
				// Update the shared preferences so that we don't perform
				// a RestoreTransactions again.
				ApplicationUtils.setPrefProperty(MainShopActivity.this, DB_INITIALIZED, true);
			} else {
				if (Consts.DEBUG) {
					Log.d(TAG, "RestoreTransactions error: " + result.getMessage());
				}
			}
		}
	};
	
	IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mIabHelper == null) return;

            if (result.isFailure()) {
                return;
            }
            
            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equalsIgnoreCase(mIssue.id)) {
                mIabHelper.consumeAsync(purchase, consumeFinishedListener);
			}
        }
    };
    
 // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener consumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mIabHelper == null) return;

            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we download issue
                Log.d(TAG, "Consumption successful. Provisioning.");
                downloadIssue(mIssue.id);
            }
        }
    };

	@Override
	public void onIssueBuyNowBtnClicked(IssueItem issue) {
		mIssue = issue;
		mIabHelper.launchPurchaseFlow(this, mIssue.appleAppStorePriceLevel, RC_REQUEST, purchaseFinishedListener, "payloadhere");
	}
}
