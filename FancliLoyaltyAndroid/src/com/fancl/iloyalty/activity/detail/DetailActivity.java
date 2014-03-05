package com.fancl.iloyalty.activity.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.beauty.BeautyRelatedArticlesActivity;
import com.fancl.iloyalty.activity.product.ProductQandAActivity;
import com.fancl.iloyalty.activity.promotion.PromotionCheckMyRecordActivity;
import com.fancl.iloyalty.activity.promotion.PromotionRelatedArticlesActivity;
import com.fancl.iloyalty.activity.purchase.PurchaseQRCodeScanActivity;
import com.fancl.iloyalty.activity.sharing.SharingPanelActivity;
import com.fancl.iloyalty.activity.sharing.TwitterSharingDialogActivity;
import com.fancl.iloyalty.adapter.DetailImagesViewPagerAdapter;
import com.fancl.iloyalty.asynctask.EarnCreditAsyncTask;
import com.fancl.iloyalty.asynctask.GetPromotionQuestionAsyncTask;
import com.fancl.iloyalty.asynctask.SubmitPromotionAnswerAsyncTask;
import com.fancl.iloyalty.asynctask.SubmitPromotionVisitAsyncTask;
import com.fancl.iloyalty.asynctask.callback.EarnCreditAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.GetPromotionQuestionAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.SubmitPromotionAnswerAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.DetailContent;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.PromotionAnswer;
import com.fancl.iloyalty.pojo.PromotionQuestion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.impl.FacebookService30Impl;
import com.gt.snssharinglibrary.service.impl.TwitterServiceImpl;
import com.viewpagerindicator.IconPageIndicator;

public class DetailActivity extends MainTabActivity implements SNSServiceCallback, EarnCreditAsyncTaskCallback,GetPromotionQuestionAsyncTaskCallback, SubmitPromotionAnswerAsyncTaskCallback {
	private int tabIndex;
	private float currentTextSize = 15;

	private LocaleService localeService;
	private SNSService facebookServiceImpl;
	private SNSService twitterServiceImpl;

	private final int FACEBOOK_CODE = 0;
	private final int TWITTER_CODE = 1;

	private DetailContent detailContent;

	private RelativeLayout contentDetailLayout;
	private TextView contentDetailTitle;
	private ImageButton contentDetailTextSizeButton;
	private LinearLayout leftFooterLayout;
	private LinearLayout rightFooterLayout;
	private TextView leftFooterTextView;
	private ImageView leftFooterImageView;
	private TextView rightFooterTextView;
	private ImageView rightFooterImageView;

	// For Multiple Images Structure
	private RelativeLayout contentDetaiBoxViewPagerLayout;
	private ViewPager contentDetailViewPager;
	private IconPageIndicator iconPageIndicator;
	private LinearLayout contentDetailBoxViewPagerLinearLayout;

	// For Non Multiple Images Structure
	private RelativeLayout contentDetailBoxImageViewLayout;
	private LinearLayout contentDetailBoxImageViewLinearLayout;

	public enum DETAIL_TEMPLATE {
		DETAIL_TEXT_ONLY,
		DETAIL_SINGLE_IMAGE_AND_TEXT,
		DETAIL_MULTIPLE_IMAGES,
		DETAIL_GROUP_CONTENT,
		DETAIL_LUCKY_DRAW,
		DETAIL_GP_RECORD,
		DETAIL_COUPON
	}

	public enum FOOTER_TYPE {
		NO_FOOTER,
		ICHANNEL_READ_MORE,
		ICHANNEL_RELATED,
		HOT_ITEM_VIDEO,
		HOT_ITEM_PRODUCT,
		HOT_ITEM_SHOP,
		HOT_ITEM_PROMOTION,
		HOT_ITEM_ICHANNEL,
		CHECK_GP_BALANCE,
		PROMOTION_READ_MORE,
		ABOUT_FANCL_READ_MORE
	}

	private DETAIL_TEMPLATE detailTemplate;
	private FOOTER_TYPE footerType;

	private boolean noMoreRelated;
	private String couponStatus;
	private String luckyDrawCode;
	private String detailId;
	private DETAIL_TEMPLATE detailType;
	private String pageTitleStr;
	private String titleStr;
	private String imagePath;
	private List<String> imageList;
	private List<String> contentList;
	private List<String> subtitleList;
	private String videoLink;
	private String videoDuration;
	private String eventId;

	private String linkRecordType;
	private String linkRecordId;
	private String linkRecordLink;

	private String bookmarkId;
	private String bookmarkType;

	private String couponSerialNumber;
	private String promotionCode;
	private String fromQRCode;
	private boolean showRedeemed;

	// For Lucky Draw
	List<PromotionQuestion> questionList;
	int q1AnsIndex = 999;
	int q2AnsIndex = 999;

