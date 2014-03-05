package com.fancl.iloyalty.activity.beauty;

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
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.activity.product.ProductHomeActivity;
import com.fancl.iloyalty.activity.promotion.PromotionHomeActivity;
import com.fancl.iloyalty.adapter.RelatedListAdapter;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.util.LogController;

public class BeautyRelatedArticlesActivity extends MainTabActivity{
	
	private LinearLayout ichannelRelatedLayout;
	private List<Promotion> relatedPromotionList;
	private List<Product> relatedProductList;
	private List<Map<String, String>> ichannelRelatedTypeList = new ArrayList<Map<String, String>>();   
	private List<Map<String, String>> ichannelRelatedList = new ArrayList<Map<String, String>>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.related));

        this.setupSpaceLayout();

        this.setupMenuButtonListener(3, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		ichannelRelatedLayout = new LinearLayout(this);
		spaceLayout.addView(ichannelRelatedLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		ListView ichannelRelatedlList = new ListView(this);
		ichannelRelatedlList.setCacheColorHint(color.transparent);
		ichannelRelatedlList.setDividerHeight(0);
		ichannelRelatedlList.setScrollingCacheEnabled(false);
		ichannelRelatedLayout.addView(ichannelRelatedlList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		try {
			relatedPromotionList = CustomServiceFactory.getPromotionService().getIchannelRelatedPromotionWithIchannelId(this.getIntent().getExtras().getString(Constants.ICHANNEL_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			relatedProductList = CustomServiceFactory.getPromotionService().getIchannelRelatedProductWithIchannelId(this.getIntent().getExtras().getString(Constants.ICHANNEL_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setData();
		RelatedListAdapter adapter = new RelatedListAdapter(this, ichannelRelatedList, ichannelRelatedTypeList);
		ichannelRelatedlList.setAdapter(adapter); 
		
		ichannelRelatedlList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub


                if (relatedProductList.size()>0) {
                	if(arg2>relatedProductList.size()){
                		Promotion promotion = relatedPromotionList.get(arg2-relatedProductList.size()-2);
                		startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(BeautyRelatedArticlesActivity.this, promotion, false, null, 1, 1));
                		
                		SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
						submitPromotionAsyncTask.execute(promotion.getCode());
						
						try {
							CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "Promotion Detail", "", promotion.getObjectId(), promotion.getTitleEn(), "View", "");
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                	}else{
                		Product product = relatedProductList.get(arg2-1);
                    	
                    	Intent intent = new Intent(BeautyRelatedArticlesActivity.this, ProductDetailActivity.class);
    					intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
    	            	startActivity(intent);
    	            	
    	            	try {
    						CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "Product Detail", "", product.getObjectId(), product.getTitleEn(), "View", "");
    					} catch (FanclException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                	}
                	
				}else{
					Promotion promotion = relatedPromotionList.get(arg2-1);
					startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(BeautyRelatedArticlesActivity.this, promotion, false, null, 1, 1));
					
					SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
					submitPromotionAsyncTask.execute(promotion.getObjectId());
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("iBeauty Channel", "Promotion Detail", "", promotion.getObjectId(), promotion.getTitleEn(), "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
            	

            }

        });
		
	}

	private void setData() {
		// TODO Auto-generated method stub
		Map<String, String> mp = new HashMap<String, String>();  

		if (relatedProductList.size() != 0) {
			mp.put("itemTitle", getString(R.string.related_product));  
			ichannelRelatedList.add(mp);  
			ichannelRelatedTypeList.add(mp);  


			for (int i = 0; i < relatedProductList.size(); i++) {  
				Product product = relatedProductList.get(i);	       
				Map<String, String> map = new HashMap<String, String>();  
				if(product != null){
					map.put("itemTitle", product.getTitleEn());  
					map.put("itemTitleTc", product.getTitleZh());
					map.put("itemImage", product.getThumbnail());
					map.put("itemType","product");
				}
				ichannelRelatedList.add(map);  

			}  
		}


		if (relatedPromotionList.size() != 0) {
			mp = new HashMap<String, String>();  
			mp.put("itemTitle", getString(R.string.related_information));  
			ichannelRelatedList.add(mp);  
			ichannelRelatedTypeList.add(mp);  

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
				ichannelRelatedList.add(map);  

			}  
		}
		
	}

}
