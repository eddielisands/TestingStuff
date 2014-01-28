package com.masterofcode.android.magreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;

import com.masterofcode.android.magreader.adapters.GridOfLibraryItemsAdapter;
import com.masterofcode.android.magreader.bookmarks.BookmarksManager;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class MainLibraryActivity extends Activity {

	private static final int 				DIALOG_DELETE_ISSUE_ID = 1;
	private static final int 				DIALOG_DOWNLOAD_ISSUE_ID = 2;
	private static final int 				DIALOG_BOOKMARK_ISSUE_ID = 3;
	
	private int                             RESOURCE_COPYING = 1;                                                                      
    private int                             THREAD_PROGRESS = 0;                                                                            
    private int                             THREAD_FINISH = 1; 
    
	private Context							mContext;
	private ProgressDialog					copyingProgessDialog;
	private boolean							isLoadData = false;
	private	List<LibraryItem>				items = null;
	private GridView 						gridView;
	private GridOfLibraryItemsAdapter		gridAdapter;
	private ProgressDialog 					progressDialog;
	private Handler							handler;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library_view);
		
		mContext = this;
		ActionBar actionBar = getActionBar();
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        	actionBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R.array.menu_categories,
            		android.R.layout.simple_dropdown_item_1line), navigationListener);
        	ActionBarView.setActionBarListView(actionBar, mContext);
        	actionBar.setSelectedNavigationItem(1);
        } else {
        	actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_feeds).setTabListener(feedsTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_library).setTabListener(libraryTabListener), true);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_shop).setTabListener(shopTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_bookmarks).setTabListener(bookmarkTabListener), false);
        	ActionBarView.setActionBarTabView(actionBar, mContext);
        }
        
        // test
