package com.fancl.iloyalty.factory;

import com.fancl.iloyalty.service.AboutFanclService;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.service.DetailContentService;
import com.fancl.iloyalty.service.GCMService;
import com.fancl.iloyalty.service.ILoyaltyTCPSocketService;
import com.fancl.iloyalty.service.ProductService;
import com.fancl.iloyalty.service.PromotionService;
import com.fancl.iloyalty.service.PurchaseService;
import com.fancl.iloyalty.service.SNSService;
import com.fancl.iloyalty.service.SettingService;
import com.fancl.iloyalty.service.ShareService;
import com.fancl.iloyalty.service.impl.AboutFanclServiceImpl;
import com.fancl.iloyalty.service.impl.AccountServiceImpl;
import com.fancl.iloyalty.service.impl.DetailContentServiceImpl;
import com.fancl.iloyalty.service.impl.FacebookServiceImpl;
import com.fancl.iloyalty.service.impl.GCMServiceImpl;
import com.fancl.iloyalty.service.impl.ILoyaltyTCPSocketServiceImpl;
import com.fancl.iloyalty.service.impl.ProductServiceImpl;
import com.fancl.iloyalty.service.impl.PromotionServiceImpl;
import com.fancl.iloyalty.service.impl.PurchaseServiceImpl;
import com.fancl.iloyalty.service.impl.SettingServiceImpl;
import com.fancl.iloyalty.service.impl.ShareServiceImpl;
import com.fancl.iloyalty.service.impl.TwitterServiceImpl;

public class CustomServiceFactory {
	/**
	 * Project requested Application Service Object
	 * Custom Service object for this project only
	 */
	private static AccountService accountService;
	private static PromotionService promotionService;
	private static ProductService productService;
	private static PurchaseService purchaseService;
	private static SNSService facebookService;
	private static SNSService twitterService;
	private static ShareService shareService;
	private static AboutFanclService aboutFanclService;
	private static SettingService settingService;
	private static ILoyaltyTCPSocketService iLoyaltyTCPSocketService;
	private static DetailContentService detailContentService;
	private static GCMService gcmService;
	
	public static AccountService getAccountService()
	{
		if(accountService == null)
		{
			accountService = new AccountServiceImpl();
		}
		
		return accountService;
	}
	
	public static PromotionService getPromotionService()
	{
		if(promotionService == null)
		{
			promotionService = new PromotionServiceImpl();
		}
		
		return promotionService;
	}
	
	public static ProductService getProductService()
	{
		if(productService == null)
		{
			productService = new ProductServiceImpl();
		}
		
		return productService;
	}
	
	public static PurchaseService getPurchaseService()
	{
		if(purchaseService == null)
		{
			purchaseService = new PurchaseServiceImpl();
		}
		
		return purchaseService;
	}
	
	public static SNSService getFacebookService()
	{
		if(facebookService == null)
		{
			facebookService = new FacebookServiceImpl();
		}
		
		return facebookService;
	}
	
	public static SNSService getTwitterService()
	{
		if(twitterService == null)
		{
			twitterService = new TwitterServiceImpl();
		}
		
		return twitterService;
	}
	
	public static ShareService getShareService()
	{
		if(shareService == null)
		{
			shareService = new ShareServiceImpl();
		}
		
		return shareService;
	}
	
	public static AboutFanclService getAboutFanclService()
	{
		if(aboutFanclService == null)
		{
			aboutFanclService = new AboutFanclServiceImpl();
		}
		
		return aboutFanclService;
	}
	
	public static SettingService getSettingService()
	{
		if(settingService == null)
		{
			settingService = new SettingServiceImpl();
		}
		
		return settingService;
	}
	
	public static ILoyaltyTCPSocketService getILoyaltyTCPSocketService()
	{
		if(iLoyaltyTCPSocketService == null)
		{
			iLoyaltyTCPSocketService = new ILoyaltyTCPSocketServiceImpl();
		}
		
		return iLoyaltyTCPSocketService;
	}
	
	public static DetailContentService getDetailContentService()
	{
		if (detailContentService == null)
		{
			detailContentService = new DetailContentServiceImpl();
		}
		
		return detailContentService;
	}
	
	public static GCMService getGCMService() {
		if(gcmService == null) {
			gcmService = new GCMServiceImpl();
		}
		
		return gcmService;
	}
}
