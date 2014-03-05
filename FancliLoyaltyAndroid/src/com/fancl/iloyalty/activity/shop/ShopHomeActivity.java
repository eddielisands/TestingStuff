package com.fancl.iloyalty.activity.shop;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.ExpandableAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.ShopRegion;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;

public class ShopHomeActivity extends MainTabActivity {
	
	private View shopLayout;
	List<ShopRegion> regionParentList;
	List<ShopRegion> regionChildList;

	private LocaleService localeService;

	

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.3, 6.3.2
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        localeService = GeneralServiceFactory.getLocaleService();
        
        headerTitleTextView.setText(this.getResources().getString(R.string.menu_shop_locator_btn_title));
        
        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(4, true);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);

    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		shopLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.shop_home_page, null);
		spaceLayout.addView(shopLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		ExpandableListView shopExpandLV = (ExpandableListView)findViewById(R.id.shopExpandableListView);  
		shopExpandLV.addHeaderView(LayoutInflater.from(this).inflate( 
		        R.layout.shop_header_row, null));
		
		try {
			regionParentList = CustomServiceFactory.getAboutFanclService().getShopParentRegionList();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		
		
		
//		Map<String, String> group1 = new HashMap<String, String>();
//		group1.put("group", "Hong Kong");
//		Map<String, String> group2 = new HashMap<String, String>();
//		group2.put("group", "Macau");
//		groups.add(group1);
//		groups.add(group2);
		
//		List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
//		Map<String, String> child1Data1 = new HashMap<String, String>();
//		child1Data1.put("child", "child1Data1");
//		Map<String, String> child1Data2 = new HashMap<String, String>();
//		child1Data2.put("child", "child1Data2");
//		child1.add(child1Data1);
//		child1.add(child1Data2);
		
//		List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
//		Map<String, String> child2Data1 = new HashMap<String, String>();
//		child2Data1.put("child", "child2Data1");
//		child2.add(child2Data1);
		
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
//		childs.add(child1);
//		childs.add(child2);
		
		for (int i = 0; i < regionParentList.size(); i++) {
			ShopRegion region = regionParentList.get(i);
			
			if (region != null) {
				
					Map<String, String> group1 = new HashMap<String, String>();
					
					
					group1.put("group", localeService.textByLangaugeChooser(this, region.getTitleEn(), region.getTitleZh(), region.getTitleSc()));
					groups.add(group1);
					
					try {
						regionChildList = CustomServiceFactory.getAboutFanclService().getShopRegionListWithParentId(region.getObjectId());
						LogController.log("regionChildList:"+ regionChildList.size());
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
					for (int j = 0; j < regionChildList.size(); j++) {
						ShopRegion regionChild = regionChildList.get(j);
						LogController.log("regionChild:"+ regionChild);

						if (regionChild != null) {

								Map<String, String> child1Data1 = new HashMap<String, String>();
			
								child1Data1.put("child", localeService.textByLangaugeChooser(this, regionChild.getTitleEn(), regionChild.getTitleZh(), regionChild.getTitleSc()));
								child1.add(child1Data1);
								
							}
							

					}
					childs.add(child1);
					
				
			}
			
		}
		
		ExpandableAdapter viewAdapter = new ExpandableAdapter(this, groups, childs);
		shopExpandLV.setAdapter(viewAdapter);
		

		RelativeLayout nearestShopLayout = (RelativeLayout) findViewById(R.id.header_row_layout);
		nearestShopLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopHomeActivity.this, ShopMapActivity.class);
				intent.putExtra("SHOW_ARROW", true);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Shop", "Google Map", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				try {
//					shopList = CustomServiceFactory.getAboutFanclService().getFullShopList();
//					intent.putExtra(Constants.SELECTED_SHOP_ITEM_KEY, shopList.get(0));
//				} catch (FanclException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				startActivity(intent);
			}
			
		});
		
		shopExpandLV.setOnChildClickListener(new OnChildClickListener()
        {
            
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
            {
            	ShopRegion region = regionParentList.get(arg2);
            	List<ShopRegion> regionChildList = null;
				try {
					regionChildList = CustomServiceFactory.getAboutFanclService().getShopRegionListWithParentId(region.getObjectId());
				} catch (FanclException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	ShopRegion regionChild = regionChildList.get(arg3);
            	
            	
            	Intent intent = new Intent(ShopHomeActivity.this, ShopListActivity.class);
            	intent.putExtra("REGION_ID", regionChild.getObjectId());
            	LogController.log("region id:"+ regionChild.getObjectId());
            	startActivity(intent);
            	
            	try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Shop", "Shop List", "", regionChild.getObjectId(), regionChild.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                return false;
            }
        });
		

	}
	
	
}

