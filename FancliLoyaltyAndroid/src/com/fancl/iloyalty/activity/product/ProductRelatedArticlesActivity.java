package com.fancl.iloyalty.activity.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;

import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.beauty.BeautyRelatedArticlesActivity;
import com.fancl.iloyalty.activity.promotion.PromotionRelatedArticlesActivity;
import com.fancl.iloyalty.adapter.RelatedListAdapter;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.util.LogController;


public class ProductRelatedArticlesActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.1.7
	private LinearLayout productRelatedLayout;
	private List<IchannelMagazine> relatedMagazineList;
	private List<Promotion> relatedPromotionList;
	private List<Map<String, String>> productRelatedTypeList = new ArrayList<Map<String, String>>();   
	private List<Map<String, String>> productRelatedList = new ArrayList<Map<String, String>>();
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.related));

        this.setupSpaceLayout();

        this.setupMenuButtonListener(4, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		productRelatedLayout = new LinearLayout(this);
		spaceLayout.addView(productRelatedLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		ListView productRelatedlList = new ListView(this);
		productRelatedlList.setCacheColorHint(color.transparent);
		productRelatedlList.setDividerHeight(0);
		productRelatedlList.setScrollingCacheEnabled(false);
		productRelatedLayout.addView(productRelatedlList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		

		
		try {
			relatedMagazineList = CustomServiceFactory.getProductService().getRelatedArticleWithProductId(this.getIntent().getExtras().getString(Constants.PRODUCT_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			relatedPromotionList = CustomServiceFactory.getProductService().getRelatedPromotionWithProductId(this.getIntent().getExtras().getString(Constants.PRODUCT_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setData();
		RelatedListAdapter adapter = new RelatedListAdapter(this, productRelatedList, productRelatedTypeList);
		productRelatedlList.setAdapter(adapter); 
		
		productRelatedlList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub

            	LogController.log("arg2:"+arg2);
            	LogController.log("relatedPromotionList:"+relatedPromotionList.size());
            	LogController.log("relatedMagazineList:"+relatedMagazineList.size());

                if (relatedPromotionList.size()>0) {
                	if(arg2>relatedPromotionList.size()){
                		IchannelMagazine ichannelMagazine = relatedMagazineList.get(arg2-relatedPromotionList.size()-2);
    					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, ProductRelatedArticlesActivity.this, false, getString(R.string.beauty_ichannel_btn), 4));
                	}else{
                		Promotion promotion = relatedPromotionList.get(arg2-1);
                		startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(ProductRelatedArticlesActivity.this, promotion, false, null, 1, 1));
                		
                		SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
						submitPromotionAsyncTask.execute(promotion.getCode());
                	}

				}else{
					IchannelMagazine ichannelMagazine = relatedMagazineList.get(arg2-1);
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, ProductRelatedArticlesActivity.this, false, getString(R.string.beauty_ichannel_btn), 4));
				}
     
            }

        });
		
	}

	private void setData() {
		// TODO Auto-generated method stub
		Map<String, String> mp = new HashMap<String, String>();  

		if (relatedPromotionList.size() != 0) {
			mp.put("itemTitle", getString(R.string.promotion_btn));  
			productRelatedList.add(mp);  
			productRelatedTypeList.add(mp);  


			for (int i = 0; i < relatedPromotionList.size(); i++) {  
				Promotion promotion = relatedPromotionList.get(i);	       
				Map<String, String> map = new HashMap<String, String>();  
				if(promotion != null){
					map.put("itemTitle", promotion.getTitleEn());  
					map.put("itemDes", promotion.getDescriptionEn());
					map.put("itemImage", promotion.getThumbnail());
					map.put("itemDate", promotion.getPromotionEndDatetime());
					map.put("itemType","promotion");
				}
				productRelatedList.add(map);  

			}  
		}


		if (relatedMagazineList.size() != 0) {
			mp = new HashMap<String, String>();  
			mp.put("itemTitle", getString(R.string.related_information));  
			productRelatedList.add(mp);  
			productRelatedTypeList.add(mp);  

			for (int i = 0; i < relatedMagazineList.size(); i++) {  
				IchannelMagazine ichannelMagazine = relatedMagazineList.get(i);
				Map<String, String> map = new HashMap<String, String>();  
				if(ichannelMagazine != null){
					map.put("itemTitle", ichannelMagazine.getTitleEn());
					map.put("itemImage", ichannelMagazine.getThumbnail());
					map.put("itemType","magazine");
				}
				productRelatedList.add(map);  

			}  
		}

	}

}
