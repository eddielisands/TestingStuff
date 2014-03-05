package com.fancl.iloyalty.activity.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.ProductSubCategoryListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.util.LogController;


public class ProductCategoryListActivity extends MainTabActivity{
	private LinearLayout productCategoryLayout;
	private ProductSubCategoryListAdapter productSubCategoryListViewAdapter;
	private List<Product> articleList = new ArrayList<Product>(); 
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.menu_product_btn_title));
        
        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(4, true);
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		productCategoryLayout = new LinearLayout(this);
//		productCategoryLayout = (RelativeLayout) this.getLayoutInflater().inflate(
//				R.layout.product_home_seasonal_page, null);
		spaceLayout.addView(productCategoryLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
//		ListView categoryList = (ListView) findViewById(R.id.seasonalListView);
		ListView categoryList = new ListView(this);
		categoryList.setCacheColorHint(color.transparent);
		categoryList.setDividerHeight(0);
		categoryList.setScrollingCacheEnabled(false);
		productCategoryLayout.addView(categoryList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		categoryList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.product_home_seasonal_intro, null));
		

		productSubCategoryListViewAdapter = new ProductSubCategoryListAdapter(this, this, handler);
		categoryList.setAdapter(productSubCategoryListViewAdapter);
		
		try {
			articleList = CustomServiceFactory
					.getProductService().getProductListWithCategoryId(this.getIntent().getExtras().getString("PRODUCT_CATE"));
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productSubCategoryListViewAdapter.setArticleList(articleList);
		categoryList.setSelectionAfterHeaderView();
		
		TextView cateTitletext = (TextView)findViewById(R.id.seasonal_title);
		TextView cateIntrotext = (TextView)findViewById(R.id.seasonal_intro);

		cateTitletext.setText(this.getIntent().getExtras().getString("PRODUCT_CATE_TITLE"));
		cateIntrotext.setText(this.getIntent().getExtras().getString("PRODUCT_CATE_DES"));
		
		categoryList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	
            	if(arg2 == 0){
            		return;
            	}

            	LogController.log("arg2:"+ arg2);
            	
            	Product product = articleList.get(arg2-1);
            	
            	Intent intent = new Intent(ProductCategoryListActivity.this, ProductDetailActivity.class);
            	intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
            	startActivity(intent);
            	
            	try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "ProductDetail", "", product.getObjectId(),product.getTitleEn() , "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }

        });
		
	}

}
