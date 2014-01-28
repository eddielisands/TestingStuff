package com.masterofcode.android.magreader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.masterofcode.android.magreader.adapters.ListOfCategoriesAdapter;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.fragments.FeedsCategoriesFragment;
import com.masterofcode.android.magreader.fragments.FeedsListFragment;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.service.GetFeedsService;
import com.masterofcode.android.magreader.utils.ActionBarView;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.FeedsListProvider;
import com.masterofcode.android.magreader.utils.RestClient;
import com.masterofcode.android.magreader.utils.SaveToDbQueue;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    	
    private static Context mContext;
    private ActionBar actionBar;
	private int mCurrentPosition = 0;
    private PopulateFeedsAsyncTask feedTask = new PopulateFeedsAsyncTask();
    private List<FeedType> feedTypes = null;
    private boolean isFirstRun = false;
    private ActiveRecordBase database;
    private RelativeLayout feedsHintLayout;
	protected boolean feedsAddHintIsShowed = false;
	protected boolean feedsHintDisabled = false;
	
	private static MainActivity instance;
	
    static class Self {
    	
    	public Self() {
        }
    	public GetFeedsService mBoundService;
    	public ServiceConnection updateConnection;
    }
    
    Self _self = new Self();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("noInetException", "in onCreate, MainActivity");
        setContentView(R.layout.main);
        
        instance = this;
        
        // feeds hint hide
        feedsHintLayout = (RelativeLayout) findViewById(R.id.included_feeds_hint);
        feedsHintLayout.setVisibility(View.GONE);
        
        isFirstRun = true;
        
        feedsHintDisabled = ApplicationUtils.getPrefPropertyBoolean(this, Constants.PREFERENCES_PROPERTY_SHOW_FEEDS_HINT_DISABLED, false);
        
        if (savedInstanceState != null) {
			setCurrentPosition(savedInstanceState.getInt("currentPosition"));
		}
        
        this.mContext = this;
        database = ((JtjApplication) ((MainActivity) mContext).getApplication()).getDatabase();
        ApplicationUtils.setPrefProperty(mContext, Constants.DATE_FEEDS_UPDATE, new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy, HH:mm").format(new Date()));
        this.actionBar = getActionBar();
        
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        	actionBar.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R.array.menu_categories,
            		android.R.layout.simple_dropdown_item_1line), navigationListener);
        	ActionBarView.setActionBarListView(actionBar, mContext);
        	actionBar.setSelectedNavigationItem(0);
        } else {
        	actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_feeds).setTabListener(feedsTabListener), true);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_library).setTabListener(libraryTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_shop).setTabListener(shopTabListener), false);
            actionBar.addTab(actionBar.newTab().setText(R.string.action_bar_bookmarks).setTabListener(bookmarkTabListener), false);
        	ActionBarView.setActionBarTabView(actionBar, mContext);
        }

        Log.i("feed_prov", getActionBar().toString());
        
        if(ApplicationUtils.isOnline(MainActivity.this)){
        	new Thread(new Runnable() {
	            public void run() {
	            	if (ApplicationUtils.checkIssueIsAvail()){
	            		feedTask.execute(mCurrentPosition);
	       		     	// set up service
	       		     	doInitUpdateConnection(); 
	            	} else {
	            		MainActivity.this.runOnUiThread(new Runnable() {
	            			public void run()
	            			{
	            				noInternetShowDialog();
	            			}
	            		});
	            	}
	            		
	            }
	          }).start();
        } else{
        	noInternetShowDialog();
        }
        LibraryManager.GetInstance().copyCoversFromRes(mContext);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d("noInetException", "in onResume, MainActivity... isFirstRun = " + isFirstRun);
    	if (!isFirstRun) {
    		Log.d("noInetException", "in onResume, before call updateAdapter ");
    		updateAdapters();    		
    	}
    	Log.d("noInetException", "in onResume, after call updateAdapter ");
    	isFirstRun = false;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.d("noInetException", "in onPause");
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	doUnbindService();
    }

    public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void setCurrentPosition(int mCurrentPosition) {
		this.mCurrentPosition = mCurrentPosition;
	}
	
	OnNavigationListener navigationListener = new OnNavigationListener() {
		
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			switch (itemPosition) {
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

	TabListener feedsTabListener = new TabListener() {    //for this tab we have empty tabListener
        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    };

    TabListener libraryTabListener = new TabListener() {

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Intent libraryIntent = new Intent();
            libraryIntent.setClass(mContext, MainLibraryActivity.class);
            startActivity(libraryIntent);
            finish();
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    };

    TabListener shopTabListener = new TabListener() {

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Intent shopIntent = new Intent();
            shopIntent.setClass(mContext, MainShopActivity.class);
            startActivity(shopIntent);
            finish();
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    };

    TabListener bookmarkTabListener = new TabListener() {

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            Intent bookmarkIntent = new Intent();
            bookmarkIntent.setClass(mContext, MainBookmarkActivity.class);
            startActivity(bookmarkIntent);
            finish();
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    };

    private class PopulateFeedsAsyncTask extends AsyncTask<Integer, Void, JSONArray> {
        private int mCurrentPosition = 0;
        List<FeedType> feedTypes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Integer... params) {
        	this.mCurrentPosition = params[0];
        	
            JSONArray remoteFeeds = RestClient.connect(Constants.FEEDS_LIST_URL);
            return remoteFeeds;
        }

        @Override
        protected void onPostExecute(JSONArray remoteFeeds) {
            int length = 0;
            try {
                if (!database.isOpen()) database.open();
                feedTypes = database.findAll(FeedType.class);//database.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });
                length = feedTypes.size();
            } catch (ActiveRecordException e) {
                e.printStackTrace();
            }


            if(remoteFeeds!=null)		// prevent crashing if no inet
            {
                if (remoteFeeds.length() != length) {
                    try {
                        String[] args = new String[1];
                        args[0]="0";
                        length = database.delete(FeedType.class,"_id>?",args);
                    } catch (ActiveRecordException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < remoteFeeds.length(); i++) {
                        JSONObject currentObject = remoteFeeds.optJSONObject(i);
                        try {                        	
                        	FeedType ft = new FeedType(currentObject.getString("name"), currentObject.getString("url"), "en_EN", i + 1, 0);
                        	if (currentObject.has(Constants.FEEDS_JSON_ATTRIBUTES_DEFAULT)) {
                        		boolean isDefaultFromJSON = currentObject.getBoolean(Constants.FEEDS_JSON_ATTRIBUTES_DEFAULT);
                            	Log.i("feed_prov -> is default: ", new Boolean(isDefaultFromJSON).toString());
                            	ft.is_default = isDefaultFromJSON;
                            	ft.is_hide = !ft.is_default;
                        	}
                        	
                            FeedsListProvider.getInstance(database).addFeedType(ft);
                            feedTypes.add(ft);
                            Log.i("feed_prov", "URL is added");
                        } catch (JSONException e) {
                            Log.e("feed_prov", "Invalid JSON");
                        }
                    }
                    
                    try {
                    	if (!database.isOpen()) database.open();
                    	if (database.isOpen()) feedTypes = database.findAll(FeedType.class);
	                    length = feedTypes.size();
					} catch (ActiveRecordException e) {
						e.printStackTrace();
					}
                }
            }
            try {
	            feedTypes = updateUnreadCount(feedTypes);
	            
	            MainActivity.this.setFeedTypes(MainActivity.this.feedTypes);
	
	            FeedsCategoriesFragment categoriesFragment = (FeedsCategoriesFragment) getFragmentManager().findFragmentById(R.id.feed_categories_fragment);
	            categoriesFragment.setListAdapter(new ListOfCategoriesAdapter(mContext, R.layout.list_categories, feedTypes));
	
	            ListView lv = categoriesFragment.getListView();
	            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	            lv.setCacheColorHint(Color.WHITE);
	            
	            showAddFeedsHint(feedTypes);
	            
	            //startService(new Intent(GetFeedsService.class.getName()));
	    		//doBindService();
	            
	            if (GetFeedsService.getInstance() != null) {
	            	doBindService();
	            } else {
	            	startService(new Intent(mContext, GetFeedsService.class));
	            	doBindService();
	            }
	            
	            if(remoteFeeds!=null)		// prevent crashing if no inet
	            	categoriesFragment.setPosition(mCurrentPosition);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
    
    public List<FeedType> updateUnreadCount(List<FeedType> feedTypes) {
    	for (FeedType feedType:feedTypes) {
				try {
					if (!database.isOpen()) database.open();
					List<FeedItem> fil = database.find(FeedItem.class, "CHANNELID=? AND IS_READ=?", new String[] {String.valueOf(feedType.order_id), String.valueOf(false)});
					feedType.unread_count = fil.size();
					SaveToDbQueue.saveToDbAsyncQueue(null, feedType);//feedType.update();
					if (!feedType.title.equals(Constants.FEED_ALL_NAME) && !feedType.title.equals(Constants.FEED_NEWS_NAME) && !feedType.title.equals(Constants.FEED_VIDEOS_NAME)) {
		            	((JtjApplication) ((MainActivity) mContext).getApplication()).addFeedsToManageArray(feedType);
		            }
				} catch (ActiveRecordException e) {
					e.printStackTrace();
				}
        }
    	try {
			feedTypes = database.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
    	return feedTypes;
    }
    
    @Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("currentPosition", mCurrentPosition);
	}
    
	public static class FeedsBroadcastListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			try {
				if (action.equals(Constants.UPDATE_FEEDS)) {
					ActiveRecordBase database = ((JtjApplication) ((MainActivity) mContext).getApplication()).getDatabase();
					List<FeedType> feedTypes = null;
					try {
						database.open();
						feedTypes = database.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });
					} catch (ActiveRecordException e) {
						e.printStackTrace();
					}
					
					feedTypes = ((MainActivity) mContext).updateUnreadCount(feedTypes);
					
					FeedsCategoriesFragment categoriesFragment = (FeedsCategoriesFragment) ((MainActivity) mContext).getFragmentManager().findFragmentById(R.id.feed_categories_fragment);
					
					categoriesFragment.setListAdapter(new ListOfCategoriesAdapter(mContext, R.layout.list_categories, feedTypes));
					ListView lv = categoriesFragment.getListView();
		            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		            lv.setCacheColorHint(Color.WHITE);
		            categoriesFragment.setPosition(((MainActivity) mContext).getmCurrentPosition());
		            ApplicationUtils.setPrefProperty(mContext, Constants.DATE_FEEDS_UPDATE, new SimpleDateFormat("dd MMMMMMMMMMMMM yyyy, HH:mm").format(new Date()));
		            ((MainActivity) mContext).getActionBar().setSubtitle("Last Update: " + ApplicationUtils.getPreferences(context).getString(Constants.DATE_FEEDS_UPDATE, null));
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	public void updateAdapters() {
		Log.d("noInetException", "in updateAdapters");
		List<FeedType> feedTypes = null;
		try {
			if (!database.isOpen()) database.open();
			feedTypes = database.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });//database.findAll(FeedType.class);
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		
		feedTypes = updateUnreadCount(feedTypes);
		
		if (feedTypes.size() <= mCurrentPosition) {
			mCurrentPosition = 0;
		}
		
		FeedsCategoriesFragment categoriesFragment = (FeedsCategoriesFragment) getFragmentManager().findFragmentById(R.id.feed_categories_fragment);
		
		categoriesFragment.setListAdapter(new ListOfCategoriesAdapter(mContext, R.layout.list_categories, feedTypes));
		
		ListView lv = categoriesFragment.getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setCacheColorHint(Color.WHITE);
        categoriesFragment.setPosition(mCurrentPosition);
        
		showAddFeedsHint(feedTypes);
		Log.d("noInetException", "in updateAdapters, end");
	}
	
	private void showAddFeedsHint(List<FeedType> feedTypes)
	{
		if(feedsHintDisabled) return;
		
		if(feedTypes.size() > Constants.CONSTANT_FEEDS_COUNT)
		{
			if(feedsAddHintIsShowed)return;		// no need to show again
			
			// animation
			Animation anim_in = AnimationUtils.loadAnimation(this, R.anim.feeds_hint_in);

	        anim_in.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}
				
				public void onAnimationRepeat(Animation animation) {
				}
				
				public void onAnimationEnd(Animation animation) {
					feedsAddHintIsShowed = true;

					feedsHintLayout.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							Log.i("-----", "click");
							Intent          intent = new Intent(MainActivity.this, SettingsActivity.class);
			                intent.putExtra(Constants.BUNDLE_KEY_SETTINGS_SELECT_FEEDS, true);
			                startActivityForResult(intent, Constants.SETTINGS_ACTIVITY);
						}
					});
				}
			});

	        feedsHintLayout.startAnimation(anim_in);

			feedsHintLayout.setVisibility(View.VISIBLE);
		} else {
			if(feedsAddHintIsShowed)	// no need to hide if is already hided
			{
/*				// animation
				Animation anim_out = AnimationUtils.loadAnimation(this, R.anim.feeds_hint_out);

		        anim_out.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}
					
					public void onAnimationRepeat(Animation animation) {
					}
					
					public void onAnimationEnd(Animation animation) {
						feedsAddHintIsShowed = false;
					}
				});

		        feedsHintLayout.startAnimation(anim_out);
*/
				feedsHintLayout.setOnClickListener(null);
				feedsHintLayout.setVisibility(View.GONE);
				feedsAddHintIsShowed = false;
				feedsHintDisabled = true;
				ApplicationUtils.setPrefProperty(mContext, Constants.PREFERENCES_PROPERTY_SHOW_FEEDS_HINT_DISABLED, feedsHintDisabled);
			}
		}
	}

	void doInitUpdateConnection(){
		if (_self.updateConnection == null) 
            _self.updateConnection = new ServiceConnection() {
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
	                    _self.mBoundService = ((GetFeedsService.Controller) service)
	                                    .getService();
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
	                    _self.mBoundService = null;
	            }
		};
	}
	
	void doBindService() {
        // Establish a connection with the service. We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
		if (_self.updateConnection == null)
			doInitUpdateConnection();
        bindService(new Intent(MainActivity.this, GetFeedsService.class),
                        _self.updateConnection, Context.BIND_AUTO_CREATE);
	}

	void doUnbindService() {
        if (_self.mBoundService != null) {
                // Detach our existing connection.
                try {
                	if(_self.updateConnection != null)
                        unbindService(_self.updateConnection);
                } catch (IllegalArgumentException e) {
                        Log.e(Constants.LOG_BS, "unbindService failed!", e);
                }

                _self.mBoundService = null;
        }
	}

	public List<FeedType> getFeedTypes() {
		return feedTypes;
	}

	public void setFeedTypes(List<FeedType> feedTypes) {
		this.feedTypes = feedTypes;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			getMenuInflater().inflate(R.menu.feed_main_menu, menu);
		} else {
			getMenuInflater().inflate(R.menu.feed_main_menu_vertical, menu);
		}		
		
		
		//search
		final SearchView searchView = (SearchView) menu.findItem(R.id.menu_feed_search).getActionView();
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchView.setIconified(true);
				Intent		intent = new Intent(MainActivity.this, SearchActivity.class);
				
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
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_mark_as_read) {
			new UpdateUnreadCountAsyncTask().execute();
			return true;
		}
		else if (itemId == R.id.menu_feed_settings) {
			startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), Constants.SETTINGS_ACTIVITY);
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case Constants.SETTINGS_ACTIVITY:
			if (resultCode == RESULT_OK) {
				//_self.mBoundService.onRebind(data);//sendBroadcast(data);//onStart(data, resultCode);
				doUnbindService();
				stopService(new Intent(mContext, GetFeedsService.class));
				doBindService();
				startService(new Intent(mContext, GetFeedsService.class).putExtra(Constants.FEEDS_TO_UPDATE, data.getIntegerArrayListExtra(Constants.FEEDS_TO_UPDATE)));
			}
		}
	}
	
	public void reloadService(Intent data){
		doUnbindService();
		stopService(new Intent(mContext, GetFeedsService.class));
		doBindService();
		startService(new Intent(mContext, GetFeedsService.class).putExtra(Constants.FEEDS_TO_UPDATE, data.getIntegerArrayListExtra(Constants.FEEDS_TO_UPDATE)));
	}
	
	public static MainActivity getInstance() {
        return instance;
    }
	
	private void noInternetShowDialog(){
		ApplicationUtils.createNoInternetDialog(MainActivity.this).show();
    	updateAdapters();
    	((FeedsCategoriesFragment) getFragmentManager().findFragmentById(R.id.feed_categories_fragment)).setListShown(true);
    	((FeedsListFragment) getFragmentManager().findFragmentById(R.id.feed_list_fragment)).setItemsForCategories(1);
	}
	
	private class UpdateUnreadCountAsyncTask extends AsyncTask<Void, Void, Void> {
		
		List<FeedType> lft;
		FeedType ft;
		FeedsCategoriesFragment categoriesFragment;
		FeedsListFragment listFragment;
		
		public UpdateUnreadCountAsyncTask() {
			this.categoriesFragment = (FeedsCategoriesFragment) getFragmentManager().findFragmentById(R.id.feed_categories_fragment);
			this.listFragment = (FeedsListFragment) getFragmentManager().findFragmentById(R.id.feed_list_fragment);
			try {
				this.lft = database.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });//findAll(FeedType.class);
				ft = lft.get(mCurrentPosition);
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.categoriesFragment.setListShown(false);
			this.listFragment.setListShown(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (!database.isOpen()) database.open();
				List<FeedItem> lfi = database.find(FeedItem.class, "CHANNELID=?", new String[] {String.valueOf(ft.order_id)});
				for (FeedItem fi:lfi) {
					List<FeedItem> lfiToUpdateWithGuid = database.find(FeedItem.class, "GUID=?", new String[] {fi.guid});
					for (FeedItem item:lfiToUpdateWithGuid) {
						if (!item.isRead) {
							item.isRead = true;
							SaveToDbQueue.saveToDbAsyncQueue(item, null);//item.update();
						}
					}
				}
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
			
			lft = updateUnreadCount(lft);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			this.categoriesFragment.setListAdapter(new ListOfCategoriesAdapter(mContext, R.layout.list_categories, lft));
			ListView lv = this.categoriesFragment.getListView();
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setCacheColorHint(Color.WHITE);
            this.categoriesFragment.setPosition(mCurrentPosition);
            this.categoriesFragment.setListShown(true);
		}
		
	}
	
	public int getmCurrentPosition() {
		return mCurrentPosition;
	}
}