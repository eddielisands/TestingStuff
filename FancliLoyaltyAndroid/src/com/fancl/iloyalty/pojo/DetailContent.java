package com.fancl.iloyalty.pojo;

import java.io.Serializable;
import java.util.List;

import com.fancl.iloyalty.activity.detail.DetailActivity.DETAIL_TEMPLATE;
import com.fancl.iloyalty.activity.detail.DetailActivity.FOOTER_TYPE;

public class DetailContent implements Serializable {
	private static final long serialVersionUID = 460096002830880618L;

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
	
	private String promotionDetailType;
	
	public DetailContent(FOOTER_TYPE footerType, boolean noMoreRelated, String couponStatus,
			String luckyDrawCode, String detailId, DETAIL_TEMPLATE detailType,
			String pageTitleStr, String titleStr, String imagePath,
			List<String> imageList, List<String> contentList,
			List<String> subtitleList, String videoLink, String videoDuration,
			String eventId, String linkRecordType, String linkRecordId,
			String linkRecordLink, String bookmarkId, String bookmarkType,
			String couponSerialNumber, String promotionCode, String fromQRCode,
			boolean showRedeemed, String promotionDetailType) {
		super();
		this.footerType = footerType;
		this.noMoreRelated = noMoreRelated;
		this.couponStatus = couponStatus;
		this.luckyDrawCode = luckyDrawCode;
		this.detailId = detailId;
		this.detailType = detailType;
		this.pageTitleStr = pageTitleStr;
		this.titleStr = titleStr;
		this.imagePath = imagePath;
		this.imageList = imageList;
		this.contentList = contentList;
		this.subtitleList = subtitleList;
		this.videoLink = videoLink;
		this.videoDuration = videoDuration;
		this.eventId = eventId;
		this.linkRecordType = linkRecordType;
		this.linkRecordId = linkRecordId;
		this.linkRecordLink = linkRecordLink;
		this.bookmarkId = bookmarkId;
		this.bookmarkType = bookmarkType;
		this.couponSerialNumber = couponSerialNumber;
		this.promotionCode = promotionCode;
		this.fromQRCode = fromQRCode;
		this.showRedeemed = showRedeemed;
		this.promotionDetailType = promotionDetailType;
	}

	public FOOTER_TYPE getFooterType() {
		return footerType;
	}

	public void setFooterType(FOOTER_TYPE footerType) {
		this.footerType = footerType;
	}

	public boolean isNoMoreRelated() {
		return noMoreRelated;
	}

	public void setNoMoreRelated(boolean noMoreRelated) {
		this.noMoreRelated = noMoreRelated;
	}

	public String getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}

	public String getLuckyDrawCode() {
		return luckyDrawCode;
	}

	public void setLuckyDrawCode(String luckyDrawCode) {
		this.luckyDrawCode = luckyDrawCode;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public DETAIL_TEMPLATE getDetailType() {
		return detailType;
	}

	public void setDetailType(DETAIL_TEMPLATE detailType) {
		this.detailType = detailType;
	}

	public String getPageTitleStr() {
		return pageTitleStr;
	}

	public void setPageTitleStr(String pageTitleStr) {
		this.pageTitleStr = pageTitleStr;
	}

	public String getTitleStr() {
		return titleStr;
	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public List<String> getContentList() {
		return contentList;
	}

	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}

	public List<String> getSubtitleList() {
		return subtitleList;
	}

	public void setSubtitleList(List<String> subtitleList) {
		this.subtitleList = subtitleList;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getLinkRecordType() {
		return linkRecordType;
	}

	public void setLinkRecordType(String linkRecordType) {
		this.linkRecordType = linkRecordType;
	}

	public String getLinkRecordId() {
		return linkRecordId;
	}

	public void setLinkRecordId(String linkRecordId) {
		this.linkRecordId = linkRecordId;
	}

	public String getLinkRecordLink() {
		return linkRecordLink;
	}

	public void setLinkRecordLink(String linkRecordLink) {
		this.linkRecordLink = linkRecordLink;
	}

	public String getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(String bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	public String getBookmarkType() {
		return bookmarkType;
	}

	public void setBookmarkType(String bookmarkType) {
		this.bookmarkType = bookmarkType;
	}

	public String getCouponSerialNumber() {
		return couponSerialNumber;
	}

	public void setCouponSerialNumber(String couponSerialNumber) {
		this.couponSerialNumber = couponSerialNumber;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getFromQRCode() {
		return fromQRCode;
	}

	public void setFromQRCode(String fromQRCode) {
		this.fromQRCode = fromQRCode;
	}

	public boolean isShowRedeemed() {
		return showRedeemed;
	}

	public void setShowRedeemed(boolean showRedeemed) {
		this.showRedeemed = showRedeemed;
	}

	public String getPromotionDetailType() {
		return promotionDetailType;
	}

	public void setPromotionDetailType(String promotionDetailType) {
		this.promotionDetailType = promotionDetailType;
	}
	
}
