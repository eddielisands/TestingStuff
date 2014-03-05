package com.fancl.iloyalty.activity.beauty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.BeautyListViewAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.CustomTabBar;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.IchannelType;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class BeautyHomeActivity extends MainTabActivity implements
		CustomTabBarCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 5.1, 5..2.1(Beauty Tips),
	// 5.3
	private int currentTabBarIndex = 0;
	private int currentSubTabBarIndex = 0;

	private CustomTabBar categoryTabBar;
	private CustomTabBar subCategoryTabBar;

	private String[] mainCateType = new String[] { "magazine", "tips", "video" };

	private List<IchannelType> iChannelTypeList = new ArrayList<IchannelType>();
	private List<String> subCateList = new ArrayList<String>();
	private List<IchannelMagazine> articleList = new ArrayList<IchannelMagazine>(); 

	private LocaleService localeService;
	
	private ListView articleListView;
	private BeautyListViewAdapter beautyListViewAdapter; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		navigationBarSearchBtn.setVisibility(View.VISIBLE);
		
		headerTitleTextView.setText(this.getResources().getString(
				R.string.beauty_ichannel_btn));

		localeService = GeneralServiceFactory.getLocaleService();

		this.setupSpaceLayout();

		this.setupMenuButtonListener(3, true);
	}

	private void setupSpaceLayout() {
		// Space Layout
		LinearLayout beautyLayout = new LinearLayout(this);
		spaceLayout.addView(beautyLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		beautyLayout.setOrientation(LinearLayout.VERTICAL);
		
		// Tab Bar
		List<String> tabBarList = new ArrayList<String>();
		tabBarList.add(this.getResources().getString(
				R.string.beauty_fancl_magazine_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.beauty_beauty_tips_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.beauty_fancl_tv_tab_bar_title));
		
		categoryTabBar = new CustomTabBar(this, currentTabBarIndex, tabBarList,
				DataUtil.dip2integerPx(this, 33), false,false, this);
		beautyLayout.addView(categoryTabBar, LayoutParams.MATCH_PARENT,
				DataUtil.dip2integerPx(this, 33));

		subCategoryTabBar = new CustomTabBar(this, 0, subCateList,
				DataUtil.dip2integerPx(this, 33), true,false, this);
		beautyLayout.addView(subCategoryTabBar, LayoutParams.MATCH_PARENT,
				DataUtil.dip2integerPx(this, 33));
		
		// List View
		beautyListViewAdapter = new BeautyListViewAdapter(this, this, handler);
		articleListView = new ListView(this);
		articleListView.setCacheColorHint(color.transparent);
		articleListView.setDividerHeight(0);
		articleListView.setScrollingCacheEnabled(false);
		articleListView.setAdapter(beautyListViewAdapter);
		beautyLayout.addView(articleListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		loadSubCateListFromApi(mainCateType[0]);
		subCategoryTabBar.resetTabBar(subCateList, true);
		
		loadArticleFromApi(mainCateType[currentTabBarIndex], iChannelTypeList.get(currentSubTabBarIndex).getObjectId());
		
		articleListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				
				LogController.log("position : " + position + "| id : " + id);
				
				IchannelMagazine ichannelMagazine = articleList.get(position);
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, BeautyHomeActivity.this, true, null, 3));
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", ichannelMagazine.getType(), "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		navigationBarSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(BeautyHomeActivity.this, BeautySearchActivity.class);
            	startActivity(intent);
			}
		});
	}

	@Override
	public void clickedIndex(CustomTabBar customTabBar, int index) {
		// TODO Auto-generated method stub
		if (customTabBar.equals(categoryTabBar)) {
			currentTabBarIndex = index;
			currentSubTabBarIndex = 0;
			
			loadSubCateListFromApi(mainCateType[index]);
			subCategoryTabBar.resetTabBar(subCateList, true);
			
			loadArticleFromApi(mainCateType[currentTabBarIndex], iChannelTypeList.get(currentSubTabBarIndex).getObjectId());
			
			try {
				CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "iChannel"+(index+1), "", "", "", "Button Click", "");
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (customTabBar.equals(subCategoryTabBar)) {
			currentSubTabBarIndex = index;
			
			loadArticleFromApi(mainCateType[currentTabBarIndex], iChannelTypeList.get(currentSubTabBarIndex).getObjectId());
			
			try {
				CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "iChannel"+(index+1), "", "", iChannelTypeList.get(currentSubTabBarIndex).getTitleEn(), "Button Click", "");
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	private void loadSubCateListFromApi(String mainCate) {
		try {
			subCateList.clear();
			iChannelTypeList = CustomServiceFactory
					.getPromotionService().getIchannelSubcateListWithMainCate(
							mainCate);
			for (int i = 0; i < iChannelTypeList.size(); i++) {
				if (localeService.getCurrentLanguageType().equals(
						LANGUAGE_TYPE.EN)) {
					subCateList.add(iChannelTypeList.get(i).getTitleEn());
				} else if (localeService.getCurrentLanguageType().equals(
						LANGUAGE_TYPE.SC)) {
					subCateList.add(iChannelTypeList.get(i).getTitleSc());
				} else if (localeService.getCurrentLanguageType().equals(
						LANGUAGE_TYPE.TC)) {
					subCateList.add(iChannelTypeList.get(i).getTitleZh());
				}
			}
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void loadArticleFromApi(String mainCate, String subCate) {
		try {
			articleList = CustomServiceFactory
					.getPromotionService().getIchannelListWithMainCate(mainCate, subCate);
			beautyListViewAdapter.setArticleList(articleList);
			articleListView.setSelectionAfterHeaderView();
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void relaodUnreadContent() {
		loadArticleFromApi(mainCateType[currentTabBarIndex], iChannelTypeList.get(currentSubTabBarIndex).getObjectId());
	}
}
