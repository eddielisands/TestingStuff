package com.fancl.iloyalty.activity.qrscanner;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.ZxingRotationTextView;
import com.fancl.iloyalty.item.ZxingRotationTextView.AlignType;
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.QRCode;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

public class QRCodeScannerActivity extends CaptureActivity implements ActivityMonitorServiceCallback {
	protected AndroidProjectApplication application;
	protected Handler handler;
	protected ActivityMonitorService activityMonitorService;
	private List<QRCode> qrCodeList = new ArrayList<QRCode>();
	private String qrCodeId = "";
	private String qrCodeType = "";
	private Boolean found = false;
	private String title;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		application = (AndroidProjectApplication)this.getApplication();
        handler = new Handler();
        
        activityMonitorService = GeneralServiceFactory.getActivityMonitorService();
        activityMonitorService.addCallbackListener(this);
        
        application.addActiveActivity(this);
        
		View view = this.getLayoutInflater().inflate(R.layout.qr_code_overlay_view, null);
		RelativeLayout rotatedHeaderLayout = (RelativeLayout) view.findViewById(R.id.rotated_header_layout);
		RelativeLayout leftBtnLayout = (RelativeLayout) view.findViewById(R.id.rotated_left_btn_layout);
		RelativeLayout maskLayout = (RelativeLayout) view.findViewById(R.id.mask_layout);

		ZxingRotationTextView headerTextView = new ZxingRotationTextView(this, AlignType.MIDDLE);
		headerTextView.setRototeDegree(270);
		headerTextView.setTextSize(18);

		RelativeLayout.LayoutParams headerLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rotatedHeaderLayout.addView(headerTextView, headerLayoutParams);
		headerTextView.setText(getResources().getString(R.string.qrcode_title));

		leftBtnLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ZxingRotationTextView hintTextView = new ZxingRotationTextView(this, AlignType.MIDDLE);
		hintTextView.setRototeDegree(270);
		hintTextView.setTextSize(15);

