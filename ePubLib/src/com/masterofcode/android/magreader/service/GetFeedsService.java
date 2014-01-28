package com.masterofcode.android.magreader.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.search.FeedSearchDbAdapter;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.RestClient;
import com.masterofcode.android.magreader.utils.SaveToDbQueue;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class GetFeedsService extends Service {
	
	private Timer timer = new Timer();
	private ActiveRecordBase _db;
	private FeedSearchDbAdapter feedSearchDbAdapter;
	private static GetFeedsService instance = null;
	private ArrayList<Integer> feedsToUpdate = new ArrayList<Integer>();
	private static final int NOTIFICATION_LOADING = 1001;
	
	private final IBinder mBinder = new Controller();
	
	public class Controller extends Binder {
		public GetFeedsService getService() {
			return GetFeedsService.this;
		}
	}
	
	public static GetFeedsService getInstance() { return instance; }
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		instance = this;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		try {
			feedsToUpdate = intent.getIntegerArrayListExtra(Constants.FEEDS_TO_UPDATE);
			if (feedsToUpdate == null) feedsToUpdate = new ArrayList<Integer>();
		} catch (NullPointerException e) {
			feedsToUpdate = new ArrayList<Integer>();
		}
		
		startService();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		instance = null;		
		stopService();
	}
	
	private void startService() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		long updateTime = prefs.getLong(Constants.PREFERENCE_UPDATE_FEEDS_TIME, Constants.DEFAULT_TIME_TO_UPDATE);
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {		
				updateDbWithFeeds();
			}
		}, 0, updateTime);
	}
	
	private void stopService() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	protected void updateDbWithFeeds() {
		showNotification();
        try {
        	ArrayList<FeedType> lft = new ArrayList<FeedType>(feedsToUpdate.size());
        	_db = ActiveRecordBase.open(this, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);//((JtjApplication)this.getApplication()).getDatabase();
        	if(!_db.isOpen()) _db.open();
			feedSearchDbAdapter = new FeedSearchDbAdapter(this);
			feedSearchDbAdapter.open();
			boolean isNewFeeds = false;
			if (feedsToUpdate == null || feedsToUpdate.size() == 0) {
				lft = (ArrayList<FeedType>) _db.find(FeedType.class, "ISHIDE=?", new String [] { String.valueOf(false) });//findAll(FeedType.class);
			} else {
				for (Integer i:feedsToUpdate) {
					if(!_db.isOpen()) _db.open();
					ArrayList<FeedType> feedTypeToUpdate = (ArrayList<FeedType>) _db.find(FeedType.class, "ORDERID=?", new String [] {String.valueOf(i)});
					lft.add(feedTypeToUpdate.get(0));
				}
			}
			for (FeedType ft : lft) {
				int channelId = ft.order_id;
				int i = 0;
				boolean haveNewItems = true;
				JSONArray jsonArray = RestClient.connect(ft.url);
				if(Constants.Debug)
					Log.d("GetFeedsService", "FeedType = " + ft.title + " jsonarray.length " + jsonArray.length());
				while (haveNewItems) {
					JSONObject currentObject = jsonArray.optJSONObject(i);
					if (currentObject != null) {
						FeedItem item = new FeedItem(currentObject, String.valueOf(channelId).trim());
						if(!_db.isOpen()) _db.open();
						if (_db.find(FeedItem.class, "GUID=? AND CHANNELID=?", new String[] {item.guid, String.valueOf(channelId).trim()}).size() == 0) {
							FeedItem moveItem = _db.newEntity(FeedItem.class);
							EntitiesHelper.copyFieldsWithoutID(moveItem, item);
							if(!_db.isOpen()) _db.open();
							SaveToDbQueue.saveToDbAsyncQueue(moveItem, null);
							i++;
							isNewFeeds = true;
							if(feedSearchDbAdapter.databaseCreated()){
								feedSearchDbAdapter.insertRow(item.guid, ApplicationUtils.getStringWithoutHTMLTags(item.description));
					        }
						} else {
							haveNewItems = false;
						}
					} else {
						haveNewItems = false;
					}
				}
			}
			if (isNewFeeds)
				sendBroadcast(new Intent(Constants.UPDATE_FEEDS));
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        feedSearchDbAdapter.close();
        closeNotification();
        _db.close();
	}
	
	private void showNotification() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(false)
                .setTicker("Feeds are updating now ...")
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Feeds are updating now ...");
        notificationManager.notify(NOTIFICATION_LOADING, builder.getNotification());
	}
	
	private void closeNotification() {
		final NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_LOADING);
	}
}
