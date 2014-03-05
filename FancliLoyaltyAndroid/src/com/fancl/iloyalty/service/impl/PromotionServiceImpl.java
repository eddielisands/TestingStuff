package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.parser.FanclResultParser;
import com.fancl.iloyalty.parser.PromotionParser;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.IchannelType;
import com.fancl.iloyalty.pojo.MagazineImage;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.service.PromotionService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.PList;

public class PromotionServiceImpl implements PromotionService {

	//	public List<HotItem> whatsHotList = new ArrayList<HotItem>();
	public HashMap<String, List<HotItem>> whatsHotHashMap = new HashMap<String, List<HotItem>>();

	//	public List<IchannelMagazine> ichannelList = new ArrayList<IchannelMagazine>();

	@Override
	public List<HotItem> getHighlightBannerList() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getHighlightBannerList");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM hot_item WHERE is_highlight = 'Y' ORDER BY sequence;";
				c = dB.rawQuery(sql, null);

				List<HotItem> bannerList = new ArrayList<HotItem>();
				HotItem hotItem = null;

				String objectId = "";
				String type = "";
				String thumbnail = "";
				String highlight_image = "";
				String title_zh = "";
				String title_sc = "";
				String title_en = "";
				String description_zh = "";
				String description_sc = "";
				String description_en = "";
				String image1 = "";
				String image2 = "";
				String image3 = "";
				String image4 = "";
				String image5 = "";
				String link_record_type = "";
				String link_record_id = "";
				String publish_start_datetime = "";
				String publish_end_datetime = "";
				String link_type = "";
				String link_zh = "";
				String link_sc = "";
				String link_en = "";
				String publish_status = "";
				String is_highlight = "";
				String sequence = "";
				String is_read = "";
				String create_datetime = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					highlight_image = DataUtil.convertImageName(c.getString(c.getColumnIndex("highlight_image")));
					title_zh = c.getString(c.getColumnIndex("title_zh"));
					title_sc = c.getString(c.getColumnIndex("title_sc"));
					title_en = c.getString(c.getColumnIndex("title_en"));
					description_zh = c.getString(c.getColumnIndex("description_zh"));
					description_sc = c.getString(c.getColumnIndex("description_sc"));
					description_en = c.getString(c.getColumnIndex("description_en"));
					image1 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image1")));
					image2 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image2")));
					image3 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image3")));
					image4 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image4")));
					image5 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image5")));
					link_record_type = c.getString(c.getColumnIndex("link_record_type"));
					link_record_id = c.getString(c.getColumnIndex("link_record_id"));
					publish_start_datetime = c.getString(c.getColumnIndex("publish_start_datetime"));
					publish_end_datetime = c.getString(c.getColumnIndex("publish_end_datetime"));
					link_type = c.getString(c.getColumnIndex("link_type"));
					link_zh = c.getString(c.getColumnIndex("link_zh"));
					link_sc = c.getString(c.getColumnIndex("link_sc"));
					link_en = c.getString(c.getColumnIndex("link_en"));
					publish_status = c.getString(c.getColumnIndex("publish_status"));
					is_highlight = c.getString(c.getColumnIndex("is_highlight"));
					sequence = c.getString(c.getColumnIndex("sequence"));
					is_read = c.getString(c.getColumnIndex("is_read"));
					create_datetime = c.getString(c.getColumnIndex("create_datetime"));

					hotItem = new HotItem(objectId, link_record_type, thumbnail, highlight_image, title_zh, title_sc, title_en, description_zh, description_sc, description_en, image1, image2, image3, image4, image5, link_record_type, link_record_id, publish_start_datetime, publish_end_datetime, link_type, link_zh, link_sc, link_en, publish_status, is_highlight, sequence, is_read, create_datetime);
					bannerList.add(hotItem);

