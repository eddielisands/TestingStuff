package com.fancl.iloyalty.activity.shop;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.product.ProductQandAActivity;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.LocaleService;

public class ShopDetailActivity extends MainTabActivity {
	private int tabIndex;
	private View shopDetailLayout;
	
	private ListView detailListView ; 
	private Shop shopDetail;
	
	private LocaleService localeService;

	
//	private ArrayAdapter<String> detailListAdapter ; 

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.3.4
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        localeService = GeneralServiceFactory.getLocaleService();
        
        headerTitleTextView.setText(this.getResources().getString(R.string.menu_shop_locator_btn_title));
        
        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(tabIndex, true);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);

    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		shopDetail = (Shop) this.getIntent().getExtras().getSerializable(Constants.SELECTED_SHOP_ITEM_KEY);
		tabIndex = this.getIntent().getIntExtra(Constants.BOTTOM_TAB_INDEX_KEY, 0);

		shopDetailLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.shop_detail_page, null);
		spaceLayout.addView(shopDetailLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		TextView shopName = (TextView)findViewById(R.id.shop_name_textview);
		shopName.setText(localeService.textByLangaugeChooser(this, shopDetail.getTitleEn(), shopDetail.getTitleZh(), shopDetail.getTitleSc()));
		
		AsyncImageView shopImage = (AsyncImageView)findViewById(R.id.shop_thumbnail);
		shopImage.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.SHOP_IMAGE_PATH) + shopDetail.getImage()), Constants.IMAGE_FOLDER);
		
		Button emailButton = (Button)findViewById(R.id.mapButton);
        emailButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(shopDetail.getLatitude() != null && !shopDetail.getLatitude().equals("") && shopDetail.getLongitude() != null && !shopDetail.getLongitude().equals("")){
					Intent intent = new Intent(ShopDetailActivity.this, ShopMapActivity.class);
					intent.putExtra(Constants.SELECTED_SHOP_ITEM_KEY, shopDetail);
					intent.putExtra("SHOW_ARROW", false);
					startActivity(intent);
				}else{
					AlertDialog alertDialog = new AlertDialog.Builder(
							ShopDetailActivity.this).create();
					alertDialog.setMessage("No Latitude/Longitude in this shop");
					alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}
				
			}         

        });
        
//        LinearLayout shopDetailLayout = (LinearLayout) findViewById(R.id.shopDetailLayout); 
//        
//        detailListView = new ListView(this);
//        detailListView.setCacheColorHint(color.transparent);
//        detailListView.setDividerHeight(0);
//        detailListView.setScrollingCacheEnabled(false);
//        shopDetailLayout.addView(detailListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
        detailListView = (ListView) findViewById(R.id.shop_listview); 
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();  
        for(int i=0;i<3;i++)  
        {  
            HashMap<String, String> map = new HashMap<String, String>();  
            if (i==0) {
            	map.put("ItemTitle", getString(R.string.shop_address));  
            	map.put("ItemText", localeService.textByLangaugeChooser(this, shopDetail.getAddressEn(), shopDetail.getAddressZh(), shopDetail.getAddressSc())); 
			}else if (i==1) {
				map.put("ItemTitle", getString(R.string.shop_phone)); 
				map.put("ItemText", shopDetail.getPhoneNumber()); 
			}else if(i==2){
				map.put("ItemTitle", getString(R.string.shop_hour)); 
				map.put("ItemText", localeService.textByLangaugeChooser(this, shopDetail.getOfficeHourEn(), shopDetail.getOfficeHourZh(), shopDetail.getOfficeHourSc())); 
			}
             
            mylist.add(map);  
        }  

        SimpleAdapter mSchedule = new SimpleAdapter(this,  
                                                    mylist,   
                                                    R.layout.shop_detail_list_row,  
                                                      
                                                             
                                                    new String[] {"ItemTitle", "ItemText"},   
 
                                                    new int[] {R.id.title_textview,R.id.detail_textview});  
  
        detailListView.setAdapter(mSchedule);
		
	}
	
}
