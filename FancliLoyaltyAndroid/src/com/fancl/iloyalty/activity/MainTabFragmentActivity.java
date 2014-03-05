package com.fancl.iloyalty.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.beauty.BeautyHomeActivity;
import com.fancl.iloyalty.activity.menu.MenuActivity;
import com.fancl.iloyalty.activity.promotion.PromotionHomeActivity;
import com.fancl.iloyalty.activity.purchase.PurchaseQRCodeScanActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;
import com.fancl.iloyalty.asynctask.PromotionCountAsyncTask;
import com.fancl.iloyalty.asynctask.callback.PromotionCountAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;

public class MainTabFragmentActivity extends AndroidProjectFrameworkFragmentActivity implements PromotionCountAsyncTaskCallback {
	private int currentTabIndex = -1;
	private boolean isNeedFinish = false;
	
	private View whatsHotBtn = null;
	private View promotionBtn = null;
	private View purchaseBtn = null;
	private View beautyiChannelBtn = null;
	private View moreBtn = null;
	
	private TextView whatsHotBadge;
	private TextView promotionBadge;
	private TextView ichannelBadge;
	
	public View navigationBarLeftBtn = null;
	public View navigationBarRightBtn = null;
	public View navigationBarSearchBtn = null;
	public View navigationBarListBtn = null;
	public View navigationBarFullPageBtn = null;
	public View navigationBarDoneBtn = null;
	public View navigationBarShareBtn = null;
	public View navigationBarEditBtn = null;
	public View navigationBarCancelBtn = null;
	public TextView headerTitleTextView = null;
	public TextView navigationBarRightTextView = null;
	public TextView navigationBarDoneTextView = null;
	
