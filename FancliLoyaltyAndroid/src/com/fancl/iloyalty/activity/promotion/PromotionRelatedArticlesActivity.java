package com.fancl.iloyalty.activity.promotion;

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
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.adapter.RelatedListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.util.LogController;


public class PromotionRelatedArticlesActivity extends MainTabActivity{
	
	private LinearLayout promotionRelatedLayout;
	private List<IchannelMagazine> relatedMagazineList;
	private List<Product> relatedProductList;
	private List<Map<String, String>> promotionRelatedTypeList = new ArrayList<Map<String, String>>();   
	private List<Map<String, String>> promotionRelatedList = new ArrayList<Map<String, String>>();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.related));

        this.setupSpaceLayout();

        this.setupMenuButtonListener(1, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		promotionRelatedLayout = new LinearLayout(this);
		spaceLayout.addView(promotionRelatedLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		ListView promotionRelatedlList = new ListView(this);
		promotionRelatedlList.setCacheColorHint(color.transparent);
		promotionRelatedlList.setDividerHeight(0);
		promotionRelatedlList.setScrollingCacheEnabled(false);
		promotionRelatedLayout.addView(promotionRelatedlList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		try {
			relatedMagazineList = CustomServiceFactory.getPromotionService().getPromotionRelatedArticleWithPromotionId(this.getIntent().getExtras().getString(Constants.PROMOTION_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			relatedProductList = CustomServiceFactory.getPromotionService().getPromotionRelatedProductWithPromotionId(this.getIntent().getExtras().getString(Constants.PROMOTION_ID_RELATED_KEY));
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setData();
		RelatedListAdapter adapter = new RelatedListAdapter(this, promotionRelatedList, promotionRelatedTypeList);
		promotionRelatedlList.setAdapter(adapter); 
		
		promotionRelatedlList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub

            	LogController.log("arg2:"+arg2);

                if (relatedProductList.size()>0) {
                	if(arg2>relatedProductList.size()){
                		IchannelMagazine ichannelMagazine = relatedMagazineList.get(arg2-relatedProductList.size()-2);
    					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, PromotionRelatedArticlesActivity.this, false, getString(R.string.beauty_ichannel_btn), 4));
                	}else{
                		Product product = relatedProductList.get(arg2-1);
                    	
                    	Intent intent = new Intent(PromotionRelatedArticlesActivity.this, ProductDetailActivity.class);
    					intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
    	            	startActivity(intent);
                	}
                	
				}else{
					IchannelMagazine ichannelMagazine = relatedMagazineList.get(arg2-1);
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, PromotionRelatedArticlesActivity.this, false, getString(R.string.beauty_ichannel_btn), 4));
				}
            	

            }

        });
		
	}

	private void setData() {
		// TODO Auto-generated method stub
		Map<String, String> mp = new HashMap<String, String>();  

		if (relatedProductList.size() != 0) {
			mp.put("itemTitle", getString(R.string.related_product));  
			promotionRelatedList.add(mp);  
			promotionRelatedTypeList.add(mp);  


			for (int i = 0; i < relatedProductList.size(); i++) {  
				Product product = relatedProductList.get(i);	       
				Map<String, String> map = new HashMap<String, String>();  
				if(product != null){
					map.put("itemTitle", product.getTitleEn());  
					map.put("itemTitleTc", product.getTitleZh());
					map.put("itemImage", product.getThumbnail());
					map.put("itemType","product");
				}
				promotionRelatedList.add(map);  

			}  
		}


		if (relatedMagazineList.size() != 0) {
			mp = new HashMap<String, String>();  
			mp.put("itemTitle", getString(R.string.related_information));  
			promotionRelatedList.add(mp);  
			promotionRelatedTypeList.add(mp);  

			for (int i = 0; i < relatedMagazineList.size(); i++) {  
				IchannelMagazine ichannelMagazine = relatedMagazineList.get(i);
				Map<String, String> map = new HashMap<String, String>();  
				if(ichannelMagazine != null){
					map.put("itemTitle", ichannelMagazine.getTitleEn());
					map.put("itemImage", ichannelMagazine.getThumbnail());
					map.put("itemType","magazine");
				}
				promotionRelatedList.add(map);  

			}  
		}
		
	}

}
