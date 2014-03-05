package com.gt.gtsnssharinglibrarydemo;

import com.gt.gtsnssharinglibrarydemo.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_page);
		
		Button button = (Button)this.findViewById(R.id.facebook_20_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Facebook20TestingActivity.class);
				startActivity(intent);
			}
		});
		
		button = (Button)this.findViewById(R.id.facebook_30_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Facebook30TestingActivity.class);
				startActivity(intent);
			}
		});
		
		button = (Button)this.findViewById(R.id.twitter_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TwitterTestingActivity.class);
				startActivity(intent);
			}
		});
		
		button = (Button)this.findViewById(R.id.weibo_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WeiboTestingActivity.class);
				startActivity(intent);
			}
		});
		
		button = (Button)this.findViewById(R.id.email_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, EmailTestingActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
