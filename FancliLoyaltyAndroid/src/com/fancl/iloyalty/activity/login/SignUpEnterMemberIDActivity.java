package com.fancl.iloyalty.activity.login;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.asynctask.ValidationAsyncTask;
import com.fancl.iloyalty.asynctask.callback.ValidationAsyncTaskCallback;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.pojo.ValidateUserParam;
import com.fancl.iloyalty.responseimpl.ValidationResult;
import com.fancl.iloyalty.util.LogController;

public class SignUpEnterMemberIDActivity extends AndroidProjectFrameworkActivity implements ValidationAsyncTaskCallback {

	/** Called when the activity is first created. */
	
	public User user;
	
	private Activity activity;
	
	private EditText memberIdEditText1;
	private EditText memberIdEditText2;
	private EditText memberIdEditText3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sign_up_enter_member_id_page);
		
//		// Alert Dialog
//		GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", getString(R.string.sign_up_scan_member_id_msg), getString(R.string.confirm_btn_title),
//		new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int id) {
//				// if this button is clicked, close
//				// current activity				
//				Intent intent = new Intent(SignUpEnterMemberIDActivity.this, SignUpScanningBarCodeActivity.class);
//				startActivityForResult(intent, Constants.SIGN_UP_BAR_CODE_INTENT);
//			}
//		},
//		getString(R.string.cancel_btn_title),
//		new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int id) {
//				// if this button is clicked, just close
//				// the dialog box and do nothing
//					dialog.cancel();
//			}
//		},
//		false, false);
		
		addListener();
	}

	private void addListener() {
		//Listener for EditText
		memberIdEditText1 = (EditText)findViewById(R.id.member_id_edit_text_1);
		memberIdEditText2 = (EditText)findViewById(R.id.member_id_edit_text_2);
		memberIdEditText3 = (EditText)findViewById(R.id.member_id_edit_text_3);
		
		
		memberIdEditText1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().length() > 1) {					
					memberIdEditText2.requestFocus();
				}
			}
		});
		
		memberIdEditText2.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().length() > 6) {					
					memberIdEditText3.requestFocus();
				}
			}
		});
		
		memberIdEditText3.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.toString().length() > 3) {					
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(memberIdEditText3.getWindowToken(), 0);
				}
			}
		});
		
		// Listener for Cancel Button
		RelativeLayout cancelBtnLayout = (RelativeLayout)findViewById(R.id.cancel_btn);
		cancelBtnLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// Go to the web login page, when the login button is pressed.				
				finish();
			}
		});
		
		// Listener for Confirm Button
		RelativeLayout confirmBtnLayout = (RelativeLayout)findViewById(R.id.confirm_btn);
		confirmBtnLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				confirmBtnAction();
			}
		});
	}
	
	private void confirmBtnAction() {
		String fanclMemberId = memberIdEditText1.getText().toString() + memberIdEditText2.getText().toString() + memberIdEditText3.getText().toString();
		
		LogController.log("fanclMemberId " + fanclMemberId);
		
		ValidateUserParam validateUserParam = new ValidateUserParam();
		validateUserParam.setFanclMemberId(fanclMemberId);
		
		ValidationAsyncTask accountServiceAsyncTask = new ValidationAsyncTask(this);
		accountServiceAsyncTask.execute(validateUserParam);
		
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
		
	private void resetActiveBox () {
		memberIdEditText1.setText("");
		memberIdEditText2.setText("");
		memberIdEditText3.setText("");
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
	  	switch(requestCode) { 
		    case (Constants.SIGN_UP_BAR_CODE_INTENT) : { 
			      if (resultCode == SignUpEnterMemberIDActivity.RESULT_OK) { 
				      String newText = data.getStringExtra(Constants.SIGN_UP_BAR_CODE_RETURN_KEY);
				      // TODO Update your TextView.
				      LogController.log("SignUpEnterMemberIDActivity onActivityResult : " + newText);
				      
				      if (newText.length() > 2) {
				    	  memberIdEditText1.setText(newText.substring(0, 2));
				      }
				      if (newText.length() > 10) {
				    	  memberIdEditText2.setText(newText.substring(2, 10));
				      }
				      if (newText.length() >= 13) {
				    	  memberIdEditText3.setText(newText.substring(10, 13));
				      }
			      } 
		      break; 
		    } 
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
			
			Intent resultIntent = new Intent(SignUpEnterMemberIDActivity.this, LoginFormActivity.class);
			resultIntent.putExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY, "");
			startActivity(resultIntent);
//			resultIntent.putExtra(Constants.SIGN_UP_ENTER_MEMBER_ID_RETURN_KEY, user.getEmail());
//			setResult(SignUpEnterMemberIDActivity.RESULT_OK, resultIntent);
			finish();
			
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
			
		} else if (validationResult.getStatus() == 2) {
			LogController.log("Status : 2 is success but not yet register");
			String fanclMemberId = memberIdEditText1.getText().toString() + memberIdEditText2.getText().toString() + memberIdEditText3.getText().toString();
			String name = validationResult.getName();
			
			Intent intent = new Intent(SignUpEnterMemberIDActivity.this, SignUpFormActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("id", fanclMemberId);
			startActivity(intent);
			finish();
			
		} else if (validationResult.getStatus() == 3) {
			LogController.log("Status : 3 is success but not yet accept TOS");
			String fanclMemberId = memberIdEditText1.getText().toString() + memberIdEditText2.getText().toString() + memberIdEditText3.getText().toString();
			Intent intent = new Intent(SignUpEnterMemberIDActivity.this, LoginTOSActivity.class);
			intent.putExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY, fanclMemberId);
			startActivity(intent);
			finish();
		}
		
//		resetActiveBox();
	}
}
