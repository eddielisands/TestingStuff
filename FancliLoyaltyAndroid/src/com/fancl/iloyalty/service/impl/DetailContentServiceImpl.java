package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.detail.DetailActivity;
import com.fancl.iloyalty.activity.detail.DetailActivity.DETAIL_TEMPLATE;
import com.fancl.iloyalty.activity.detail.DetailActivity.FOOTER_TYPE;
import com.fancl.iloyalty.activity.detail.YoutubeVideoActivity;
import com.fancl.iloyalty.activity.product.ProductDetailActivity;
import com.fancl.iloyalty.activity.shop.ShopDetailActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.DetailContent;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.DetailContentService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.StringUtil;

public class DetailContentServiceImpl implements DetailContentService {

	private LocaleService localeService = GeneralServiceFactory
			.getLocaleService();

	@Override
	public Intent getDetailContentActivity(Object tmpObj, Context context,
			boolean showFooter, String pageTitle, int bottomTabIndex) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (tmpObj instanceof HotItem) {
			LogController.log("is HotItem");
		    try {
				CustomServiceFactory.getSettingService().updateReadContentToDatabase("hot_item", ((HotItem) tmpObj).getObjectId());
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FOOTER_TYPE footerType = FOOTER_TYPE.NO_FOOTER;

			DETAIL_TEMPLATE detailType = DETAIL_TEMPLATE.DETAIL_MULTIPLE_IMAGES;

			String pageTitleStr;
			if (pageTitle == null) {
				pageTitleStr = context.getResources().getString(
						R.string.whats_hot_btn);
			}
			else {
				pageTitleStr = pageTitle;
			}

			String titleStr = localeService.textByLangaugeChooser(context,
					((HotItem) tmpObj).getTitleEn(),
					((HotItem) tmpObj).getTitleZh(),
					((HotItem) tmpObj).getTitleSc());

			String imagePath = ApiConstant
					.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH);

			String content = localeService.textByLangaugeChooser(context,
					((HotItem) tmpObj).getDescriptionEn(),
					((HotItem) tmpObj).getDescriptionZh(),
					((HotItem) tmpObj).getDescriptionSc());
			List<String> contentList = new ArrayList<String>();
			contentList.add(content);

			List<String> tmpList = new ArrayList<String>();
			tmpList.add(((HotItem) tmpObj).getImage1());
			tmpList.add(((HotItem) tmpObj).getImage2());
			tmpList.add(((HotItem) tmpObj).getImage3());
			tmpList.add(((HotItem) tmpObj).getImage4());
			tmpList.add(((HotItem) tmpObj).getImage5());
			List<String> imageList = checkImageList(tmpList);

			String linkRecordLink = "";
			String linkRecordId = "";
			String linkRecordType = "";
			if (((HotItem) tmpObj).getLinkType() != null) {
				if (((HotItem) tmpObj).getLinkType().length() > 0) {
					linkRecordId = ((HotItem) tmpObj).getLinkRecordId();
					linkRecordType = ((HotItem) tmpObj).getLinkRecordType();
					if (((HotItem) tmpObj).getLinkType().equals("video")) {
						linkRecordLink = localeService.textByLangaugeChooser(
								context, ((HotItem) tmpObj).getLinkEn(),
								((HotItem) tmpObj).getLinkZh(),
								((HotItem) tmpObj).getLinkSc());
						footerType = FOOTER_TYPE.HOT_ITEM_VIDEO;
					} else if (((HotItem) tmpObj).getLinkType().equals("link")) {
						linkRecordLink = localeService.textByLangaugeChooser(
								context, ((HotItem) tmpObj).getLinkEn(),
								((HotItem) tmpObj).getLinkZh(),
								((HotItem) tmpObj).getLinkSc());
						footerType = FOOTER_TYPE.HOT_ITEM_VIDEO;
					}
				}
			} else if (((HotItem) tmpObj).getLinkRecordType() != null) {
				if (((HotItem) tmpObj).getLinkRecordType().length() > 0) {
					linkRecordId = ((HotItem) tmpObj).getLinkRecordId();
					linkRecordType = ((HotItem) tmpObj).getLinkRecordType();

					if (((HotItem) tmpObj).getLinkRecordType().equals("shop")) {
						footerType = FOOTER_TYPE.HOT_ITEM_SHOP;
					} else if (((HotItem) tmpObj).getLinkRecordType().equals(
							"product")) {
						footerType = FOOTER_TYPE.HOT_ITEM_PRODUCT;
					} else if (((HotItem) tmpObj).getLinkRecordType().equals(
							"promotion")) {
						footerType = FOOTER_TYPE.HOT_ITEM_PROMOTION;
					} else if (((HotItem) tmpObj).getLinkRecordType().equals(
							"ichannel")) {
						footerType = FOOTER_TYPE.HOT_ITEM_ICHANNEL;
					}
				}
			}
			String couponStatus = "";
			String couponSerialNumber = "";
			boolean noMoreRelated = !showFooter;
			String luckyDrawCode = "";
			String detailId = ((HotItem) tmpObj).getObjectId();
			List<String> subtitleList = new ArrayList<String>();
			String videoLink = "";
			String videoDuration = "";
			String eventId = "";
			String bookmarkId = ((HotItem) tmpObj).getObjectId();
			String bookmarkType = "hot";
			String promotionCode = "";
			String fromQRCode = "";
			boolean showRedeemed = false;
			String promotionDetailType = "";

			DetailContent detailContent = new DetailContent(footerType, noMoreRelated, couponStatus, 
					luckyDrawCode, detailId, detailType, pageTitleStr, titleStr, 
					imagePath, imageList, contentList, subtitleList, videoLink, 
					videoDuration, eventId, linkRecordType, linkRecordId, linkRecordLink, 
					bookmarkId, bookmarkType, couponSerialNumber, promotionCode, fromQRCode, showRedeemed, promotionDetailType);

			intent = new Intent(context, DetailActivity.class);
			intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
		} else if (tmpObj instanceof Promotion) {
			LogController.log("is Promotion");
			return getPromotionDetailAction(context, (Promotion) tmpObj, showFooter, pageTitle, bottomTabIndex, 0);

		} else if (tmpObj instanceof IchannelMagazine) {
			LogController.log("is IchannelMagazine");
			try {
				CustomServiceFactory.getSettingService().updateReadContentToDatabase("ichannel_magazine", ((IchannelMagazine) tmpObj).getObjectId());
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (((IchannelMagazine) tmpObj).getType().equals("video")) {
				String pageTitleStr;
				if (pageTitle == null) {
					pageTitleStr = context.getResources().getString(
							R.string.beauty_ichannel_btn);
				}
				else {
					pageTitleStr = pageTitle;
				}
				intent = new Intent(context, YoutubeVideoActivity.class);
				intent.putExtra(Constants.BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY, (IchannelMagazine) tmpObj);
				intent.putExtra(Constants.PAGE_TITLE_KEY, pageTitleStr);
				intent.putExtra(Constants.YOUTUBE_LINK_ONLY_KEY, false);
			}
			else {
				int templateType = 0;
				if (((IchannelMagazine) tmpObj).getTemplateType() != null) {
					templateType = Integer.valueOf(((IchannelMagazine) tmpObj)
							.getTemplateType());
				}
				DETAIL_TEMPLATE detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
				switch (templateType) {
				case 1:
					detailType = DETAIL_TEMPLATE.DETAIL_TEXT_ONLY;
					break;

				case 2:
					detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
					break;

				case 3:
					detailType = DETAIL_TEMPLATE.DETAIL_MULTIPLE_IMAGES;
					break;

				case 4:
					detailType = DETAIL_TEMPLATE.DETAIL_GROUP_CONTENT;
					break;

				case 5:
					detailType = DETAIL_TEMPLATE.DETAIL_LUCKY_DRAW;
					break;

				case 6:
					detailType = DETAIL_TEMPLATE.DETAIL_LUCKY_DRAW;
					break;

				case 7:
					detailType = DETAIL_TEMPLATE.DETAIL_COUPON;
					break;

				default:
					break;
				}

				FOOTER_TYPE footerType = FOOTER_TYPE.ICHANNEL_RELATED;

				String pageTitleStr;
				if (pageTitle == null) {
					pageTitleStr = context.getResources().getString(
							R.string.beauty_ichannel_btn);
				}
				else {
					pageTitleStr = pageTitle;
				}

				String titleStr = localeService.textByLangaugeChooser(context,
						((IchannelMagazine) tmpObj).getTitleEn(),
						((IchannelMagazine) tmpObj).getTitleZh(),
						((IchannelMagazine) tmpObj).getTitleSc());

				String imagePath = ApiConstant
						.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH);

				List<IchannelMagazine> detailDescList = null;
				try {
					detailDescList = CustomServiceFactory.getPromotionService()
							.getIchannelDescriptionWithIchannelId(
									((IchannelMagazine) tmpObj).getObjectId());
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> contentList = new ArrayList<String>();
				List<String> tmpImageList = new ArrayList<String>();
				if (detailDescList != null) {
					for (int i = 0; i < detailDescList.size(); i++) {
						IchannelMagazine tmpMagazine = detailDescList.get(i);
						String content = localeService
								.textByLangaugeChooser(context,
										((IchannelMagazine) tmpMagazine)
										.getDescriptionEn(),
										((IchannelMagazine) tmpMagazine)
										.getDescriptionZh(),
										((IchannelMagazine) tmpMagazine)
										.getDescriptionSc());
						tmpImageList.add(((IchannelMagazine) tmpMagazine)
								.getImage());
						contentList.add(content);
					}
				}
				List<String> imageList = checkImageList(tmpImageList);

				String linkRecordLink = "";
				String linkRecordId = "";
				String linkRecordType = "";
				String couponStatus = "";
				String couponSerialNumber = "";
				boolean noMoreRelated = !showFooter;
				String luckyDrawCode = "";
				String detailId = ((IchannelMagazine) tmpObj).getObjectId();
				List<String> subtitleList = new ArrayList<String>();
				String videoLink = localeService.textByLangaugeChooser(context,
						((IchannelMagazine) tmpObj).getVideoLinkEn(),
						((IchannelMagazine) tmpObj).getVideoLinkZh(),
						((IchannelMagazine) tmpObj).getVideoLinkSc());
				String videoDuration = ((IchannelMagazine) tmpObj)
						.getVideoDuration();
				String eventId = "";
				String bookmarkId = ((IchannelMagazine) tmpObj).getObjectId();
				String bookmarkType = "ichannel";
				String promotionCode = "";
				String fromQRCode = "";
				boolean showRedeemed = false;
				String promotionDetailType = "";

				DetailContent detailContent = new DetailContent(footerType, noMoreRelated, couponStatus, 
						luckyDrawCode, detailId, detailType, pageTitleStr, titleStr, 
						imagePath, imageList, contentList, subtitleList, videoLink, 
						videoDuration, eventId, linkRecordType, linkRecordId, linkRecordLink, 
						bookmarkId, bookmarkType, couponSerialNumber, promotionCode, fromQRCode, showRedeemed, promotionDetailType);

				intent = new Intent(context, DetailActivity.class);
				intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
			}
		} else if (tmpObj instanceof Product) {
			LogController.log("is Product");
			intent = new Intent(context, ProductDetailActivity.class);
			intent.putExtra(Constants.PRODUCT_ITEM_KEY, (Product) tmpObj);
		} else if (tmpObj instanceof Shop) {
			LogController.log("is Shop");
			intent = new Intent(context, ShopDetailActivity.class);
			intent.putExtra(Constants.SELECTED_SHOP_ITEM_KEY, (Boolean) tmpObj);
		} else if(tmpObj instanceof GPRewardHistoryItem){
			
			DETAIL_TEMPLATE detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
			
			String linkRecordLink = "";
			String linkRecordId = "";
			String linkRecordType = "";
			String couponStatus = "";
			String couponSerialNumber = "";
			boolean noMoreRelated = !showFooter;
			String luckyDrawCode = "";
			String detailId = "";
			List<String> subtitleList = new ArrayList<String>();
			String videoLink = "";
			String videoDuration = "";
			String eventId = "";
			String bookmarkId = "";
			String bookmarkType = "";
			String promotionCode = "";
			String fromQRCode = "";
			String promotionDetailType = "";
			boolean showRedeemed = false;
			FOOTER_TYPE footerType = FOOTER_TYPE.NO_FOOTER;
			String pageTitleStr = pageTitle;
			
			String titleStr = localeService.textByLangaugeChooser(context,
					((GPRewardHistoryItem) tmpObj).getNameEn(),
					((GPRewardHistoryItem) tmpObj).getNameZh(),
					((GPRewardHistoryItem) tmpObj).getNameSc());
			
			String imagePath = ApiConstant
					.getAPI(ApiConstant.GIFT_IMAGE_PATH);
			
			String giftCodeTitle = "Gift Code: ";
			String giftCode = "";
			String giftQuantity = "Quantity: ";
			String quantity = String.valueOf(((GPRewardHistoryItem) tmpObj).getItemQuantity());
			String gpRedeem = "GP Redeemed: ";
			String redeemPoint = String.valueOf(((GPRewardHistoryItem) tmpObj).getGpAmount());
			subtitleList.add(giftCodeTitle);
			subtitleList.add(giftCode);
			subtitleList.add(giftQuantity);
			subtitleList.add(quantity);
			subtitleList.add(gpRedeem);
			subtitleList.add(redeemPoint);
			
			List<String> tmpImageList = new ArrayList<String>();
			tmpImageList.add(((GPRewardHistoryItem) tmpObj).getImage());
			List<String> imageList = checkImageList(tmpImageList);
			
			List<String> contentList = new ArrayList<String>();
			String content = localeService.textByLangaugeChooser(context, ((GPRewardHistoryItem) tmpObj).getDescriptionEn(), ((GPRewardHistoryItem) tmpObj).getDescriptionZh(), ((GPRewardHistoryItem) tmpObj).getDescriptionSc());
			contentList.add(content);
			
			DetailContent detailContent = new DetailContent(footerType, noMoreRelated, couponStatus, 
					luckyDrawCode, detailId, detailType, pageTitleStr, titleStr, 
					imagePath, imageList, contentList, subtitleList, videoLink, 
					videoDuration, eventId, linkRecordType, linkRecordId, linkRecordLink, 
					bookmarkId, bookmarkType, couponSerialNumber, promotionCode, fromQRCode, showRedeemed, promotionDetailType);

			intent = new Intent(context, DetailActivity.class);
			intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
			
		}
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	@Override
	public Intent getYoutubeVideoActivity(String link, Context context,
			String pageTitle) {
		String pageTitleStr;
		if (pageTitle == null) {
			pageTitleStr = context.getResources().getString(
					R.string.beauty_ichannel_btn);
		}
		else {
			pageTitleStr = pageTitle;
		}
		Intent intent = null;
		intent = new Intent(context, YoutubeVideoActivity.class);
		intent.putExtra(Constants.YOUTUBE_LINK_KEY, link);
		intent.putExtra(Constants.PAGE_TITLE_KEY, pageTitleStr);
		intent.putExtra(Constants.YOUTUBE_LINK_ONLY_KEY, true);
		return intent;
	}

	@Override
	public Intent getYoutubeVideoActivityWithIChannelMagazine(IchannelMagazine tmpObj, Context context,
			String pageTitle) {
		String pageTitleStr;
		if (pageTitle == null) {
			pageTitleStr = context.getResources().getString(
					R.string.beauty_ichannel_btn);
		}
		else {
			pageTitleStr = pageTitle;
		}
		Intent intent = null;
		intent = new Intent(context, YoutubeVideoActivity.class);
		intent.putExtra(Constants.BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY, (IchannelMagazine) tmpObj);
		intent.putExtra(Constants.PAGE_TITLE_KEY, pageTitleStr);
		intent.putExtra(Constants.YOUTUBE_LINK_ONLY_KEY, false);
		return intent;
	}

	@Override
	public Intent getProductDetailActivity(Product product, Context context, int bottomTabIndex) {
		Intent intent = null;
		intent = new Intent(context, ProductDetailActivity.class);
		intent.putExtra(Constants.PRODUCT_ITEM_KEY, (Product) product);
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	@Override
	public Intent getShopDetailActivity(Shop shop, Context context, int bottomTabIndex) {
		Intent intent = null;
		intent = new Intent(context, ShopDetailActivity.class);
		intent.putExtra(Constants.SELECTED_SHOP_ITEM_KEY, shop);
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	@Override
	public Intent getDetailContentActivityForAboutFancl(AboutFancl tmpObj, Context context, String type, boolean showFooter, int bottomTabIndex) {
		Intent intent = null;
		intent = new Intent(context, DetailActivity.class);
		LogController.log("is AboutFancl");
		FOOTER_TYPE footerType = FOOTER_TYPE.ABOUT_FANCL_READ_MORE;

		DETAIL_TEMPLATE detailType = DETAIL_TEMPLATE.DETAIL_GROUP_CONTENT;

		String pageTitleStr;
		pageTitleStr = context.getResources().getString(R.string.menu_about_fancl_btn_title);

		String titleStr = localeService.textByLangaugeChooser(context,
				((AboutFancl) tmpObj).getTitleEn(),
				((AboutFancl) tmpObj).getTitleZh(),
				((AboutFancl) tmpObj).getTitleSc());

		String imagePath = ApiConstant
				.getAPI(ApiConstant.ABOUT_FANCL_IMAGE_PATH);

		List<String> tmpImageList = new ArrayList<String>();
		List<String> contentList = new ArrayList<String>();
		try {
			List<AboutFancl> aboutFanclList = CustomServiceFactory.getAboutFanclService().getFanclBackgroundDescriptionWithType(type);
			for (int i = 0; i < aboutFanclList.size(); i++) {
				AboutFancl tmpAboutFancl = aboutFanclList.get(i);
				if (tmpAboutFancl.getImage() != null) {
					tmpImageList.add(tmpAboutFancl.getImage());
				}
				String content = localeService.textByLangaugeChooser(context,
						tmpAboutFancl.getDescriptionEn(),
						tmpAboutFancl.getDescriptionZh(),
						tmpAboutFancl.getDescriptionSc());
				contentList.add(content);
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> imageList = checkImageList(tmpImageList);

		String linkRecordLink = "";
		String linkRecordId = "";
		String linkRecordType = "";
		String couponStatus = "";
		String couponSerialNumber = "";
		boolean noMoreRelated = !showFooter;
		String luckyDrawCode = "";
		String detailId = ((AboutFancl) tmpObj).getObjectId();
		List<String> subtitleList = new ArrayList<String>();
		String videoLink = "";
		String videoDuration = "";
		String eventId = "";
		String bookmarkId = ((AboutFancl) tmpObj).getObjectId();
		String bookmarkType = "about";
		String promotionCode = "";
		String fromQRCode = "";
		boolean showRedeemed = false;
		String promotionDetailType = "";

		DetailContent detailContent = new DetailContent(footerType, noMoreRelated, couponStatus, 
				luckyDrawCode, detailId, detailType, pageTitleStr, titleStr, 
				imagePath, imageList, contentList, subtitleList, videoLink, 
				videoDuration, eventId, linkRecordType, linkRecordId, linkRecordLink, 
				bookmarkId, bookmarkType, couponSerialNumber, promotionCode, fromQRCode, showRedeemed, promotionDetailType);

		intent = new Intent(context, DetailActivity.class);
		intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	public Intent getDetailContentActivityForAboutFanclWithDetailContent(DetailContent tmpObj, Context context, boolean showFooter, int bottomTabIndex) {
		Intent intent = null;
		DetailContent detailContent = tmpObj;
		if (!showFooter) {
			detailContent.setFooterType(FOOTER_TYPE.NO_FOOTER);
		}
		intent = new Intent(context, DetailActivity.class);
		intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	private List<String> checkImageList(List<String> list) {
		List<String> tmpList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			String imageStr = list.get(i);
			if (!StringUtil.isStringEmpty(list.get(i))) {
				tmpList.add(imageStr);
			}
		}
		return tmpList;
	}

	public Intent getPromotionDetailAction(Context context, Promotion tmpObj, boolean showFooter, String pageTitle, int bottomTabIndex, int promotionIndex) {
		try {
			CustomServiceFactory.getSettingService().updateReadContentToDatabase("promotion", tmpObj.getObjectId());
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		Intent intent = null;
		DETAIL_TEMPLATE detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
		FOOTER_TYPE footerType = FOOTER_TYPE.PROMOTION_READ_MORE;

		String pageTitleStr;
		if (pageTitle == null) {
			pageTitleStr = context.getResources().getString(
					R.string.promotion_btn);
		}
		else {
			pageTitleStr = pageTitle;
		}

		String titleStr = localeService.textByLangaugeChooser(context,
				((Promotion) tmpObj).getTitleEn(),
				((Promotion) tmpObj).getTitleZh(),
				((Promotion) tmpObj).getTitleSc());

		String imagePath = ApiConstant
				.getAPI(ApiConstant.PROMOTION_IMAGE_PATH);

		List<String> contentList = new ArrayList<String>();
		String content = localeService.textByLangaugeChooser(context, ((Promotion) tmpObj).getDescriptionEn(), ((Promotion) tmpObj).getDescriptionZh(), ((Promotion) tmpObj).getDescriptionSc());
		contentList.add(content);
		List<String> tmpImageList = new ArrayList<String>();
		tmpImageList.add(((Promotion) tmpObj).getImage());
		List<String> imageList = checkImageList(tmpImageList);

		String linkRecordLink = "";
		String linkRecordId = "";
		String linkRecordType = "";
		String couponStatus = "";
		String couponSerialNumber = "";
		boolean noMoreRelated = !showFooter;
		String luckyDrawCode = "";
		String detailId = ((Promotion) tmpObj).getObjectId();
		List<String> subtitleList = new ArrayList<String>();
		String videoLink = "";
		String videoDuration = "";
		String eventId = "";
		String bookmarkId = ((Promotion) tmpObj).getObjectId();
		String bookmarkType = "promotion";
		String promotionCode = "";
		String fromQRCode = "";
		String promotionDetailType = "";

		boolean showRedeemed = false;

		switch (promotionIndex) {
		case 1:

			break;

		case 2: {
			promotionDetailType = "promotion";
			if (tmpObj.getPromotionType() != null) {
				couponStatus = tmpObj.getPromotionType();
			}
			if (tmpObj.getIsLuckyDraw() != null) {
				if (tmpObj.getIsLuckyDraw().equals("Y")) {
					luckyDrawCode = tmpObj.getCode();
					detailType = DETAIL_TEMPLATE.DETAIL_LUCKY_DRAW;
					promotionCode = tmpObj.getCode();
					if (tmpObj.getIsParticipated() != null) {
						if (tmpObj.getIsParticipated().equals("Y")) {
							return null;
						}
					}
				}
			}
			else {
				if (tmpObj.getIsParticipated() != null) {
					if (tmpObj.getIsParticipated().equals("Y")) {
						showRedeemed = true;
					}
				}
				detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
			}
			pageTitleStr = context.getString(R.string.promotion_icoupon_tab_bar_title);
			if (tmpObj.getCouponSerialNumber() != null) {
				couponSerialNumber = tmpObj.getCouponSerialNumber();
				promotionCode = tmpObj.getCode();

			}
			String validDate = "";
			if (tmpObj.getPromotionEndDatetime() != null) {
				validDate = tmpObj.getPromotionEndDatetime();
			}
			String couponNo = tmpObj.getCouponSerialNumber();
			if (couponNo != null) {
				if (couponNo.length() > 0) {
					subtitleList.add(context.getString(R.string.promotion_detail_coupon_no));
					subtitleList.add(couponNo);
					subtitleList.add(context.getString(R.string.promotion_detail_valid));
					subtitleList.add(validDate);
				}
				else {
					subtitleList.add(context.getString(R.string.promotion_detail_valid));
					if (tmpObj.getPromotionEndDatetime() != null) {
						validDate = tmpObj.getPromotionEndDatetime();
					}
					subtitleList.add(validDate);
				}
			}
			else {
				subtitleList.add(context.getString(R.string.promotion_detail_valid));
				if (tmpObj.getPromotionEndDatetime() != null) {
					validDate = tmpObj.getPromotionEndDatetime();
				}
				subtitleList.add(validDate);
			}
		}
		break;

		case 3: {
			promotionDetailType = "promotion";
			if (tmpObj.getPromotionType() != null) {
				couponStatus = tmpObj.getPromotionType();
			}
			if (tmpObj.getIsLuckyDraw() != null) {
				if (tmpObj.getIsLuckyDraw().equals("Y")) {
					luckyDrawCode = tmpObj.getCode();
					detailType = DETAIL_TEMPLATE.DETAIL_LUCKY_DRAW;
					promotionCode = tmpObj.getCode();
					if (tmpObj.getIsParticipated() != null) {
						if (tmpObj.getIsParticipated().equals("Y")) {
							return null;
						}
					}
				}
			}
			else {
				if (tmpObj.getIsParticipated() != null) {
					if (tmpObj.getIsParticipated().equals("Y")) {
						showRedeemed = true;
					}
				}
				detailType = DETAIL_TEMPLATE.DETAIL_SINGLE_IMAGE_AND_TEXT;
			}
			pageTitleStr = context.getString(R.string.promotion_vip_promotion_tab_bar_title);
			String validDate = "";
			if (tmpObj.getPromotionEndDatetime() != null) {
				validDate = tmpObj.getPromotionEndDatetime();
			}
			String couponNo = tmpObj.getCouponSerialNumber();
			if (couponNo != null) {
				if (couponNo.length() > 0) {
					subtitleList.add(context.getString(R.string.promotion_detail_coupon_no));
					subtitleList.add(couponNo);
					subtitleList.add(context.getString(R.string.promotion_detail_valid));
					subtitleList.add(validDate);
				}
				else {
					subtitleList.add(context.getString(R.string.promotion_detail_valid));
					if (tmpObj.getPromotionEndDatetime() != null) {
						validDate = tmpObj.getPromotionEndDatetime();
					}
					subtitleList.add(validDate);
				}
			}
			else {
				subtitleList.add(context.getString(R.string.promotion_detail_valid));
				if (tmpObj.getPromotionEndDatetime() != null) {
					validDate = tmpObj.getPromotionEndDatetime();
				}
				subtitleList.add(validDate);
			}
		}
		break;

		case 4: {
			promotionDetailType = "point gift";
			String validDate = "";
			if (tmpObj.getPromotionEndDatetime() != null) {
				validDate = tmpObj.getPromotionEndDatetime();
			}
			subtitleList.add(context.getString(R.string.promotion_detail_valid));
			if (tmpObj.getPromotionEndDatetime() != null) {
				validDate = tmpObj.getPromotionEndDatetime();
			}
			subtitleList.add(validDate);
			footerType = FOOTER_TYPE.CHECK_GP_BALANCE;
		}
		break;

		case 5: {
			promotionDetailType = "product promotion";
			String pointNeeded = context.getString(R.string.promotion_detail_gift_point_required);
			String point = tmpObj.getGp();
			String giftName = context.getString(R.string.promotion_detail_gift_name);
			String gift = localeService.textByLangaugeChooser(context, tmpObj.getTitleEn(), tmpObj.getTitleZh(), tmpObj.getTitleSc());
			subtitleList.add(pointNeeded);
			subtitleList.add(point);
			subtitleList.add(giftName);
			subtitleList.add(gift);
			
		}

		break;

		default:
			break;
		}
		

		DetailContent detailContent = new DetailContent(footerType, noMoreRelated, couponStatus, 
				luckyDrawCode, detailId, detailType, pageTitleStr, titleStr, 
				imagePath, imageList, contentList, subtitleList, videoLink, 
				videoDuration, eventId, linkRecordType, linkRecordId, linkRecordLink, 
				bookmarkId, bookmarkType, couponSerialNumber, promotionCode, fromQRCode, showRedeemed, promotionDetailType);

		intent = new Intent(context, DetailActivity.class);
		intent.putExtra(Constants.DETAIL_CONTENT_KEY, detailContent);
		intent.putExtra(Constants.BOTTOM_TAB_INDEX_KEY, bottomTabIndex);
		return intent;
	}

	
}
