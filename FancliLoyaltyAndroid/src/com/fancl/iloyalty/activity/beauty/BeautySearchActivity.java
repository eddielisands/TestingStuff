package com.fancl.iloyalty.activity.beauty;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.BeautySearchListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.util.LogController;

public class BeautySearchActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 5.4
	
	private View beautySearchLayout;
	private BeautySearchListAdapter beautySearchListViewAdapter; 
	private List<IchannelMagazine> articleList = new ArrayList<IchannelMagazine>(); 
	private EditText searchEditText;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);

        headerTitleTextView.setText(this.getResources().getString(
        		R.string.beauty_ichannel_btn));

        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(3, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		beautySearchLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.search_page, null);
		spaceLayout.addView(beautySearchLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		

		
		ListView articleListView = (ListView) findViewById(R.id.search_result_layout);
		
		beautySearchListViewAdapter = new BeautySearchListAdapter(this, this, handler);

		articleListView.setCacheColorHint(color.transparent);
		articleListView.setDividerHeight(0);
		articleListView.setScrollingCacheEnabled(false);
		articleListView.setAdapter(beautySearchListViewAdapter);
		
		
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		searchEditText.setHint(getString(R.string.beauty_fancl_search_hint));
		searchEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				LogController.log("search:"+searchEditText.getText());
				String tmpSearch = searchEditText.getText().toString();
				try {
					articleList = CustomServiceFactory
							.getPromotionService().getIchannelSearchResultWithKeyword(tmpSearch);
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beautySearchListViewAdapter.setArticleList(articleList);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "Search", "", "", tmpSearch, "Search", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		articleListView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub

            	IchannelMagazine ichannelMagazine = articleList.get(arg2);
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, BeautySearchActivity.this, true, null, 3));
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "Search", "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }

        });
		
		
	}
	
}
