package com.fancl.iloyalty.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;

public class SignUpResultActivity extends AndroidProjectFrameworkActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 1.2.4
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.sign_up_welcome_userguide_page);
        
        RelativeLayout confirmLayout = (RelativeLayout)findViewById(R.id.sign_up_welcome_userguide_confirm_btn);
        confirmLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(SignUpResultActivity.this, WhatsHotHomeActivity.class);
				application.removeExistingActivity();
				startActivity(intent);

				
			}
		});
        
    }
	
}
