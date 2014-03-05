package com.fancl.iloyalty.activity.whatshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.WhatsHotListViewPagerAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.util.LogController;

public class WhatsHotListActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 2.1, 2.2, 2.3

	private RelativeLayout whatsHotListLayout;

	private List<HotItem> whatsHotList = new ArrayList<HotItem>();

	private WhatsHotListViewPagerAdapter whatsHotListViewPagerAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String whatsHotHotItemType = this.getIntent().getExtras().getString(Constants.WHATS_HOT_HOT_ITEM_TYPE_KEY);

		HashMap<String, List<HotItem>> whatsHotHashMap = CustomServiceFactory.getPromotionService().getWhatsHotHashMap(); 
		whatsHotList = whatsHotHashMap.get(whatsHotHotItemType);

		if (whatsHotHotItemType.equals("campaign,shop")) {
			headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_category_new_campaign));
		}
		else if (whatsHotHotItemType.equals("product")) {
			headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_category_new_product));
		}
		else if (whatsHotHotItemType.equals("reading")) {
			headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_category_new_reading));
		}
		else if (whatsHotHotItemType.equals("promotion")) {
			headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_category_new_promotion));
		}
		else {
			headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_btn));
		}
		
		this.setupSpaceLayout();

		this.setupMenuButtonListener(0, true);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
	}

	private void setupSpaceLayout() {
		// Space Layout
		whatsHotListLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.whats_hot_list_page, null);
		spaceLayout.addView(whatsHotListLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// List View
		whatsHotListViewPagerAdapter = new WhatsHotListViewPagerAdapter(this, this, handler, whatsHotList);
		ListView whatsHotListView = (ListView)whatsHotListLayout.findViewById(R.id.whats_hot_list_view);
		if (whatsHotListViewPagerAdapter != null) {
			whatsHotListView.setAdapter(whatsHotListViewPagerAdapter);
		}

		whatsHotListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				LogController.log("position : " + position + "| id : " + id);

				HotItem hotItem = whatsHotList.get(position);
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(hotItem, WhatsHotListActivity.this, true, headerTitleTextView.getText().toString(), 0));
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "Detail Page", "", hotItem.getObjectId(),hotItem.getTitleEn() , "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void relaodUnreadContent() {
		String whatsHotHotItemType = this.getIntent().getExtras().getString(Constants.WHATS_HOT_HOT_ITEM_TYPE_KEY);

		HashMap<String, List<HotItem>> whatsHotHashMap = CustomServiceFactory.getPromotionService().getWhatsHotHashMap(); 
		whatsHotList = whatsHotHashMap.get(whatsHotHotItemType);
		whatsHotListViewPagerAdapter.setList(whatsHotList);
	}
}
