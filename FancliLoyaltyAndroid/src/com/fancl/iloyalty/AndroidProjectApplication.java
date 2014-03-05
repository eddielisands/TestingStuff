package com.fancl.iloyalty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.MainTabFragmentActivity;
import com.fancl.iloyalty.activity.SplashScreenActivity;
import com.fancl.iloyalty.activity.advertisement.AdvertisementActivity;
import com.fancl.iloyalty.activity.beauty.BeautyHomeActivity;
import com.fancl.iloyalty.activity.login.LoginForgetPasswordActivity;
import com.fancl.iloyalty.activity.login.LoginFormActivity;
import com.fancl.iloyalty.activity.login.LoginNonMemberActivity;
import com.fancl.iloyalty.activity.login.LoginTOSActivity;
import com.fancl.iloyalty.activity.login.SignUpEnterMemberIDActivity;
import com.fancl.iloyalty.activity.login.SignUpFormActivity;
import com.fancl.iloyalty.activity.login.SignUpResultActivity;
import com.fancl.iloyalty.activity.login.SignUpScanningBarCodeActivity;
import com.fancl.iloyalty.activity.login.WelcomeActivity;
import com.fancl.iloyalty.activity.menu.MenuActivity;
import com.fancl.iloyalty.activity.promotion.PromotionHomeActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotListActivity;
import com.fancl.iloyalty.asynctask.DatabaseDownloadAsyncTask;
import com.fancl.iloyalty.asynctask.TillIdDatabaseDownloadAsyncTask;
import com.fancl.iloyalty.asynctask.callback.DatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.TillIdDatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.LoadingDialog;
import com.fancl.iloyalty.pojo.Version;
import com.fancl.iloyalty.service.SettingService;
import com.fancl.iloyalty.util.LogController;

public class AndroidProjectApplication extends Application implements DatabaseDownloadAsyncTaskCallback, TillIdDatabaseDownloadAsyncTaskCallback {

	public static AndroidProjectApplication application;

	private boolean applicationStarted = false;

	private List<Activity> activityList = new ArrayList<Activity>();

	private LoadingDialog loadingDialog;

	public boolean firstCallDbChecking = true;
	
	public int whatsHotUnread = 0;
	public int promotionUnread = 0;
	public int ichannelUnread = 0;

	public AndroidProjectApplication()
	{
		AndroidProjectApplication.application = this;
	}

