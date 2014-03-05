package com.fancl.iloyalty.service;

import android.content.Context;
import android.content.Intent;

import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.DetailContent;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;

public interface DetailContentService {
	public Intent getDetailContentActivity(Object tmpObj, Context context, boolean showFooter, String pageTitle, int bottomTabIndex);
	public Intent getYoutubeVideoActivity(String link, Context context, String pageTitle);
	public Intent getYoutubeVideoActivityWithIChannelMagazine(IchannelMagazine tmpObj, Context context, String pageTitle);
	public Intent getProductDetailActivity(Product product, Context context, int bottomTabIndex);
	public Intent getShopDetailActivity(Shop shop, Context context, int bottomTabIndex);
	public Intent getDetailContentActivityForAboutFancl(AboutFancl tmpObj, Context context, String type, boolean showFooter, int bottomTabIndex);
	public Intent getDetailContentActivityForAboutFanclWithDetailContent(DetailContent tmpObj, Context context, boolean showFooter, int bottomTabIndex);
	public Intent getPromotionDetailAction(Context context, Promotion tmpObj, boolean showFooter, String pageTitle, int bottomTabIndex, int promotionIndex);
}
