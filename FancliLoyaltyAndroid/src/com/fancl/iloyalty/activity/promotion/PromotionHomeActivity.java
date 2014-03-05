package com.fancl.iloyalty.activity.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.CustomSpinnerActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.product.ProductHomeActivity;
import com.fancl.iloyalty.adapter.PromotionListViewAdapter;
import com.fancl.iloyalty.asynctask.GetPromotionListAsyncTask;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetPromotionListAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.item.CustomTabBar;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class PromotionHomeActivity extends MainTabActivity implements CustomTabBarCallback, GetPromotionListAsyncTaskCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 3.1, 3.2, 3.3, 3.3.2
	
	private List<Promotion> promotionList;
	private String[] promotionType = new String[] {"latest", "iCoupon", "vip"};
	private String[] latestPromotionType = new String[] {"redemption", "latest", "vip"};
	
	private int currentTabBarIndex = 0;
	private int currentSubTabBarIndex = 0;
	private ListView promotionListView;
	private PromotionListViewAdapter promotionListViewAdapter;
	private CustomTabBar categoryTabBar;
	private CustomTabBar subCategoryTabBar;
	
	private int selectedPromtionIndex = 4;
	
	private TextView noRecordText;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.promotion_btn));
        
        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(1, true);
        
    }
	
	private void setupSpaceLayout() {
		
		// Space Layout
		LinearLayout promotionLayout = new LinearLayout(this);
		spaceLayout.addView(promotionLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		promotionLayout.setOrientation(LinearLayout.VERTICAL);
		
		// Tab Bar
		List<String> tabBarList = new ArrayList<String>();
		tabBarList.add(this.getResources().getString(R.string.promotion_latest_promotion_tab_bar_title));
		tabBarList.add(this.getResources().getString(R.string.promotion_icoupon_tab_bar_title));
		tabBarList.add(this.getResources().getString(R.string.promotion_vip_promotion_tab_bar_title));
				
		categoryTabBar = new CustomTabBar(this, currentTabBarIndex, tabBarList, DataUtil.dip2integerPx(this, 33), false, false, this);
		promotionLayout.addView(categoryTabBar, LayoutParams.MATCH_PARENT, DataUtil.dip2integerPx(this, 33));
		
		List<String> tabBarSubCatList = new ArrayList<String>();
		tabBarSubCatList.add(this.getResources().getString(R.string.promotion_latest_promotion_point_gift_sub_tab_bar_title));
		tabBarSubCatList.add(this.getResources().getString(R.string.promotion_latest_promotion_skincare_promotion_sub_tab_bar_title));
//		tabBarSubCatList.add(this.getResources().getString(R.string.promotion_latest_promotion_fs_promotion_sub_tab_bar_title));
		
		subCategoryTabBar = new CustomTabBar(this, currentTabBarIndex, tabBarSubCatList, DataUtil.dip2integerPx(this, 33), true, false, this);
		promotionLayout.addView(subCategoryTabBar, LayoutParams.MATCH_PARENT, DataUtil.dip2integerPx(this, 33));
		
		RelativeLayout promotionContentLayout = new RelativeLayout(this);
		promotionLayout.addView(promotionContentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		// List View
		promotionListView = new ListView(this);
		promotionListView.setCacheColorHint(color.transparent);
		promotionListView.setDividerHeight(0);
		promotionListView.setScrollingCacheEnabled(false);
		promotionContentLayout.addView(promotionListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		noRecordText = new TextView(this);
		noRecordText.setText(R.string.alert_no_record);
		noRecordText.setTextColor(getResources().getColor(R.color.Fancl_Blue));
		noRecordText.setVisibility(View.GONE);
		noRecordText.setTextSize(16);
		noRecordText.setGravity(Gravity.CENTER_HORIZONTAL);
		promotionContentLayout.addView(noRecordText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		loadLatestPromotionListFromDatabase(latestPromotionType[currentSubTabBarIndex]);
		
		promotionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LogController.log("item clicked: " + arg2);
				Promotion promotion = promotionList.get(arg2);
				
				SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
				submitPromotionAsyncTask.execute(promotion.getCode());
				
				if(selectedPromtionIndex == 2 || selectedPromtionIndex==3){
					if(promotion.getIsLuckyDraw().equalsIgnoreCase("Y") && promotion.getIsParticipated().equalsIgnoreCase("Y")){
						return;
					}
				}
				
				Intent intent = new Intent();
				intent = CustomServiceFactory.getDetailContentService().getPromotionDetailAction(PromotionHomeActivity.this, promotion, true, null, 1, selectedPromtionIndex);
				startActivityForResult(intent, Constants.PROMOTION_LUCKY_DRAW);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Detail Page", "", promotion.getObjectId(),promotion.getTitleEn() , "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void loadLatestPromotionListFromDatabase(String type) {
		try {
			LogController.log("type " + type);
			promotionList = CustomServiceFactory.getPromotionService()
					.getLatestPromotionWithType(type);
			LogController.log("promotionList:"+ promotionList.size());
			promotionListViewAdapter = new PromotionListViewAdapter(this, this, handler);
			promotionListView.setAdapter(promotionListViewAdapter);
			promotionListViewAdapter.setPromotionIndex(selectedPromtionIndex);
			promotionListViewAdapter.setPromotionList(promotionList);
			
			if(promotionList.size()==0)
				noRecordText.setVisibility(View.VISIBLE);
			
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void loadPromotionListFromApi(String type) {
		promotionListView.setAdapter(null);
		GetPromotionListAsyncTask getPromotionListAsyncTask = new GetPromotionListAsyncTask(this);
		getPromotionListAsyncTask.execute(type);
	}

	@Override
	public void clickedIndex(CustomTabBar customTabBar, int index) {
		// TODO Auto-generated method stub
		noRecordText.setVisibility(View.GONE);
		
		if (customTabBar.equals(categoryTabBar)) {
			if (currentTabBarIndex != index) {
				LogController.log("clickedIndex " + index);
				
				if (index == 0) {
					if (currentSubTabBarIndex == 0) {
						selectedPromtionIndex = 4;					
					}
					else {
						selectedPromtionIndex = 5;					
					}
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Promotion (in store)", "", "", "", "ButtonClick", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					this.runOnUiThread(new Runnable() {
						public void run() {
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
					});

					selectedPromtionIndex = index + 1;
				}
				
				if (index == 0) {
					loadLatestPromotionListFromDatabase(latestPromotionType[currentSubTabBarIndex]);
					subCategoryTabBar.setVisibility(View.VISIBLE);
				} else {
					loadPromotionListFromApi(promotionType[index]);
					subCategoryTabBar.setVisibility(View.GONE);
				}
				
				promotionListViewAdapter.setPromotionIndex(selectedPromtionIndex);
				
				promotionListViewAdapter.notifyDataSetChanged();
				
				currentTabBarIndex = index;
				
				if (index == 1) {
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Promotion (iCoupon)", "", "", "", "ButtonClick", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(index == 2){
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Promotion (VIP)", "", "", "", "ButtonClick", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		} else {
			if (currentSubTabBarIndex != index) {
				LogController.log("1. clickedIndex " + index);
				
				currentSubTabBarIndex = index;
				
				if (index == 0) {
					selectedPromtionIndex = 4;
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Point Gift", "", "", "", "ButtonClick", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					selectedPromtionIndex = 5;
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Promotion", "Other Promotion", "", "", "", "ButtonClick", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				loadLatestPromotionListFromDatabase(latestPromotionType[currentSubTabBarIndex]);
			}
		}
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if ((List<Promotion>) results != null) {
			if (((List<Promotion>) results).size() > 0) {
				promotionList = (List<Promotion>) results;
				promotionListViewAdapter = new PromotionListViewAdapter(this, this, handler);
				promotionListView.setAdapter(promotionListViewAdapter);
				promotionListViewAdapter.setPromotionIndex(selectedPromtionIndex);
				promotionListViewAdapter.setPromotionList(promotionList);
			}
			else {
				promotionListView.setAdapter(null);
				noRecordText.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void relaodUnreadContent() {
		if (selectedPromtionIndex == 4 || selectedPromtionIndex == 5) {
			loadLatestPromotionListFromDatabase(latestPromotionType[currentSubTabBarIndex]);
		}
		else {
			loadPromotionListFromApi(promotionType[currentTabBarIndex]);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
		case (Constants.PROMOTION_LUCKY_DRAW) : { 
			if (resultCode == PromotionHomeActivity.RESULT_OK) { 
				loadPromotionListFromApi("vip");
				if (checkLoadingDialog()) {
					loadingDialog.loading();
				}
			}
		}
		}
	}

}
