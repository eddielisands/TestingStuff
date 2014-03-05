package com.fancl.iloyalty.activity.shop;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.ShopListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;

public class ShopListActivity extends MainTabActivity{

	private List<Map<String, String>> shoplist = new ArrayList<Map<String, String>>();   
	private List<Map<String, String>> splitList = new ArrayList<Map<String, String>>();  

	List<Shop> shopFanclList;
	List<Shop> shopFnhList;

	private View shopLocationListLayout;
	
	private LocaleService localeService;

	

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.3.3

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

		shopLocationListLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.shop_location_list_page, null);
		spaceLayout.addView(shopLocationListLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ListView list = (ListView) findViewById(R.id.MyListView);  

		setData(); 

		ShopListAdapter adapter = new ShopListAdapter(this, shoplist, splitList);  

		list.setAdapter(adapter);  

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Shop shopDetail;
				if (arg2 > shopFanclList.size()) {
					shopDetail = shopFnhList.get(arg2-shopFanclList.size()-2);
					LogController.log("shop fnh:"+ (arg2-shopFanclList.size()-2));
				}else{
					shopDetail = shopFanclList.get(arg2-1);
					LogController.log("shop fancl:"+ (arg2-1));
				}

				startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, ShopListActivity.this, 4));
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Shop", "Store Detail", "",shopDetail.getObjectId() , shopDetail.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	private void setData() {  

		LogController.log("list - region id:"+ this.getIntent().getExtras().getString("REGION_ID"));

		try {
			shopFanclList = CustomServiceFactory.getAboutFanclService().getShopListForFancl(true, this.getIntent().getExtras().getString("REGION_ID"));
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			shopFnhList = CustomServiceFactory.getAboutFanclService().getShopListForFancl(false, this.getIntent().getExtras().getString("REGION_ID"));
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> mp = new HashMap<String, String>();  

		if (shopFanclList.size() != 0) {
			mp.put("itemTitle", "FANCL");  
			shoplist.add(mp);  
			splitList.add(mp);  


			for (int i = 0; i < shopFanclList.size(); i++) {  
				Shop shopFancl = shopFanclList.get(i);

				Map<String, String> map = new HashMap<String, String>();  
				if(shopFancl != null)
					map.put("itemTitle", localeService.textByLangaugeChooser(this, shopFancl.getTitleEn(), shopFancl.getTitleZh(), shopFancl.getTitleSc()));  
				shoplist.add(map);  

			}  
		}


		if (shopFnhList.size() != 0) {
			mp = new HashMap<String, String>();  
			mp.put("itemTitle", "F&H");  
			shoplist.add(mp);  
			splitList.add(mp);  

			for (int i = 0; i < shopFnhList.size(); i++) {  
				Shop shopFnh = shopFnhList.get(i);
				Map<String, String> map = new HashMap<String, String>();  
				if(shopFnh != null)
					map.put("itemTitle", localeService.textByLangaugeChooser(this, shopFnh.getTitleEn(), shopFnh.getTitleZh(), shopFnh.getTitleSc())); 
					
				shoplist.add(map);  

			}  
		}
		LogController.log("splitList:"+ splitList);

	}

}