//        Log.i("---", "path="+LibraryManager.GetInstance().libraryPath(this));
		//copyResourceMagazines();
		//new DownloadManager(mContext).downloadCoversPurchasedIssues();
		//Moved to MainActivity now since we need to install the issues not only when we
		//enter the lib view, we also need to have them installed when entering the shop view
		//LibraryManager.GetInstance().copyCoversFromRes(mContext);
        gridView = (GridView) findViewById(R.id.issues_gridview);
	    gridView.setOnItemClickListener(gridViewItemClickListener);
	    gridView.setOnItemLongClickListener(gridViewItemLongClickListener);
	    
	    handler = new Handler() {
	    	public void handleMessage(Message msg) {
	    		progressDialog.dismiss();
	    		gridAdapter.notifyDataSetChanged();
	    	}
	    };
	}
	
	private OnItemClickListener gridViewItemClickListener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if (position < items.size()){
				if(items.get(position).isDownloaded){
					Log.i("---", "clicked on="+items.get(position).magazine_filepath);
					
					 Intent          intent = new Intent(MainLibraryActivity.this, EpubViewerActivity.class);
	                 intent.putExtra(Constants.BUNDLE_KEY_EPUB_FILE_PATH, items.get(position).magazine_filepath);
	                 intent.putExtra(Constants.BUNDLE_KEY_EPUB_COVER_FILE_PATH, items.get(position).magazine_cover_filepath);
	                 startActivity(intent);
				} else{
						onCreateDialog(DIALOG_DOWNLOAD_ISSUE_ID, position);
				}
            }
	    }
	};
	
	private OnItemLongClickListener gridViewItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if(position < items.size() && items.get(position).isDownloaded){
				onCreateDialog(DIALOG_DELETE_ISSUE_ID, position);
			}
			return false;
		}
	};
	
    private void loadLibrary()
    {
    	if(isLoadData)isLoadData = false;
    	new getLibraryItemsAsyncTask().execute();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		loadLibrary();
	}
	
	private class getLibraryItemsAsyncTask extends AsyncTask<Integer, Void, List<LibraryItem>> {

		@Override
		protected List<LibraryItem> doInBackground(Integer... params) {
			if (!isLoadData) {
				 List<LibraryItem> libraryItems = null;
				 
			 	 libraryItems = LibraryManager.GetInstance().queryLibraryItems();
				 isLoadData = true;
				 return libraryItems;
			} else {
				return items;
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(isLoadData)return;
			
//			bookmarksProgressBar.setVisibility(View.VISIBLE);
//			bookmarksListView.setAdapter(null);
		}

		@Override
		protected void onPostExecute(List<LibraryItem> libraryItemsList) {
			super.onPostExecute(libraryItemsList);
			
//			bookmarksProgressBar.setVisibility(View.GONE);

			if(libraryItemsList==null)libraryItemsList = new ArrayList<LibraryItem>();
			items = libraryItemsList;

			Log.i("---", "post");

			for(int i=0; i<items.size();i++)
			{
				Log.i("---", "path="+items.get(i).magazine_filepath);
			}
//        	bookmarksAdapter = new ListOfBookmarksAdapter(mContext, R.layout.bookmarks_list_item, items, (Activity) mContext);
//        	bookmarksListView.setAdapter(bookmarksAdapter);
			gridAdapter = new GridOfLibraryItemsAdapter(mContext, items, (Activity) mContext);
		    gridView.setAdapter(gridAdapter);

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
			case 2:
				Intent shopIntent = new Intent();
	            shopIntent.setClass(mContext, MainShopActivity.class);
	            startActivity(shopIntent);
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

	TabListener feedsTabListener = new TabListener() {	
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
	
	TabListener libraryTabListener = new TabListener() {	//for this tab we have empty tabListener
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {}		
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
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Intent bookmarkIntent = new Intent();
			bookmarkIntent.setClass(mContext, MainBookmarkActivity.class);
			startActivity(bookmarkIntent);
			finish();
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	};
	
	
	Handler progressHandler = new Handler()
	{                                                                                               
        public void handleMessage(Message msg)
        {                                                                                                
        	if (msg.what==RESOURCE_COPYING)
        	{                                                                                    
        		if(msg.arg1==THREAD_FINISH)                                                                                             
                {                                                                                                                       
                   copyingProgessDialog.dismiss();            
                   loadLibrary();
                } else {                                                                                                                
                   copyingProgessDialog.setProgress(msg.arg2);                                                           
                }                                                                                                                       
            }                                                                                                                               
        }                                                                                                                                       
    };
    
    protected void onCreateDialog(int id, int issuePosition) {
	    switch (id) {
	      case DIALOG_DELETE_ISSUE_ID:
	          createDialog(R.string.delete_issue_dialog_title, R.string.delete_issue_dialog_message, issuePosition, android.R.drawable.stat_sys_warning, id).show();
	          return;
	      case DIALOG_DOWNLOAD_ISSUE_ID:
	          createDialog(R.string.download_issue_dialog_title, R.string.download_issue_dialog_message, issuePosition, android.R.drawable.stat_sys_download, id).show();
	          return;
	      case DIALOG_BOOKMARK_ISSUE_ID:
	          createDialog(R.string.bookmark_issue_dialog_title, R.string.bookmark_issue_dialog_message, issuePosition, android.R.drawable.stat_sys_warning, id).show();
	          return;
	      default:
	    	  return;
	      }
	}
    
    private Dialog createDialog(final int dialogTitle, final int dialogMessage, final int position, final int dialogIcon, final int dialogId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainLibraryActivity.this);
        builder.setTitle((MainLibraryActivity.this).getString(dialogTitle))
            .setIcon(dialogIcon)
            .setMessage((MainLibraryActivity.this).getString(dialogMessage))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(dialogId == DIALOG_DOWNLOAD_ISSUE_ID)
					{
						//download issue here
						if(items.get(position).magazine_type ==  Constants.MAGAZINE_TYPE_FROM_RESOURCES)
						{
							LibraryManager.GetInstance().copyMagazineFromResources(MainLibraryActivity.this, items.get(position).magazine_filepath.substring(items.get(position).magazine_filepath.lastIndexOf("/") + 1, items.get(position).magazine_filepath.length()));
						} else if(items.get(position).magazine_type ==  Constants.MAGAZINE_TYPE_NORMAL)	{
							if(ApplicationUtils.isOnline(MainLibraryActivity.this)){
						            		Log.d("deleteIssue", "download issue="+items.get(position).magazine_url + " items." + items.get(position).magazine_title);
											LibraryManager.GetInstance().downloadMagazine(MainLibraryActivity.this, items.get(position));
							} else {
								ApplicationUtils.createNoInternetDialog(MainLibraryActivity.this).show();
							}
						}
						gridAdapter.notifyDataSetChanged();
					} else if(dialogId == DIALOG_DELETE_ISSUE_ID)
						{
						
							String		magazineName = items.get(position).magazine_filepath;
							int			pos = magazineName.lastIndexOf(File.separator);
						
							if(pos!=-1)
							{
								magazineName = magazineName.substring(pos + 1);
							}
							
							if(BookmarksManager.GetInstance().isMagazineHasBookmarks(mContext, magazineName))
							{
								onCreateDialog(DIALOG_BOOKMARK_ISSUE_ID, position);
							} else {
								//remove issue from sd
								Log.d("deleteIssue", "delete issue="+items.get(position).magazine_url + " items." + items.get(position).magazine_title);
								progressDialog = ProgressDialog.show(MainLibraryActivity.this, null, 
							    		"Deleting. Please wait...", true);
								Thread startRemoving = new Thread() {  
						    		   public void run() {
						    			  LibraryManager.GetInstance().deleteMagazine(MainLibraryActivity.this, items.get(position));
						    		      handler.sendEmptyMessage(0);
						    		      }
								};
								startRemoving.start();
							}
						} else if(dialogId == DIALOG_BOOKMARK_ISSUE_ID)
								{
									String		magazineName = items.get(position).magazine_filepath;
									int			pos = magazineName.lastIndexOf(File.separator);
						
									if(pos!=-1)
									{
										magazineName = magazineName.substring(pos + 1);
									}
							
							        BookmarksManager.GetInstance().removeMagazineBookmarks(mContext, magazineName);
									
							        Log.d("deleteIssue", "delete issue="+items.get(position).magazine_url + " items." + items.get(position).magazine_title);
							        progressDialog = ProgressDialog.show(MainLibraryActivity.this, null, 
							        		"Deleting. Please wait...", true);
									Thread startRemoving = new Thread() {  
							    		   public void run() {
							    			  LibraryManager.GetInstance().deleteMagazine(MainLibraryActivity.this, items.get(position));
							    		      handler.sendEmptyMessage(0);
							    		      }
									};
									startRemoving.start();
								}
				}
			})
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        return builder.create();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.library_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		
		//search
		final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setIconified(true);
				Intent		intent = new Intent(MainLibraryActivity.this, SearchActivity.class);
				
				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_KEYWORD, query);
				intent.putExtra(Constants.BUNDLE_KEY_SEARCH_TYPE, Constants.SEARCH_TYPE_LIBRARY);
				startActivity(intent);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		
	    return true;
	}
	
}
