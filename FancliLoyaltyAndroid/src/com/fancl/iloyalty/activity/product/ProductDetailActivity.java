package com.fancl.iloyalty.activity.product;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.detail.DetailActivity;
import com.fancl.iloyalty.activity.sharing.SharingPanelActivity;
import com.fancl.iloyalty.activity.sharing.TwitterSharingDialogActivity;
import com.fancl.iloyalty.adapter.ProductChoiceViewFlowAdapter;
import com.fancl.iloyalty.adapter.ProductChoiceViewFlowAdapter.ChoiceItemClickedListener;
import com.fancl.iloyalty.asynctask.EarnCreditAsyncTask;
import com.fancl.iloyalty.asynctask.callback.EarnCreditAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.item.CircleFlowIndicator;
import com.fancl.iloyalty.item.ViewFlow;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.ProductCategory;
import com.fancl.iloyalty.pojo.ProductChoice;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.impl.FacebookService30Impl;
import com.gt.snssharinglibrary.service.impl.TwitterServiceImpl;

public class ProductDetailActivity extends MainTabActivity implements SNSServiceCallback, EarnCreditAsyncTaskCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.1.3
	private int tabIndex;
	private LocaleService localeService;
	private boolean isNoMoreRelated;
	private Product product;
	private List<ProductChoice> productChoiceList;
	private List<IchannelMagazine> relatedMagazineList;
	private List<Promotion> relatedPromotionList;

	private RelativeLayout contentDetailLayout;
	private TextView productTitleTextView;
	private RelativeLayout singleImageLayout;
	private RelativeLayout choiceImageLayout;
	
	private AsyncImageView singleImageView;
	
	private AsyncImageView choiceImageView;
	private TextView subtitleTextView;
	private ViewFlow choiceViewFlow;
	private CircleFlowIndicator choiceFlowIndicator;
	private ProductChoiceViewFlowAdapter productChoiceViewFlowAdapter;
	
	private LinearLayout functionsLayout;
	private LinearLayout howtouseLayout;
	private LinearLayout ingredientsLayout;
	private TextView functionTextView;
	private TextView howtouseTextView;
	private TextView ingredientsTextView;
	private LinearLayout footerLayout;
	
	private SNSService facebookServiceImpl;
	private SNSService twitterServiceImpl;
	
	private final int FACEBOOK_CODE = 0;
	private final int TWITTER_CODE = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localeService = GeneralServiceFactory.getLocaleService();
		
		Config.TWITTER_CONSUMER_KEY = "jvKys4Lr2afHg7WxvQ4M1g";
		Config.TWITTER_CONSUMER_SECRET = "Z9icVCBu2j1Da1O2mMEiggkBcXgmfk2OjAE7qvrV0A";
		Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.btn_cross;
		twitterServiceImpl = new TwitterServiceImpl(ProductDetailActivity.this);
		twitterServiceImpl.setSNSServiceCallback(ProductDetailActivity.this);
		
		Config.facebook30SharingType = SHARING_TYPE.WEB_DIALOG;
		Config.FACEBOOK_PERMISSIONS = new String[] { "email", "user_birthday", "read_friendlists", "user_likes" };
		facebookServiceImpl = new FacebookService30Impl();
		facebookServiceImpl.setSNSServiceCallback(ProductDetailActivity.this);
		facebookServiceImpl.onCreate(ProductDetailActivity.this, savedInstanceState);

		Intent intent = this.getIntent();
		isNoMoreRelated = intent.getBooleanExtra(Constants.NO_MORE_RELATED_KEY, true);
		tabIndex = intent.getIntExtra(Constants.BOTTOM_TAB_INDEX_KEY, 0);
		product = (Product) intent.getExtras().getSerializable(Constants.PRODUCT_ITEM_KEY);
		
		if (product != null) {
			String eventId = null;
			try {
				List<Event> events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(product.getObjectId());
				for (int i = 0; i < events.size(); i++) {
					Event event = events.get(i);
					if (event.getEventType().equals("read")) {
						eventId = event.getEventId();
						break;
					}
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (eventId != null) {
				EarnCreditAsyncTask earnCreditAsyncTask = new EarnCreditAsyncTask(this);
				earnCreditAsyncTask.execute(eventId);
			}
		}
		
		try {
			productChoiceList = CustomServiceFactory.getProductService().getProductChoiceWithProductId(product.getObjectId());
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			relatedMagazineList = CustomServiceFactory.getProductService().getRelatedArticleWithProductId(product.getObjectId());
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			relatedPromotionList = CustomServiceFactory.getProductService().getRelatedPromotionWithProductId(product.getObjectId());
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ProductCategory productCategory = null;
		try {
			productCategory = (ProductCategory) CustomServiceFactory.getProductService().getProductCategoryWithProductId(product.getObjectId());
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (productCategory != null) {
			headerTitleTextView.setText(localeService.textByLangaugeChooser(this, productCategory.getTitleEn(), productCategory.getTitleZh(), productCategory.getTitleSc()));
		}
		else {
			headerTitleTextView.setText(getString(R.string.menu_product_btn_title));
		}

		this.setupSpaceLayout();

		this.setupMenuButtonListener(tabIndex, true);
		
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarShareBtn.setVisibility(View.VISIBLE);

		navigationBarShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProductDetailActivity.this, SharingPanelActivity.class);
				startActivityForResult(intent, Constants.SHARING_PANEL_INTENT);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void setupSpaceLayout() {
		// Space Layout
		contentDetailLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.product_detail_page, null);
		spaceLayout.addView(contentDetailLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		productTitleTextView = (TextView) findViewById(R.id.product_title);
		singleImageLayout = (RelativeLayout) findViewById(R.id.product_detail_single_image_view_layout);
		choiceImageLayout = (RelativeLayout) findViewById(R.id.product_detail_selective_image_view_layout);
		singleImageView = (AsyncImageView) findViewById(R.id.product_detail_single_image);
		choiceImageView = (AsyncImageView) findViewById(R.id.product_detail_selective_image);
		subtitleTextView = (TextView) findViewById(R.id.product_detail_subtitle);
		choiceViewFlow = (ViewFlow) findViewById(R.id.product_detail_view_pager);
		choiceFlowIndicator = (CircleFlowIndicator) findViewById(R.id.product_detail_view_pager_page_indicator);
		functionsLayout = (LinearLayout) findViewById(R.id.product_detail_function_layout);
		howtouseLayout = (LinearLayout) findViewById(R.id.product_detail_howtouse_layout);
		ingredientsLayout = (LinearLayout) findViewById(R.id.product_detail_ingredients_layout);
		functionTextView = (TextView) findViewById(R.id.product_function_content);
		howtouseTextView = (TextView) findViewById(R.id.product_howtouse_content);
		ingredientsTextView = (TextView) findViewById(R.id.product_ingredients_content);
		footerLayout = (LinearLayout) findViewById(R.id.product_detail_right_footer_layout);

		productTitleTextView.setText(localeService.textByLangaugeChooser(this, product.getTitleEn(), product.getTitleZh(), product.getTitleSc()));
		if (productChoiceList != null) {
			if (productChoiceList.size() > 0) {
				choiceImageView.setRequestingUrl(handler, ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getImage(), Constants.IMAGE_FOLDER);
				
				choiceImageLayout.setVisibility(View.VISIBLE);
				
				ChoiceItemClickedListener tmpChoiceItemClickedListener = new ChoiceItemClickedListener() {

					@Override
					public void onChoiceItemClicked(ProductChoice productChoice) {
						// TODO Auto-generated method stub
						subtitleTextView.setText(localeService.textByLangaugeChooser(ProductDetailActivity.this, productChoice.getTitleEn(), productChoice.getTitleZh(), productChoice.getTitleSc()));
					}
				};
				productChoiceViewFlowAdapter = new ProductChoiceViewFlowAdapter(handler, this, productChoiceList, tmpChoiceItemClickedListener);
				choiceViewFlow.setAdapter(productChoiceViewFlowAdapter);
				choiceViewFlow.setmSideBuffer(Math.round(((float)(productChoiceList.size() / 6) + 0.5f)));
				choiceViewFlow.setFlowIndicator(choiceFlowIndicator);  
				productChoiceViewFlowAdapter.autoSelectFirstItem();
			}
			else {
				
				singleImageView.setRequestingUrl(handler, ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getImage(), Constants.IMAGE_FOLDER);
				singleImageLayout.setVisibility(View.VISIBLE);
			}
		}
		else {
			singleImageView.setRequestingUrl(handler, ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getImage(), Constants.IMAGE_FOLDER);
			singleImageLayout.setVisibility(View.VISIBLE);
		}
		
		String functionContent = localeService.textByLangaugeChooser(this, product.getBenefitEn(), product.getBenefitZh(), product.getBenefitSc());
		if (functionContent.length() > 0) {
			functionsLayout.setVisibility(View.VISIBLE);
			functionTextView.setText(functionContent);
		}
		
		String howtouseContent = localeService.textByLangaugeChooser(this, product.getHowToUseEn(), product.getHowToUseZh(), product.getHowToUseSc());
		if (howtouseContent.length() > 0) {
			howtouseLayout.setVisibility(View.VISIBLE);
			howtouseTextView.setText(howtouseContent);
		}
		
		String ingredientContent = localeService.textByLangaugeChooser(this, product.getIngredientEn(), product.getIngredientZh(), product.getIngredientSc());
		if (ingredientContent.length() > 0) {
			ingredientsLayout.setVisibility(View.VISIBLE);
			ingredientsTextView.setText(ingredientContent);
		}

		if (!isNoMoreRelated || relatedMagazineList != null || relatedPromotionList != null) {
			if (relatedMagazineList.size() > 0 || relatedPromotionList.size() > 0) {
				footerLayout.setVisibility(View.VISIBLE);
				footerLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LogController.log("product related");
						
						Intent intent = new Intent(ProductDetailActivity.this, ProductRelatedArticlesActivity.class);
						intent.putExtra(Constants.PRODUCT_ID_RELATED_KEY, product.getObjectId());
		            	startActivity(intent);
						
					}
				});
			}
		}
	}
	
	private void facebookPostAction() {
		SNSShareDetail snsShareDetail = null;

		if (Config.facebook30SharingType.equals(Config.SHARING_TYPE.WEB_DIALOG))
		{
			String title = localeService.textByLangaugeChooser(ProductDetailActivity.this, 
					product.getTitleEn(), product.getTitleZh(), product.getTitleSc());
			String caption = null;
			String description = null;
			String link = "http://www.fancl-hk.com/";
			String pictureLink = ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getImage();
			String friendId = null;
			Bitmap bitmap = null;
			String picSavePath = null;

			snsShareDetail = new SNSShareDetail(title, caption, description, link, pictureLink, friendId, bitmap, picSavePath);
		}
		else if (Config.facebook30SharingType.equals(Config.SHARING_TYPE.PLAIN_TEXT))
		{
			String message = "Facebook Plain Text Message Testing. " + (new Date()).toString();
			snsShareDetail = new SNSShareDetail(message);
		}

		facebookServiceImpl.post(ProductDetailActivity.this, snsShareDetail);
	}
	
	private void twitterPostAction() {
		Intent intent = new Intent(ProductDetailActivity.this, TwitterSharingDialogActivity.class);
		intent.putExtra(Constants.SHARING_FORMAT_KEY, "product");
		intent.putExtra(Constants.SHARING_CONTENT_KEY, product);
		startActivity(intent);
	}
	
	private void sendEmailAction() {
		String title = localeService.textByLangaugeChooser(this, product.getTitleEn(), product.getTitleZh(), product.getTitleSc());
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_SUBJECT, title);
		email.putExtra(Intent.EXTRA_TEXT, "");
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if (resultCode == DetailActivity.RESULT_OK) {
			int shareKey = data.getIntExtra(Constants.SHARING_PANEL_RETURN_KEY, -1);
			switch (shareKey) {
			case Constants.SHARING_PANEL_RETURN_FB:
				if (facebookServiceImpl.isLogged(ProductDetailActivity.this)) {
					facebookPostAction();
				}
				else {
					facebookServiceImpl.login(ProductDetailActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
				}
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Share to facebook", "", product.getObjectId(), product.getTitleEn()
							, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Constants.SHARING_PANEL_RETURN_TW:
				if (twitterServiceImpl.isLogged(ProductDetailActivity.this)) {
					twitterPostAction();
				}
				else {
					twitterServiceImpl.login(ProductDetailActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
				}
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Share to twitter", "", product.getObjectId(), product.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Constants.SHARING_PANEL_RETURN_EMAIL:
				sendEmailAction();
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Email to friends", "", product.getObjectId(), product.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Constants.SHARING_PANEL_RETURN_FAVOURITE:
				LogController.log("Share favourite");
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Add to bookmark", "", product.getObjectId(), product.getTitleEn(), "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Boolean isInFavouriteList = false;

				SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
						Constants.SHARED_PREFERENCE_APPLICATION_KEY,
						Context.MODE_PRIVATE);
				String favouriteTypeStr = sharedPreferences.getString(Constants.MY_FAVOURITE_TYPE_KEY,
						null);
				String favouriteIdStr = sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
						null);

				String[] qrIdSplitArray;
				String[] qrTypeSplitArray;
				if(favouriteTypeStr != null){
					qrTypeSplitArray = favouriteTypeStr.split(",");
					qrIdSplitArray = favouriteIdStr.split(",");

					for (int i = 0; i < qrTypeSplitArray.length; i++) {
						if(qrIdSplitArray[i].equals(product.getObjectId()) && qrTypeSplitArray[i].equals("product")){
							isInFavouriteList = true;
						}
					}
				}

				if(!isInFavouriteList){
					CustomServiceFactory.getAccountService().saveFavouriteList("product", product.getObjectId(), "myFavourite");
					LogController.log("add to favourite list");
				}else{
					LogController.log("already in favourite list");
				}
				break;

			default:
				if (facebookServiceImpl != null)
				{
					facebookServiceImpl.onActivityResult(this, requestCode, resultCode, data);
				}
				break;
			}
		}
		else {
			LogController.log("Share Cancel");
		}
	}
	
	@Override
	public void logginStatus(int snsCode, boolean isSuccessLogin,
			Object errorObject) {
		LogController.log("logginStatus >> "+ snsCode + " " + isSuccessLogin);
		if (!isSuccessLogin) {
			return;
		}
		if (snsCode == FACEBOOK_CODE) {
			facebookPostAction();
		}
		else if (snsCode == TWITTER_CODE) {
			twitterPostAction();
		}
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> "+ snsCode + " " + isSuccessGetProfile);
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		LogController.log("loggoutStatus >> "+ snsCode + " " + isSuccessLogout);
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  "+ snsCode + " " + isSuccessPost);
		if (isSuccessPost) {
			String eventId = null;
			try {
				List<Event> events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(product.getObjectId());
				for (int i = 0; i < events.size(); i++) {
					Event event = events.get(i);
					if (event.getEventType().equals("share")) {
						eventId = event.getEventId();
						break;
					}
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (eventId != null) {
				EarnCreditAsyncTask earnCreditAsyncTask = new EarnCreditAsyncTask(this);
				earnCreditAsyncTask.execute(eventId);
			}
		}
	}

	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (results instanceof String) {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					("eraned credit: " + ((String)results)),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
		else {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, ((FanclGeneralResult) results).getErrMsgEn(), ((FanclGeneralResult) results).getErrMsgZh(), ((FanclGeneralResult) results).getErrMsgSc()),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
	}
}
