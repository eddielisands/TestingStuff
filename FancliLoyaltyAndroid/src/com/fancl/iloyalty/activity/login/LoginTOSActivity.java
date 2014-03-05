package com.fancl.iloyalty.activity.login;

import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.asynctask.CardReplacementAsyncTask;
import com.fancl.iloyalty.asynctask.callback.CardReplacementAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.responseimpl.TOSResult;
import com.fancl.iloyalty.util.LogController;

public class LoginTOSActivity extends AndroidProjectFrameworkActivity implements CardReplacementAsyncTaskCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.2.3
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.login_tos_page);
        
        try {
			CustomServiceFactory.getSettingService().addUserLogWithSection("LoginL", "TermOfAgreement", "", "", "", "View", "");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        addListener();
    }
	
	private void addListener () {
		// Back Button
		RelativeLayout tosBackBtnLayout = (RelativeLayout)findViewById(R.id.login_tos_back_btn);
		tosBackBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// Alert Dialog
				popBackAlert();
			}
		});
		
		// Accept Button
		RelativeLayout tosAcceptBtnLayout = (RelativeLayout)findViewById(R.id.login_tos_accept_btn);
		tosAcceptBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String memberId = getIntent().getStringExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY);
				
				CardReplacementAsyncTask cardReplacementAsyncTask = new CardReplacementAsyncTask(LoginTOSActivity.this);
				cardReplacementAsyncTask.execute(memberId);
				
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
	
	private void popBackAlert() {
		GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", getString(R.string.login_tos_page_back_msg), getString(R.string.confirm_btn_title),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity				
						finish();
					}
				},
				getString(R.string.cancel_btn_title),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
							dialog.cancel();
					}
				},
				false, false);
	}

	@Override
	public void onPostExecuteCallback(TOSResult results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if (results == null) {
			return;
		}

		if (results.getStatus() == 0) {
			LogController.log("Status : 0 is success");
//			Intent intent = new Intent(LoginTOSActivity.this, WhatsHotHomeActivity.class);
			Intent intent = new Intent(LoginTOSActivity.this, SignUpResultActivity.class);
			application.removeExistingActivity();
			startActivity(intent);

		} else if (results.getStatus() == 1) {
			LogController.log("Status : 1 is fail");

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
}
