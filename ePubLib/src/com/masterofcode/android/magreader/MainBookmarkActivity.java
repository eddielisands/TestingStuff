package com.masterofcode.android.magreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.masterofcode.android.magreader.adapters.ListOfBookmarksAdapter;
import com.masterofcode.android.magreader.adapters.ListOfBookmarksFiltersAdapter;
import com.masterofcode.android.magreader.bookmarks.BookmarksManager;
import com.masterofcode.android.magreader.db.entity.BookmarksItem;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class MainBookmarkActivity extends Activity {
	private ListView 						filterListView;
	private ListOfBookmarksFiltersAdapter	filtersAdapter;
	private Context 						mContext;
	private EditText						searchCriteriaEditText;
	private int								mFilterId = 0;
	private boolean							isLoadData = false;
	private	List<BookmarksItem>				items = null;
	private ListOfBookmarksAdapter			bookmarksAdapter;
	private ListView						bookmarksListView;
	private ProgressBar						bookmarksProgressBar;
	private RelativeLayout					no_bookmark_layout;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmark_view);
		
		this.mContext = this;
		ActionBar actionBar = getActionBar();
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        	actionBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R.array.menu_categories,
            		android.R.layout.simple_dropdown_item_1line), navigationListener);
        	ActionBarView.setActionBarListView(actionBar, mContext);
        	actionBar.setSelectedNavigationItem(3);
        } else {
        	actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_feeds).setTabListener(feedsTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_library).setTabListener(libraryTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_shop).setTabListener(shopTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_bookmarks).setTabListener(bookmarkTabListener), true);
        	ActionBarView.setActionBarTabView(actionBar, mContext);
        }
        
        // init filter list
        filterListView = (ListView) findViewById(R.id.filters_list_view);
        filterListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        no_bookmark_layout = (RelativeLayout) findViewById(R.id.no_bookmarks_relative_layout);
        
        
        String[]				bookmarks_filters_array = getResources().getStringArray(R.array.bookmarks_filters);
        List<String>			bookmarks_filters = new ArrayList<String>(Arrays.asList(bookmarks_filters_array));

        filtersAdapter = new ListOfBookmarksFiltersAdapter(this, R.id.bookmarks_filter_name, bookmarks_filters);
        filterListView.setAdapter(filtersAdapter);
        
    	bookmarksListView = (ListView) findViewById(R.id.bookmarks_list_view);

        
        filterListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
			{
				//filterListView.setItemChecked(position, true);
				filtersAdapter.setCurrentPosition(position);
				filterChanged(position);
			}
		});

        filterChanged(filtersAdapter.getCurrentPosition());
        
        
        bookmarksListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				clickOnBookmarksItem(position);
			}
		});
        
        bookmarksListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if(items.get(position).item_type != Constants.BOOKMARKS_ITEM_TYPE_NONE)
					removeBookmarkAtPosition(position);
				return true;
			}
		});
        
        Button			clearSearchCriteriaButton = (Button) findViewById(R.id.clear_search_criteria_button);
        
        clearSearchCriteriaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearSearchCriteria();
			}
		});
        
        searchCriteriaEditText = (EditText) findViewById(R.id.search_criteria_edit_text);
        
        bookmarksProgressBar = (ProgressBar) findViewById(R.id.bookmarks_progress_bar);
	}
	
	private void filterChanged(int newPosition)
	{
		if(mFilterId==newPosition)return;
		mFilterId = newPosition;
		isLoadData = false;
		new getBookmarkedItemsAsyncTask().execute(getCurrentFilterId());
	}
	
	private void clickOnBookmarksItem(long position)
	{
			BookmarksItem		selectedBookmarksItem = items.get((int) position);
			
			if(selectedBookmarksItem.item_type == Constants.BOOKMARKS_ITEM_TYPE_FEED)
			{
				FeedItem		selectedFeedItem = selectedBookmarksItem.getFeedItem();
				
				if(selectedFeedItem!=null)
				{
					Intent feedsActivity = new Intent(mContext, FeedsActivity.class);
					feedsActivity.putExtra("category_id", Integer.parseInt(selectedFeedItem.channel_id));
					feedsActivity.putExtra("item_guid", selectedFeedItem.guid);
					startActivity(feedsActivity);
				}
				
			} else
				if(selectedBookmarksItem.item_type == Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE)
				{
					Intent magazineActivity = new Intent(mContext, EpubViewerActivity.class);
					
					magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_FILE_PATH, selectedBookmarksItem.magazine_path);
					magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH, selectedBookmarksItem.magazine_cover_path);
					magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_TOPIC_INDEX, selectedBookmarksItem.magazine_topic_index);
					magazineActivity.putExtra(Constants.BUNDLE_KEY_EPUB_TOPIC_CONTENT_OFFSET, selectedBookmarksItem.magazine_topic_offset);
					startActivity(magazineActivity);
				}
	}

	private void removeBookmarkAtPosition(int position)
	{
		if(bookmarksAdapter!=null)
		{
			showBookmarkRemoveConfirmationDialog(position);
		}
	}

	private void showBookmarkRemoveConfirmationDialog(int index)
	{
		
		 DialogFragment newFragment = BookmarkRemovingAlertDialogFragment.newInstance(R.string.bookmark_removing_question, index);
	     newFragment.show(getFragmentManager(), "dialog");
	}
	
	private void clearSearchCriteria()
	{
		searchCriteriaEditText.setText("");
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		// loading bookmarks
		if(isLoadData)isLoadData = false;
		new getBookmarkedItemsAsyncTask().execute(getCurrentFilterId());
	}
	
	private int getCurrentFilterId()
	{
		return mFilterId;
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
			case 2:
				Intent shopIntent = new Intent();
				shopIntent.setClass(mContext, MainShopActivity.class);
				startActivity(shopIntent);
				finish();
				break;
			}
			return false;
		}
	};

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
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Intent shopIntent = new Intent();
			shopIntent.setClass(mContext, MainShopActivity.class);
			startActivity(shopIntent);
			finish();
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};
	
	TabListener bookmarkTabListener = new TabListener() {
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bookmarks_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_bookmark_settings) {
			startActivity(new Intent(MainBookmarkActivity.this, SettingsActivity.class));
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class getBookmarkedItemsAsyncTask extends AsyncTask<Integer, Void, List<BookmarksItem>> {

		@Override
		protected List<BookmarksItem> doInBackground(Integer... params) {
			Integer curFilterId = params[0];
			
			if (!isLoadData) {
				 List<BookmarksItem> bookmarks = null;
				 
				 switch (curFilterId) {
				 	case 0:	bookmarks = BookmarksManager.GetInstance().queryBookmarkedFeedsAndMagazines(mContext);
				 			break;

				 	case 1:	bookmarks = BookmarksManager.GetInstance().queryBookmarkedFeeds(mContext);
		 					break;
		 					
				 	case 2:	bookmarks = BookmarksManager.GetInstance().queryBookmarkedMagazines(mContext);
		 					break;

				 	default: bookmarks = BookmarksManager.GetInstance().queryBookmarkedFeeds(mContext);
				 			 break;
				 }
				 isLoadData = true;
				 return bookmarks;
			} else {
				return items;
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(isLoadData)return;
			
			bookmarksProgressBar.setVisibility(View.VISIBLE);
			bookmarksListView.setAdapter(null);
		}

		@Override
		protected void onPostExecute(List<BookmarksItem> bookmarkedList) {
			super.onPostExecute(bookmarkedList);
			
			bookmarksProgressBar.setVisibility(View.GONE);

			if(bookmarkedList==null)bookmarkedList = new ArrayList<BookmarksItem>();
			items = bookmarkedList;
			if (items.isEmpty()){
				no_bookmark_layout.setVisibility(View.VISIBLE);
			} else {
				no_bookmark_layout.setVisibility(View.GONE);
				bookmarksAdapter = new ListOfBookmarksAdapter(mContext, R.layout.bookmarks_list_item, items, (Activity) mContext);
				bookmarksListView.setAdapter(bookmarksAdapter);
			}
		}
	}
	
	private void doBookmarkRemove(int index)
	{
		if(BookmarksManager.GetInstance().removeBookmark(items.get(index)))
		{
			items.remove(index);
			bookmarksAdapter.notifyDataSetChanged();
			if (items.isEmpty())
				no_bookmark_layout.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(this, R.string.bookmark_removing_error, Toast.LENGTH_SHORT).show();
		}
	}
    
	private void doCancelBookmarkRemoving(int index)
	{
		
	}
	
	public static class BookmarkRemovingAlertDialogFragment extends DialogFragment {

        public static BookmarkRemovingAlertDialogFragment newInstance(int title, int index) {
            BookmarkRemovingAlertDialogFragment frag = new BookmarkRemovingAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("index", index);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            final int index = getArguments().getInt("index");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainBookmarkActivity)getActivity()).doBookmarkRemove(index);
                            }
                        }
                    )
                    .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainBookmarkActivity)getActivity()).doCancelBookmarkRemoving(index);
                            }
                        }
                    )
                    .create();
        }
    }
}
