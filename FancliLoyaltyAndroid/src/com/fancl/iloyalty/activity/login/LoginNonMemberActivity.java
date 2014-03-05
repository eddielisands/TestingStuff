package com.fancl.iloyalty.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;

public class LoginNonMemberActivity extends AndroidProjectFrameworkActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.4
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.non_member_login_page);
        
        addListener();
    }
	
	private void addListener() {
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
		
		RelativeLayout continueBtnLayout = (RelativeLayout)findViewById(R.id.continue_btn);
		continueBtnLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginNonMemberActivity.this, WhatsHotHomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
