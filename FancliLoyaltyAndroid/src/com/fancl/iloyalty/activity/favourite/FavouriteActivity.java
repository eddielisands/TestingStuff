package com.fancl.iloyalty.activity.favourite;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.message.MessageHomeActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.activity.qrscanner.QRCodeScannerActivity;
import com.fancl.iloyalty.adapter.FavouriteListAdapter;
import com.fancl.iloyalty.adapter.FavouriteListAdapter.DeleteBtnClickedListener;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.asynctask.callback.SubmitPromotionAnswerAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;


public class FavouriteActivity extends MainTabActivity implements SubmitPromotionAnswerAsyncTaskCallback{
	private View favouriteLayout;
	private View qrScanLayout;
	private View myFavouriteLayout;
//	Boolean qrScanSelect;
	Boolean isMyFavouriteSelect = false;
	private String currentFavouriteType = "QR Scan History";
	private String title;
	private FavouriteListAdapter favouriteListViewAdapter;
	private String[] qrIdSplitArray;
	private String[] qrTypeSplitArray;
	private String newFavouriteIdStr = "";
	private String newFavouriteTypeStr = "";
	private List<Object> favouriteArray = new ArrayList<Object>();
	private Boolean isEdit = false;
	private int currentFavouriteRow = 0;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        navigationBarRightBtn.setVisibility(View.VISIBLE);
        navigationBarRightTextView.setText("");
        
        navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.menu_bookmark_btn_title));
        
        this.setupSpaceLayout();
        
        this.setupMenuButtonListener(4, true);
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		favouriteLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.favourite_home_page, null);
		spaceLayout.addView(favouriteLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		qrScanLayout = (RelativeLayout) findViewById(R.id.qr_scan_layout);
		qrScanLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogController.log("qr scan");
				
				isMyFavouriteSelect = false;
				currentFavouriteType="QR Scan History";
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", "", "", "Button Click", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				qrScanLayout.setBackgroundResource(R.drawable.btn_magazine_cat_lft_on);
				myFavouriteLayout.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_off);
				
				getQrScanFavouriteList();
				
				navigationBarRightTextView.setText("");
				navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);
				favouriteListViewAdapter.setShowDeleteBtn(false);
				isEdit = false;
			}
			
		});
		
		myFavouriteLayout = (RelativeLayout) findViewById(R.id.my_favourite_layout);
		myFavouriteLayout.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogController.log("my favourite");
				
				isMyFavouriteSelect = true;
				currentFavouriteType="My Favourite";
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", "", "", "Button Click", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				qrScanLayout.setBackgroundResource(R.drawable.btn_magazine_cat_lft_off);
				myFavouriteLayout.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_on);
				
				getMyFavouriteList();
				
				navigationBarRightTextView.setText("");
				navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);
				favouriteListViewAdapter.setShowDeleteBtn(false);
				isEdit = false;

			}
			
		});
		
		ListView favouriteListView = (ListView) findViewById(R.id.my_favourite_listview);
		favouriteListView.setCacheColorHint(color.transparent);
		favouriteListView.setDividerHeight(0);
		favouriteListView.setScrollingCacheEnabled(false);
		favouriteListViewAdapter = new FavouriteListAdapter(this, this, handler);
		favouriteListView.setAdapter(favouriteListViewAdapter);
		
		this.getQrScanFavouriteList();
		
		
		favouriteListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text

				
				if(isEdit){


					
					
				}else{
					if(qrTypeSplitArray[position].equals("hot")){
						LogController.log("campaign");
						try {
							HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(qrIdSplitArray[position]);
							//							if(tmpObject.getLinkType().equals("product")){
							//								title = getString(R.string.whats_hot_category_new_product);
							//							}else if(tmpObject.getLinkType().equals("shop")){
							//								title = getString(R.string.whats_hot_category_new_shop);
							//							}else if(tmpObject.getLinkType().equals("reading")){
							//								title = getString(R.string.whats_hot_category_new_reading);
							//							}else if(tmpObject.getLinkType().equals("promotion")){
							//								title = getString(R.string.whats_hot_category_new_promotion);
							//							}else if(tmpObject.getLinkType().equals("campaign")){
							title = getString(R.string.whats_hot_category_new_campaign);
							//							}
							if(tmpObject == null){
								AlertDialog alertDialog = new AlertDialog.Builder(
										FavouriteActivity.this).create();
								alertDialog.setMessage(getString(R.string.alert_content_expired));
								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed

									}
								});

								alertDialog.show();
							}else{

								startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, FavouriteActivity.this, true, title, 4));
								
								CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", tmpObject.getObjectId(), tmpObject.getTitleEn(), "View", tmpObject.getObjectId());
							}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}else if(qrTypeSplitArray[position].equals("product")){
						LogController.log("product");
						
						try {
							Product product = CustomServiceFactory
									.getProductService().getProductDetailWithProductId(qrIdSplitArray[position]);
							if(product == null){
								AlertDialog alertDialog = new AlertDialog.Builder(
										FavouriteActivity.this).create();
								alertDialog.setMessage(getString(R.string.alert_content_expired));
								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed

									}
								});

								alertDialog.show();
							}else{
							Intent intent = new Intent(FavouriteActivity.this, ProductDetailActivity.class);
							intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
			            	startActivity(intent);
			            	
			            	CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", product.getObjectId(), product.getTitleEn(), "View", product.getObjectId());
							}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}else if(qrTypeSplitArray[position].equals("shop")){
						LogController.log("shop");
						try {
						Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(qrIdSplitArray[position]);
						if(shopDetail == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									FavouriteActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, FavouriteActivity.this, 4));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", shopDetail.getObjectId(), shopDetail.getTitleEn(), "View", shopDetail.getObjectId());
						}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					
					}else if(qrTypeSplitArray[position].equals("ichannel")){
						LogController.log("ichannel");
						title = getString(R.string.beauty_ichannel_btn);
						try {
							IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(qrIdSplitArray[position]);
							if(ichannelMagazine == null){
								AlertDialog alertDialog = new AlertDialog.Builder(
										FavouriteActivity.this).create();
								alertDialog.setMessage(getString(R.string.alert_content_expired));
								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed

									}
								});

								alertDialog.show();
							}else{
							startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, FavouriteActivity.this, true, title, 4));
							
							CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", ichannelMagazine.getObjectId());
							}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
					}else if(qrTypeSplitArray[position].equals("promotion")){
						LogController.log("promotion");
						title = getString(R.string.promotion_btn);
						try {
							Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(qrIdSplitArray[position]);
							if(promotion == null){
								AlertDialog alertDialog = new AlertDialog.Builder(
										FavouriteActivity.this).create();
								alertDialog.setMessage(getString(R.string.alert_content_expired));
								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed

									}
								});

								alertDialog.show();
							}else{
								SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
								submitPromotionAsyncTask.execute(promotion.getCode());
								int selectedPromtionIndex = 1;
								if(promotion.getPromotionType().equalsIgnoreCase("redemption"))
									selectedPromtionIndex = 4;
								else if(promotion.getPromotionType().equalsIgnoreCase("iCoupon"))
									selectedPromtionIndex = 2;
								else if(promotion.getPromotionType().equalsIgnoreCase("vip"))
									selectedPromtionIndex = 3;
								else if(promotion.getPromotionType().equalsIgnoreCase("latest"))
									selectedPromtionIndex = 5;
								
								startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(FavouriteActivity.this, promotion, true, null, 1, selectedPromtionIndex));
								
								CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", promotion.getObjectId(), promotion.getTitleEn(), "View", promotion.getObjectId());
							}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
					}else if(qrTypeSplitArray[position].equals("fanclMagazine")){
						LogController.log("fanclMagazine");
						title = getString(R.string.menu_fancl_magazine_btn_title);
						try {
						IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(qrIdSplitArray[position]);
						if(ichannelMagazine == null){
							AlertDialog alertDialog = new AlertDialog.Builder(
									FavouriteActivity.this).create();
							alertDialog.setMessage(getString(R.string.alert_content_expired));
							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// Write your code here to execute after dialog closed

								}
							});

							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, FavouriteActivity.this, true, title, 4));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", ichannelMagazine.getObjectId());
						}
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else if(qrTypeSplitArray[position].equals("about")){
						LogController.log("about");
						title = getString(R.string.menu_about_fancl_btn_title);
						try {
							AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground(qrIdSplitArray[position]);
							if(aboutFancl == null){
								AlertDialog alertDialog = new AlertDialog.Builder(
										FavouriteActivity.this).create();
								alertDialog.setMessage(getString(R.string.alert_content_expired));
								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// Write your code here to execute after dialog closed

									}
								});

								alertDialog.show();
							}else{
							startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFancl(aboutFancl, FavouriteActivity.this, aboutFancl.getType(), true, 4));
							
							CustomServiceFactory.getSettingService().addUserLogWithSection("Bookmark", currentFavouriteType, "", aboutFancl.getObjectId(), aboutFancl.getTitleEn(), "View", aboutFancl.getObjectId());
							}
							} catch (FanclException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
					}
				}

				
				

			}
		});
		
		

		DeleteBtnClickedListener listener = new DeleteBtnClickedListener() {
			public void deleteBtnClicked(int row) {
				// TODO Auto-generated method stub
				LogController.log("row:"+row);
				int deleteRow = row;
				
				if(isMyFavouriteSelect)	{
					SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
							Constants.SHARED_PREFERENCE_APPLICATION_KEY,
							Context.MODE_PRIVATE);
					String favouriteTypeStr = sharedPreferences.getString(Constants.MY_FAVOURITE_TYPE_KEY,
							null);
					String favouriteIdStr = sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
							null);

					LogController.log("Favourite - myfavourite:"+ sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
							null));
					
					newFavouriteIdStr = "";
					newFavouriteTypeStr = "";

					if(favouriteTypeStr != null){
						qrTypeSplitArray = favouriteTypeStr.split(",");
						qrIdSplitArray = favouriteIdStr.split(",");

						for (int i = 0; i < qrTypeSplitArray.length; i++) {
							if(i != deleteRow){
								if(newFavouriteIdStr.equals("")){
									newFavouriteIdStr = qrIdSplitArray[i];
									newFavouriteTypeStr = qrTypeSplitArray[i];
								}else{
									newFavouriteIdStr = newFavouriteIdStr+","+ qrIdSplitArray[i];
									newFavouriteTypeStr = newFavouriteTypeStr+","+qrTypeSplitArray[i];
           
								}
							}


						}
					}
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(Constants.MY_FAVOURITE_TYPE_KEY, newFavouriteTypeStr);
					editor.putString(Constants.MY_FAVOURITE_ID_KEY, newFavouriteIdStr);
					editor.commit();
					
					getMyFavouriteList();
				}else{
					SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
							Constants.SHARED_PREFERENCE_APPLICATION_KEY,
							Context.MODE_PRIVATE);
					String favouriteTypeStr = sharedPreferences.getString(Constants.QR_FAVOURITE_TYPE_KEY,
							null);
					String favouriteIdStr = sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
							null);

					LogController.log("Favourite - myfavourite:"+ sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
							null));
					
					newFavouriteIdStr = "";
					newFavouriteTypeStr = "";

					if(favouriteTypeStr != null){
						qrTypeSplitArray = favouriteTypeStr.split(",");
						qrIdSplitArray = favouriteIdStr.split(",");

						for (int i = 0; i < qrTypeSplitArray.length; i++) {
							if(i != deleteRow){
								if(newFavouriteIdStr.equals("")){
									newFavouriteIdStr = qrIdSplitArray[i];
									newFavouriteTypeStr = qrTypeSplitArray[i];
								}else{
									newFavouriteIdStr = newFavouriteIdStr+","+ qrIdSplitArray[i];
									newFavouriteTypeStr = newFavouriteTypeStr+","+qrTypeSplitArray[i];
								}
							}


						}
					}
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(Constants.QR_FAVOURITE_TYPE_KEY, newFavouriteTypeStr);
					editor.putString(Constants.QR_FAVOURITE_ID_KEY, newFavouriteIdStr);
					editor.commit();
					
					getQrScanFavouriteList();
					
				}
				
		        
			}
		};
		
		favouriteListViewAdapter.setListener(listener);
		

		
		
		navigationBarRightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isEdit){
					navigationBarRightTextView.setText("");
					navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);
					favouriteListViewAdapter.setShowDeleteBtn(false);

				}
				else{
					navigationBarRightTextView.setText(getResources().getString(R.string.done_btn_title));
					navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit);
					favouriteListViewAdapter.setShowDeleteBtn(true);

				}
				
				isEdit = !isEdit;

			}
		});
	}
	
	public void getMyFavouriteList(){
		favouriteArray.clear();
		
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String favouriteTypeStr = sharedPreferences.getString(Constants.MY_FAVOURITE_TYPE_KEY,
				null);
		String favouriteIdStr = sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
				null);
		
		LogController.log("Favourite - myfavourite:"+ sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
				null));
		
		if(favouriteTypeStr != null){
			qrTypeSplitArray = favouriteTypeStr.split(",");
			qrIdSplitArray = favouriteIdStr.split(",");
			
			for (int i = 0; i < qrTypeSplitArray.length; i++) {
				if(qrTypeSplitArray[i].equals("ichannel")){
					IchannelMagazine ichannel;
					try {
						ichannel = CustomServiceFactory
								.getPromotionService().getIchannelInfoWithIchannelId(qrIdSplitArray[i]);
						if(ichannel != null)
							favouriteArray.add(ichannel);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(qrTypeSplitArray[i].equals("hot")){
					HotItem hotItem;
					try {
						hotItem = CustomServiceFactory
								.getPromotionService().getHotItemFromHotItemId(qrIdSplitArray[i]);
						if(hotItem != null)
							favouriteArray.add(hotItem);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(qrTypeSplitArray[i].equals("promotion")){
					Promotion promotion;
					try {
						promotion = CustomServiceFactory
								.getPromotionService().getPromotionObjectWithPromotionId(qrIdSplitArray[i]);
						if(promotion != null)
							favouriteArray.add(promotion);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(qrTypeSplitArray[i].equals("product")){
					Product product;
					try {
						product = CustomServiceFactory
								.getProductService().getProductDetailWithProductId(qrIdSplitArray[i]);
						if(product != null)
							favouriteArray.add(product);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else if(qrTypeSplitArray[i].equals("about")){
					AboutFancl aboutFancl;
					try {
						aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground(qrIdSplitArray[i]);
						if(aboutFancl != null)
							favouriteArray.add(aboutFancl);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}
		
		
		
		favouriteListViewAdapter.setArticleList(favouriteArray);
		
	}
	
	public void getQrScanFavouriteList(){
		favouriteArray.clear();
		
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String favouriteTypeStr = sharedPreferences.getString(Constants.QR_FAVOURITE_TYPE_KEY,
				null);
		String favouriteIdStr = sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
				null);

		LogController.log("Favourite - qrScan:"+ sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
				null));

		if(favouriteTypeStr != null){
			qrTypeSplitArray = favouriteTypeStr.split(",");
			qrIdSplitArray = favouriteIdStr.split(",");

			for (int i = 0; i < qrTypeSplitArray.length; i++) {
				if(qrTypeSplitArray[i].equals("ichannel")){
					IchannelMagazine ichannel;
					try {
						ichannel = CustomServiceFactory
								.getPromotionService().getIchannelInfoWithIchannelId(qrIdSplitArray[i]);
						favouriteArray.add(ichannel);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(qrTypeSplitArray[i].equals("hot")){
					HotItem hotItem;
					try {
						hotItem = CustomServiceFactory
								.getPromotionService().getHotItemFromHotItemId(qrIdSplitArray[i]);
						favouriteArray.add(hotItem);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else if(qrTypeSplitArray[i].equals("promotion")){
					Promotion promotion;
					try {
						promotion = CustomServiceFactory
								.getPromotionService().getPromotionObjectWithPromotionId(qrIdSplitArray[i]);
						favouriteArray.add(promotion);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else if(qrTypeSplitArray[i].equals("shop")){
					Shop shop;
					try {
						shop = CustomServiceFactory
								.getAboutFanclService().getShopDetailWithId(qrIdSplitArray[i]);
						favouriteArray.add(shop);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else if(qrTypeSplitArray[i].equals("product")){
					Product product;
					try {
						product = CustomServiceFactory
								.getProductService().getProductDetailWithProductId(qrIdSplitArray[i]);
						favouriteArray.add(product);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else if(qrTypeSplitArray[i].equals("about")){
					AboutFancl aboutFancl;
					try {
						aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground(qrIdSplitArray[i]);
						favouriteArray.add(aboutFancl);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
		
		favouriteListViewAdapter.setArticleList(favouriteArray);
		

	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		
	}


}
