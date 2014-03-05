package com.fancl.iloyalty.activity.myaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.product.ProductQandAActivity;
import com.fancl.iloyalty.asynctask.GetPurchaseHistoryAsyncTask;
import com.fancl.iloyalty.asynctask.GetUserProfileAsyncTask;
import com.fancl.iloyalty.asynctask.UpdateUserPasswordAsyncTask;
import com.fancl.iloyalty.asynctask.callback.UpdateUserPasswordAsyncTaskCallback;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class MyAccountChangePasswordActivity extends MainTabActivity implements UpdateUserPasswordAsyncTaskCallback{
	
	private RelativeLayout changePasswordLayout;
	private EditText oldPassword;
	private EditText newPassword;
	private EditText reEnterPassword;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.2.3
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		navigationBarCancelBtn.setVisibility(View.VISIBLE);
		
		navigationBarRightBtn.setVisibility(View.VISIBLE);
		navigationBarRightTextView.setText(getString(R.string.save_btn_title));

		headerTitleTextView.setText(this.getResources().getString(R.string.menu_my_account_btn_title));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		changePasswordLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.my_account_change_password, null);
		spaceLayout.addView(changePasswordLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		navigationBarCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		oldPassword = (EditText) findViewById(R.id.oldPasswordEditText);
		newPassword = (EditText) findViewById(R.id.newPasswordEditText);
		reEnterPassword = (EditText) findViewById(R.id.reEnterPasswordEditText);
		
		navigationBarRightBtn.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				String oldPasswordText = oldPassword.getText().toString();
				String newPasswordText = newPassword.getText().toString();
				String reEnterPasswordText = reEnterPassword.getText().toString();
				
				if(newPasswordText.equals(reEnterPasswordText)){
					UpdateUserPasswordAsyncTask updatePasswordAsyncTask = new UpdateUserPasswordAsyncTask(MyAccountChangePasswordActivity.this);
					updatePasswordAsyncTask.execute(oldPasswordText,newPasswordText,reEnterPasswordText);
					
				}else{
					AlertDialog alertDialog = new AlertDialog.Builder(
							MyAccountChangePasswordActivity.this).create();
					alertDialog.setMessage("Please re-enter your new password");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}
				

			}
		});
		
		
	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		if (results == null) {
			return;
		}
		
		final FanclGeneralResult status = results;

		GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
				GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, results.getErrMsgEn(), results.getErrMsgZh(), results.getErrMsgSc()),
				getString(R.string.ok_btn_title),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				dialog.cancel();
				if (status.getStatus() == 0) {
					finish();
				}
			}
		},
		"", null, false, false);
		
		
	}
		
	
	
}
