package com.fancl.iloyalty.activity.login;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.CustomSpinnerActivity;
import com.fancl.iloyalty.asynctask.RegistrationAsyncTask;
import com.fancl.iloyalty.asynctask.callback.RegistrationAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.FormContent;
import com.fancl.iloyalty.pojo.UserRegistrationParam;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.LogController;

public class SignUpFormActivity extends AndroidProjectFrameworkActivity implements RegistrationAsyncTaskCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.2.1, 1.2.2

	private Spinner spinner;

	private List<FormContent> monthContentList;
	private String[] yearContentList = new String[100];
	private List<FormContent> genderContentList;
	private List<FormContent> skinContentList;
	private List<FormContent> countryContentList;

	private LocaleService localeService;

	private EditText nameEditText;
	private EditText surnameEditText;
	private EditText emailEditText;
	private EditText retypeEmailEditText;
	private EditText mobileEditText;
	private EditText passwordEditText;
	private EditText retypePasswordEditText;
	private EditText livingCityEditText;
	private EditText address1EditText;
	private EditText address2EditText;
	private EditText address3EditText;
	private TextView signUpFormBirthMonthContent;
	private TextView signUpFormBirthYearContent;
	private TextView signUpFormGenderContent;
	private TextView signUpFormSkinTypeContent;
	private TextView signUpFormLivingCountryContent;

	private int monthIndex= -1;
	private int yearIndex= -1;
	private int genderIndex= -1;
	private int skinIndex= -1;
	private int countryIndex= -1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			CustomServiceFactory.getSettingService().addUserLogWithSection("Login", "RegeristationPage", "", "", "", "View", "");
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String name = getIntent().getStringExtra("name");
		String memberId = getIntent().getStringExtra("id");

		setContentView(R.layout.sign_up_form_page);

		localeService = GeneralServiceFactory.getLocaleService();

		TextView signUpFormMemberIdTextView = (TextView)findViewById(R.id.sign_up_form_member_id_content);
		TextView signUpFormAccountNameTextView = (TextView)findViewById(R.id.sign_up_form_account_name_content);

		signUpFormMemberIdTextView.setText(memberId);
		signUpFormAccountNameTextView.setText(name);

		nameEditText = (EditText) findViewById(R.id.name_edit_text);
		surnameEditText = (EditText) findViewById(R.id.surname_edit_text);
		emailEditText = (EditText) findViewById(R.id.email_edit_text);
		retypeEmailEditText = (EditText) findViewById(R.id.retype_email_edit_text);
		mobileEditText = (EditText) findViewById(R.id.mobile_edit_text);
		passwordEditText = (EditText) findViewById(R.id.password_edit_text);
		retypePasswordEditText = (EditText) findViewById(R.id.retype_password_edit_text);
		livingCityEditText = (EditText) findViewById(R.id.city_edit_text);
		address1EditText = (EditText) findViewById(R.id.address1_edit_text);
		address2EditText = (EditText) findViewById(R.id.address2_edit_text);
		address3EditText = (EditText) findViewById(R.id.address3_edit_text);

		signUpFormBirthMonthContent = (TextView)findViewById(R.id.sign_up_form_month_of_birth_content);
		signUpFormBirthYearContent = (TextView)findViewById(R.id.sign_up_form_year_of_birth_content);
		signUpFormGenderContent = (TextView)findViewById(R.id.sign_up_form_gender_content);
		signUpFormSkinTypeContent = (TextView)findViewById(R.id.sign_up_form_skin_type_content);
		signUpFormLivingCountryContent = (TextView)findViewById(R.id.sign_up_form_living_country_content);

		// Get Chooses        
		try {
			monthContentList = CustomServiceFactory.getAccountService().getFormContentWithType("month");
			genderContentList = CustomServiceFactory.getAccountService().getFormContentWithType("gender");
			skinContentList = CustomServiceFactory.getAccountService().getFormContentWithType("skin");
			countryContentList = CustomServiceFactory.getAccountService().getFormContentWithType("country");

			Calendar today = Calendar.getInstance();
			int year = today.get(Calendar.YEAR);

			for (int i = 0; i < 100; i++) {
				yearContentList[i] = String.valueOf(year-i); 
			}

		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addListener();
	}

	private void addListener() {
		// Back Button
		RelativeLayout signUpFormBackBtn = (RelativeLayout)findViewById(R.id.sign_up_form_back_btn);
		signUpFormBackBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Confirm Button
		RelativeLayout signUpFormConfirmBtn = (RelativeLayout)findViewById(R.id.sign_up_form_confirm_button);
		signUpFormConfirmBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callRegister();
			}
		});

		// Month of Birth Button
		RelativeLayout signUpFormMonthOfBirthBtn = (RelativeLayout)findViewById(R.id.sign_up_form_month_of_birth_button);
		signUpFormMonthOfBirthBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpFormActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(monthContentList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_MONTH);
			}
		});

		// Year of Birth Button
		RelativeLayout signUpFormYearOfBirthBtn = (RelativeLayout)findViewById(R.id.sign_up_form_year_of_birth_button);
		signUpFormYearOfBirthBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpFormActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, yearContentList);
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_YEAR);
			}
		});

		// Gender
		RelativeLayout signUpFormGenderBtn = (RelativeLayout)findViewById(R.id.sign_up_form_gender_button);
		signUpFormGenderBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpFormActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(genderContentList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_GENDER);
			}
		});

		// Skin Type
		RelativeLayout signUpFormSkinTypeBtn = (RelativeLayout)findViewById(R.id.sign_up_form_skin_type_button);
		signUpFormSkinTypeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpFormActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(skinContentList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_SKIN);
			}
		});

		// Living Country
		RelativeLayout signUpFormLivingCountryBtn = (RelativeLayout)findViewById(R.id.sign_up_form_living_country_button);
		signUpFormLivingCountryBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SignUpFormActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(countryContentList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_COUNTRY);
			}
		});
	}

	private void callRegister() {
		boolean isError = false;
		String errorMsg = "";		

		if (nameEditText.getText().toString().length() == 0 && surnameEditText.getText().toString().length() == 0) {
			isError = true;
			errorMsg = "registration first name empty";
		}
		else if (emailEditText.getText().toString().length() == 0 || retypeEmailEditText.getText().toString().length() == 0) {
			isError = true;
			errorMsg = "registration email empty";
		}
		else if (mobileEditText.getText().toString().length() == 0) {
			isError = true;
			errorMsg = "registration mobile empty";
		}
		else if (passwordEditText.getText().toString().length() == 0 || retypePasswordEditText.getText().toString().length() == 0) {
			isError = true;
			errorMsg = "registration password empty";
		}
		else if (signUpFormGenderContent.getText().length() == 0) {
			isError = true;
			errorMsg = "gender empty";
		}

		if (isError) {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", errorMsg, getString(R.string.confirm_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity				
					dialog.cancel();
				}
			},
			null,
			null,
			false, false);
		}
		else {
			String country;
			String skinType;
			String monthOfBirth;
			String yearOfBirth;
			String gender;
			if (countryIndex != -1) {
				FormContent formContent = countryContentList.get(countryIndex);
				country = formContent.getCode();
			}
			else {
				country = "-";
			}

			if (skinIndex != -1) {
				FormContent formContent = skinContentList.get(skinIndex);
				skinType = formContent.getCode();
			}
			else {
				skinType = "-";
			}

			if (monthIndex != -1) {
				FormContent formContent = monthContentList.get(monthIndex);
				monthOfBirth = formContent.getCode();
			}
			else {
				monthOfBirth = "";
			}

			if (yearIndex != -1) {
				yearOfBirth = yearContentList[yearIndex];
			}
			else {
				yearOfBirth = "";
			}

			if (genderIndex != -1) {
				FormContent formContent = genderContentList.get(genderIndex);
				gender = formContent.getCode();
			}
			else {
				FormContent formContent = genderContentList.get(0);
				gender = formContent.getCode();
			}

			String id = getIntent().getStringExtra("id");
			String surname = surnameEditText.getText().toString();
			String name = nameEditText.getText().toString();
			String mobile = mobileEditText.getText().toString();
			String email = emailEditText.getText().toString();
			String retypeEmail = retypeEmailEditText.getText().toString();
			String password = passwordEditText.getText().toString();
			String retypePassword = retypePasswordEditText.getText().toString();
			String address1 = address1EditText.getText().toString();
			String address2 = address2EditText.getText().toString();
			String address3 = address3EditText.getText().toString();
			String city = livingCityEditText.getText().toString();

			UserRegistrationParam userRegistrationParam = new UserRegistrationParam(id, surname, name, mobile, email, 
					retypeEmail, gender, password, retypePassword, skinType, address1, address2, address3, country, 
					city, monthOfBirth, yearOfBirth);

			RegistrationAsyncTask registrationAsyncTask = new RegistrationAsyncTask(this);
			registrationAsyncTask.execute(userRegistrationParam);
			
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
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
		case (Constants.CUSTOM_SPINNER_SIGN_UP_MONTH) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
				LogController.log("SignUpFormActivity onActivityResult : " + newInt);
				monthIndex = newInt;
				signUpFormBirthMonthContent.setText(formContentToString(monthContentList.get(newInt)));
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_YEAR) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
				LogController.log("SignUpFormActivity onActivityResult : " + newInt);
				yearIndex = newInt;
				signUpFormBirthYearContent.setText(yearContentList[newInt]);
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_GENDER) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
				LogController.log("SignUpFormActivity onActivityResult : " + newInt);
				genderIndex = newInt;
				signUpFormGenderContent.setText(formContentToString(genderContentList.get(newInt)));
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_SKIN) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
				LogController.log("SignUpFormActivity onActivityResult : " + newInt);
				skinIndex = newInt;
				signUpFormSkinTypeContent.setText(formContentToString(skinContentList.get(newInt)));
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_COUNTRY) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
				LogController.log("SignUpFormActivity onActivityResult : " + newInt);
				countryIndex = newInt;
				signUpFormLivingCountryContent.setText(formContentToString(countryContentList.get(newInt)));
			} 
			break; 
		}
		}
	}

	private String[] formContentToStringArray(List<FormContent> formContentList) {
		LogController.log("formContentToStringArray");
		LogController.log("formContentList.size() = " + formContentList.size());

		String[] currentLangContent = new String[formContentList.size()];

		for (int i = 0; i < formContentList.size(); i++) {
			FormContent formContent = formContentList.get(i);

			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				currentLangContent[i] = formContent.getTitleEn();
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				currentLangContent[i] = formContent.getTitleZh();
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				currentLangContent[i] = formContent.getTitleSc();
			}
			LogController.log("currentLangContent[i] = " + currentLangContent[i]);
		}

		return currentLangContent;		
	}

	private String formContentToString(FormContent formContent) {
		String returnString = "";
		if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			returnString = formContent.getTitleEn();
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			returnString = formContent.getTitleZh();
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			returnString = formContent.getTitleSc();
		}
		return returnString;
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

		// status: 0 is success, 1 is fail, 2 is success but not yet register, 3 is success but not yet accept TOS
		if (results.getStatus() == 0) {
			LogController.log("Status : 0 is success");
			Intent intent = new Intent(SignUpFormActivity.this, LoginTOSActivity.class);
			intent.putExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY, getIntent().getStringExtra("id"));
			startActivity(intent);
			finish();

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