	private void setDetailContent(DetailContent detailContent) {
		footerType = detailContent.getFooterType();
		noMoreRelated = detailContent.isNoMoreRelated();
		couponStatus = detailContent.getCouponStatus();
		luckyDrawCode = detailContent.getLuckyDrawCode();
		detailId = detailContent.getDetailId();
		detailType = detailContent.getDetailType();
		detailTemplate = detailContent.getDetailType();
		pageTitleStr = detailContent.getPageTitleStr();
		titleStr = detailContent.getTitleStr();
		imagePath = detailContent.getImagePath();
		imageList = detailContent.getImageList();
		contentList = detailContent.getContentList();
		subtitleList = detailContent.getSubtitleList();
		videoLink = detailContent.getVideoLink();
		videoDuration = detailContent.getVideoDuration();
		eventId = detailContent.getEventId();
		linkRecordType = detailContent.getLinkRecordType();
		linkRecordId = detailContent.getLinkRecordId();
		linkRecordLink = detailContent.getLinkRecordLink();
		bookmarkId = detailContent.getBookmarkId();
		bookmarkType = detailContent.getBookmarkType();
		couponSerialNumber = detailContent.getCouponSerialNumber();
		promotionCode = detailContent.getPromotionCode();
		fromQRCode = detailContent.getFromQRCode();
		showRedeemed = detailContent.isShowRedeemed();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = this.getIntent();
		detailContent = (DetailContent) intent.getExtras().getSerializable(Constants.DETAIL_CONTENT_KEY);
		tabIndex = intent.getIntExtra(Constants.BOTTOM_TAB_INDEX_KEY, 0);
		setDetailContent(detailContent);
		
		if (footerType != FOOTER_TYPE.ICHANNEL_READ_MORE && footerType != FOOTER_TYPE.ABOUT_FANCL_READ_MORE &&
				footerType != FOOTER_TYPE.HOT_ITEM_ICHANNEL && footerType != FOOTER_TYPE.HOT_ITEM_PRODUCT &&
				footerType != FOOTER_TYPE.HOT_ITEM_PROMOTION && footerType != FOOTER_TYPE.HOT_ITEM_SHOP) {
			String eventId = null;
			try {
				List<Event> events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(detailContent.getDetailId());
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
		
		localeService = GeneralServiceFactory.getLocaleService();
		Config.TWITTER_CONSUMER_KEY = "jvKys4Lr2afHg7WxvQ4M1g";
		Config.TWITTER_CONSUMER_SECRET = "Z9icVCBu2j1Da1O2mMEiggkBcXgmfk2OjAE7qvrV0A";
		Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.btn_cross;
		twitterServiceImpl = new TwitterServiceImpl(DetailActivity.this);
		twitterServiceImpl.setSNSServiceCallback(DetailActivity.this);
		
		Config.facebook30SharingType = SHARING_TYPE.WEB_DIALOG;
		Config.FACEBOOK_PERMISSIONS = new String[] { "email", "user_birthday", "read_friendlists", "user_likes" };
		facebookServiceImpl = new FacebookService30Impl();
		facebookServiceImpl.setSNSServiceCallback(DetailActivity.this);
		facebookServiceImpl.onCreate(DetailActivity.this, savedInstanceState);

		headerTitleTextView.setText(pageTitleStr);

		this.setupSpaceLayout();
		this.setupBoxViewContent();
		this.setupFooterBtn();


		this.setupMenuButtonListener(tabIndex, true);
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarShareBtn.setVisibility(View.VISIBLE);

		navigationBarShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DetailActivity.this, SharingPanelActivity.class);
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
				R.layout.content_detail_page, null);
		spaceLayout.addView(contentDetailLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Title
		contentDetailTitle = (TextView) contentDetailLayout.findViewById(R.id.content_detail_title);
		contentDetailTitle.setTextColor(getResources().getColor(R.color.Fancl_Blue));
		contentDetailTitle.setTypeface(null,Typeface.BOLD);
		contentDetailTitle.setText(titleStr);

		// Detail Box View Pager with Text Layout
		contentDetaiBoxViewPagerLayout = (RelativeLayout) contentDetailLayout.findViewById(R.id.content_detail_box_view_pager_layout);
		contentDetailBoxViewPagerLinearLayout = (LinearLayout) contentDetailLayout.findViewById(R.id.content_detail_view_pager_layout);

		// Detail Box Linear Layout
		contentDetailBoxImageViewLayout = (RelativeLayout) contentDetailLayout.findViewById(R.id.content_detail_box_image_view_layout);
		contentDetailBoxImageViewLinearLayout = (LinearLayout) contentDetailLayout.findViewById(R.id.content_detail_image_view_layout);

		// Thumbnail View Pager
		contentDetailViewPager = (ViewPager) contentDetailLayout.findViewById(R.id.content_detail_view_pager);

		// Text Size Button
		contentDetailTextSizeButton = (ImageButton) contentDetailLayout.findViewById(R.id.content_detail_text_size_btn);
		contentDetailTextSizeButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if (currentTextSize == 15) {
					currentTextSize = 18;
				} else {
					currentTextSize = 15;
				}

				if (detailTemplate == DETAIL_TEMPLATE.DETAIL_MULTIPLE_IMAGES) {
					contentDetailBoxViewPagerLinearLayout.removeAllViews();
					setupBoxViewContent();
				}
				else {
					contentDetailBoxImageViewLinearLayout.removeAllViews();
					setupBoxViewContent();
				}
			}
		});

		// Left Footer Layout
		leftFooterLayout = (LinearLayout) contentDetailLayout.findViewById(R.id.content_detail_left_footer_layout);
		leftFooterTextView = (TextView) contentDetailLayout.findViewById(R.id.content_detail_left_footer_text_view);
		leftFooterImageView = (ImageView) contentDetailLayout.findViewById(R.id.content_detail_left_footer_image_view);

		// Right Footer Layout
		rightFooterLayout = (LinearLayout) contentDetailLayout.findViewById(R.id.content_detail_right_footer_layout);
		rightFooterTextView = (TextView) contentDetailLayout.findViewById(R.id.content_detail_right_footer_text_view);
		rightFooterImageView = (ImageView) contentDetailLayout.findViewById(R.id.content_detail_right_footer_image_view);
	}

	private void setupFooterBtn() {
		if (footerType == FOOTER_TYPE.NO_FOOTER) {
			return;
		}

		if (footerType == FOOTER_TYPE.ICHANNEL_RELATED) {
			List<Product> relatedProductList = null;
			try {
				relatedProductList = CustomServiceFactory.getPromotionService().getIchannelRelatedProductWithIchannelId(detailId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Promotion> relatedPromotionList = null;
			try {
				relatedPromotionList = CustomServiceFactory.getPromotionService().getIchannelRelatedPromotionWithIchannelId(detailId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (noMoreRelated || ((relatedProductList.size() + relatedPromotionList.size()) == 0))
			{    
				return;
			}
		}
		else if (footerType == FOOTER_TYPE.PROMOTION_READ_MORE) {
			List<Product> relatedProductList = null;
			try {
				relatedProductList = CustomServiceFactory.getPromotionService().getPromotionRelatedProductWithPromotionId(detailId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<IchannelMagazine> relatedArticleList = null;
			try {
				relatedArticleList = CustomServiceFactory.getPromotionService().getPromotionRelatedArticleWithPromotionId(detailId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (noMoreRelated || ((relatedProductList.size() + relatedArticleList.size()) == 0))
			{    
				return;
			}
		}

		String buttonTitle = "";

		switch (footerType) {
		case ICHANNEL_READ_MORE:
		case ABOUT_FANCL_READ_MORE:
		case HOT_ITEM_ICHANNEL:
		case HOT_ITEM_PRODUCT:	
		case HOT_ITEM_PROMOTION:
		case HOT_ITEM_SHOP:	
			buttonTitle = getResources().getString(R.string.read_more);
			break;

		case ICHANNEL_RELATED:
			buttonTitle = getResources().getString(R.string.related);
			break;

		case HOT_ITEM_VIDEO:
			buttonTitle = getResources().getString(R.string.play_video);
			rightFooterImageView.setImageResource(R.drawable.btn_play);
			break;

		case CHECK_GP_BALANCE:
			buttonTitle = getResources().getString(R.string.gp_record);
			break;

		case PROMOTION_READ_MORE:
			buttonTitle = getResources().getString(R.string.related);
			break;

		default:
			break;
		}

		if (footerType == FOOTER_TYPE.HOT_ITEM_PROMOTION) {
			Promotion aPromotion = null;
			try {
				aPromotion = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(linkRecordId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if (aPromotion == null) {
				return;
			}

		}else if (footerType == FOOTER_TYPE.HOT_ITEM_ICHANNEL){
			IchannelMagazine aIchannelMagazine = null;
			try {
				aIchannelMagazine = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(linkRecordId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if (aIchannelMagazine == null) {
				return;
			}
		}

		if (footerType == FOOTER_TYPE.CHECK_GP_BALANCE) {
			leftFooterLayout.setVisibility(View.VISIBLE);
			leftFooterTextView.setText(buttonTitle);
		}
		else {
			rightFooterLayout.setVisibility(View.VISIBLE);
			rightFooterTextView.setText(buttonTitle);
		}

		if (footerType == FOOTER_TYPE.CHECK_GP_BALANCE) {
			List<Product> relatedProductList = null;
			try {
				relatedProductList = CustomServiceFactory.getPromotionService().getPromotionRelatedProductWithPromotionId(detailId);
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			if (relatedProductList == null) {
				return;
			}
			else if (relatedProductList.size() > 0) {
				rightFooterLayout.setVisibility(View.VISIBLE);
				rightFooterTextView.setText(getResources().getString(R.string.related));
			}
		}

		leftFooterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				// Go to GP Record
				
				LogController.log("leftFooterLayout footerType:"+footerType);
				Intent intentPromotion = new Intent(DetailActivity.this, PromotionCheckMyRecordActivity.class);
            	startActivity(intentPromotion);
            	
            	try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Detail Page", "GP Reward", "", detailId, titleStr, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		rightFooterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogController.log("footerType:"+footerType);
				switch (footerType) {
				case ICHANNEL_READ_MORE:
					// Go to showIchannelReadMore
					// Call earnBeautyCredit
					break;

				case ABOUT_FANCL_READ_MORE:
					// Go to showAboutFanclReadMore
					startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivityForAboutFanclWithDetailContent(detailContent, DetailActivity.this, false, 4));
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Detail Page", "aboutFanclReadMore", "", detailId, titleStr, "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Call earnBeautyCredit
					break;

				case HOT_ITEM_ICHANNEL:
					try {
						IchannelMagazine tmpObject = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(linkRecordId);
						startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(tmpObject, DetailActivity.this, true, pageTitleStr, 0));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "Beauty iChannel", "", detailId, titleStr, "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case HOT_ITEM_PRODUCT:
					try {
						Product tmpObj = CustomServiceFactory.getProductService().getProductDetailWithProductId(linkRecordId);
						//						Product tmpObj = CustomServiceFactory.getProductService().getProductDetailWithProductId("259");
						startActivity(CustomServiceFactory.getDetailContentService().getProductDetailActivity(tmpObj, DetailActivity.this, 0));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "ProductDetail", "", detailId, titleStr, "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Call earnBeautyCredit
					break;

				case HOT_ITEM_PROMOTION:
					// Go to showHotItemReadMorePromotion
					try {
						Promotion tmpObj = CustomServiceFactory.getPromotionService().getPromotionObjectWithPromotionId(linkRecordId);
						// Call earnBeautyCredit
						startActivity(CustomServiceFactory.getDetailContentService().getPromotionDetailAction(DetailActivity.this, tmpObj, true, pageTitleStr, 0, 0));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "Latest Promotion", "", detailId, titleStr, "View", "");
						
						SubmitPromotionVisitAsyncTask submitPromotionAsyncTask = new SubmitPromotionVisitAsyncTask();
						submitPromotionAsyncTask.execute(tmpObj.getCode());
					} catch (FanclException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;

				case HOT_ITEM_SHOP:	
					try {
						Shop tmpObj = CustomServiceFactory.getAboutFanclService().getShopDetailWithId(linkRecordId);
						startActivity(CustomServiceFactory.getDetailContentService().getShopDetailActivity(tmpObj, DetailActivity.this, 0));
						
						CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "StoreDetail", "", detailId, titleStr, "View", "");

					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Call earnBeautyCredit
					break;

				case ICHANNEL_RELATED:
					// Go to showIchannelRelatedContent
					Intent intent = new Intent(DetailActivity.this, BeautyRelatedArticlesActivity.class);
					intent.putExtra(Constants.ICHANNEL_ID_RELATED_KEY, detailId);
	            	startActivity(intent);
	            	
	            	try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Beauty iChannel", "RelatedProduct", "", detailId, titleStr, "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case HOT_ITEM_VIDEO:
					startActivity(CustomServiceFactory.getDetailContentService().getYoutubeVideoActivity(linkRecordLink, DetailActivity.this, pageTitleStr));
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Whats Hot", "", "", detailId, titleStr, "Video", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;

				case CHECK_GP_BALANCE:
					// Go to showPromotionRelatedContent 
					Intent intentPromotion = new Intent(DetailActivity.this, PromotionRelatedArticlesActivity.class);
					intentPromotion.putExtra(Constants.PROMOTION_ID_RELATED_KEY, detailId);
	            	startActivity(intentPromotion);
					break;

				case PROMOTION_READ_MORE:
					// Go to showPromotionRelatedContent
					Intent intentPromo = new Intent(DetailActivity.this, PromotionRelatedArticlesActivity.class);
					intentPromo.putExtra(Constants.PROMOTION_ID_RELATED_KEY, detailId);
	            	startActivity(intentPromo);
					break;

				default:
					break;
				}
			}
		});
	}

	private void setupViewPagerThumbnail() {
		if (videoLink != null) {
			if (videoLink.length() > 0) {
				contentDetailViewPager.setAdapter(new DetailImagesViewPagerAdapter(this, imageList, handler, imagePath, true));
			}
			else {
				contentDetailViewPager.setAdapter(new DetailImagesViewPagerAdapter(this, imageList, handler, imagePath, false));
			}
		}
		else {
			contentDetailViewPager.setAdapter(new DetailImagesViewPagerAdapter(this, imageList, handler, imagePath, false));
		}
		contentDetailViewPager.setCurrentItem(0);

		iconPageIndicator = (IconPageIndicator) findViewById(R.id.viewpager_icon_indicator);
		if (imageList.size() > 1) {
			iconPageIndicator.setViewPager(contentDetailViewPager);
		}
		else {
			iconPageIndicator.setVisibility(View.GONE);
		}
	}

	private void setupBoxViewContent() {
		if (footerType == FOOTER_TYPE.ABOUT_FANCL_READ_MORE) {
			contentDetailBoxImageViewLayout.setVisibility(View.VISIBLE);
			this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, 0);
			this.setupDetailBriefIntro(contentDetailBoxImageViewLinearLayout);
		}
		else {
			if (detailTemplate == DETAIL_TEMPLATE.DETAIL_MULTIPLE_IMAGES) {
				contentDetaiBoxViewPagerLayout.setVisibility(View.VISIBLE);
				this.setupViewPagerThumbnail();
				this.setupDetailSubtitle(contentDetailBoxViewPagerLinearLayout);
				this.setupDetailTextContent(contentDetailBoxViewPagerLinearLayout, 0);
			}
			else {
				contentDetailBoxImageViewLayout.setVisibility(View.VISIBLE);

				switch (detailTemplate) {
				case DETAIL_TEXT_ONLY:
					this.setupDetailTextContent(contentDetailBoxImageViewLinearLayout, 0);
					break;

				case DETAIL_SINGLE_IMAGE_AND_TEXT:
					this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, 0);
					this.setupDetailSubtitle(contentDetailBoxImageViewLinearLayout);
					this.setupDetailTextContent(contentDetailBoxImageViewLinearLayout, 0);
					break;

				case DETAIL_GROUP_CONTENT:
					for (int i = 0; i < contentList.size(); i++) {
						this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, i);
						this.setupDetailTextContent(contentDetailBoxImageViewLinearLayout, i);
					}
					break;

				case DETAIL_LUCKY_DRAW:
					this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, 0);
					this.setupDetailTextContent(contentDetailBoxImageViewLinearLayout, 0);
					GetPromotionQuestionAsyncTask getPromotionQuestionAsyncTask = new GetPromotionQuestionAsyncTask(this);
					getPromotionQuestionAsyncTask.execute(luckyDrawCode);
					if (checkLoadingDialog()) {
						loadingDialog.loading();
					}
//					this.setupDetailLuckyDrawContent(contentDetailBoxImageViewLinearLayout);
					break;

				case DETAIL_GP_RECORD:
					this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, 0);
					this.setupDetailSubtitle(contentDetailBoxImageViewLinearLayout);
					break;

				case DETAIL_COUPON:
					this.setupDetailSingleImage(contentDetailBoxImageViewLinearLayout, 0);
					this.setupDetailSubtitle(contentDetailBoxImageViewLinearLayout);
					break;

				default:
					break;
				}

			}
		}
	}

	private void setupDetailSingleImage(LinearLayout layout, int index) {
		if (imageList != null) {
			if (imageList.size() - 1 < index || contentList.size() - 1 < index) {
				return;
			}

			if (imageList.get(index).length() == 0) {
				return;
			}

			RelativeLayout imageViewLayout = new RelativeLayout(this);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Math.round(DataUtil.dip2px(this, 124)));
			layoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, 0);
			layout.addView(imageViewLayout, layoutParams);

			AsyncImageView detailAsyncImageView = new AsyncImageView(this);
			detailAsyncImageView.setId(1);
			detailAsyncImageView.setBackgroundColor(getResources().getColor(R.color.LightGrey));
			RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(Math.round(DataUtil.dip2px(this, 280)), RelativeLayout.LayoutParams.MATCH_PARENT);
			imageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			imageViewLayout.addView(detailAsyncImageView, imageViewLayoutParams);
			detailAsyncImageView.setRequestingUrl(handler, (imagePath + imageList.get(index)), Constants.IMAGE_FOLDER);

			if (videoLink != null) {
				if ((footerType != FOOTER_TYPE.ICHANNEL_READ_MORE && footerType != FOOTER_TYPE.ABOUT_FANCL_READ_MORE) && 
						videoLink.length() > 0 && index == 0) {

					ImageView playImageView = new ImageView(this);
					playImageView.setBackgroundColor(getResources().getColor(R.color.Transparent));
					playImageView.setImageDrawable(getResources().getDrawable(R.drawable.btn_play_large));
					RelativeLayout.LayoutParams playImageViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					playImageViewLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, detailAsyncImageView.getId());
					playImageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
					imageViewLayout.addView(playImageView, playImageViewLayoutParams);

					RelativeLayout playButton = new RelativeLayout(this);
					RelativeLayout.LayoutParams playButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
					imageViewLayout.addView(playButton, playButtonLayoutParams);

					playButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							try {
								IchannelMagazine tmpObj = CustomServiceFactory.getPromotionService().getIchannelInfoWithIchannelId(detailId);
								startActivity(CustomServiceFactory.getDetailContentService().getYoutubeVideoActivityWithIChannelMagazine(tmpObj, DetailActivity.this, pageTitleStr));
							} catch (FanclException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}

	private void setupDetailSubtitle(LinearLayout layout) {
		if (subtitleList != null) {
			if (subtitleList.size() == 0) {
				return;
			}

			for (int i = 0; i < subtitleList.size(); i+=2) {
				//Create relative layout including both labels
				LinearLayout subtitleLayout = new LinearLayout(this);
				subtitleLayout.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(DataUtil.dip2px(this, 280)), LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, 0);
				layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
				layout.addView(subtitleLayout, layoutParams);

				// Create label 0
				TextView subtitleTextView = new TextView(this);
				subtitleTextView.setTextColor(getResources().getColor(R.color.Fancl_Blue));
				subtitleTextView.setTextSize(currentTextSize);
				subtitleTextView.setText(subtitleList.get(i));

				LinearLayout.LayoutParams subtitleTextViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				subtitleLayout.addView(subtitleTextView, subtitleTextViewLayoutParams);

				// Create label 1
				TextView valueTextView = new TextView(this);
				valueTextView.setTextColor(getResources().getColor(R.color.Fancl_Blue));
				valueTextView.setTextSize(currentTextSize);
				valueTextView.setText(subtitleList.get(i+1));

				LinearLayout.LayoutParams valueTextViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				valueTextViewLayoutParams.setMargins(Math.round(DataUtil.dip2px(this, 10)), 0, 0, 0);
				subtitleLayout.addView(valueTextView, valueTextViewLayoutParams);
			}

			if (couponStatus.equals("iCoupon")) {
				// create a redeem button and align to right hand side
				Button redeemButton = new Button(this);
				redeemButton.setText(R.string.promotion_detail_redeem_btn_title);
				redeemButton.setTextColor(Color.WHITE);
				redeemButton.setGravity(Gravity.CENTER);
				redeemButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_redeem));
				
				redeemButton.setOnClickListener(new OnClickListener(){  
		            public void onClick(View v) {  
		            	Intent intent = new Intent(DetailActivity.this, PurchaseQRCodeScanActivity.class);
						startActivity(intent);
		  
		            } 
				});
		          

				LinearLayout.LayoutParams redeemLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				redeemLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 5)), Math.round(DataUtil.dip2px(this, 10)), 0);
				redeemLayoutParams.gravity = Gravity.RIGHT;
				layout.addView(redeemButton, redeemLayoutParams);
			}
		}
	}

	private void setupDetailBriefIntro(LinearLayout layout) {
		if (contentList != null) {
			if (contentList.size() == 0) {
				return;
			}
			final TextView detailTextView = new TextView(this);
			detailTextView.setTextColor(getResources().getColor(R.color.Fancl_Grey));
			detailTextView.setText(contentList.get(0));
			detailTextView.setTextSize(currentTextSize);

			ViewTreeObserver vto = detailTextView.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					ViewTreeObserver obs = detailTextView.getViewTreeObserver();
					obs.removeGlobalOnLayoutListener(this);
					if(detailTextView.getLineCount() > 10)
					{
						int lineEndIndex = detailTextView.getLayout().getLineEnd(9);
						String text = detailTextView.getText().subSequence(0, lineEndIndex-3) +"...";
						detailTextView.setText(text);
					}
				}
			});

			LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(Math.round(DataUtil.dip2px(this, 280)), Math.round(DataUtil.dip2px(this, 190)));
			textViewLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, 0);
			textViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

			layout.addView(detailTextView, textViewLayoutParams);
		}
	}

	private void setupDetailTextContent(LinearLayout layout, int index) {
		if (contentList != null) {
			if (contentList.size() == 0) {
				return;
			}
			TextView detailTextView = new TextView(this);
			detailTextView.setTextColor(getResources().getColor(R.color.Fancl_Grey));
			detailTextView.setText(contentList.get(index));
			detailTextView.setTextSize(currentTextSize);
			LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(Math.round(DataUtil.dip2px(this, 280)), LayoutParams.WRAP_CONTENT);
			textViewLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, Math.round(DataUtil.dip2px(this, 10)));
			textViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

			layout.addView(detailTextView, textViewLayoutParams);
		}
	}

	private void setupDetailLuckyDrawContent(LinearLayout layout) {
		for (int i = 0; i < questionList.size(); i++) {
			this.addQuestion(layout, localeService.textByLangaugeChooser(this, questionList.get(i).getQuestionEn(), questionList.get(i).getQuestionZh(), questionList.get(i).getQuestionSc()));

			RadioGroup answersGroup = new RadioGroup(this);
			answersGroup.setOrientation(RadioGroup.VERTICAL);
			List<String> answerList = new ArrayList<String>();
			//			for (int j = 0; j < answerList.size(); j++) {
			
			List<PromotionAnswer> promotionAnswerList = questionList.get(i).getPromotionAnswerList();
			for (int j = 0; j < promotionAnswerList.size(); j++) {
				int answerIndex = i*10+j+1;
				this.addAnswer(answersGroup, localeService.textByLangaugeChooser(this, promotionAnswerList.get(j).getAnswerEn(), promotionAnswerList.get(j).getAnswerZh(), promotionAnswerList.get(j).getAnswerSc()), answerIndex);
			}

			LinearLayout.LayoutParams answersGroupLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			answersGroupLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 5)), 0, 0);
			layout.addView(answersGroup, answersGroupLayoutParams);
		}

		// Create a confirm button to submit the user selection
		final Button confirmButton = new Button(this);
		confirmButton.setText(R.string.confirm_btn_title);
		confirmButton.setTextColor(getResources().getColor(R.color.White));
		confirmButton.setGravity(Gravity.CENTER);
		confirmButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_confirm));

		LinearLayout.LayoutParams confirmLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		confirmLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, Math.round(DataUtil.dip2px(this, 10)));
		confirmLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		layout.addView(confirmButton, confirmLayoutParams);

		confirmButton.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				LogController.log("q1AnsIndex: " + q1AnsIndex + " q2AnsIndex: " + q2AnsIndex);

				if ((q1AnsIndex == 999 || q2AnsIndex == 999) && questionList.size() == 2) {
					// Show Please Answer All Question alert
					AlertDialog alertDialog = new AlertDialog.Builder(
							DetailActivity.this).create();
					alertDialog.setMessage(getResources().getString(R.string.promotion_detail_answer_all_questions));
					alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
					return;
				}else if (q1AnsIndex == 999 && questionList.size() == 1){
					// Show Please Answer All Question alert
					AlertDialog alertDialog = new AlertDialog.Builder(
							DetailActivity.this).create();
					alertDialog.setMessage(getResources().getString(R.string.promotion_detail_answer_all_questions));
					alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed

						}
					});

					alertDialog.show();
					return;
				}

				//call api
				List<PromotionAnswer> promotionAnswer1List = questionList.get(0).getPromotionAnswerList();
				String q1AnsKey = promotionAnswer1List.get(q1AnsIndex).getKey();
				String q2AnsKey; 
				if (questionList.size() == 2) {
					List<PromotionAnswer> promotionAnswer2List = questionList.get(1).getPromotionAnswerList();
					q2AnsKey = promotionAnswer2List.get(q2AnsIndex).getKey();
				}else{
					q2AnsKey = "";
				}

				SubmitPromotionAnswerAsyncTask submitPromotionAnswerAsyncTask = new SubmitPromotionAnswerAsyncTask(DetailActivity.this);
				submitPromotionAnswerAsyncTask.execute(promotionCode, q1AnsKey, q2AnsKey);
				if (checkLoadingDialog()) {
					loadingDialog.loading();
				}
				
			}
		});
	}

	private void addQuestion(LinearLayout layout, String questionStr) {
		TextView questionTextView = new TextView(this);
		questionTextView.setTextColor(getResources().getColor(R.color.Black));
		questionTextView.setTextSize(currentTextSize);
		questionTextView.setText(questionStr);
		LinearLayout.LayoutParams questionTextViewLayoutParams = new LinearLayout.LayoutParams(Math.round(DataUtil.dip2px(this, 280)), LayoutParams.WRAP_CONTENT);
		questionTextViewLayoutParams.setMargins(0, Math.round(DataUtil.dip2px(this, 10)), 0, 0);
		questionTextViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

		layout.addView(questionTextView, questionTextViewLayoutParams);
	}

	private void addAnswer(RadioGroup group, String answerStr, int answerId) {
		LogController.log("create answer button");
		RadioButton answerButton = new RadioButton(this);
		answerButton.setId(answerId);
		answerButton.setTextColor(getResources().getColor(R.color.Black));
		answerButton.setTextSize(currentTextSize);
		answerButton.setText(answerStr);
		answerButton.setButtonDrawable(R.drawable.radiobutton);
		group.addView(answerButton);

		answerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RadioButton tmpButton = (RadioButton) v;
				int questionIndex = tmpButton.getId()/10;

				if (questionIndex == 0)
					q1AnsIndex = tmpButton.getId()%10-1;
				else
					q2AnsIndex = tmpButton.getId()%10-1;
			}
		});
	}
	
	private void facebookPostAction() {
		SNSShareDetail snsShareDetail = null;
		String image = null;
		if (detailContent.getImageList() != null) {
			if (detailContent.getImageList().size() > 0) {
				image = detailContent.getImageList().get(0);
			}
		}

		if (Config.facebook30SharingType.equals(Config.SHARING_TYPE.WEB_DIALOG))
		{
			String title = detailContent.getTitleStr();
			String caption = null;
			String description = detailContent.getContentList().get(0);
			String link = "http://www.fancl-hk.com/";
			String pictureLink = imagePath + image;
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

		facebookServiceImpl.post(DetailActivity.this, snsShareDetail);
	}
	
	private void twitterPostAction() {
		Intent intent = new Intent(DetailActivity.this, TwitterSharingDialogActivity.class);
		intent.putExtra(Constants.SHARING_FORMAT_KEY, "nonproduct");
		intent.putExtra(Constants.SHARING_CONTENT_KEY, detailContent);
		startActivity(intent);
	}
	
	private void sendEmailAction() {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_SUBJECT, detailContent.getTitleStr());
		email.putExtra(Intent.EXTRA_TEXT, detailContent.getContentList().get(0));
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if (resultCode == DetailActivity.RESULT_OK) {
			int shareKey = data.getIntExtra(Constants.SHARING_PANEL_RETURN_KEY, -1);
			switch (shareKey) {
			case Constants.SHARING_PANEL_RETURN_FB: {
				if (facebookServiceImpl.isLogged(DetailActivity.this)) {
					facebookPostAction();
				}
				else {
					facebookServiceImpl.login(DetailActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
				}
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Share to facebook", "", detailId, titleStr
							, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;

			case Constants.SHARING_PANEL_RETURN_TW: {
				if (twitterServiceImpl.isLogged(DetailActivity.this)) {
					twitterPostAction();
				}
				else {
					twitterServiceImpl.login(DetailActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
				}
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Share to twitter", "", detailId, titleStr, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;

			case Constants.SHARING_PANEL_RETURN_EMAIL:
				sendEmailAction();
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Email to friends", "", detailId, titleStr, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case Constants.SHARING_PANEL_RETURN_FAVOURITE: {
				//add to favourite
				if(!bookmarkType.equals("") || !bookmarkId.equals("")){
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
							if(qrIdSplitArray[i].equals(bookmarkId) && qrTypeSplitArray[i].equals(bookmarkType)){
								isInFavouriteList = true;
							}
						}
					}

					if(!isInFavouriteList){
						CustomServiceFactory.getAccountService().saveFavouriteList(bookmarkType, bookmarkId, "myFavourite");
						LogController.log("add to favourite list");
					}else{
						LogController.log("already in favourite list");
					}
				}
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Share", "Add to bookmark", "", detailId, titleStr, "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				List<Event> events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(detailContent.getDetailId());
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

	@Override
	public void onPostPromotionQuestionExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		if(results == null)
			return;

		LogController.log("results:"+results);
		
		questionList = (List<PromotionQuestion>) results;
		
		this.setupDetailLuckyDrawContent(contentDetailBoxImageViewLinearLayout);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		if(results == null)
			return;
		
		FanclGeneralResult fanclGeneralResult = results;
		
		if(fanclGeneralResult.getStatus() == 0){
			AlertDialog alertDialog = new AlertDialog.Builder(
					DetailActivity.this).create();
			alertDialog.setMessage(getResources().getString(R.string.alert_submit_success));
			alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					Intent intent = new Intent();
					setResult(RESULT_OK,intent );  
					finish();
				}
			});

			alertDialog.show();
			
		}else{
			AlertDialog alertDialog = new AlertDialog.Builder(
					DetailActivity.this).create();
			alertDialog.setMessage(localeService.textByLangaugeChooser(this, fanclGeneralResult.getErrMsgEn(), fanclGeneralResult.getErrMsgZh(), fanclGeneralResult.getErrMsgSc()));
			alertDialog.setButton(getResources().getString(R.string.ok_btn_title), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed

				}
			});

			alertDialog.show();
			
		}
		
		
	}
}
