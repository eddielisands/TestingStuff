package com.fancl.iloyalty.activity.sharing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;

public class SharingPanelActivity extends AndroidProjectFrameworkActivity {
	private RelativeLayout facebookLayout;
	private RelativeLayout twitterLayout;
	private RelativeLayout emailLayout;
	private RelativeLayout favouriteLayout;
	private Button cancelButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sharing_panel_page);


		this.setupSpaceLayout();
	}

	private void setupSpaceLayout() {
		facebookLayout = (RelativeLayout) findViewById(R.id.facebook_layout);
		twitterLayout = (RelativeLayout) findViewById(R.id.twitter_layout);
		emailLayout = (RelativeLayout) findViewById(R.id.email_layout);
		favouriteLayout = (RelativeLayout) findViewById(R.id.favourite_layout);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		facebookLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				resultIntent.putExtra(Constants.SHARING_PANEL_RETURN_KEY, Constants.SHARING_PANEL_RETURN_FB);
				setResult(SharingPanelActivity.RESULT_OK, resultIntent);
				finish();
				
				
			}
		});

		twitterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				resultIntent.putExtra(Constants.SHARING_PANEL_RETURN_KEY, Constants.SHARING_PANEL_RETURN_TW);
				setResult(SharingPanelActivity.RESULT_OK, resultIntent);
				finish();
				
				
			}
		});

		emailLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				resultIntent.putExtra(Constants.SHARING_PANEL_RETURN_KEY, Constants.SHARING_PANEL_RETURN_EMAIL);
				setResult(SharingPanelActivity.RESULT_OK, resultIntent);
				finish();
				
				
			}
		});

		favouriteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				resultIntent.putExtra(Constants.SHARING_PANEL_RETURN_KEY, Constants.SHARING_PANEL_RETURN_FAVOURITE);
				setResult(SharingPanelActivity.RESULT_OK, resultIntent);
				finish();
				
				
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
}
