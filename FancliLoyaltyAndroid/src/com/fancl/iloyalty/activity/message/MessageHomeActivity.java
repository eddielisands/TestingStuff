package com.fancl.iloyalty.activity.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.myaccount.MyAccountChangePasswordActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.adapter.MessageListViewAdapter;
import com.fancl.iloyalty.asynctask.GetNotificationListAsyncTask;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetNotificationListAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.item.LoadingDialog;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Notification;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.util.LogController;

public class MessageHomeActivity extends MainTabActivity implements GetNotificationListAsyncTaskCallback{
	
	private RelativeLayout messageListLayout;
	private List<Notification> messageList = new ArrayList<Notification>();
	private List<Notification> messageLatestList = new ArrayList<Notification>();
	private MessageListViewAdapter messageListViewAdapter;
	private String itemType;
	private String itemId;
	private String title;
	private ListView messageListView;
	private Boolean orderFromLatest = false;
	private TextView noRecordText;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		navigationBarRightBtn.setVisibility(View.VISIBLE);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);

		headerTitleTextView.setText(this.getResources().getString(R.string.menu_notification_btn_title));
		
		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		messageListLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.message_home_page, null);
		spaceLayout.addView(messageListLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		GetNotificationListAsyncTask messageAsyncTask = new GetNotificationListAsyncTask(this);
		messageAsyncTask.execute();
		
		
		if (checkLoadingDialog()) {
			loadingDialog.loading();

			final Timer t = new Timer();
			t.schedule(new TimerTask() {
				public void run() {
					if (loadingDialog != null) {
						loadingDialog.stop();
					}
					// when the task active then close the dialog
					t.cancel(); 
				}
			}, Constants.LOADING_DIALOG_TIMEOUT); 
		}
			  
		
		// List View
		messageListViewAdapter = new MessageListViewAdapter(this, this, handler);
		messageListView = (ListView)messageListLayout.findViewById(R.id.message_list_view);
		messageListView.setAdapter(messageListViewAdapter);
		
		noRecordText = new TextView(this);
		noRecordText.setText(R.string.alert_no_record);
		noRecordText.setTextColor(getResources().getColor(R.color.Fancl_Blue));
		noRecordText.setVisibility(View.GONE);
		noRecordText.setTextSize(16);
		noRecordText.setGravity(Gravity.CENTER_HORIZONTAL);
		messageListLayout.addView(noRecordText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		

		messageListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				if(!orderFromLatest){
					itemType = messageList.get(position).getContentType();
					itemId = messageList.get(position).getRecordId();
				}else{
					itemType = messageLatestList.get(position).getContentType();
					itemId = messageLatestList.get(position).getRecordId();
				}
				
				
				if(itemType.equals("campaign")){
					LogController.log("campaign");
					try {
						HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(itemId);
//						if(tmpObject.getLinkType().equals("product")){
//							title = getString(R.string.whats_hot_category_new_product);
//						}else if(tmpObject.getLinkType().equals("shop")){
//							title = getString(R.string.whats_hot_category_new_shop);
//						}else if(tmpObject.getLinkType().equals("reading")){
//							title = getString(R.string.whats_hot_category_new_reading);
//						}else if(tmpObject.getLinkType().equals("promotion")){
//							title = getString(R.string.whats_hot_category_new_promotion);
//						}else if(tmpObject.getLinkType().equals("campaign")){
							title = getString(R.string.whats_hot_category_new_campaign);
//						}
						if(tmpObject == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									MessageHomeActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
							startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, MessageHomeActivity.this, true, title, 4));
							
							CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "HotItem", "", tmpObject.getLinkRecordId(), tmpObject.getTitleEn(), "View", "");
						}
						
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
				}else if(itemType.equals("product")){
					LogController.log("product");
					
					try {
						Product product = CustomServiceFactory
								.getProductService().getProductDetailWithProductId(itemId);
						if(product == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									MessageHomeActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
						Intent intent = new Intent(MessageHomeActivity.this, ProductDetailActivity.class);
						intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
		            	startActivity(intent);
		            	
		            	CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "ProductDetail", "", product.getObjectId(), product.getTitleEn(), "View", "");
						}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}else if(itemType.equals("shop")){
					LogController.log("shop");
					try {
					Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(itemId);
					if(shopDetail == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								MessageHomeActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
					startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, MessageHomeActivity.this, 4));
					
					CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "StoreDetail", "", shopDetail.getObjectId(), shopDetail.getTitleEn(), "View", "");
					}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				
				}else if(itemType.equals("ichannel")){
					LogController.log("ichannel");
					title = getString(R.string.beauty_ichannel_btn);
					try {
						IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(itemId);
						if(ichannelMagazine == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									MessageHomeActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, MessageHomeActivity.this, true, title, 4));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "Beuaty iChannel", "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", "");
						}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
				}else if(itemType.equals("promotion")){
					LogController.log("promotion");
					title = getString(R.string.promotion_btn);
					try {
						Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(itemId);
						if(promotion == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									MessageHomeActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(MessageHomeActivity.this, promotion, true, null, 1, 1));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "Promotion", "", promotion.getObjectId(), promotion.getTitleEn(), "View", "");
						
						SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
						submitPromotionAsyncTask.execute(promotion.getCode());
						}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
				}else if(itemType.equals("fanclMagazine")){
					LogController.log("fanclMagazine");
					title = getString(R.string.menu_fancl_magazine_btn_title);
					try {
					IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(itemId);
					if(ichannelMagazine == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								MessageHomeActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, MessageHomeActivity.this, true, title, 4));
					
					CustomServiceFactory.getSettingService().addUserLogWithSection("Notification", "Magazine", "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", "");
					}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		});
		
		navigationBarRightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(orderFromLatest){
					navigationBarRightTextView.setText(getString(R.string.newest_btn_title));
					messageListViewAdapter.setArticleList(messageList);
				}
				else{
					navigationBarRightTextView.setText(getString(R.string.oldest_btn_title));
					messageListViewAdapter.setArticleList(messageLatestList);
				}
				
				orderFromLatest = !orderFromLatest;

			}
		});
		
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if (results == null) {
			noRecordText.setVisibility(View.VISIBLE);
			return;
		}

		noRecordText.setVisibility(View.GONE);
		
		messageList = (List<Notification>) results;
		messageListViewAdapter.setArticleList(messageList);
		
		
		
		for (int i = messageList.size()-1; i >= 0; i--) {
			messageLatestList.add(messageList.get(i));
		}
		
	    
		
	}

}
