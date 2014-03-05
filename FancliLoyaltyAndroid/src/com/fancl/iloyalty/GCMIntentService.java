package com.fancl.iloyalty;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.asynctask.AddAndUpdateUserAsyncTask;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.NetworkConnective;
import com.fancl.iloyalty.util.StringUtil;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private ActivityMonitorService monitorService = GeneralServiceFactory.getActivityMonitorService();

	public GCMIntentService()
	{
		super(Constants.GCM_PROJECT_ID);
	}

	@Override
	protected void onRegistered(final Context context, final String registrationId) {
		LogController.log("GCMIntentService onRegistered");
		LogController.log(("GCMIntentService onRegistered: registrationId = " + registrationId));

		if (!StringUtil.isStringEmpty(registrationId))
		{
			GCMRegistrar.setRegisteredOnServer(context, true);

			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_APPLICATION_KEY, Context.MODE_PRIVATE);
			sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY, registrationId).commit();

			if (NetworkConnective.checkNetwork(this))
			{
				final LocaleService localeService = GeneralServiceFactory.getLocaleService();
				
				if(AndroidProjectApplication.application.getFrontActivity() != null)
				{
					if(!AndroidProjectApplication.application.getFrontActivity().isFinishing())
					{
						AndroidProjectApplication.application.getFrontActivity().runOnUiThread(new Runnable(){
							@Override
							public void run() {
								AddAndUpdateUserAsyncTask addAndUpdateUserAsyncTask = new AddAndUpdateUserAsyncTask();
								addAndUpdateUserAsyncTask.execute((Void) null);
							}
						});
					}
				}
			}
		}
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		LogController.log("GCMIntentService onUnregistered");
		if (GCMRegistrar.isRegisteredOnServer(context))
		{
			GCMRegistrar.setRegisteredOnServer(context, false);

			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_APPLICATION_KEY, Context.MODE_PRIVATE);
			sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY, null).commit();
		}
		else
		{
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			LogController.log("GCMIntentService onUnregistered: Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String message = intent.getStringExtra(Constants.GCM_MESSAGE_RECEIVE_KEY);
		final String pushType = intent.getStringExtra(Constants.GCM_PUSH_TYPE_RECEIVE_KEY);
		final String pushId = intent.getStringExtra(Constants.GCM_PUSH_ID_RECEIVE_KEY);
		final Context aContext = context;
		
		LogController.log("message:"+ message + ", pushType:" + pushType + ", pushId:" + pushId);
		
		if (monitorService.isInBackground())
		{
			generateNotification(context, message, pushType, pushId);
		}
		else
		{
			if (!Constants.isPushAlertDialogDisplaying)
			{
				Constants.isPushAlertDialogDisplaying = true;
				String positiveBtnLabel = "OK";
				DialogInterface.OnClickListener positiveBtnListener = new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Constants.isPushAlertDialogDisplaying = false;
						notificationPushDetail(aContext, pushType, pushId);

					}
				};
				GeneralServiceFactory.getAlertDialogService().makeNativeDialog(AndroidProjectApplication.application.getFrontActivity(), "iFANCL", message, positiveBtnLabel, positiveBtnListener, null, null, true, false);
			}
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		LogController.log("GCMIntentService onDeletedMessages");
	}

	@Override
	public void onError(Context context, String errorId) {
		LogController.log("GCMIntentService onError: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		LogController.log("GCMIntentService onRecoverableError: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message, String pushType, String pushId) {
		int icon = R.drawable.fancl_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, AndroidProjectFrameworkActivity.class);
//		notificationIntent.putExtra(Constants.GCM_PUSH_NOTIFICATION_MESSAGE, message);
//		notificationIntent.putExtra(Constants.GCM_PUSH_TYPE_RECEIVE_KEY, pushType);
//		notificationIntent.putExtra(Constants.GCM_PUSH_ID_RECEIVE_KEY, pushId);
//		notificationIntent.setAction(Intent.ACTION_MAIN);
		
		notificationIntent = backgroundNotificationPushDetail(context, pushType, pushId);
		
//		
		// set intent so it does not start a new activity
		if (pushType.length() > 0 && notificationIntent!=null) {
			AndroidProjectApplication.application.removeAllActivity();
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}
	
	@SuppressWarnings("deprecation")
	public void notificationPushDetail(Context context, String pushType, String pushId){
		if(pushType.equals("campaign")){
			try {
				HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(pushId);

				if(tmpObject == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					Intent intent = new Intent();
					intent = CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, context, true, getString(R.string.whats_hot_category_new_campaign), 4);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity(intent);
//					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, context, true, getString(R.string.whats_hot_category_new_campaign), 4));
					
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(pushType.equals("product")){
			try {
				Product product = CustomServiceFactory
						.getProductService().getProductDetailWithProductId(pushId);
				if(product == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
				Intent intent = new Intent(context, ProductDetailActivity.class);
				intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            	startActivity(intent);
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(pushType.equals("shop")){
			try {
				Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(pushId);
				if(shopDetail == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					Intent intent = new Intent();
					intent = CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, context, 4);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            	startActivity(intent);
//					startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, context, 4));

				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

		}else if(pushType.equals("ichannel")){
			try {
				IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(pushId);
				if(ichannelMagazine == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					Intent intent = new Intent();
					intent = CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, getString(R.string.beauty_ichannel_btn), 4);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            	startActivity(intent);
//					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, getString(R.string.beauty_ichannel_btn), 4));
				
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}else if(pushType.equals("fanclMagazine")){
			try {
				IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(pushId);
				if(ichannelMagazine == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					Intent intent = new Intent();
					intent = CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, getString(R.string.menu_fancl_magazine_btn_title), 4);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            	startActivity(intent);
//					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, getString(R.string.menu_fancl_magazine_btn_title), 4));

				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if(pushType.equals("promotion")){
			try {
				Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(pushId);
				if(promotion == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
					submitPromotionAsyncTask.execute(promotion.getCode());
					
					Intent intent = new Intent();
					intent = CustomServiceFactory.getDetailContentService().getPromotionDetailAction(context, promotion, true, null, 1, 1);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	            	startActivity(intent);
//					startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(context, promotion, true, null, 1, 1));
					
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}else if(pushType.equals("purchase")){
			
		}
		
	}
	
	public static Intent backgroundNotificationPushDetail(Context context, String pushType, String pushId){
		
		if(pushType.equals("campaign")){
			try {
				HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(pushId);

				if(tmpObject == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{

					return (CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, context, true, context.getString(R.string.whats_hot_category_new_campaign), 4));
					
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(pushType.equals("product")){
			try {
				Product product = CustomServiceFactory
						.getProductService().getProductDetailWithProductId(pushId);
				if(product == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
				Intent intent = new Intent(context, ProductDetailActivity.class);
				intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
            	return intent;
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(pushType.equals("shop")){
			try {
				Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(pushId);
				if(shopDetail == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					return (CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, context, 4));

				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

		}else if(pushType.equals("ichannel")){
			try {
				IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(pushId);
				if(ichannelMagazine == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
				return (CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, context.getString(R.string.beauty_ichannel_btn), 4));
				
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}else if(pushType.equals("fanclMagazine")){
			try {
				IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(pushId);
				if(ichannelMagazine == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					return (CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, context, true, context.getString(R.string.menu_fancl_magazine_btn_title), 4));

				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if(pushType.equals("promotion")){
			try {
				Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(pushId);
				if(promotion == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							context).create();
					alertDialog.setMessage(context.getString(R.string.alert_content_expired));
					alertDialog.setButton(context.getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
					SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
					submitPromotionAsyncTask.execute(promotion.getCode());
					
					return (CustomServiceFactory.getDetailContentService().getPromotionDetailAction(context, promotion, true, null, 1, 1));
					
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}else if(pushType.equals("purchase")){
			
		}
		
		return null;
		
	}
}