		RelativeLayout.LayoutParams hintLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		hintLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		hintLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		hintLayoutParams.setMargins(DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10));
		maskLayout.addView(hintTextView, hintLayoutParams);
		hintTextView.setText(getResources().getString(R.string.scan_to_more));
		
		setupOverlayView(view);
	}
	
	@Override
    protected void onResume () 
    {
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnResumed(this);
    	}
    	
    	super.onResume();
    }
	
	@Override
    protected void onDestroy () 
    {
    	activityMonitorService.removeCallbackListener(this);
		application.removeActiveActivity(this);
    	super.onDestroy();
    }
	
	@Override
    protected void onStop () 
    {
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnStopped(this);
    	}
    	
    	super.onStop();
    }

	@SuppressWarnings("deprecation")
	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {
		super.handleDecode(rawResult, barcode);

		LogController.log("QRCodeScanner handleDecode : " + rawResult.getText());
		
		try {
			qrCodeList = CustomServiceFactory.getAboutFanclService().getQRCodeObjects();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LogController.log("QRcode size:"+qrCodeList.size());
		
		for (int i = 0; i < qrCodeList.size(); i++) {
			LogController.log("QRcode:"+qrCodeList.get(i).getQrCodeString());
	        if (qrCodeList.get(i).getQrCodeString().equals(rawResult.getText())) {
	            qrCodeId = qrCodeList.get(i).getQrCodeId();
	            qrCodeType = qrCodeList.get(i).getObjectType();
	            found = true;
	            break;
	        }
	    }
		
		if(found){
			LogController.log("QRcode found");
			
			//add to favourite
			Boolean isInFavouriteList = false;
			
			SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
					Constants.SHARED_PREFERENCE_APPLICATION_KEY,
					Context.MODE_PRIVATE);
			String favouriteTypeStr = sharedPreferences.getString(Constants.QR_FAVOURITE_TYPE_KEY,
					null);
			String favouriteIdStr = sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
					null);
			
			String[] qrIdSplitArray;
			String[] qrTypeSplitArray;
			if(favouriteTypeStr != null){
				qrTypeSplitArray = favouriteTypeStr.split(",");
				qrIdSplitArray = favouriteIdStr.split(",");
				
				for (int i = 0; i < qrTypeSplitArray.length; i++) {
					if(qrIdSplitArray[i].equals(qrCodeId) && qrTypeSplitArray[i].equals(qrCodeType)){
						isInFavouriteList = true;
					}
				}
			}
			
			if(!isInFavouriteList){
				CustomServiceFactory.getAccountService().saveFavouriteList(qrCodeType, qrCodeId, "qrScan");
				LogController.log("add to favourite list");
			}else{
				LogController.log("already in favourite list");
			}
			
			
			//qrScan
			if(qrCodeType.equals("campaign") || qrCodeType.equals("hot")){
				LogController.log("campaign");
				try {
					HotItem tmpObject = CustomServiceFactory.getPromotionService().getHotItemFromHotItemId(qrCodeId);
					//					if(tmpObject.getLinkType().equals("product")){
					//						title = getString(R.string.whats_hot_category_new_product);
					//					}else if(tmpObject.getLinkType().equals("shop")){
					//						title = getString(R.string.whats_hot_category_new_shop);
					//					}else if(tmpObject.getLinkType().equals("reading")){
					//						title = getString(R.string.whats_hot_category_new_reading);
					//					}else if(tmpObject.getLinkType().equals("promotion")){
					//						title = getString(R.string.whats_hot_category_new_promotion);
					//					}else if(tmpObject.getLinkType().equals("campaign")){
					title = getString(R.string.whats_hot_category_new_campaign);
					//					}
					if(tmpObject == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								QRCodeScannerActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
						startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, QRCodeScannerActivity.this, true, title, 4));
					}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}else if(qrCodeType.equals("product")){
				LogController.log("product");
				
				try {
					Product product = CustomServiceFactory
							.getProductService().getProductDetailWithProductId(qrCodeId);
					if(product == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								QRCodeScannerActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
					Intent intent = new Intent(QRCodeScannerActivity.this, ProductDetailActivity.class);
					intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
	            	startActivity(intent);
					}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}else if(qrCodeType.equals("shop")){
				LogController.log("shop");
				try {
				Shop shopDetail = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(qrCodeId);
				if(shopDetail == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							QRCodeScannerActivity.this).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
				startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(shopDetail, QRCodeScannerActivity.this, 4));
				}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			
			}else if(qrCodeType.equals("ichannel")){
				LogController.log("ichannel");
				title = getString(R.string.beauty_ichannel_btn);
				try {
					IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(qrCodeId);
					if(ichannelMagazine == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								QRCodeScannerActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, QRCodeScannerActivity.this, true, title, 4));
					}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
			}else if(qrCodeType.equals("promotion")){
				LogController.log("promotion");
				title = getString(R.string.promotion_btn);
				
				try {
					Promotion promotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(qrCodeId);
					if(promotion == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								QRCodeScannerActivity.this).create();
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
						
					startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(QRCodeScannerActivity.this, promotion, true, null, 1, 1));
					}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
			}else if(qrCodeType.equals("fanclMagazine")){
				LogController.log("fanclMagazine");
				title = getString(R.string.menu_fancl_magazine_btn_title);
				try {
				IchannelMagazine ichannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(qrCodeId);
				if(ichannelMagazine == null){
					AlertDialog alertDialog = new AlertDialog.Builder(
							QRCodeScannerActivity.this).create();
					alertDialog.setMessage(getString(R.string.alert_content_expired));
					alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
				}else{
				startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(ichannelMagazine, QRCodeScannerActivity.this, true, title, 4));
				}
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(qrCodeType.equals("about")){
				LogController.log("about");
				title = getString(R.string.menu_about_fancl_btn_title);
				try {
					AboutFancl aboutFancl = CustomServiceFactory.getAboutFanclService().getFanclBackground(qrCodeId);
					if(aboutFancl == null){
						AlertDialog alertDialog = new AlertDialog.Builder(
								QRCodeScannerActivity.this).create();
						alertDialog.setMessage(getString(R.string.alert_content_expired));
						alertDialog.setButton(getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to execute after dialog closed

							}
						});

						alertDialog.show();
					}else{
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFancl(aboutFancl, QRCodeScannerActivity.this, aboutFancl.getType(), true, 4));
					}
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			
		}else{
			
			LogController.log("QRcode not found");
			
//			AlertDialog alertDialog = new AlertDialog.Builder(
//					QRCodeScannerActivity.this).create();
//			alertDialog.setMessage("Invalid QR code");
//			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					// Write your code here to execute after dialog closed
//
//				}
//			});
//
//			alertDialog.show();
			
			finish();
		}
		
	}

	@Override
	public void applicationGoingToBackground() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applicationGoingToForeground() {
		// TODO Auto-generated method stub
		
	}
}
