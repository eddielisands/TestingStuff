package com.fancl.iloyalty.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.asynctask.ForgetPasswordAsyncTask;
import com.fancl.iloyalty.asynctask.callback.ForgetPasswordAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;

public class LoginForgetPasswordActivity extends AndroidProjectFrameworkActivity implements ForgetPasswordAsyncTaskCallback {
	private EditText mobileEditText;
	private EditText emailEditText;
	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.3, 1.3.1
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.login_forget_password_page);  
        
        try {
			CustomServiceFactory.getSettingService().addUserLogWithSection("Login", "Forget Password Page", "", "", "", "View", "");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        addListener();
    }
	
	private void addListener () {
		mobileEditText = (EditText) findViewById(R.id.mobile_edit_text);
		emailEditText = (EditText) findViewById(R.id.email_edit_text);
		RelativeLayout cancelBtnLayout = (RelativeLayout) findViewById(R.id.login_forget_password_cancel_btn);
		cancelBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		RelativeLayout confirmBtnLayout = (RelativeLayout) findViewById(R.id.login_forget_password_confirm_btn);
		confirmBtnLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ForgetPasswordAsyncTask forgetPasswordAsyncTask = new ForgetPasswordAsyncTask(LoginForgetPasswordActivity.this);
				forgetPasswordAsyncTask.execute(mobileEditText.getText().toString(), emailEditText.getText().toString());
				
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
				
			}
		});
		
	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if (results == null) {
			return;
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
