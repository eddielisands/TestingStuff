package com.fancl.iloyalty.service;

import java.util.HashMap;
import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.IchannelType;
import com.fancl.iloyalty.pojo.MagazineImage;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.PromotionQuestion;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;

public interface PromotionService {
	public List<HotItem> getHighlightBannerList() throws FanclException;
	
	public List<HotItem> getHighlightListWithType(String type , boolean isHighLight) throws FanclException;
	
	public HotItem getHotItemFromHotItemId(String hotItemId) throws FanclException;
	
	public List<Promotion> getPromotionListWithType(String type) throws FanclException;
	
	public FanclGeneralResult submitPromotionVisitWithCode(String promotionId) throws FanclException;
	
	public Object getPromotionQuestionWithPromotionId(String promotionId) throws FanclException;
	
	public FanclGeneralResult submitPromotionAnswerWithPromotionId(String promotionId, String answer1Key, String answer2Key) throws FanclException;
	
	public List<Promotion> getLatestPromotionWithType(String type) throws FanclException;
	
	public List<Product> getPromotionRelatedProductWithPromotionId(String promotionId) throws FanclException;
	
	public List<IchannelMagazine> getPromotionRelatedArticleWithPromotionId(String promotionId) throws FanclException;
	
	public Promotion getPromotionObjectWithPromotionId(String promotionId) throws FanclException;
	
	public List<IchannelType> getIchannelSubcateListWithMainCate(String mainCate) throws FanclException;
	
	public List<IchannelMagazine> getIchannelListWithMainCate(String mainCate, String subCate) throws FanclException;
	
	public List<MagazineImage> getMagazineImageWithMagazineType(String type) throws FanclException;
	
	public IchannelMagazine getIchannelInfoWithIchannelId(String channelId) throws FanclException;
	
	public List<IchannelMagazine> getIchannelDescriptionWithIchannelId(String channelId) throws FanclException;
	
	public List<Product> getIchannelRelatedProductWithIchannelId(String channelId) throws FanclException;
	
	public List<Promotion> getIchannelRelatedPromotionWithIchannelId(String channelId) throws FanclException;
	
	public List<IchannelMagazine> getIchannelSearchResultWithKeyword(String keyword) throws FanclException;
	
	public List<Event> getEventItemListWithItemId(String itemId) throws FanclException;

	public HashMap<String, List<HotItem>> getWhatsHotHashMap();
	
	public void setWhatsHotHashMap(HashMap<String, List<HotItem>> whatsHotHashMap);
	
	public FanclGeneralResult redeemICouponWithCode(String coupoonCode, String couponNo) throws FanclException;
	
}
