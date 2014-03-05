package com.fancl.iloyalty.activity.advertisement;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.favourite.FavouriteActivity;
import com.fancl.iloyalty.activity.message.MessageHomeActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.AdBanner;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;



public class AdvertisementActivity extends AndroidProjectFrameworkActivity {
	private List<AdBanner> bannerList = new ArrayList<AdBanner>(); 
	private AsyncImageView bannerView;
	private WebView bannerWebView;
	private String bannerURL;
	private String title;
	private int ranNum;
	private LocaleService localeService;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		localeService = GeneralServiceFactory.getLocaleService();

		setContentView(R.layout.advertisement_page); 
		
		try {
			bannerList = CustomServiceFactory.getAboutFanclService().getFrontAdObjects();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		WebViewClient mWebViewClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		};
		
		int Min = 0;
		int Max = bannerList.size();
		ranNum = (int) (Math.random() * ( Max - Min ));
		LogController.log("ranNum:"+ranNum);
		
		bannerWebView = (WebView) findViewById(R.id.bannerWebView);
		bannerWebView.setWebViewClient(mWebViewClient);
		

		bannerView = (AsyncImageView) findViewById(R.id.banner_image);
		String tmpImage = localeService.textByLangaugeChooser(this, bannerList.get(ranNum).getImageEn(), bannerList.get(ranNum).getImageZh(), bannerList.get(ranNum).getImageSc());
		bannerView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.AD_IMAGE_PATH) + tmpImage), Constants.IMAGE_FOLDER);
		bannerView.setOnClickListener(new View.OnClickListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				
				LogController.log("itemType:"+bannerList.get(ranNum).getItemType());
				
				try {
					CustomServiceFactory.getSettingService().countAdHitRateWithBannerId(bannerList.get(ranNum).getItemId());
				} catch (FanclException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (bannerList.get(ranNum).getItemType() == "" || bannerList.get(ranNum).getItemType() == null) {
					bannerURL = localeService.textByLangaugeChooser(AdvertisementActivity.this, bannerList.get(ranNum).getLinkEn(), bannerList.get(ranNum).getLinkZh(), bannerList.get(ranNum).getLinkSc());
					bannerWebView.loadUrl(bannerURL); 
					bannerView.setVisibility(View.GONE);
					
				}else{
					if(bannerList.get(ranNum).getItemType().equals("whatsHot")){
						LogController.log("whatsHot");
						try {
							HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(bannerList.get(ranNum).getItemId());
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
//									AlertDialog alertDialog = new AlertDialog.Builder(
//											AdvertisementActivity.this).create();
//									alertDialog.setMessage(getString(R.string.alert_content_expired));
//									alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog, int which) {
//											// Write your code here to execute after dialog closed
//
//										}
//									});
//
//									alertDialog.show();
								}else{
							
							startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, AdvertisementActivity.this, true, title, 0));
							CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", tmpObject.getObjectId(), tmpObject.getTitleEn(), "View", tmpObject.getLinkRecordId());
								}
							
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						
					}else if(bannerList.get(ranNum).getItemType().equals("product")){
						LogController.log("product");
						
						try {
							Product product = CustomServiceFactory
									.getProductService().getProductDetailWithProductId(bannerList.get(ranNum).getItemId());
							if(product == null){
//								AlertDialog alertDialog = new AlertDialog.Builder(
//										AdvertisementActivity.this).create();
//								alertDialog.setMessage(getString(R.string.alert_content_expired));
//								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int which) {
//										// Write your code here to execute after dialog closed
//
//									}
//								});
//
//								alertDialog.show();
							}else{
							Intent intent = new Intent(AdvertisementActivity.this, ProductDetailActivity.class);
							intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
			            	startActivity(intent);
			            	
			            	CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", product.getObjectId(), product.getTitleEn(), "View", product.getObjectId());
							}
			            	
			            	
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
					}else if(bannerList.get(ranNum).getItemType().equals("shop")){
						LogController.log("shop");
						try {
						Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(bannerList.get(ranNum).getItemId());
						if(shopDetail == null){
//							AlertDialog alertDialog = new AlertDialog.Builder(
//									AdvertisementActivity.this).create();
//							alertDialog.setMessage(getString(R.string.alert_content_expired));
//							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									// Write your code here to execute after dialog closed
//
//								}
//							});
//
//							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, AdvertisementActivity.this, 0));
						CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", shopDetail.getObjectId(), shopDetail.getTitleEn(), "View", shopDetail.getObjectId());
						}
						
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					
					}else if(bannerList.get(ranNum).getItemType().equals("ichannel")){
						LogController.log("ichannel");
						title = getString(R.string.beauty_ichannel_btn);
						try {
							IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(bannerList.get(ranNum).getItemId());
							if(ichannelMagazine == null){
//								AlertDialog alertDialog = new AlertDialog.Builder(
//										AdvertisementActivity.this).create();
//								alertDialog.setMessage(getString(R.string.alert_content_expired));
//								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int which) {
//										// Write your code here to execute after dialog closed
//
//									}
//								});
//
//								alertDialog.show();
							}else{
							startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, AdvertisementActivity.this, true, title, 0));
							CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", ichannelMagazine.getObjectId());
							}
							
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
						
					}else if(bannerList.get(ranNum).getItemType().equals("promotion")){
						LogController.log("promotion");
						title = getString(R.string.promotion_btn);
						
						try {
							Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(bannerList.get(ranNum).getItemId());
							if(promotion == null){
//								AlertDialog alertDialog = new AlertDialog.Builder(
//										AdvertisementActivity.this).create();
//								alertDialog.setMessage(getString(R.string.alert_content_expired));
//								alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog, int which) {
//										// Write your code here to execute after dialog closed
//
//									}
//								});
//
//								alertDialog.show();
							}else{
								startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(AdvertisementActivity.this, promotion, true, null, 0, 1));

								CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", promotion.getObjectId(), promotion.getTitleEn(), "View", promotion.getObjectId());

								SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
								submitPromotionAsyncTask.execute(promotion.getCode());
							}
							
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else if(bannerList.get(ranNum).getItemType().equals("fanclMagazine")){
						LogController.log("fanclMagazine");
						title = getString(R.string.menu_fancl_magazine_btn_title);
						try {
						IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(bannerList.get(ranNum).getItemId());
						if(ichannelMagazine == null){
//							AlertDialog alertDialog = new AlertDialog.Builder(
//									AdvertisementActivity.this).create();
//							alertDialog.setMessage(getString(R.string.alert_content_expired));
//							alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog, int which) {
//									// Write your code here to execute after dialog closed
//
//								}
//							});
//
//							alertDialog.show();
						}else{
						startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, AdvertisementActivity.this, true, title, 0));
						CustomServiceFactory.getSettingService().addUserLogWithSection("Advertisement", "", "", ichannelMagazine.getObjectId(), ichannelMagazine.getTitleEn(), "View", ichannelMagazine.getObjectId());
						}
						
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					finish();
					
				}
				
				
			}
		});
		
		

		Button closeBtn = (Button) findViewById(R.id.close_btn);
		closeBtn.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				LogController.log("close ad");
				finish();

			}         

		});
		
		
	}
	
	

	

}

