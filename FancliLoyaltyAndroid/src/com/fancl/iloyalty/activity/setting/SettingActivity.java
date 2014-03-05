package com.fancl.iloyalty.activity.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.login.WelcomeActivity;
import com.fancl.iloyalty.activity.product.ProductQandAActivity;
import com.fancl.iloyalty.asynctask.GetUserReceiptSettingAsyncTask;
import com.fancl.iloyalty.asynctask.SetUserReceiptSettingAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetUserReceiptSettingAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.SetUserReceiptSettingAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.ReceiptSetting;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.LogController;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.impl.FacebookService30Impl;
import com.gt.snssharinglibrary.service.impl.TwitterServiceImpl;

public class SettingActivity extends MainTabActivity implements GetUserReceiptSettingAsyncTaskCallback, 
SetUserReceiptSettingAsyncTaskCallback, SNSServiceCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.6
	private View settingLayout;

	private Button facebookButton;
	private Button twitterButton;
	private Button emailButton;
	private Button loginButton;
	private RadioButton enButton;
	private RadioButton tcButton;
	private RadioButton scButton;

	private String emailBtnBool = "N";
	private boolean isBackBlocked = true;

	private SNSService facebookServiceImpl;
	private SNSService twitterServiceImpl;

	private final int FACEBOOK_CODE = 0;
	private final int TWITTER_CODE = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Config.TWITTER_CONSUMER_KEY = "jvKys4Lr2afHg7WxvQ4M1g";
		Config.TWITTER_CONSUMER_SECRET = "Z9icVCBu2j1Da1O2mMEiggkBcXgmfk2OjAE7qvrV0A";
		Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.btn_cross;
		twitterServiceImpl = new TwitterServiceImpl(SettingActivity.this);
		twitterServiceImpl.setSNSServiceCallback(SettingActivity.this);
		
		Config.facebook30SharingType = SHARING_TYPE.WEB_DIALOG;
		Config.FACEBOOK_PERMISSIONS = new String[] { "email", "user_birthday", "read_friendlists", "user_likes" };
		facebookServiceImpl = new FacebookService30Impl();
		facebookServiceImpl.setSNSServiceCallback(SettingActivity.this);
		facebookServiceImpl.onCreate(SettingActivity.this, savedInstanceState);
		
		headerTitleTextView.setText(this.getResources().getString(R.string.setting));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);

		if (CustomServiceFactory.getAccountService().isLogin()) {
			if (checkLoadingDialog()) {
				loadingDialog.loading();
			}
			GetUserReceiptSettingAsyncTask getUserReceiptSettingAsyncTask = new GetUserReceiptSettingAsyncTask(this);
			getUserReceiptSettingAsyncTask.execute();
		}

		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void setupSpaceLayout() {
		settingLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.setting_page, null);
		spaceLayout.addView(settingLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		facebookButton = (Button)findViewById(R.id.facebookLogin);
		if (facebookServiceImpl.isLogged(SettingActivity.this)) {
			facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
			facebookButton.setText(getResources().getString(R.string.logout));
		}
		else {
			facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
			facebookButton.setText(getResources().getString(R.string.login_btn_title));
		}
		facebookButton.setOnClickListener(new Button.OnClickListener(){ 

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (facebookServiceImpl.isLogged(SettingActivity.this)) {
					
					
					GeneralServiceFactory.getAlertDialogService().makeNativeDialog(SettingActivity.this, "", 
							getResources().getString(R.string.alert_confirm_logout),
							getString(R.string.confirm_btn_title),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							facebookServiceImpl.logout(SettingActivity.this, handler);
							try {
								CustomServiceFactory.getSettingService().addUserLogWithSection("Setting", "", "", "", "Facebook", "Logout", "");
							} catch (FanclException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					},
					getString(R.string.cancel_btn_title), 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							dialog.cancel();
						}
					}, false, false);
					
					
				}
				else {
					facebookServiceImpl.login(SettingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Setting", "", "", "", "Facebook", "Login", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}         

		});     

		twitterButton = (Button)findViewById(R.id.twitterLogin);
		if (twitterServiceImpl.isLogged(SettingActivity.this)) {
			twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
			twitterButton.setText(getResources().getString(R.string.logout));
		}
		else {
			twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
			twitterButton.setText(getResources().getString(R.string.login_btn_title));
		}
		twitterButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				if (twitterServiceImpl.isLogged(SettingActivity.this)) {
					
					
					GeneralServiceFactory.getAlertDialogService().makeNativeDialog(SettingActivity.this, "", 
							getResources().getString(R.string.alert_confirm_logout),
							getString(R.string.confirm_btn_title),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							twitterServiceImpl.logout(SettingActivity.this, handler);
							try {
								CustomServiceFactory.getSettingService().addUserLogWithSection("Setting", "", "", "", "Twitter", "Logout", "");
							} catch (FanclException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					},
					getString(R.string.cancel_btn_title), 
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							dialog.cancel();
						}
					}, false, false);
					
					
				}
				else {
					twitterServiceImpl.login(SettingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Setting", "", "", "", "Twitter", "Login", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}         

		});

		emailButton = (Button)findViewById(R.id.email_switch);
		emailButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (emailBtnBool.equals("Y")) {
					emailBtnBool = "N";
					emailButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_cursor_off));
				}
				else {
					emailBtnBool = "Y";
					emailButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_cursor_on));
				}
			}         

		});

		loginButton = (Button)findViewById(R.id.login_btn);
		loginButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralServiceFactory.getAlertDialogService().makeNativeDialog(SettingActivity.this, "", 
						getResources().getString(R.string.alert_confirm_logout),
						getString(R.string.confirm_btn_title),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						logoutAction();
						
						try {
							CustomServiceFactory.getSettingService().addUserLogWithSection("Setting", "", "", "", "", "Logout", "");
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				},
				getString(R.string.cancel_btn_title), 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						dialog.cancel();
					}
				}, false, false);
			}         
		});

		enButton = (RadioButton) findViewById(R.id.enRadioButton);
		tcButton = (RadioButton) findViewById(R.id.tcRadioButton);
		scButton = (RadioButton) findViewById(R.id.scRadioButton);

		LANGUAGE_TYPE languageType = GeneralServiceFactory.getLocaleService().getCurrentLanguageType();
		if (languageType.equals(LANGUAGE_TYPE.EN)) {
			enButton.setChecked(true);
		}
		else if (languageType.equals(LANGUAGE_TYPE.TC)) {
			tcButton.setChecked(true);
		}
		else if (languageType.equals(LANGUAGE_TYPE.SC)) {
			scButton.setChecked(true);
		}

		enButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralServiceFactory.getLocaleService().setEnglish(SettingActivity.this, false);
				changeLanguage();
			}
		});

		tcButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralServiceFactory.getLocaleService().setTChinese(SettingActivity.this, false);
				changeLanguage();
			}
		});

		scButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralServiceFactory.getLocaleService().setSChinese(SettingActivity.this, false);
				changeLanguage();
			}
		});
	}

	private void logoutAction() {
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_MEMBER_ID_KEY, "").commit();

		Intent intent = new Intent(SettingActivity.this, WelcomeActivity.class);
		application.removeExistingActivity();
		startActivity(intent);
	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		isBackBlocked = false;
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		onBackPressed();
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		if (results == null) {
			return;
		}
		if (results instanceof ReceiptSetting) {
			if (((ReceiptSetting) results).getStatus() == 0) {
				if (((ReceiptSetting) results).getEmailReceipt().equals("Y")) {
					emailBtnBool = "Y";
					emailButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_cursor_on));
				}
			}
		}
		else {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, ((FanclGeneralResult) results).getErrMsgEn(), ((FanclGeneralResult) results).getErrMsgZh(), ((FanclGeneralResult) results).getErrMsgSc()),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
	}

	@Override
	public void onBackPressed() {
		if (CustomServiceFactory.getAccountService().isLogin()) {
			if (isBackBlocked) {
				if (checkLoadingDialog()) {
					loadingDialog.loading();
				}
				
				SetUserReceiptSettingAsyncTask setUserReceiptSettingAsyncTask = new SetUserReceiptSettingAsyncTask(this);
				setUserReceiptSettingAsyncTask.execute("N", emailBtnBool);
			}
			else {
				super.onBackPressed();
			}
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public void logginStatus(int snsCode, boolean isSuccessLogin,
			Object errorObject) {
		LogController.log("logginStatus >> "+ snsCode + " " + isSuccessLogin);
		if (!isSuccessLogin) {
			return;
		}
		if (snsCode == FACEBOOK_CODE) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
					facebookButton.setText(getResources().getString(R.string.logout));
				}
			});
		}
		else if (snsCode == TWITTER_CODE) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
					twitterButton.setText(getResources().getString(R.string.logout));
				}
			});
		}
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> "+ snsCode + " " + isSuccessGetProfile);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (facebookServiceImpl != null)
		{
			facebookServiceImpl.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		if (!isSuccessLogout) {
			return;
		}
		LogController.log("loggoutStatus >> "+ snsCode + " " + isSuccessLogout);
		if (snsCode == FACEBOOK_CODE) {
			facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
			facebookButton.setText(getResources().getString(R.string.login_btn_title));
		}
		else if (snsCode == TWITTER_CODE) {
			twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
			twitterButton.setText(getResources().getString(R.string.login_btn_title));
		}
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  "+ snsCode + " " + isSuccessPost);
	}

	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}
	
	public void changeLanguage(){
//		loginButton.setText(getResources().getString(R.string.logout));
//		
//		if (twitterServiceImpl.isLogged(SettingActivity.this)) {
//			twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
//			twitterButton.setText(getResources().getString(R.string.logout));
//		}
//		else {
//			twitterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
//			twitterButton.setText(getResources().getString(R.string.login_btn_title));
//		}
//		
//		if (facebookServiceImpl.isLogged(SettingActivity.this)) {
//			facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_logout));
//			facebookButton.setText(getResources().getString(R.string.logout));
//		}
//		else {
//			facebookButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_setting_login));
//			facebookButton.setText(getResources().getString(R.string.login_btn_title));
//		}
//		
//		headerTitleTextView.setText(this.getResources().getString(R.string.setting));
//		
//		TextView language = (TextView)findViewById(R.id.languageText);
//		language.setText(getResources().getString(R.string.language));
//		
//		TextView email = (TextView)findViewById(R.id.emailReceiptText);
//		email.setText(getResources().getString(R.string.email_receipt));
		
		application.resetMenu();
		
		Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
		startActivity(intent);
		finish();
	}
}
