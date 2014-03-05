package com.fancl.iloyalty.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.about.AboutHomeActivity;
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.magazine.MagazineHomeActivity;
import com.fancl.iloyalty.activity.message.MessageHomeActivity;
import com.fancl.iloyalty.activity.myaccount.MyAccountHomeActivity;
import com.fancl.iloyalty.activity.product.ProductHomeActivity;
import com.fancl.iloyalty.activity.qrscanner.QRCodeScannerActivity;
import com.fancl.iloyalty.activity.setting.SettingActivity;
import com.fancl.iloyalty.activity.shop.ShopHomeActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;


public class MenuActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.0

	private View menuLayout;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		headerTitleTextView.setText(this.getResources().getString(R.string.more_btn));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
	}

	private void setupSpaceLayout() {
		menuLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.menu_page, null);
		spaceLayout.addView(menuLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		RelativeLayout productLayout = (RelativeLayout) findViewById(R.id.product_layout);
		productLayout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, ProductHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Product", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		RelativeLayout magazineLayout = (RelativeLayout) findViewById(R.id.magazine_layout);
		magazineLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, MagazineHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Magazine", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		RelativeLayout aboutFanclLayout = (RelativeLayout) findViewById(R.id.about_fancl_layout);
		aboutFanclLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, AboutHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "About FANCL", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		RelativeLayout accountLayout = (RelativeLayout) findViewById(R.id.account_layout);
		accountLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, MyAccountHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "My Account", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		RelativeLayout storeLayout = (RelativeLayout) findViewById(R.id.store_layout);
		storeLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, ShopHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Shop", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		RelativeLayout notificationLayout = (RelativeLayout) findViewById(R.id.notification_layout);
		notificationLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, MessageHomeActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Notification", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		RelativeLayout settingLayout = (RelativeLayout) findViewById(R.id.setting_layout);
		settingLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Setting", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		RelativeLayout qrCodeLayout = (RelativeLayout) findViewById(R.id.qrcode_layout);
		qrCodeLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, QRCodeScannerActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Scan QR code", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		RelativeLayout bookmarkLayout = (RelativeLayout) findViewById(R.id.bookmark_layout);
		bookmarkLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MenuActivity.this, FavouriteActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Menu", "Bookmark", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	@Override
	public void resetView() {
		super.resetView();

		headerTitleTextView.setText(this.getResources().getString(R.string.more_btn));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
	}

}
