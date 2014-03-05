package com.fancl.iloyalty.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;
import com.fancl.iloyalty.asynctask.LoginAsyncTask;
import com.fancl.iloyalty.asynctask.ValidationAsyncTask;
import com.fancl.iloyalty.asynctask.callback.LoginAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.ValidationAsyncTaskCallback;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.pojo.ValidateUserParam;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.responseimpl.ValidationResult;
import com.fancl.iloyalty.util.LogController;

public class LoginFormActivity extends AndroidProjectFrameworkActivity implements LoginAsyncTaskCallback, ValidationAsyncTaskCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.3, 1.3.1
	public User user;
	
	private String passInEmail;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.login_page);        
        
        passInEmail = (String)this.getIntent().getExtras().getSerializable(Constants.LOGIN_FORM_MEMBER_ID_KEY);
                
        EditText loginNameTextView = (EditText) findViewById(R.id.login_email_field);
        loginNameTextView.setText(passInEmail);
        
        addListener();
    }
	
	private void addListener () {
		// Listener for Cancel Button
		RelativeLayout cancelBtnLayout = (RelativeLayout) findViewById(R.id.cancel_btn);
		cancelBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Listener for Forget Password Button
		RelativeLayout forgetPasswordBtnLayout = (RelativeLayout) findViewById(R.id.forget_password_btn);
		forgetPasswordBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// Go to the web login page, when the login button is pressed.
				Intent intent = new Intent(LoginFormActivity.this, LoginForgetPasswordActivity.class);
				startActivity(intent);
			}
		});
		
		// Listener for Confirm Button
		RelativeLayout confirmBtnLayout = (RelativeLayout) findViewById(R.id.confirm_btn);
		confirmBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText loginNameTextView = (EditText) findViewById(R.id.login_email_field);
				EditText pwdTextView = (EditText) findViewById(R.id.login_pwd_field);
				
				String loginNameStr = loginNameTextView.getText().toString();
				String pwdStr = pwdTextView.getText().toString();
				
				loginFromApi(loginNameStr, pwdStr);
			}
		});
	}
	
	private void loginFromApi(String loginName, String password) {
		if (checkLoadingDialog()) {
			loadingDialog.loading();
			
			final Timer t = new Timer();
			t.schedule(new TimerTask() {
				public void run() {
					if (loadingDialog != null) {
						loadingDialog.stop();
					}
					// when the task active then close the dialog
					t.cancel(); 
				}
			}, Constants.LOADING_DIALOG_TIMEOUT); 
		}
		
		LoginAsyncTask loginAsyncTask = new LoginAsyncTask(this);
		loginAsyncTask.execute(loginName, password);
	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		if (results == null) {
			return;
		}

		// status: 0 is success, 1 is fail, 2 is success but not yet register, 3 is success but not yet accept TOS
		if (results.getStatus() == 0) {
			LogController.log("Status : 0 is success");
			String fanclMemberId = results.getFanclMemberId();
			
			LogController.log("fanclMemberId " + fanclMemberId);
			
			ValidateUserParam validateUserParam = new ValidateUserParam();
			validateUserParam.setFanclMemberId(fanclMemberId);
			
			ValidationAsyncTask validationAsyncTask = new ValidationAsyncTask(this);
			validationAsyncTask.execute(validateUserParam);

		} else if (results.getStatus() == 1) {
			LogController.log("Status : 1 is fail");
			if (loadingDialog != null) {
				loadingDialog.stop();
			}

			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, results.getErrMsgEn(), results.getErrMsgZh(), results.getErrMsgSc()),
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
	public void onPostUserValidateUserAsyncTaskCallback(
			ValidationResult validationResult) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if (validationResult.getStatus() == null) {
			return;
		}
		
		// status: 0 is success, 1 is fail, 2 is success but not yet register, 3 is success but not yet accept TOS
		if (validationResult.getStatus() == 0) {
			LogController.log("Status : 0 is success");
			
//			Intent intent = new Intent(LoginFormActivity.this, WhatsHotHomeActivity.class);
			Intent intent = new Intent(LoginFormActivity.this, SignUpResultActivity.class);
			application.removeExistingActivity();
			startActivity(intent);
			
		} else if (validationResult.getStatus() == 1) {
			LogController.log("Status : 1 is fail");
			
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
			GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, validationResult.getErrMsgEn(), validationResult.getErrMsgZh(), validationResult.getErrMsgSc()),
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
}
