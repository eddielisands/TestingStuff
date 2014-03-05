package com.fancl.iloyalty.activity.magazine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.BeautyListViewAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.CustomTabBar;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.IchannelType;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class MagazineHomeActivity extends MainTabActivity implements CustomTabBarCallback {
	private LocaleService localeService;
	private View magazineLayout;
	private View espoirLayout;
	private View fhLayout;
	private CustomTabBar magazineSectionBar;
	private int currentTabBarIndex = 0;
	private String selectedMainCate;
	private String selectedSubCate;
	private List<String> magazineSectionBarItemList;
	private List<String> magazineSubCateIdList;
	
	private ListView magazineListView;
	private BeautyListViewAdapter magazineListViewAdapter;
	private List<IchannelMagazine> articleList;

	private final String MAGAZINE_TYPE_1 = "skincare";
	private final String MAGAZINE_TYPE_2 = "intake";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		localeService = GeneralServiceFactory.getLocaleService();
		magazineSectionBarItemList = new ArrayList<String>();
		magazineSubCateIdList = new ArrayList<String>();
		articleList = new ArrayList<IchannelMagazine>();
		
		headerTitleTextView.setText(this.getResources().getString(R.string.menu_fancl_magazine_btn_title));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);

		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarFullPageBtn.setVisibility(View.VISIBLE);
		navigationBarFullPageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MagazineHomeActivity.this, MagazineFullPageActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		selectedMainCate = MAGAZINE_TYPE_1;
		
		magazineLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.magazine_home_page, null);
		spaceLayout.addView(magazineLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		espoirLayout = (RelativeLayout) findViewById(R.id.espoir_layout);
		espoirLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectedMainCate = MAGAZINE_TYPE_1;
				espoirLayout.setBackgroundResource(R.drawable.btn_magazine_cat_lft_on);
				fhLayout.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_off);
				setMagazineSectionBar();
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("FANCL Magazine", "ESPOIR", "", "", "", "Button Click", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		fhLayout = (RelativeLayout) findViewById(R.id.fh_layout);
		fhLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectedMainCate = MAGAZINE_TYPE_2;
				espoirLayout.setBackgroundResource(R.drawable.btn_magazine_cat_lft_off);
				fhLayout.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_on);
				setMagazineSectionBar();
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("FANCL Magazine", "F&H", "", "", "", "Button Click", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		magazineSectionBar = (CustomTabBar) findViewById(R.id.magazine_section_bar);
		magazineSectionBar.setConstructors(this, currentTabBarIndex, new ArrayList<String>(), DataUtil.dip2integerPx(this, 33), true, false, this);

		magazineListViewAdapter = new BeautyListViewAdapter(this, this, handler);
		magazineListViewAdapter.setArticleList(articleList);
		magazineListView = (ListView) findViewById(R.id.magazine_list_view);
		magazineListView.setAdapter(magazineListViewAdapter);
		
		magazineListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				
				LogController.log("position : " + position + "| id : " + id);
				
				IchannelMagazine ichannelMagazine = articleList.get(position);
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, MagazineHomeActivity.this, true, getResources().getString(R.string.menu_fancl_magazine_btn_title), 4));
			}
		});
		
		setMagazineSectionBar();
		reloadListViewContent();
	}

	private void setMagazineSectionBar() {
		magazineSectionBarItemList.clear();
		magazineSubCateIdList.clear();
		try {
			List<IchannelType> tmpList = CustomServiceFactory.getPromotionService().getIchannelSubcateListWithMainCate(selectedMainCate);
			for (int i = 0; i < tmpList.size(); i++) {
				IchannelType type = tmpList.get(i);
				if (i == 0) {
					selectedSubCate = type.getObjectId();
				}
				String title = localeService.textByLangaugeChooser(this, type.getTitleEn(), type.getTitleZh(), type.getTitleSc());
				magazineSectionBarItemList.add(title);
				magazineSubCateIdList.add(type.getObjectId());
				selectedSubCate = magazineSubCateIdList.get(0);
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		magazineSectionBar.resetTabBar(magazineSectionBarItemList, true);
	}

	private void reloadListViewContent() {
		try {
			articleList = CustomServiceFactory.getPromotionService().getIchannelListWithMainCate(selectedMainCate, selectedSubCate);
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		magazineListViewAdapter.setArticleList(articleList);
	}
	
	@Override
	public void clickedIndex(CustomTabBar customTabBar, int index) {
		// TODO Auto-generated method stub
		selectedSubCate = magazineSubCateIdList.get(index);
		reloadListViewContent();
	}
}
