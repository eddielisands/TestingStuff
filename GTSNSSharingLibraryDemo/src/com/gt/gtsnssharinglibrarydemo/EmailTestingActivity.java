package com.gt.gtsnssharinglibrarydemo;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.impl.EmailServiceImpl;

public class EmailTestingActivity extends Activity{
	
	private SNSService emailServiceImpl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("EmailTestingActivity");
		
		emailServiceImpl = new EmailServiceImpl();
		
		Button loginBtn = (Button)findViewById(R.id.login_btn);
		loginBtn.setVisibility(View.GONE);
		
		Button logoutBtn = (Button)findViewById(R.id.logout_btn);
		logoutBtn.setVisibility(View.GONE);
		
		Button postFeedBtn = (Button)findViewById(R.id.post_feed_btn);
		postFeedBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String message = "Email Plain Text Message Testing. " + (new Date()).toString();
				
				SNSShareDetail snsShareDetail = new SNSShareDetail(message);
				emailServiceImpl.post(EmailTestingActivity.this, snsShareDetail);
			}
		});
	}
}
