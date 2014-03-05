package com.fancl.iloyalty.activity.about;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.ContactUs;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;

public class AboutHomeActivity extends MainTabActivity {

	private View aboutLayout;
	private LocaleService localeService;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.4

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		localeService = GeneralServiceFactory.getLocaleService();
		headerTitleTextView.setText(this.getResources().getString(R.string.menu_about_fancl_btn_title));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		aboutLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.about_fancl_home_page, null);
		spaceLayout.addView(aboutLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		RelativeLayout fanclLayout = (RelativeLayout) findViewById(R.id.fancl_layout);
		TextView row1TextView = (TextView) findViewById(R.id.row_1_testview);
		try {
			AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground();
			row1TextView.setText(localeService.textByLangaugeChooser(this, aboutFancl.getTitleEn(), aboutFancl.getTitleZh(), aboutFancl.getTitleSc()));

		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		fanclLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground();
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFancl(aboutFancl, AboutHomeActivity.this, "background", true, 4));
					
					CustomServiceFactory.getSettingService().addUserLogWithSection("About FANCL", "", "", "", "Background", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		RelativeLayout productLayout = (RelativeLayout) findViewById(R.id.product_layout);
		TextView row2TextView = (TextView) findViewById(R.id.row_2_testview);
		try {
			AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getLessIsMore();
			row2TextView.setText(localeService.textByLangaugeChooser(this, aboutFancl.getTitleEn(), aboutFancl.getTitleZh(), aboutFancl.getTitleSc()));

		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		productLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getLessIsMore();
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFancl(aboutFancl, AboutHomeActivity.this, "lessIsMore", true, 4));
					
					CustomServiceFactory.getSettingService().addUserLogWithSection("About FANCL", "", "", "", "LESS IS MORE", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		RelativeLayout membershipLayout = (RelativeLayout) findViewById(R.id.membership_layout);
		TextView row3TextView = (TextView) findViewById(R.id.row_3_testview);
		try {
			AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getHowToUse();
			row3TextView.setText(localeService.textByLangaugeChooser(this, aboutFancl.getTitleEn(), aboutFancl.getTitleZh(), aboutFancl.getTitleSc()));

		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		membershipLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getHowToUse();
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFancl(aboutFancl, AboutHomeActivity.this, "howToUse", true, 4));
					
					CustomServiceFactory.getSettingService().addUserLogWithSection("About FANCL", "", "", "", "How To Use", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		RelativeLayout contactLayout = (RelativeLayout) findViewById(R.id.contact_layout);
		contactLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("About FANCL", "", "", "", "Contact Us", "View", "");
					
					final ContactUs contactUs = CustomServiceFactory.getAboutFanclService().getContactUs();
					LogController.log("phone:"+contactUs.getPhone());
					String phoneNo = contactUs.getPhone().substring(0, 4) + " " + contactUs.getPhone().substring(4, 8);
					String dialogMsg = getString(R.string.alert_contact_us_call) + phoneNo + "?\n"+getString(R.string.alert_contact_us_call_time); 
				
					
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							AboutHomeActivity.this);

					// set title
					alertDialogBuilder.setTitle("");

					// set dialog message
					alertDialogBuilder
					.setMessage(dialogMsg)
					.setCancelable(false)
					.setPositiveButton(getString(R.string.confirm_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							callIntent.setData(Uri.parse("tel:" + contactUs.getPhone()));
							startActivity(callIntent);
						}
					})
					.setNegativeButton(getString(R.string.cancel_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

}