	protected RelativeLayout spaceLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main_tab);
		
		spaceLayout = (RelativeLayout) this.findViewById(R.id.space_layout);
		navigationBarLeftBtn = (RelativeLayout) this.findViewById(R.id.header_bar_left_btn_title);
		navigationBarRightBtn = (RelativeLayout) this.findViewById(R.id.header_bar_right_btn_title);
		navigationBarSearchBtn = (RelativeLayout) this.findViewById(R.id.header_bar_search_btn_title);
		navigationBarListBtn = (RelativeLayout) this.findViewById(R.id.header_bar_list_btn_title);
		navigationBarFullPageBtn = (RelativeLayout) this.findViewById(R.id.header_bar_fullpage_btn_title);
		navigationBarDoneBtn = (RelativeLayout) this.findViewById(R.id.header_bar_done_btn_title);
		navigationBarShareBtn = (RelativeLayout) this.findViewById(R.id.header_bar_share_btn_title);
		navigationBarEditBtn = (RelativeLayout) this.findViewById(R.id.header_bar_edit_btn_title);
		navigationBarCancelBtn = (RelativeLayout) this.findViewById(R.id.header_bar_cancel_btn_title);
		headerTitleTextView = (TextView) this.findViewById(R.id.header_navigation_title);
		navigationBarRightTextView = (TextView) this.findViewById(R.id.header_bar_right_btn_text);
		navigationBarDoneTextView = (TextView) this.findViewById(R.id.header_bar_done_btn_text);

		navigationBarLeftBtn.setVisibility(View.GONE);
		navigationBarRightBtn.setVisibility(View.GONE);
		navigationBarSearchBtn.setVisibility(View.GONE);
		navigationBarListBtn.setVisibility(View.GONE);
		navigationBarFullPageBtn.setVisibility(View.GONE);
		navigationBarDoneBtn.setVisibility(View.GONE);
		navigationBarShareBtn.setVisibility(View.GONE);
		navigationBarEditBtn.setVisibility(View.GONE);
		navigationBarCancelBtn.setVisibility(View.GONE);

		navigationBarLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
	protected void setupMenuButtonListener(int currentTabIndex, boolean isNeedFinish) {
		this.currentTabIndex = currentTabIndex;
		this.isNeedFinish = isNeedFinish;
		
		whatsHotBtn = this.findViewById(R.id.whats_hot_btn);
		promotionBtn = this.findViewById(R.id.promotion_btn);
		purchaseBtn = this.findViewById(R.id.purchase_btn);
		LocaleService localeService = GeneralServiceFactory.getLocaleService();
		if (localeService.getCurrentLanguageType() == LANGUAGE_TYPE.EN) {
			purchaseBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_purchase_off));
		}
		else {
			purchaseBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_purchase_tc_off));

		}
		beautyiChannelBtn = this.findViewById(R.id.beauty_ichannel_btn);
		moreBtn = this.findViewById(R.id.more_btn);
		
		whatsHotBadge = (TextView) this.findViewById(R.id.whats_hot_badge);
		promotionBadge = (TextView) this.findViewById(R.id.promotion_badge);
		ichannelBadge = (TextView) this.findViewById(R.id.ichannel_badge);
		
		if(whatsHotBtn != null)
		{
			// go to What's Hot
			whatsHotBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
//					if(MainTabFragmentActivity.this.currentTabIndex != 0)
//					{
						clearExisitingActivity();
						Intent intent = new Intent(MainTabFragmentActivity.this, WhatsHotHomeActivity.class);
						startActivity(intent);
						checkFinishAfterOpenNextActivity();
//					}
				}
			});
		}
		
		if(promotionBtn != null)
		{
			// go to Promotion
			promotionBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
//					if(MainTabFragmentActivity.this.currentTabIndex != 1)
//					{
						clearExisitingActivity();
						Intent intent = new Intent(MainTabFragmentActivity.this, PromotionHomeActivity.class);
						startActivity(intent);
						checkFinishAfterOpenNextActivity();
//					}
				}
			});
		}
		
		if(purchaseBtn != null)
		{
			// go to Purchase
			purchaseBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
//					if(MainTabFragmentActivity.this.currentTabIndex != 2)
//					{
						Intent intent = new Intent(MainTabFragmentActivity.this, PurchaseQRCodeScanActivity.class);
						startActivity(intent);
//					}
				}
			});
		}
		
		if(beautyiChannelBtn != null)
		{
			// go to Beauty iChannel
			beautyiChannelBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
//					if(MainTabFragmentActivity.this.currentTabIndex != 3)
//					{
						clearExisitingActivity();
						Intent intent = new Intent(MainTabFragmentActivity.this, BeautyHomeActivity.class);
						startActivity(intent);
						checkFinishAfterOpenNextActivity();
//					}
				}
			});
		}
		
		if(moreBtn != null)
		{
			// go to Purchase
			moreBtn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
//					if(MainTabFragmentActivity.this.currentTabIndex != 4)
//					{
						clearExisitingActivity();
						Intent intent = new Intent(MainTabFragmentActivity.this, MenuActivity.class);
						startActivity(intent);
						checkFinishAfterOpenNextActivity();
//					}
				}
			});
		}
		
		setSessionButtonOn();
		
		if (application.promotionUnread == 0) {
			setBadgesNumber();
		}
		else {
			if (application.whatsHotUnread > 0) {
				whatsHotBadge.setText(String.valueOf(application.whatsHotUnread));
				whatsHotBadge.setVisibility(View.VISIBLE);
			}
			else {
				whatsHotBadge.setVisibility(View.GONE);
			}
			
			if (application.promotionUnread > 0) {
				promotionBadge.setText(String.valueOf(application.promotionUnread));
				promotionBadge.setVisibility(View.VISIBLE);
			}
			else {
				promotionBadge.setVisibility(View.GONE);
			}
			
			if (application.ichannelUnread > 0) {
				ichannelBadge.setText(String.valueOf(application.ichannelUnread));
				ichannelBadge.setVisibility(View.VISIBLE);
			}
			else {
				ichannelBadge.setVisibility(View.GONE);
			}
		}
	}
	
	private void setSessionButtonOn() {
		try
		{
			switch (currentTabIndex)
			{
				case 0:
					whatsHotBtn.setBackgroundResource(R.drawable.btn_news_on);
					break;
				case 1:
					promotionBtn.setBackgroundResource(R.drawable.btn_promotion_on);
					break;
				case 3:
					beautyiChannelBtn.setBackgroundResource(R.drawable.btn_channel_on);
					break;
				case 4:
					moreBtn.setBackgroundResource(R.drawable.btn_menu_on);
					break;
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	
	private void checkFinishAfterOpenNextActivity()
	{
		if(isNeedFinish)
		{
			finish();
		}
	}
	
	private void clearExisitingActivity() {
		((AndroidProjectApplication) application).removeExistingActivity();
	}
	
	public void setBadgesNumber() {
		try {
			String unread = CustomServiceFactory.getAboutFanclService().getUnreadNumberWithType();
			application.whatsHotUnread = Integer.valueOf(unread);
			if (unread.length() > 0 && !unread.equals("0")) {
				whatsHotBadge.setVisibility(View.VISIBLE);
				whatsHotBadge.setText(unread);
			}
			else {
				whatsHotBadge.setVisibility(View.GONE);
				whatsHotBadge.setText(null);
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String unread = CustomServiceFactory.getAboutFanclService().getUnreadChannel();
			application.ichannelUnread = Integer.valueOf(unread);
			if (unread.length() > 0  && !unread.equals("0")) {
				ichannelBadge.setVisibility(View.VISIBLE);
				ichannelBadge.setText(unread);
			}
			else {
				ichannelBadge.setVisibility(View.GONE);
				ichannelBadge.setText(null);
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PromotionCountAsyncTask promotionCountAsyncTask = new PromotionCountAsyncTask(this);
		promotionCountAsyncTask.execute();
	}

	@Override
	public void onPostExecuteCallback(String results) {
		int totalUnread = Integer.valueOf(results);
		
		try {
			String unread = CustomServiceFactory.getAboutFanclService().getUnreadPromotion();
			totalUnread = totalUnread + Integer.valueOf(unread);
		} catch (FanclException e) {
			e.printStackTrace();
		}
		
		application.promotionUnread = Integer.valueOf(totalUnread);
		if (totalUnread > 0) {
			promotionBadge.setVisibility(View.VISIBLE);
			promotionBadge.setText(String.valueOf(totalUnread));
		}
		else {
			promotionBadge.setVisibility(View.GONE);
			promotionBadge.setText(null);
		}
	}
}
