package com.fancl.iloyalty.service;

import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.AdBanner;
import com.fancl.iloyalty.pojo.ContactUs;
import com.fancl.iloyalty.pojo.QRCode;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.pojo.ShopRegion;

public interface AboutFanclService {
	public List<ShopRegion> getShopParentRegionList() throws FanclException;
	
	public List<ShopRegion> getShopRegionListWithParentId(String parentId) throws FanclException;
	
	public List<ShopRegion> getShopRegionListWithId(String parentId) throws FanclException;
	
	public List<Shop> getFullShopList() throws FanclException;
	
	public List<Shop> getShopListForFancl(boolean isFancl, String regionId) throws FanclException;
	
	public Shop getShopDetailWithId(String shopId) throws FanclException;
	
	public Shop getShopDetailWithCode(String shopCode) throws FanclException;
	
	public AboutFancl getFanclBackground(String aboutId) throws FanclException;
	
	public List<AboutFancl> getFanclBackgroundDescriptionWithType(String fanclType) throws FanclException;
	
	public AboutFancl getFanclBackground() throws FanclException;
	
	public List<AboutFancl> getFanclBackgroundDescription() throws FanclException;
	
	public AboutFancl getLessIsMore() throws FanclException;
	
	public List<AboutFancl> getLessIsMoreDescription() throws FanclException;
	
	public AboutFancl getHowToUse() throws FanclException;
	
	public List<AboutFancl> getHowToUseDescription() throws FanclException;
	
	public ContactUs getContactUs() throws FanclException;
	
	public List<QRCode> getQRCodeObjects() throws FanclException;
	
	public List<AdBanner> getFrontAdObjects() throws FanclException;
	
	public String getUnreadNumberWithType() throws FanclException;
	
	public String getUnreadChannel() throws FanclException;
	
	public String getUnreadPromotion() throws FanclException;
	
	public String getPromotionCount() throws FanclException;
}
