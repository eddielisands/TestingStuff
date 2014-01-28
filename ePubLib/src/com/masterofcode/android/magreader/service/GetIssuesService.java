package com.masterofcode.android.magreader.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import com.masterofcode.android.magreader.MainShopActivity;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.IssueItem;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.RequestingIssuesResult;
import com.masterofcode.android.magreader.utils.RestClient;
import com.masterofcode.android.magreader.utils.constants.Constants;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class GetIssuesService extends Service {

	private Timer timer = new Timer();
	private ActiveRecordBase _db;
	private static GetIssuesService instance = null;
	private final IBinder mBinder = new Controller();
	private boolean updatingInProgress;
	
	public class Controller extends Binder {
		public GetIssuesService getService() {
			return GetIssuesService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public static GetIssuesService getInstance() { return instance; }
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		startService();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopService();
	}
	
	private void startService() {
		if(Constants.Debug)
			Log.d("debug", "Start service...");
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {		
				if(Constants.Debug)
					Log.d("debug", "starting updateDbWithIssues()...");
				updateDbWithIssues();
			}
		}, 1, Constants.DEFAULT_TIME_TO_UPDATE_ISSUES);
	}
	
	private void stopService() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	private void updateDbWithIssues()
	{
		Log.i("----------", "updateDbWithIssues");
		updatingInProgress = true;
		 try {
			 if(Constants.Debug)
					Log.d("debug", "in updateDbWithIssues()...");
	        	_db = ((JtjApplication)this.getApplication()).getDatabase();
				int i = 0;
				boolean isNewIssues = false;
				boolean downloadableFlagIsChanged = false;

				RequestingIssuesResult		result = RestClient.sendJson(Constants.ISSUE_DETAILS_URL, null, -1, -1);
				
				if(result==null) return;
				
				JSONArray jsonArray = result.getJsonResult();
						
				if(jsonArray==null)
				{
					// check ioerror
					if(result.isIOError())
					{
						sendBroadcast(new Intent(Constants.UPDATING_ISSUES_IO_ERROR));
					}
					return;
				}
				
				boolean isLogged = ApplicationUtils.getPrefPropertyBoolean(JtjApplication.getContext(), Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
				
				if(isLogged && !result.isAuthValid())
				{
					// reset login state
					ApplicationUtils.setPrefProperty(this, Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
					sendBroadcast(new Intent(Constants.SUBSCRIPTION_PASSWORD_IS_INCORRECT));
				}
				
				if(jsonArray != null)
					while (jsonArray.length() > i)
					{
						JSONObject currentObject = jsonArray.optJSONObject(i);
						if (currentObject != null)
						{
							IssueItem item = new IssueItem(currentObject);
							Log.i("MagazinReader", "Updating db item for: " + item.issueID);
							
							
							if(!TextUtils.isEmpty(item.publicationDate))
							{
								_db.open();
								if (_db.find(IssueItem.class, "PUBLICATION_DATE=?", new String[] {item.publicationDate}).size() == 0)
								{
									if(Constants.Debug)
										Log.d("debug", "Adding new issue...");
									IssueItem moveItem = _db.newEntity(IssueItem.class);
									EntitiesHelper.copyFieldsWithoutID(moveItem, item);
									moveItem.issueID = item.issueID;
									Log.i("MagazinReader", "Updated db item for: " + moveItem.issueID);
									if(!_db.isOpen())
									{
										_db.open();
									}
									moveItem.save();
									isNewIssues = true;
								}
							}
							
							//check for downloadable flag is changed
							if(!_db.isOpen())
							{
								_db.open();
							}
							List<IssueItem>  downlodableTest = _db.find(IssueItem.class, "GOOGLECHECKOUTID=? and DOWNLOADABLE=?", new String[] {item.googlecheckoutid, (!item.downloadable) ? "true" : "false"});
							if(downlodableTest!=null)
							{
								if(downlodableTest.size()>0)
								{
									IssueItem		changedItem = downlodableTest.get(0);
									
									changedItem.downloadable = item.downloadable;
									changedItem.save();
								}
							}
						}
						i++;
					}
				if (isNewIssues || downloadableFlagIsChanged)
					sendBroadcast(new Intent(Constants.UPDATE_ISSUE));
				if(Constants.Debug)
					Log.d("debug", "Intent was sended...");
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		 updatingInProgress = false;
	}

}
