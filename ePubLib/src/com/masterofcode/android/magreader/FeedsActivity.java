package com.masterofcode.android.magreader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.bookmarks.BookmarksManager;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.fragments.FeedsListForViewFragment;
import com.masterofcode.android.magreader.fragments.FeedsViewFragment;
import com.masterofcode.android.magreader.share.fb.BaseDialogListener;
import com.masterofcode.android.magreader.share.fb.BaseRequestListener;
import com.masterofcode.android.magreader.share.fb.SampleAuthListener;
import com.masterofcode.android.magreader.share.fb.SampleLogoutListener;
import com.masterofcode.android.magreader.share.fb.SessionEvents;
import com.masterofcode.android.magreader.share.fb.SessionStore;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class FeedsActivity extends Activity {

	private Context mContext;
	private int mCurrentPosition = -1;
	private int mCurrentCategory = 0;
	private String mCurrentGuid;
	private String mSearchKeywords = null;
	private boolean isLoadData = false;
	private Facebook mFacebook;
	private Dialog fbDialog;
	private AsyncFacebookRunner mAsyncRunner;
	List<FeedItem> items;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_view);

		this.mContext = this;
		ActionBar actionBar = getActionBar();

		setCurrentCategory(getIntent().getIntExtra("category_id", 0));
		setCurrentGid(getIntent().getStringExtra("item_guid"));
		setmKeywords(getIntent().getStringExtra("search_keyword"));

		initFacebook();

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("curPosition")) {
				setCurrentPosition(savedInstanceState.getInt("curPosition"));
			} else {
				setCurrentPosition(0);
			}
			setCurrentGid(savedInstanceState.getString("curGuid"));
			setCurrentCategory(savedInstanceState.getInt("curCategory"));
			if (savedInstanceState.getBoolean("fbShare")){
				try {
					facebookShare(savedInstanceState.getString("feedTitle") + " " + savedInstanceState.getString("feedUrl"));
				} catch (Exception e) {

				}
			}
		}

		ActionBarView.setActionBarStandartView(actionBar, mContext);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new getItemsAsyncTask().execute(getCurrentCategory());
	}

	@Override
	protected void onPause() {
		super.onPause();		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curPosition", getCurrentPosition());
		outState.putInt("curCategory", getCurrentCategory());
		outState.putString("curGuid", getCurrentGuid());
		if (fbDialog != null && fbDialog.isShowing()){
			fbDialog.dismiss();
			FeedItem curFeed = items.get(mCurrentPosition);
			outState.putBoolean("fbShare", true);
			outState.putString("feedTitle", curFeed.title);
			outState.putString("feedUrl", curFeed.link);
		}
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void setCurrentPosition(int mCurrentPosition) {
		this.mCurrentPosition = mCurrentPosition;
		if(isLoadData)this.invalidateOptionsMenu();
	}

	public List<FeedItem> getItems() {
		return items;
	}

	private String getmKeywords() {
		return mSearchKeywords;
	}

	private void setmKeywords(String mKeywords) {
		this.mSearchKeywords = mKeywords;
	}

	public int getCurrentCategory() {
		return mCurrentCategory;
	}

	public void setCurrentCategory(int mCurrentCategory) {
		this.mCurrentCategory = mCurrentCategory;
	}

	public String getCurrentGuid() {
		return mCurrentGuid;
	}

	public void setCurrentGid(String mCurrentGuid) {
		this.mCurrentGuid = mCurrentGuid;
	}

	private class getItemsAsyncTask extends AsyncTask<Integer, Void, List<FeedItem>> {

		@Override
		protected List<FeedItem> doInBackground(Integer... params) {
			List<FeedItem> listFeeds = null;
			if (!isLoadData) {
				try {
					Integer curCategory = params[0];
					ActiveRecordBase _db = ((JtjApplication) ((FeedsActivity) mContext).getApplication()).getDatabase();
					if (!_db.isOpen()) _db.open();
					listFeeds = _db.find(FeedItem.class, false, "CHANNELID=?", new String[] { String.valueOf(curCategory) }, null, null, "PUBLICATIONDATE DESC", null);
					//_db.close();
					int curPos = findPositionByGid(listFeeds, getCurrentGuid());
					if (curPos != -1) setCurrentPosition(curPos);
					isLoadData = true;
					//_db.close();
					return listFeeds;
				} catch (ActiveRecordException e) {
					e.printStackTrace();
				}
			} else {
				return items;
			}
			return items;
		}

		@Override
		protected void onPostExecute(List<FeedItem> listFeeds) {
			super.onPostExecute(listFeeds);

			items = listFeeds;
			FrameLayout frl = (FrameLayout) findViewById(R.id.frameForListView);			
			if (frl.getVisibility() != View.GONE) {
				FeedsListForViewFragment fl = (FeedsListForViewFragment) getFragmentManager().findFragmentById(R.id.feed_list_for_view);
				fl.setItemsToList(items, getCurrentPosition(), getmKeywords());
				updateBookmarkingMenuItem(items.get(getCurrentPosition()).isBookmarked);
			} else {
				FeedsViewFragment fv = (FeedsViewFragment) getFragmentManager().findFragmentById(R.id.feed_view_fragment);
				fv.setViewItem(items.get(getCurrentPosition()), getmKeywords());
				updateBookmarkingMenuItem(items.get(getCurrentPosition()).isBookmarked);
			}

			updateBookmarkingMenuItem(true);
		}
	}

	private int findPositionByGid(List<FeedItem> listFeeds, String gid){
		for(FeedItem item : listFeeds){
			if (gid.equals(item.guid))
				return listFeeds.indexOf(item);
		}
		return -1;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.feed_view_menu, menu);

		// search settings
		final SearchView searchView = (SearchView) menu.findItem(R.id.menu_feed_search).getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setIconified(true);
				Intent		intent = new Intent(FeedsActivity.this, SearchActivity.class);

				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_KEYWORD, query);
				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_TYPE, Constants.SEARCH_TYPE_FEEDS);
				startActivity(intent);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		MenuItem item = menu.findItem(R.id.twitter_menu);
		if(findTwitterClient() == null){
			item.setVisible(false);
			item.setEnabled(false);
		} else {
			item.setVisible(true);
			item.setEnabled(true);
		}

		MenuItem bookmrarkItem = menu.findItem(R.id.menu_feed_bookmark);
		if(bookmrarkItem!=null)
		{
			if(items!=null)
			{
				FeedItem currentFeed = items.get(getCurrentPosition());	    	

				if(currentFeed != null)
				{
					bookmrarkItem.setVisible(!currentFeed.isBookmarked);
					bookmrarkItem.setEnabled(!currentFeed.isBookmarked);
				}
			}
		}
		return true;
	}

	public void updateBookmarkingMenuItem(boolean isBookmarked)
	{
		this.invalidateOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FeedItem curFeed;
		String url;
		curFeed = items.get(mCurrentPosition);
		url = curFeed.link;

		int itemId = item.getItemId();
		if (itemId == R.id.menu_feed_browser) {
			curFeed = items.get(mCurrentPosition);
			url = curFeed.link;
			startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(url)),Constants.REQUEST_CODE_VIEW_IN_BROWSER);
			return true;
		}
		else if (itemId == android.R.id.home) {
			finish();
			return true;
		}
		else if (itemId == R.id.menu_feed_bookmark) {
			FeedItem currentFeed = items.get(mCurrentPosition);
			BookmarksManager.GetInstance().BoomarkFeed(this, currentFeed);
			Toast.makeText(this, R.string.bookmarks_feed_added, Toast.LENGTH_SHORT).show();
			this.invalidateOptionsMenu();
			return true;
		}
		else if (itemId == R.id.fb_menu) {
			facebookShare(curFeed.title + " " + url);
			return true;
		}
		else if (itemId == R.id.twitter_menu) {
			twitterShare(curFeed.title + " " + url);
			return true;
		}
		else if (itemId == R.id.mail_menu) {
			mailShare(curFeed.title, curFeed.title + " " + url);
			return true;
		}
		else if (itemId == R.id.menu_feed_share) {
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	public ResolveInfo findTwitterClient() {
		final String[] twitterApps = {
				// package // name - nb installs (thousands)
				"com.twitter.android", // official - 10 000
				"com.twidroid", // twidroyd - 5 000
				"com.handmark.tweetcaster", // Tweecaster - 5 000
		"com.thedeck.android"}; // TweetDeck - 5 000 };
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		final PackageManager packageManager = getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
		for (int i = 0; i <twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return resolveInfo;
				}
			}
		}
		return null;
	}

	private void initFacebook(){

		//       	mFacebook = new Facebook(Constants.APP_ID);
		//Eddie Li id from string file
		mFacebook = new Facebook(getApplication().getString(R.string.app_id));
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);

		SessionStore.restore(mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());    	
	}

	private void facebookShare(String data){
		try {                	                	
			Bundle parameters = new Bundle();
			Configuration config = getBaseContext().getResources().getConfiguration();
			parameters.putString("locale", config.locale.toString());
			parameters.putString("message", data);
			parameters.putString("picture", "http://entwickler.com/magazin/eclipse/android-ecm-app-icon.png");
			parameters.putString("name", "Eclipse Magazin für Android");
			parameters.putString("link", "http://www.eclipse-magazin.de");
			parameters.putString("description", "Eclipse Magazin: Alle Ausgaben auf Ihrem Android-Tablet lesen!");
			fbDialog = mFacebook.dialog(FeedsActivity.this, "stream.publish", parameters, new SampleDialogListener());// "stream.publish" is an API call
			fbDialog.show();
		} catch (Exception e) {
			if (Constants.Debug) Log.d("Facebook", "Error:" + e.getMessage());
		}
	}

	private void twitterShare(String data){
		ResolveInfo twDefaultCl = findTwitterClient();
		if (twDefaultCl != null){
			try{
				Intent intent;
				final ActivityInfo activity=twDefaultCl.activityInfo;
				final ComponentName name=new ComponentName(activity.applicationInfo.packageName, activity.name);
				intent=new Intent(Intent.ACTION_SEND);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				intent.setComponent(name);
				intent.putExtra(Intent.EXTRA_TEXT, data);
				mContext.startActivity(intent);
			}
			catch(final ActivityNotFoundException e) {
				Log.i("jtj", "no twitter native",e );
			}
		}			
	}

	private void mailShare(String subject ,String data){
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("plain/text");  
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  
		emailIntent.setType("plain/text");  
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
		startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
	}

	public class SampleDialogListener extends BaseDialogListener{

		public void onComplete(Bundle values) {
			final String message = values.getString("post_id");
			if (!TextUtils.isEmpty(message)) {
				if (Constants.Debug) Log.d("Facebook-Example", "Dialog Success! post_id=" + message);
				mAsyncRunner.request(message, new WallPostRequestListener());
			} else {
				if (Constants.Debug) Log.d("Facebook-Example", "No wall post made");
			}
		}
	}

	public class WallPostRequestListener extends BaseRequestListener {

		public void onComplete(final String response) {
			if (Constants.Debug) Log.d("Facebook-Example", "Got response: " + response);
			String message = "<empty>";
			try {
				JSONObject json = Util.parseJson(response);
				message = json.getString("message");
			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
			final String text = "Your Wall Post: " + message;
			FeedsActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					Log.w("Facebook", text);
				}
			});
		}

		@Override
		public void onComplete(String response, Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
		}

		@Override
		public void onIOException(IOException e, Object state) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
		}  
	}

}
