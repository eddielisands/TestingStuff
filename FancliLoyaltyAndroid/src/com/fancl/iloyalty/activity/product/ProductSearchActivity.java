package com.fancl.iloyalty.activity.product;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.adapter.ProductSearchListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.util.LogController;

public class ProductSearchActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.1.6
	
	private View productSearchLayout;
	private ProductSearchListAdapter productSearchListViewAdapter; 
	private List<Product> articleList = new ArrayList<Product>(); 
	private EditText searchEditText;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);

        headerTitleTextView.setText(this.getResources().getString(
        		R.string.menu_product_btn_title));

        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(4, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		productSearchLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.search_page, null);
		spaceLayout.addView(productSearchLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		

		
		ListView articleListView = (ListView) findViewById(R.id.search_result_layout);
		
		productSearchListViewAdapter = new ProductSearchListAdapter(this, this, handler);

		articleListView.setCacheColorHint(color.transparent);
		articleListView.setDividerHeight(0);
		articleListView.setScrollingCacheEnabled(false);
		articleListView.setAdapter(productSearchListViewAdapter);

		
		
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		searchEditText.setHint(getString(R.string.product_search_hint));
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
							.getProductService().getProductSearchResultWithKeyword(tmpSearch);
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				productSearchListViewAdapter.setArticleList(articleList);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "ProductSearchResultPage", "", "", tmpSearch, "Search", "");
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

            	Product product = articleList.get(arg2);
            	Intent intent = new Intent(ProductSearchActivity.this, ProductDetailActivity.class);
            	intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
            	startActivity(intent);
            	
            	try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "Product Detail", "", product.getObjectId(), product.getTitleEn(), "FromSearch", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

            }

        });
		
	}
	
}