					c.moveToNext();
				}

				c.close();
				c = null;

				return bannerList;
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
	public List<HotItem> getHighlightListWithType(String sqlType,
			boolean isHighLight) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getHighlightListWithType");

		//		NSString *isHighLightSql = isHighLight ? @"and is_highlight = 'Y'" : @"";
		//	    NSString *sqlStatement = [NSString stringWithFormat:@"select * from hot_item where type = '%@' %@ order by publish_start_datetime desc", aType, isHighLightSql];

		String[] separated = sqlType.split(",");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{				
				String isHighLightSql = "";
				if (isHighLight) {
					isHighLightSql = "AND is_highlight = 'Y'";
				}
				String sql = "SELECT * FROM hot_item WHERE type = '";
				for (int i = 0; i < separated.length; i++) {
					if (i == 0) {
						sql += separated[i] + "' ";
					} else {
						sql += "OR type = '" + separated[i] + "' ";
					}
				}
				sql += isHighLightSql + " ORDER BY publish_start_datetime desc;";
				//				LogController.log(sql);
				//				String sql1 = "SELECT * FROM hot_item WHERE type = '" + sqlType + "' " + isHighLightSql + " ORDER BY publish_start_datetime desc;";
				LogController.log("sql : " + sql);
				c = dB.rawQuery(sql, null);

				//				whatsHotList.clear();
				List<HotItem> whatsHotList = new ArrayList<HotItem>();
				HotItem hotItem = null;

				String objectId = "";
				String type = "";
				String thumbnail = "";
				String highlight_image = "";
				String title_zh = "";
				String title_sc = "";
				String title_en = "";
				String description_zh = "";
				String description_sc = "";
				String description_en = "";
				String image1 = "";
				String image2 = "";
				String image3 = "";
				String image4 = "";
				String image5 = "";
				String link_record_type = "";
				String link_record_id = "";
				String publish_start_datetime = "";
				String publish_end_datetime = "";
				String link_type = "";
				String link_zh = "";
				String link_sc = "";
				String link_en = "";
				String publish_status = "";
				String is_highlight = "";
				String sequence = "";
				String is_read = "";
				String create_datetime = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					highlight_image = DataUtil.convertImageName(c.getString(c.getColumnIndex("highlight_image")));
					title_zh = c.getString(c.getColumnIndex("title_zh"));
					title_sc = c.getString(c.getColumnIndex("title_sc"));
					title_en = c.getString(c.getColumnIndex("title_en"));
					description_zh = c.getString(c.getColumnIndex("description_zh"));
					description_sc = c.getString(c.getColumnIndex("description_sc"));
					description_en = c.getString(c.getColumnIndex("description_en"));
					image1 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image1")));
					image2 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image2")));
					image3 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image3")));
					image4 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image4")));
					image5 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image5")));
					link_record_type = c.getString(c.getColumnIndex("link_record_type"));
					link_record_id = c.getString(c.getColumnIndex("link_record_id"));
					publish_start_datetime = c.getString(c.getColumnIndex("publish_start_datetime"));
					publish_end_datetime = c.getString(c.getColumnIndex("publish_end_datetime"));
					link_type = c.getString(c.getColumnIndex("link_type"));
					link_zh = c.getString(c.getColumnIndex("link_zh"));
					link_sc = c.getString(c.getColumnIndex("link_sc"));
					link_en = c.getString(c.getColumnIndex("link_en"));
					publish_status = c.getString(c.getColumnIndex("publish_status"));
					is_highlight = c.getString(c.getColumnIndex("is_highlight"));
					sequence = c.getString(c.getColumnIndex("sequence"));
					is_read = c.getString(c.getColumnIndex("is_read"));
					create_datetime = c.getString(c.getColumnIndex("create_datetime"));

					hotItem = new HotItem(objectId, link_record_type, thumbnail, highlight_image, title_zh, title_sc, title_en, description_zh, description_sc, description_en, image1, image2, image3, image4, image5, link_record_type, link_record_id, publish_start_datetime, publish_end_datetime, link_type, link_zh, link_sc, link_en, publish_status, is_highlight, sequence, is_read, create_datetime);
					whatsHotList.add(hotItem);

					c.moveToNext();
				}

				c.close();
				c = null;

				whatsHotHashMap.put(sqlType, whatsHotList);

				return whatsHotList;
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
	public HotItem getHotItemFromHotItemId(String hotItemId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getHotItemFromHotItemId");

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM hot_item WHERE id = '" +  hotItemId + "';";
				c = dB.rawQuery(sql, null);

				HotItem hotItem = null;

				String objectId = "";
				String type = "";
				String thumbnail = "";
				String highlight_image = "";
				String title_zh = "";
				String title_sc = "";
				String title_en = "";
				String description_zh = "";
				String description_sc = "";
				String description_en = "";
				String image1 = "";
				String image2 = "";
				String image3 = "";
				String image4 = "";
				String image5 = "";
				String link_record_type = "";
				String link_record_id = "";
				String publish_start_datetime = "";
				String publish_end_datetime = "";
				String link_type = "";
				String link_zh = "";
				String link_sc = "";
				String link_en = "";
				String publish_status = "";
				String is_highlight = "";
				String sequence = "";
				String is_read = "";
				String create_datetime = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					thumbnail = DataUtil.convertImageName(c.getString(c.getColumnIndex("thumbnail")));
					highlight_image = DataUtil.convertImageName(c.getString(c.getColumnIndex("highlight_image")));
					title_zh = c.getString(c.getColumnIndex("title_zh"));
					title_sc = c.getString(c.getColumnIndex("title_sc"));
					title_en = c.getString(c.getColumnIndex("title_en"));
					description_zh = c.getString(c.getColumnIndex("description_zh"));
					description_sc = c.getString(c.getColumnIndex("description_sc"));
					description_en = c.getString(c.getColumnIndex("description_en"));
					image1 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image1")));
					image2 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image2")));
					image3 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image3")));
					image4 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image4")));
					image5 = DataUtil.convertImageName(c.getString(c.getColumnIndex("image5")));
					link_record_type = c.getString(c.getColumnIndex("link_record_type"));
					link_record_id = c.getString(c.getColumnIndex("link_record_id"));
					publish_start_datetime = c.getString(c.getColumnIndex("publish_start_datetime"));
					publish_end_datetime = c.getString(c.getColumnIndex("publish_end_datetime"));
					link_type = c.getString(c.getColumnIndex("link_type"));
					link_zh = c.getString(c.getColumnIndex("link_zh"));
					link_sc = c.getString(c.getColumnIndex("link_sc"));
					link_en = c.getString(c.getColumnIndex("link_en"));
					publish_status = c.getString(c.getColumnIndex("publish_status"));
					is_highlight = c.getString(c.getColumnIndex("is_highlight"));
					sequence = c.getString(c.getColumnIndex("sequence"));
					is_read = c.getString(c.getColumnIndex("is_read"));
					create_datetime = c.getString(c.getColumnIndex("create_datetime"));

					hotItem = new HotItem(objectId, link_record_type, thumbnail, highlight_image, title_zh, title_sc, title_en, description_zh, description_sc, description_en, image1, image2, image3, image4, image5, link_record_type, link_record_id, publish_start_datetime, publish_end_datetime, link_type, link_zh, link_sc, link_en, publish_status, is_highlight, sequence, is_read, create_datetime);

					c.moveToNext();
				}

				c.close();
				c = null;
				
				return hotItem;
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
	public List<Promotion> getPromotionListWithType(String type) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionListWithType");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_LIST_API);

		String[] keys = new String[] { "fanclMemberId", "type", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), type, CustomServiceFactory.getAccountService().currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			PromotionParser promotionParser = new PromotionParser();
			return promotionParser.parsePromotionList(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}		
	}

	@Override
	public FanclGeneralResult submitPromotionVisitWithCode(String promotionId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("submitPromotionVisitWithCode");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_VISIT_API);

		String[] keys = new String[] { "fanclMemberId", "promotionId", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), promotionId, CustomServiceFactory.getAccountService().currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			// Set the Parser
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public Object getPromotionQuestionWithPromotionId(String promotionId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionQuestionWithPromotionId");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_QUESTION_API);

		String[] keys = new String[] { "fanclMemberId", "promotionId", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), promotionId, CustomServiceFactory.getAccountService().currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			// Set the Parser
			PromotionParser promotionParser = new PromotionParser();
			return promotionParser.parsePromotionQuestionAnswerList(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult submitPromotionAnswerWithPromotionId(
			String promotionId, String answer1Key,
			String answer2Key) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionQuestionWithPromotionId");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_ANSWER_API);

		String[] keys = new String[] { "fanclMemberId", "promotionId", "answer1", "answer2", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), promotionId, answer1Key, answer2Key, CustomServiceFactory.getAccountService().currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			// Set the Parser
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public List<Promotion> getLatestPromotionWithType(String type)
			throws FanclException {
		LogController.log("getLatestPromotionWithType");

		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM promotion WHERE promotion_type = '" + type + "'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Promotion> latestPromotionList = new ArrayList<Promotion>();
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
				//				String couponTypeId;
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
					
					promotion = new Promotion(objectId, code, thumbnail, image, titleZh, titleSc, titleEn, shortDescriptionZh, 
							shortDescriptionSc, shortDescriptionEn, descriptionZh, descriptionSc, descriptionEn, 
							promotionStartDatetime, promotionEndDatetime, publishStatus, promotionType, isLuckyDraw, 
							luckyDrawType, isNew, isPublic, gp, createDatetime, null, null, isRead);
					latestPromotionList.add(promotion);
					c.moveToNext();
				}

				c.close();
				c = null;

				return latestPromotionList;
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
	public List<Product> getPromotionRelatedProductWithPromotionId(
			String promotionId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionRelatedProductWithPromotionId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM promotion_product_mapping WHERE promotion_id = '" + promotionId + "') AND publish_status = 'publish';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> relatedProductList = new ArrayList<Product>();
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
					relatedProductList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return relatedProductList;
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
	public List<IchannelMagazine> getPromotionRelatedArticleWithPromotionId(
			String promotionId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionRelatedArticleWithPromotionId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				//				String sql = "select * from ichannel_magazine where type = '%@' and id in (select ichannel_magazine_id from ichannel_magazine_mapping where ichannel_type_id = '%@') order by publish_start_datetime desc";
				String sql = "SELECT * FROM ichannel_magazine WHERE id IN (SELECT ichannel_magazine_id FROM ichannel_promotion_mapping WHERE promotion_id = '" + promotionId + "') AND publish_status = 'publish' AND publish_start_datetime <= datetime('now','localtime') AND publish_end_datetime >= datetime('now','localtime');";
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
	public Promotion getPromotionObjectWithPromotionId(String promotionId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getPromotionObjectWithPromotionId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM promotion WHERE id = '" + promotionId + "'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

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
				//				String couponTypeId;
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
					
					promotion = new Promotion(objectId, code, thumbnail, image, titleZh, titleSc, titleEn, 
							shortDescriptionZh, shortDescriptionSc, shortDescriptionEn, descriptionZh, descriptionSc, 
							descriptionEn, promotionStartDatetime, promotionEndDatetime, publishStatus, promotionType, 
							isLuckyDraw, luckyDrawType, isNew, isPublic, gp, createDatetime, null, null, isRead);
					c.moveToNext();
				}

				c.close();
				c = null;

				return promotion;
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
	public List<IchannelType> getIchannelSubcateListWithMainCate(String mainCate)
			throws FanclException {
		LogController.log("getIchannelSubcateListWithMainCate");

		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				//				String sql = "select * from ichannel_type where type = '%@' order by sequence;";
				String sql = "SELECT * FROM ichannel_type WHERE type = '" + mainCate + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<IchannelType> ichannelSubcateList = new ArrayList<IchannelType>();
				IchannelType ichannelType = null;

				String objectId;
				String type;
				String titleZh;
				String titleSc;
				String titleEn;
				String sequence;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					sequence = c.getString(c.getColumnIndex("sequence"));

					ichannelType = new IchannelType(objectId, type, titleZh, titleSc, titleEn, sequence);
					ichannelSubcateList.add(ichannelType);
					c.moveToNext();
				}

				c.close();
				c = null;

				return ichannelSubcateList;
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
	public List<IchannelMagazine> getIchannelListWithMainCate(String mainCate,
			String subCate) throws FanclException {
		LogController.log("getIchannelListWithMainCate");

		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				//				String sql = "select * from ichannel_magazine where type = '%@' and id in (select ichannel_magazine_id from ichannel_magazine_mapping where ichannel_type_id = '%@') order by publish_start_datetime desc";
				String sql = "SELECT * FROM ichannel_magazine WHERE type = '" + mainCate + "' AND id IN (SELECT ichannel_magazine_id FROM ichannel_magazine_mapping WHERE ichannel_type_id = '" + subCate + "') ORDER BY publish_start_datetime DESC";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<IchannelMagazine> ichannelList = new ArrayList<IchannelMagazine>();
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
					ichannelList.add(ichannelMagazine);
					c.moveToNext();
				}

				c.close();
				c = null;

				return ichannelList;
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
	public List<MagazineImage> getMagazineImageWithMagazineType(String type) throws FanclException {
		LogController.log("getMagazineImageWithMagazineType");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT i.* FROM ichannel_magazine_image i, ichannel_magazine_mapping it, " +
						"ichannel_type t, ichannel_magazine m WHERE i.magazine_id = it.ichannel_magazine_id " +
						"AND t.id = it.ichannel_type_id AND m.id = it.ichannel_magazine_id AND t.type = '" + type + 
						"' ORDER BY t.sequence, m.publish_start_datetime desc, i.magazine_id, i.sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<MagazineImage> magazineImageList = new ArrayList<MagazineImage>();
				MagazineImage magazineImage = null;

				String objectId;
				String magazineId;
				String imageEn;
				String imageZh;
				String imageSc;
				String thumbnailEn;
				String thumbnailZh;
				String thumbnailSc;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					magazineId = c.getString(c.getColumnIndex("magazine_id"));
					imageEn = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_en")));
					imageZh = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_zh")));
					imageSc = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_sc")));
					thumbnailEn = DataUtil.convertToThumbnailImageName(c.getString(c.getColumnIndex("image_en")));
					thumbnailEn = DataUtil.convertImageName(thumbnailEn);
					thumbnailZh = DataUtil.convertToThumbnailImageName(c.getString(c.getColumnIndex("image_zh")));
					thumbnailZh = DataUtil.convertImageName(thumbnailZh);
					thumbnailSc = DataUtil.convertToThumbnailImageName(c.getString(c.getColumnIndex("image_sc")));
					thumbnailSc = DataUtil.convertImageName(thumbnailSc);

					magazineImage = new MagazineImage(objectId, magazineId, imageZh, imageSc, imageEn, thumbnailZh, thumbnailSc, thumbnailEn);
					magazineImageList.add(magazineImage);
					c.moveToNext();
				}

				c.close();
				c = null;

				return magazineImageList;
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
	public IchannelMagazine getIchannelInfoWithIchannelId(String channelId)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getIchannelInfoWithIchannelId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM ichannel_magazine WHERE id = '" + channelId + "'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

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
					c.moveToNext();
				}

				c.close();
				c = null;

				return ichannelMagazine;
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
	public List<IchannelMagazine> getIchannelDescriptionWithIchannelId(
			String channelId) throws FanclException {
		LogController.log("getIchannelDescriptionWithIchannelId");

		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				//				String sql = "select * from ichannel_magazine_description where ichannel_magazine_id = '%@' order by sequence";
				String sql = "SELECT * FROM ichannel_magazine_description WHERE ichannel_magazine_id = '" + channelId + "' ORDER BY sequence";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				//				ichannelList.clear();
				List<IchannelMagazine> ichannelList = new ArrayList<IchannelMagazine>();
				IchannelMagazine ichannelMagazine = null;

				String objectId;
				String image;
				String descriptionZh;
				String descriptionSc;
				String descriptionEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					ichannelMagazine = new IchannelMagazine(objectId, image, descriptionZh, descriptionSc, descriptionEn);
					ichannelList.add(ichannelMagazine);
					c.moveToNext();
				}

				c.close();
				c = null;

				return ichannelList;
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
	public List<Product> getIchannelRelatedProductWithIchannelId(
			String channelId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getIchannelRelatedProductWithIchannelId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM product WHERE id IN (SELECT product_id FROM ichannel_product_mapping WHERE ichannel_magazine_id = '" + channelId + "') AND publish_status = 'publish';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Product> relatedProductList = new ArrayList<Product>();
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
					relatedProductList.add(product);
					c.moveToNext();
				}

				c.close();
				c = null;

				return relatedProductList;
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
	public List<Promotion> getIchannelRelatedPromotionWithIchannelId(
			String channelId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getIchannelRelatedPromotionWithIchannelId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM promotion WHERE id IN (SELECT promotion_id FROM ichannel_promotion_mapping WHERE ichannel_magazine_id = '" + channelId + "') AND is_lucky_draw != 'Y' AND publish_status = 'publish' AND promotion_start_datetime <= datetime('now','localtime') AND promotion_end_datetime >= datetime('now','localtime');";
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
				//				String couponTypeId;
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

					promotion = new Promotion(objectId, code, thumbnail, image, titleZh, titleSc, titleEn, shortDescriptionZh, 
							shortDescriptionSc, shortDescriptionEn, descriptionZh, descriptionSc, descriptionEn, 
							promotionStartDatetime, promotionEndDatetime, publishStatus, promotionType, isLuckyDraw, 
							luckyDrawType, isNew, isPublic, gp, createDatetime, null, null, isRead);
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
	public List<IchannelMagazine> getIchannelSearchResultWithKeyword(
			String keyword) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getIchannelSearchResultWithKeyword");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM ichannel_magazine WHERE (title_zh LIKE '%%" + keyword + "%%' OR title_sc LIKE '%%" + keyword + "%%' OR title_en LIKE '%%" + keyword + "%%') AND (type != 'skincare' AND type != 'intake');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<IchannelMagazine> ichannelSearchList = new ArrayList<IchannelMagazine>();
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
					ichannelSearchList.add(ichannelMagazine);
					c.moveToNext();
				}

				c.close();
				c = null;

				return ichannelSearchList;
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

	public List<Event> getEventItemListWithItemId(String itemId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getEventItemListWithItemId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM event_id WHERE item_id = '" + itemId + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Event> eventList = new ArrayList<Event>();
				Event event = null;

				String type;
				String eventType;
				String eventId;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					type = c.getString(c.getColumnIndex("type"));
					eventType = c.getString(c.getColumnIndex("event_type"));
					eventId = c.getString(c.getColumnIndex("event_id"));

					event = new Event(type, eventType, eventId);
					eventList.add(event);
					c.moveToNext();
				}

				c.close();
				c = null;

				return eventList;
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

	public HashMap<String, List<HotItem>> getWhatsHotHashMap() {
		return whatsHotHashMap;
	}

	public void setWhatsHotHashMap(HashMap<String, List<HotItem>> whatsHotHashMap) {
		this.whatsHotHashMap = whatsHotHashMap;
	}

	public FanclGeneralResult redeemICouponWithCode(String coupoonCode, String couponNo) throws FanclException {
		LogController.log("redeemICouponWithCode");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_COUPON_SELECT_API);

		String[] keys = new String[] { "fanclMemberId", "promotionId", "couponNo", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), coupoonCode, couponNo, CustomServiceFactory.getAccountService().currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			// Set the Parser
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}
}
