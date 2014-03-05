package com.fancl.iloyalty.activity.whatshot;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.magazine.MagazineHomeActivity;
import com.fancl.iloyalty.adapter.ViewFlowImageAdapter;
import com.fancl.iloyalty.adapter.ViewFlowImageAdapter.ImageItemClickedListener;
import com.fancl.iloyalty.adapter.WhatsHotHomeExpandListViewAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.item.CircleFlowIndicator;
import com.fancl.iloyalty.item.ViewFlow;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.util.LogController;

public class WhatsHotHomeActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 2.1, 2.2, 2.3

	private View whatsHotLayout;

	private ViewFlow viewFlow;

	private List<HotItem> bannerList;

	private WhatsHotHomeExpandListViewAdapter exAdapter;
	private ExpandableListView exList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_btn));

		this.loadBannerListFromDatabase();

		this.setupSpaceLayout();

		this.setupMenuButtonListener(0, true);
		
		this.setupThumbnail();
	}
	
	private void setupSpaceLayout() {
		// Space Layout
		whatsHotLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.whats_hot_home_page, null);
		spaceLayout.addView(whatsHotLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Thumbnail View Flow Loop
		viewFlow = (ViewFlow) findViewById(R.id.viewflow_loop);  

		// Expandable List
		exAdapter = new WhatsHotHomeExpandListViewAdapter(this, this, handler);
		exList = (ExpandableListView)findViewById(R.id.whats_hot_home_page_list_view);
		exList.setAdapter(exAdapter);
		exList.setGroupIndicator(null);
		exList.setDivider(null);
		exList.setOnGroupClickListener(new OnGroupClickListener()
		{
		    @Override
		    public boolean onGroupClick(ExpandableListView parent, 
		        View v, int groupPosition, long id)
		    {
		    	if (groupPosition != 4) {
		    		String[] titles = new String[] { "campaign,shop", "product", "reading", "promotion" };

			    	Intent intent = new Intent(WhatsHotHomeActivity.this, WhatsHotListActivity.class);
					intent.putExtra(Constants.WHATS_HOT_HOT_ITEM_TYPE_KEY, titles[groupPosition]);
					startActivity(intent);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Hot Item", "Hot Item ("+titles[groupPosition]+")", "", "", "", "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    	else {
		    		Intent intent = new Intent(WhatsHotHomeActivity.this, MagazineHomeActivity.class);
					startActivity(intent);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Hot Item", "Hot Item (Magazine)", "", "", "", "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		        return true; // This way the expander cannot be collapsed
		    }
		});
	}

	private void setupThumbnail() {
		if (bannerList == null) {
			return;
		}
		ImageItemClickedListener tmpImageItemClickedListener = new ImageItemClickedListener() {
			
			@Override
			public void onImageItemClicked(int position) {
				// TODO Auto-generated method stub
				LogController.log("on item click: " + position);
				HotItem tmpHotItem = bannerList.get(position);
				
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpHotItem, WhatsHotHomeActivity.this, true, null, 0));
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Hot Item", "Highlight Item", tmpHotItem.getLinkRecordType(), tmpHotItem.getObjectId(), tmpHotItem.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		viewFlow.setAdapter(new ViewFlowImageAdapter(this, tmpImageItemClickedListener, bannerList));  
		viewFlow.setmSideBuffer(bannerList.size());
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);  
		viewFlow.setFlowIndicator(indic);  
		viewFlow.setTimeSpan(4500);  
		viewFlow.setSelection(bannerList.size() * 1000);
	}

	private void loadBannerListFromDatabase() {
		try {
			bannerList = CustomServiceFactory.getPromotionService()
					.getHighlightBannerList();
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (viewFlow != null) {
			if (bannerList != null) {
				if (bannerList.size() > 0) {
					viewFlow.startAutoFlowTimer();
				}
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (viewFlow != null) {
			if (bannerList != null) {
				if (bannerList.size() > 0) {
					viewFlow.stopAutoFlowTimer();
				}
			}
		}
	}
	
	public void relaodUnreadContent() {
		exAdapter.resetListContent();
	}
}