	/**
	 * Call when application start up
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		LogController.log("AndroidProjectApplication onCreate");

	}

	public void applicationStartup()
	{
		if(!applicationStarted)
		{
			applicationStarted = true;

			//gcm register
			CustomServiceFactory.getGCMService().register(this);

			SharedPreferences sharedPreferences = application.getSharedPreferences(
					Constants.SHARED_PREFERENCE_APPLICATION_KEY,
					Context.MODE_PRIVATE);
			String restoredText = sharedPreferences.getString(Constants.SHARED_PREFERENCE_FIRST_INITIALIZATION_KEY,
					null);
			if (restoredText == null) {
				try {
					this.copyDatabase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LogController.log("IOException error");
					e.printStackTrace();
				}
				LogController.log("NO VALUE");
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(Constants.SHARED_PREFERENCE_FIRST_INITIALIZATION_KEY, "NO");
				editor.commit();
			} else {
				LogController.log("HAS VALUE");
			}

			String registrationId = sharedPreferences.getString(Constants.SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY, null);
			LogController.log("registrationId " + registrationId);

			//process of application startup

			GeneralServiceFactory.getImageService().prepareFileStructure();

			//start thread pool for image
			GeneralServiceFactory.getThreadService().startImageExecutor();
		}
	}

	public void applicationClosing()
	{
		if(applicationStarted)
		{
			//process of application closing

			//close thread pool of image receive
			GeneralServiceFactory.getThreadService().stopImageExecutor();

			applicationStarted = false;
		}

	}

	public void databaseVersionChecking() {
		LogController.log("call databaseVersionChecking");
		if (firstCallDbChecking) {
			return;
		}

		DatabaseDownloadAsyncTask dbVersionCheckingAsyncTask = new DatabaseDownloadAsyncTask(ApiConstant.getAPI(ApiConstant.DATABASE_API), (Constants.DATABASE_FOLDER + Constants.DATABASE_FILE_NAME), this);
		dbVersionCheckingAsyncTask.execute((Void)null);
	}

	private void tillIdDatabaseVersionChecking() {
		LogController.log("call tillIdDatabaseVersionChecking");
		TillIdDatabaseDownloadAsyncTask tillIdDbVersionCheckingAsyncTask = new TillIdDatabaseDownloadAsyncTask(ApiConstant.getAPI(ApiConstant.TILL_ID_DATABASE_API), (Constants.DATABASE_FOLDER + Constants.TILL_ID_DATABASE_FILE_NAME), this);
		tillIdDbVersionCheckingAsyncTask.execute((Void)null);
	}

	@Override
	public void noNeedToDownloadNewDatabase() {
		// TODO Auto-generated method stub
		tillIdDatabaseVersionChecking();
	}

	@Override
	public void databaseStartDownloading() {
		// TODO Auto-generated method stub
		getFrontActivity().runOnUiThread(new Runnable() {
			public void run() {
				loadingDialog = null;
				loadingDialog = new LoadingDialog(getFrontActivity());
				loadingDialog.loading();
			}
		});
	}

	@Override
	public void databaseDownloadFinished() {
		// TODO Auto-generated method stub
		LogController.log("databaseDownloadFinished");
		GeneralServiceFactory.getSQLiteDatabaseService().closeSQLiteDatabase();
		GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();

		try {
			Version version = CustomServiceFactory.getSettingService().currentDatabaseVersion();
			if (version == null) {
				LogController.log("version == nil, redownload");
				databaseVersionChecking();
				return;
			}
			else {
				LogController.log("Download success, open new activity");
				
				//Replace all read content
				SettingService settingService = CustomServiceFactory.getSettingService();
				SharedPreferences sharedPreferences = application.getSharedPreferences(
						Constants.SHARED_PREFERENCE_APPLICATION_KEY,
						Context.MODE_PRIVATE);
				String hotItemArrayString = sharedPreferences.getString(Constants.HOT_ITEM_READ_ARRAY_KEY,
						null);
				if (hotItemArrayString != null) {
					LogController.log("hotItemArrayString: " + hotItemArrayString);
					String[] itemIdList = hotItemArrayString.split(",");
					for (int i = 0; i < itemIdList.length; i++) {
						settingService.updateDatabaseContentAfterUpdating("hot_item", itemIdList[i]);
					}
				}
				String promotionArrayString = sharedPreferences.getString(Constants.PROMOTION_READ_ARRAY_KEY,
						null);
				if (promotionArrayString != null) {
					LogController.log("promotionArrayString: " + promotionArrayString);
					String[] itemIdList = promotionArrayString.split(",");
					for (int i = 0; i < itemIdList.length; i++) {
						settingService.updateDatabaseContentAfterUpdating("promotion", itemIdList[i]);
					}
				}
				String ichannelArrayString = sharedPreferences.getString(Constants.ICHANNEL_READ_ARRAY_KEY,
						null);
				if (ichannelArrayString != null) {
					LogController.log("ichannelArrayString: " + ichannelArrayString);
					String[] itemIdList = ichannelArrayString.split(",");
					for (int i = 0; i < itemIdList.length; i++) {
						settingService.updateDatabaseContentAfterUpdating("ichannel_magazine", itemIdList[i]);
					}
				}
				
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						reloadAllActivityTabBar();
					}
				}, 500);
				
				tillIdDatabaseVersionChecking();
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogController.log("FanclException, redownload");
			databaseVersionChecking();
			return;
		}
	}

	@Override
	public void databaseDownloadFailed() {
		// TODO Auto-generated method stub
		tillIdDatabaseVersionChecking();
	}

	@Override
	public void databaseDownloadingProgress(int progress) {
		LogController.log("databaseDownloadingProgress progress >>> " + progress);
	}

	private void copyDatabase() throws IOException {
		// Open your local db as the input stream
		LogController.log("Going to get the db file");
		InputStream myInput = application.getResources().openRawResource(
				R.raw.iloyalty);
		LogController.log("Get the db file");
		// Path to the just created empty db
		File path = new File(Constants.DATABASE_FOLDER);
		path.mkdirs();
		LogController.log("created the new path");

		// Open the empty db as the output stream
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(Constants.DATABASE_FOLDER + Constants.DATABASE_FILE_NAME);

			// transfer bytes from the inputfile to the outputfile
			LogController.log("write the file");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LogController.log("Going to get the log db file");
		InputStream logDbInput = application.getResources().openRawResource(
				R.raw.userlog);
		LogController.log("Get the log db file");
		// Path to the just created empty db
		File logDbPath = new File(Constants.DATABASE_FOLDER);
		logDbPath.mkdirs();
		LogController.log("created the new path");

		// Open the empty db as the output stream
		OutputStream logDbOutput;
		try {
			logDbOutput = new FileOutputStream(Constants.DATABASE_FOLDER + Constants.LOG_DATABASE_FILE_NAME);

			// transfer bytes from the inputfile to the outputfile
			LogController.log("write the log db file");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = logDbInput.read(buffer)) > 0) {
				logDbOutput.write(buffer, 0, length);
			}
			// Close the streams
			logDbOutput.flush();
			logDbOutput.close();
			logDbInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		LogController.log("Going to get the till id db file");
		InputStream tillIdDbInput = application.getResources().openRawResource(
				R.raw.userlog);
		LogController.log("Get the till id db file");
		// Path to the just created empty db
		File tillIdDbPath = new File(Constants.DATABASE_FOLDER);
		tillIdDbPath.mkdirs();
		LogController.log("created the new path");

		// Open the empty db as the output stream
		OutputStream tillIdDbOutput;
		try {
			tillIdDbOutput = new FileOutputStream(Constants.DATABASE_FOLDER + Constants.TILL_ID_DATABASE_FILE_NAME);

			// transfer bytes from the inputfile to the outputfile
			LogController.log("write the till id db file");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = tillIdDbInput.read(buffer)) > 0) {
				tillIdDbOutput.write(buffer, 0, length);
			}
			// Close the streams
			tillIdDbOutput.flush();
			tillIdDbOutput.close();
			tillIdDbInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addActiveActivity(Activity activity) {
		if (activity != null)
		{
			activityList.add(activity);
		}
	}

	public void removeActiveActivity(Activity activity) {
		if (activity != null)
		{
			activityList.remove(activity);
		}
	}

	public Activity getFrontActivity()
	{
		if(activityList != null)
		{
			if(activityList.size() > 0)
			{
				try {
					return activityList.get(activityList.size()-1);
				} catch (Exception e) {
					return null;
				}
			}
		}

		return null;
	}

	public void removeAllActivity()
	{
		try {
			if(activityList != null)
			{
				for(Activity activity : activityList)
				{
					if(activity != null)
					{
						if(!activity.isFinishing())
						{
							activity.finish();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeExistingActivity() {
		try {
			if(activityList != null)
			{
				for(Activity activity : activityList)
				{
					if(activity != null)
					{
						if (activity instanceof AndroidProjectFrameworkActivity) {
							if(!activity.isFinishing())
							{
								activity.finish();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void noNeedToDownloadNewTillIdDatabase() {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		Runnable mLaunchTask = new Runnable() {
			public void run() {
				Activity activity = getFrontActivity();
				LogController.log("getFrontActivity:"+activity);
				if(activity instanceof AdvertisementActivity){
					LogController.log("not open banner");
					return;
				}
				if (activity instanceof AndroidProjectFrameworkActivity) {
					((AndroidProjectFrameworkActivity) activity).callToOpenAd();
				}
			}
		};

		Handler mHandler = new Handler();
		mHandler.postDelayed(mLaunchTask,5000);
	}

	@Override
	public void tillIdDatabaseStartDownloading() {
		// TODO Auto-generated method stub
		if (loadingDialog == null) {
			getFrontActivity().runOnUiThread(new Runnable() {
				public void run() {
					loadingDialog = null;
					loadingDialog = new LoadingDialog(getFrontActivity());
					loadingDialog.loading();
				}
			});
		}
	}

	@Override
	public void tillIdDatabaseDownloadFinished() {
		// TODO Auto-generated method stub
		GeneralServiceFactory.getSQLiteDatabaseService().closeTillIdDatabase();
		GeneralServiceFactory.getSQLiteDatabaseService().getTillIdDatabase();

		try {
			Version version = CustomServiceFactory.getSettingService().currentTillIdDatabaseVersion();
			if (version == null) {
				LogController.log("version == nil, redownload");
				tillIdDatabaseVersionChecking();
				return;
			}
			else {
				LogController.log("Download success, open new activity");
				if (loadingDialog != null) {
					loadingDialog.stop();
				}

				Activity activity = getFrontActivity();
				if (activity instanceof LoginForgetPasswordActivity || 
						activity instanceof LoginFormActivity ||
						activity instanceof LoginTOSActivity ||
						activity instanceof SignUpEnterMemberIDActivity ||
						activity instanceof SignUpFormActivity ||
						activity instanceof SignUpResultActivity ||
						activity instanceof WelcomeActivity ||
						activity instanceof SignUpScanningBarCodeActivity ||
						activity instanceof LoginNonMemberActivity ||
						activity instanceof SplashScreenActivity) {
					return;
				}

				Intent intent = new Intent(application, WhatsHotHomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				activity.finish();
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogController.log("FanclException, redownload");
			tillIdDatabaseVersionChecking();
			return;
		}

		Runnable mLaunchTask = new Runnable() {
			public void run() {
				Activity activity = getFrontActivity();
				LogController.log("getFrontActivity:"+activity);
				if(activity instanceof AdvertisementActivity){
					LogController.log("not open banner");
					return;
				}
				if (activity instanceof AndroidProjectFrameworkActivity) {
					((AndroidProjectFrameworkActivity) activity).callToOpenAd();
				}
			}
		};

		Handler mHandler = new Handler();
		mHandler.postDelayed(mLaunchTask,5000);
	}

	@Override
	public void tillIdDatabaseDownloadFailed() {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
	}

	@Override
	public void tillIdDatabaseDownloadingProgress(int progress) {
		LogController.log("tillIdDatabaseDownloadingProgress progress >>> " + progress);

	}

	public void reloadAllActivityTabBar() {
		if(activityList != null)
		{
			for (int i = 0; i < activityList.size(); i++) {
				Activity activity = activityList.get(i);
				if (activity instanceof MainTabActivity) {
					((MainTabActivity) activity).setBadgesNumber();
				}

				if (activity instanceof MainTabFragmentActivity) {
					((MainTabFragmentActivity) activity).setBadgesNumber();
				}

				if (activity instanceof WhatsHotHomeActivity) {
					((WhatsHotHomeActivity) activity).relaodUnreadContent();
				}

				if (activity instanceof WhatsHotListActivity) {
					((WhatsHotListActivity) activity).relaodUnreadContent();
				}

				if (activity instanceof BeautyHomeActivity) {
					((BeautyHomeActivity) activity).relaodUnreadContent();
				}

				if (activity instanceof PromotionHomeActivity) {
					((PromotionHomeActivity) activity).relaodUnreadContent();
				}
			}
		}
	}

	public void resetMenu() {
		if (activityList != null) {
			for (int i = 0; i < activityList.size(); i++) {
				Activity activity = activityList.get(i);
				if (activity instanceof MenuActivity) {
					((MenuActivity) activity).resetView();
				}
			}

		}
	}
	
	public int getActiveListCount() {
		return activityList.size();
	}
	
	public void resetLoadingDialog() {
		loadingDialog = null;
	}
}
