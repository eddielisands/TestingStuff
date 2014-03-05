package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.ProductAnswer;
import com.fancl.iloyalty.pojo.ProductCategory;
import com.fancl.iloyalty.pojo.ProductChoice;
import com.fancl.iloyalty.pojo.ProductQuestion;
import com.fancl.iloyalty.pojo.ProductSeries;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Setting;
import com.fancl.iloyalty.service.ProductService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class ProductServiceImpl implements ProductService{

	@Override
	public List<ProductSeries> getProductSeriesList() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductSeriesList");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_series ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductSeries> productSeriesList = new ArrayList<ProductSeries>();
				ProductSeries productSeries = null;

				String objectId;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					productSeries = new ProductSeries(objectId, titleZh, titleSc, titleEn);
					productSeriesList.add(productSeries);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productSeriesList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}
	
	@Override
	public List<ProductCategory> getProductPartentCategoryListWithSeriesId(
			String id) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductPartentCategoryListWithSeriesId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_category WHERE parent_id = -1 AND series_id = '" + id + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
				ProductCategory productCategory = null;

				String objectId;
				String seriesId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					productCategory = new ProductCategory(objectId, seriesId, parentId, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					productCategoryList.add(productCategory);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productCategoryList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductCategory> getProductSubCategoryListWithParentId(
			String id) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductSubCategoryListWithParentId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_category WHERE parent_id = '" + id + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
				ProductCategory productCategory = null;

				String objectId;
				String seriesId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					productCategory = new ProductCategory(objectId, seriesId, parentId, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					productCategoryList.add(productCategory);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productCategoryList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Product> getProductListWithCategoryId(String categoryId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductListWithCategoryId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM product_category_mapping WHERE category_id = '" + categoryId + "');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> productList = new ArrayList<Product>();
				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					productList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public Product getProductDetailWithProductId(String productId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductDetailWithProductId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id = '" + productId + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					c.moveToNext();
				}

				c.close();
				c = null;
				
				return product;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductChoice> getProductChoiceWithProductId(String id)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductChoiceWithProductId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_choice WHERE product_id = '" + id + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductChoice> productChoiceList = new ArrayList<ProductChoice>();
				ProductChoice productChoice = null;

				String objectId;
				String productId;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					productId = c.getString(c.getColumnIndex("product_id"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					productChoice = new ProductChoice(objectId, productId, image, titleZh, titleSc, titleEn);
					productChoiceList.add(productChoice);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productChoiceList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public ProductCategory getProductCategoryWithProductId(String productId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductCategoryWithProductId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_category WHERE id = (SELECT category_id FROM product_category_mapping WHERE product_id = '" + productId + "');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				ProductCategory productCategory = null;

				String objectId;
				String seriesId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					productCategory = new ProductCategory(objectId, seriesId, parentId, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productCategory;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<IchannelMagazine> getRelatedArticleWithProductId(
			String productId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getRelatedArticleWithProductId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM ichannel_magazine WHERE id IN (SELECT ichannel_magazine_id FROM ichannel_product_mapping WHERE product_id = '" + productId + "') AND publish_status = 'publish' AND publish_start_datetime <= datetime('now','localtime') AND publish_end_datetime >= datetime('now','localtime');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<IchannelMagazine> relatedIchannelList = new ArrayList<IchannelMagazine>();
				IchannelMagazine ichannelMagazine = null;

				String objectId;
				String templateType;
				String thumbnail;
				//				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				//				String descriptionZh;
				//				String descriptionSc;
				//				String descriptionEn;
				String videoThumbnailZh;
				String videoThumbnailSc;
				String videoThumbnailEn;
				String videoLinkZh;
				String videoLinkSc;
				String videoLinkEn;
				String videoDuration;
				String type;
				String publishStatus;
				String isNew;
				String publishStartDatetime;
				String publishEndDatetime;
				String createDatetime;
				String isRead;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					templateType = c.getString(c.getColumnIndex("template_type"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					//					image = c.getString(c.getColumnIndex("image"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					//					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					//					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					//					descriptionEn = c.getString(c.getColumnIndex("description_en"));
					videoThumbnailZh = c.getString(c.getColumnIndex("video_thumbnail_zh"));
					videoThumbnailSc = c.getString(c.getColumnIndex("video_thumbnail_sc"));
					videoThumbnailEn = c.getString(c.getColumnIndex("video_thumbnail_en"));
					videoLinkZh = c.getString(c.getColumnIndex("video_link_zh"));
					videoLinkSc = c.getString(c.getColumnIndex("video_link_sc"));
					videoLinkEn = c.getString(c.getColumnIndex("video_link_en"));
					videoDuration = c.getString(c.getColumnIndex("video_duration"));
					type = c.getString(c.getColumnIndex("type"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					publishStartDatetime = c.getString(c.getColumnIndex("publish_start_datetime"));
					publishEndDatetime = c.getString(c.getColumnIndex("publish_end_datetime"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));
					isRead = c.getString(c.getColumnIndex("is_read"));

					ichannelMagazine = new IchannelMagazine(objectId, templateType, thumbnail, titleZh, titleSc, titleEn, videoThumbnailZh, videoThumbnailSc, videoThumbnailEn, videoLinkZh, videoLinkSc, videoLinkEn, videoDuration, type, publishStatus, isNew, publishStartDatetime, publishEndDatetime, createDatetime, isRead);
					relatedIchannelList.add(ichannelMagazine);
					c.moveToNext();
				}

				c.close();
				c = null;

				return relatedIchannelList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Promotion> getRelatedPromotionWithProductId(String productId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getRelatedPromotionWithProductId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM promotion WHERE id IN (SELECT promotion_id FROM promotion_product_mapping WHERE product_id = '" + productId + "') AND is_lucky_draw != 'Y' AND publish_status = 'publish' AND promotion_start_datetime <= datetime('now','localtime') AND promotion_end_datetime >= datetime('now','localtime');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Promotion> relatedPromotionList = new ArrayList<Promotion>();
				Promotion promotion = null;

				String objectId;
				String code;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String shortDescriptionZh;
				String shortDescriptionSc;
				String shortDescriptionEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;
				String promotionStartDatetime;
				String promotionEndDatetime;
				String publishStatus;
				String promotionType;
				String isLuckyDraw;
				String luckyDrawType;
				String isNew;
				String isPublic;
				String gp;
				String createDatetime;
				String isRead;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					shortDescriptionZh = c.getString(c.getColumnIndex("short_description_zh"));
					shortDescriptionSc = c.getString(c.getColumnIndex("short_description_sc"));
					shortDescriptionEn = c.getString(c.getColumnIndex("short_description_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));
					promotionStartDatetime = c.getString(c.getColumnIndex("promotion_start_datetime"));
					promotionEndDatetime = c.getString(c.getColumnIndex("promotion_end_datetime"));
					//					couponTypeId = c.getString(c.getColumnIndex("coupon_type_id"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					promotionType = c.getString(c.getColumnIndex("promotion_type"));
					isLuckyDraw = c.getString(c.getColumnIndex("is_lucky_draw"));
					luckyDrawType = c.getString(c.getColumnIndex("lucky_draw_type"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isPublic = c.getString(c.getColumnIndex("is_public"));
					gp = c.getString(c.getColumnIndex("gp"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					
					promotion = new Promotion(objectId, code, thumbnail, image, titleZh, titleSc, titleEn, shortDescriptionZh, shortDescriptionSc, shortDescriptionEn, descriptionZh, descriptionSc, descriptionEn, promotionStartDatetime, promotionEndDatetime, publishStatus, promotionType, isLuckyDraw, luckyDrawType, isNew, isPublic, gp, createDatetime, null, null, isRead);
					relatedPromotionList.add(promotion);
					c.moveToNext();
				}

				c.close();
				c = null;

				return relatedPromotionList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Product> getProductSearchResultWithKeyword(String keyword)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductSearchResultWithKeyword");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "select * from product where title_zh like '%%" + keyword + "%%' or title_sc like '%%" + keyword
						+ "%%' or title_en like '%%" + keyword + "%%' or benefit_zh like '%%" + keyword + "%%' or benefit_sc like '%%" + keyword
						+ "%%' or benefit_en like '%%" + keyword + "%%';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> productList = new ArrayList<Product>();
				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					productList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public Setting getSesaonalDescription() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getSesaonalDescription");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM setting WHERE type = 'season';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				Setting setting = null;

				String objectId;
				String type;
				String titleZh;
				String titleSc;
				String titleEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					setting = new Setting(objectId, type, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return setting;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductCategory> getSeasonalProductCategory()
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getSeasonalProductCategory");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_season_category ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
				ProductCategory productCategory = null;

				String objectId;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					productCategory = new ProductCategory(objectId, null, null, titleZh, titleSc, titleEn, null, null, null);
					productCategoryList.add(productCategory);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productCategoryList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Product> getSeasonalProductWithCategoryId(String categoryId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getProductSearchResultWithKeyword");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM product_season_mapping WHERE category_id = '" + categoryId + "');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> productList = new ArrayList<Product>();
				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					productList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductQuestion> getQnaProductQuestion() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getQnaProductQuestion");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_question;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductQuestion> productQuestionList = new ArrayList<ProductQuestion>();
				ProductQuestion productQuestion = null;

				String objectId;
				String questionZh;
				String questionSc;
				String questionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					questionZh = c.getString(c.getColumnIndex("question_zh"));
					questionSc = c.getString(c.getColumnIndex("question_sc"));
					questionEn = c.getString(c.getColumnIndex("question_en"));

					productQuestion = new ProductQuestion(objectId, questionZh, questionSc, questionEn);
					productQuestionList.add(productQuestion);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productQuestionList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductAnswer> getQnaAnswerWithQuestionId(String id)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getQnaAnswerWithQuestionId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_answer WHERE question_id = '" + id + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductAnswer> productAnswerList = new ArrayList<ProductAnswer>();
				ProductAnswer productAnswer = null;

				String objectId;
				String questionId;
				String answerCode;
				String answerZh;
				String answerSc;
				String answerEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					questionId = c.getString(c.getColumnIndex("question_id"));
					answerCode = c.getString(c.getColumnIndex("code"));
					answerZh = c.getString(c.getColumnIndex("answer_zh"));
					answerSc = c.getString(c.getColumnIndex("answer_sc"));
					answerEn = c.getString(c.getColumnIndex("answer_en"));

					productAnswer = new ProductAnswer(objectId, questionId, answerCode, answerZh, answerSc, answerEn);
					productAnswerList.add(productAnswer);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productAnswerList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public void saveUserQnaAnwserId(List<String> userAnswerId, List<String> userAnswerCode) {
		// TODO Auto-generated method stub
		String answerIdStr = TextUtils.join(",", userAnswerId);
		String answerCodeStr = TextUtils.join(",", userAnswerCode);
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(Constants.QNA_ANSWER_ID, answerIdStr);
		editor.putString(Constants.QNA_ANSWER_CODE, answerCodeStr);
		editor.commit();
	}

	@Override
	public List<ProductAnswer> getUserQnaAnswer() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUserQnaAnswer");
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String qnaAnswerId = sharedPreferences.getString(Constants.QNA_ANSWER_ID,
				null);
		if (qnaAnswerId == null) {
			return null;
		}
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_answer WHERE id IN (" + qnaAnswerId + ") ORDER BY question_id;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductAnswer> productAnswerList = new ArrayList<ProductAnswer>();
				ProductAnswer productAnswer = null;

				String objectId;
				String questionId;
				String answerCode;
				String answerZh;
				String answerSc;
				String answerEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					questionId = c.getString(c.getColumnIndex("question_id"));
					answerCode = c.getString(c.getColumnIndex("code"));
					answerZh = c.getString(c.getColumnIndex("answer_zh"));
					answerSc = c.getString(c.getColumnIndex("answer_sc"));
					answerEn = c.getString(c.getColumnIndex("answer_en"));

					productAnswer = new ProductAnswer(objectId, questionId, answerCode, answerZh, answerSc, answerEn);
					productAnswerList.add(productAnswer);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productAnswerList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Product> getUserQnaSuggestProduct() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUserQnaSuggestProduct");
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String answerCodeStr = sharedPreferences.getString(Constants.QNA_ANSWER_CODE,
				null);
		if (answerCodeStr == null) {
			return null;
		}
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM product_question_mapping WHERE answer_code = '" + answerCodeStr + "');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> productList = new ArrayList<Product>();
				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					productList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<ProductCategory> getUserQnaSuggestProductCategory() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUserQnaSuggestProductCategory");
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String answerCodeStr = sharedPreferences.getString(Constants.QNA_ANSWER_CODE,
				null);
		if (answerCodeStr == null) {
			return null;
		}
		int numAnswer = answerCodeStr.split(", ").length;
		
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product_category WHERE id IN (SELECT category_id FROM product p, " +
						"product_category_mapping pcm WHERE pcm.product_id = p.id AND p.id IN (SELECT product_id " +
						"FROM product_question_mapping WHERE answer_code IN (" + answerCodeStr + ") GROUP BY " +
						"product_id HAVING COUNT(*) = " + numAnswer + "));";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
				ProductCategory productCategory = null;

				String objectId;
				String seriesId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					productCategory = new ProductCategory(objectId, seriesId, parentId, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					productCategoryList.add(productCategory);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productCategoryList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public List<Product> getUserQnaSuggestProductWithCategoryId(String categoryId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUserQnaSuggestProductWithCategoryId");
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String answerCodeStr = sharedPreferences.getString(Constants.QNA_ANSWER_CODE,
				null);
		if (answerCodeStr == null) {
			return null;
		}
		int numAnswer = answerCodeStr.split(", ").length;
		
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM product_category_mapping WHERE category_id = '" + categoryId 
						+ "') AND id IN (SELECT product_id FROM product_question_mapping WHERE answer_code IN (" + answerCodeStr + 
						") GROUP BY product_id HAVING COUNT(*) = " + numAnswer + ");";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> productList = new ArrayList<Product>();
				Product product = null;

				String objectId;
				String seriesId;
				String thumbnail;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String benefitZh;
				String benefitSc;
				String benefitEn;
				String sizeZh;
				String sizeSc;
				String sizeEn;
				String ingredientZh;
				String ingredientSc;
				String ingredientEn;
				String howToUseZh;
				String howToUseSc;
				String howToUseEn;
				String volumnZh;
				String volumnSc;
				String volumnEn;
				String publishStatus;
				String isNew;
				String isSeason;
				String isRead;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					seriesId = c.getString(c.getColumnIndex("series_id"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					benefitZh = c.getString(c.getColumnIndex("benefit_zh"));
					benefitSc = c.getString(c.getColumnIndex("benefit_sc"));
					benefitEn = c.getString(c.getColumnIndex("benefit_en"));
					sizeZh = c.getString(c.getColumnIndex("size_zh"));
					sizeSc = c.getString(c.getColumnIndex("size_sc"));
					sizeEn = c.getString(c.getColumnIndex("size_en"));
					ingredientZh = c.getString(c.getColumnIndex("ingredient_zh"));
					ingredientSc = c.getString(c.getColumnIndex("ingredient_sc"));
					ingredientEn = c.getString(c.getColumnIndex("ingredient_en"));
					howToUseZh = c.getString(c.getColumnIndex("how_to_use_zh"));
					howToUseSc = c.getString(c.getColumnIndex("how_to_use_sc"));
					howToUseEn = c.getString(c.getColumnIndex("how_to_use_en"));
					volumnZh = c.getString(c.getColumnIndex("volumn_zh"));
					volumnSc = c.getString(c.getColumnIndex("volumn_sc"));
					volumnEn = c.getString(c.getColumnIndex("volumn_en"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					isSeason = c.getString(c.getColumnIndex("is_season"));
					isRead = c.getString(c.getColumnIndex("is_read"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					product = new Product(objectId, seriesId, thumbnail, image, titleZh, titleSc, titleEn, benefitZh, benefitSc, benefitEn, sizeZh, sizeSc, sizeEn, ingredientZh, ingredientSc, ingredientEn, howToUseZh, howToUseSc, howToUseEn, volumnZh, volumnSc, volumnEn, publishStatus, isNew, isSeason, isRead, createDatetime);
					productList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return productList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

}
