package com.fancl.iloyalty.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;

public class WelcomeActivity extends AndroidProjectFrameworkActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.1, 1.2
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.menu_bottom_bar);
        setContentView(R.layout.welcome_page);
        
        try {
			CustomServiceFactory.getSettingService().addUserLogWithSection("Login", "Welcome Page", "", "", "", "View", "");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        addListener();
    }
	
	private void addListener() {
		// Listener for Sign Up
		RelativeLayout signUpLayout = (RelativeLayout)findViewById(R.id.sign_up_btn_bg);
		signUpLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// Go to the web login page, when the login button is pressed.
				Intent intent = new Intent(WelcomeActivity.this, SignUpEnterMemberIDActivity.class);
//				Intent intent = new Intent(WelcomeActivity.this, SignUpFormActivity.class);
				startActivity(intent);
//				startActivityForResult(intent, Constants.SIGN_UP_ENTER_MEMBER_ID_INTENT);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Login", "Sign Up Page", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// Listener for Login
		RelativeLayout loginLayout = (RelativeLayout)findViewById(R.id.login_btn_bg);
		loginLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// Go to the web login page, when the login button is pressed.
				Intent intent = new Intent(WelcomeActivity.this, LoginFormActivity.class);
				intent.putExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY, "");
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Login", "Login Page", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
//		// Listener for Non Member Login
//		RelativeLayout nonMemberLayout = (RelativeLayout)findViewById(R.id.non_member_login_btn);
//		nonMemberLayout.setOnClickListener(new View.OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(WelcomeActivity.this, LoginNonMemberActivity.class);
//				startActivity(intent);
//			}
//		});
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
	  	switch(requestCode) { 
		    case (Constants.SIGN_UP_ENTER_MEMBER_ID_INTENT) : { 
			      if (resultCode == WelcomeActivity.RESULT_OK) { 
				      String newText = data.getStringExtra(Constants.SIGN_UP_ENTER_MEMBER_ID_RETURN_KEY);
				      				      
				      Intent intent = new Intent(WelcomeActivity.this, LoginFormActivity.class);
				      intent.putExtra(Constants.LOGIN_FORM_MEMBER_ID_KEY, newText);
				      startActivity(intent);
			      } 
		      break; 
		    } 
	  	}
	}
}
