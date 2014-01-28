package com.masterofcode.android.magreader.fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.masterofcode.android.magreader.EpubViewerActivity;
import com.masterofcode.android.magreader.MainShopActivity;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageLoader;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class IssueDetailsFragment extends Fragment {

	OnIssueBuyNowBtnClickedListener listener;

	private IssueItem issueItem;
	private IssueItem mIssue;
	private ActiveRecordBase _db;
	private Button issueBuyNowBtn;
	private static IssueDetailsFragment Instance = null;

	public static IssueDetailsFragment getInstance(){
		return Instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mIssue = _db.find(IssueItem.class, false, "_id>?", new String[] { String.valueOf("0") }, null, null, "PUBLICATION_DATE DESC", null).get(getShownIndex());
		mIssue = generateIssuesList().get(getShownIndex());
		Log.d("purchase_test", "mIssue: " + mIssue.googlecheckoutid +" IssueDetailsFragment.onCreate(): " + getShownIndex());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//container.removeAllViewsInLayout();
		//container.removeAllViews();
		View mView = (View)inflater.inflate(R.layout.shop_issue_detail_layout, null);

		ScrollView issueItemView = (ScrollView) mView.findViewById(R.id.viewOfIssueItem);
		issueItemView.setVisibility(View.INVISIBLE);
		issueItemView.scrollTo(0, 0);
		LinearLayout loading = (LinearLayout) mView.findViewById(R.id.loadingIssueDetailLayout);
		loading.setVisibility(View.VISIBLE);

		TextView issueTitle = (TextView)mView.findViewById(R.id.issue_title);
		if ((mIssue.title != null) && (mIssue.title != "")) {
			issueTitle.setText(mIssue.title);
		}

		ImageView issueDetailImage = (ImageView)mView.findViewById(R.id.issueDetailsImage);
		if(ImageLoader.getImageMap().containsKey(mIssue.detailCoverUrl))
			issueDetailImage.setImageBitmap(ImageLoader.getImageMap().get(mIssue.detailCoverUrl));
		WebView issueSummary = (WebView)mView.findViewById(R.id.issue_summary);

		if ((mIssue.summary != null) && (mIssue.summary != "")) {
			String text_in_html = "<html><body>" + mIssue.summary + "</body></html>";
			issueSummary.loadData(text_in_html, "text/html", null);
		} else if ((mIssue.shopDetailTextUrlText != null) && (mIssue.shopDetailTextUrlText != "")) {
			Log.i("MagazinReader" , mIssue.shopDetailTextUrlText);
			// OK.. if shopDetailTextUrlText is not null and if summary on issue is null we should load something here
			new DownloadIssueShopDetailsTextAsync(issueSummary).execute(mIssue.shopDetailTextUrlText);
		}
		TextView issuePublicationDate = (TextView)mView.findViewById(R.id.issue_publication_date);
		//issuePublicationDate.setText(ApplicationUtils.formatDateForIssueDetail(mIssue.publicationDate));
		issuePublicationDate.setText(ApplicationUtils.getPublicationDateIssueDetails(mIssue.publicationDate));
		TextView issuePrice = (TextView)mView.findViewById(R.id.issue_price);
		issueBuyNowBtn = (Button)mView.findViewById(R.id.issue_buttonBuyNow);
		if(!mIssue.isBuyed){
			if(mIssue.downloadable){
				issuePrice.setVisibility(View.GONE);
				issueBuyNowBtn.setText("FREE");
			} else {
				issuePrice.setText("€" + mIssue.androidreadergooglecheckoutprice);
			}
		} else {
			issuePrice.setVisibility(View.GONE);
			if(!mIssue.isDownloaded){
				issueBuyNowBtn.setText("DOWNLOAD");
			} else {
				issueBuyNowBtn.setText("READ");
			}
		}
		issueBuyNowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub com.sandsmedia.content.jtj4ipad.2010.01
				//PurchasedManager.GetInstance().insertOrder(getActivity().getApplicationContext(), "com.sandsmedia.content.jtj4ipad.2010.01", "com.sandsmedia.content.jtj4ipad.2010.01", Consts.PurchaseState.PURCHASED, 12, "payload");
				if(!TextUtils.isEmpty(getGoogleAccount()))
				{
					if(!mIssue.downloadable)
					{	if (!mIssue.isBuyed){
						//Eddie Li
//							if (BillingHelper.getBillingService().requestPurchase(mIssue.googlecheckoutid, "payloadhere"))
						if (listener != null);
						{
							listener.onIssueBuyNowBtnClicked(mIssue);
							Log.d("purchase_test", "onClick: " + mIssue.googlecheckoutid);
						}
					} else if(mIssue.isBuyed && !mIssue.isDownloaded){
						LibraryItem mLibraryItem = LibraryManager.GetInstance().getLibraryItemByGoogleCheckoutId(mIssue.googlecheckoutid);
						createDialog(R.string.download_issue_dialog_title, R.string.download_issue_dialog_message, android.R.drawable.stat_sys_download, mLibraryItem).show();
					} else if(mIssue.isBuyed && mIssue.isDownloaded){
						LibraryItem mLibraryItem = LibraryManager.GetInstance().getLibraryItemByGoogleCheckoutId(mIssue.googlecheckoutid);
						openIssue(mLibraryItem);
					}
					} else {
						if (!mIssue.isBuyed){
							((MainShopActivity)getActivity()).downloadIssue(mIssue.googlecheckoutid);
						} else if(mIssue.isBuyed && !mIssue.isDownloaded){
							LibraryItem mLibraryItem = LibraryManager.GetInstance().getLibraryItemByGoogleCheckoutId(mIssue.googlecheckoutid);
							createDialog(R.string.download_issue_dialog_title, R.string.download_issue_dialog_message, android.R.drawable.stat_sys_download, mLibraryItem).show();
						} else if(mIssue.isBuyed && mIssue.isDownloaded){
							LibraryItem mLibraryItem = LibraryManager.GetInstance().getLibraryItemByGoogleCheckoutId(mIssue.googlecheckoutid);
							openIssue(mLibraryItem);
						}
					}
				} else {
					if(mIssue.downloadable)
					{
						((MainShopActivity)getActivity()).downloadIssue(mIssue.googlecheckoutid);
					}
				}
			}
		});
						issueItemView.setVisibility(View.VISIBLE);
						loading.setVisibility(View.GONE);
						Instance = this;		
						return mView;
						//return inflater.inflate(R.layout.shop_issue_detail_layout, null);
	}

	private void openIssue(LibraryItem mLibraryItem){
		Intent          intent = new Intent(getActivity(), EpubViewerActivity.class);
		intent.putExtra(Constants.BUNDLE_KEY_EPUB_FILE_PATH, mLibraryItem.magazine_filepath);
		intent.putExtra(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH, mLibraryItem.magazine_cover_filepath);
		startActivity(intent);
	}

	private Dialog createDialog(final int dialogTitle, final int dialogMessage, final int dialogIcon, final LibraryItem mLibraryItem) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle((getActivity()).getString(dialogTitle))
		.setIcon(dialogIcon)
		.setMessage((getActivity()).getString(dialogMessage))
		.setCancelable(false)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(mLibraryItem.magazine_type ==  Constants.MAGAZINE_TYPE_NORMAL)	{
					if(ApplicationUtils.isOnline(getActivity())){
						LibraryManager.GetInstance().downloadMagazine(getActivity(), mLibraryItem);
					} else {
						ApplicationUtils.createNoInternetDialog(getActivity()).show();
					}
				}
				if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					ShopGalleryForViewFragment.getInstance().getIssueAdapter().notifyDataSetChanged();
				} else {
					ShopGridForViewFragment.getInstance().getIssueAdapter().notifyDataSetChanged();
				}

				issueBuyNowBtn.setText("READ");
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return builder.create();
	}

	/**
	 * Asynctask for downloading the issues details from an url
	 * @author Robert Munsky
	 *
	 */
	class DownloadIssueShopDetailsTextAsync extends AsyncTask<String, String, String> {
		private WebView detailsWebView;
		private String text = "";

		/**
		 * Constructor
		 * @param webView the view to place the text in
		 */
		protected DownloadIssueShopDetailsTextAsync(WebView webView) {
			detailsWebView = webView;
		}


		@Override
		protected String doInBackground(String... aurl) {
			BufferedReader in = null;
			try {
				URL url = new URL(aurl[0]);
				HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
				String authString = Constants.TECH_USER_LOGIN + ":" + Constants.TECH_USER_PASSWORD;
				byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT );
				String authStringEnc = new String(authEncBytes);
				conexion.setRequestProperty("Authorization", "Basic " + authStringEnc);
				in = new BufferedReader(new InputStreamReader(
						conexion.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					sb.append(inputLine);
				}
				if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
					text = sb.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			return text;
		}

		@Override
		protected void onPostExecute(String result) {
			if (detailsWebView != null) {
				//this we do not use since we changed the issue_summary from TextView to WebView type
				//detailsWebView.setText(Html.fromHtml(text));
				detailsWebView.loadData(text, "text/html", null);
			}
		}
	}

	public String getGoogleAccount(){
		AccountManager manager = AccountManager.get(getActivity());
		Account[] accounts = manager.getAccountsByType("com.google");
		if (accounts.length == 0) {
			return null;
		} else {
			return accounts[0].name;
		}
	}

	public static IssueDetailsFragment newInstance(int index) {
		IssueDetailsFragment f = new IssueDetailsFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	public void setissueBuyNowBtnTitle(String title){
		issueBuyNowBtn.setText(title);
	}

	private List<IssueItem> generateIssuesList(){
		ActiveRecordBase _db;
		List<IssueItem> mIssues = null;
		try {
			_db = ((JtjApplication)getActivity().getApplication()).getDatabase();
			_db.open();
			mIssues = _db.find(IssueItem.class, false, "_id>?", new String[] { String.valueOf("0") }, null, null, null, null);
			Log.d("purchase_test", "in generateArrayListToAdapter() mIssues size: " + mIssues.size());
		} catch (ActiveRecordException exc) {
			exc.printStackTrace();
		}
		return mIssues;
	}

	public interface OnIssueBuyNowBtnClickedListener {
		public void onIssueBuyNowBtnClicked(IssueItem issue);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnIssueBuyNowBtnClickedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnIssueBuyNowBtnClickedListener");
		}
	}
}