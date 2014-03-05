package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
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
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.AdBanner;
import com.fancl.iloyalty.pojo.ContactUs;
import com.fancl.iloyalty.pojo.QRCode;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.pojo.ShopRegion;
import com.fancl.iloyalty.service.AboutFanclService;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.PList;

public class AboutFanclServiceImpl implements AboutFanclService {

	@Override
	public List<ShopRegion> getShopParentRegionList() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getShopParentRegionList");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop_region WHERE parent_id = -1 ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ShopRegion> shopRegionList = new ArrayList<ShopRegion>();
				ShopRegion shopRegion = null;

				String objectId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					shopRegion = new ShopRegion(objectId, parentId, titleZh, titleSc, titleEn);
					shopRegionList.add(shopRegion);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shopRegionList;
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
	public List<ShopRegion> getShopRegionListWithParentId(String id)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getShopRegionListWithParentId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop_region WHERE parent_id = '" + id + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ShopRegion> shopRegionList = new ArrayList<ShopRegion>();
				ShopRegion shopRegion = null;

				String objectId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					shopRegion = new ShopRegion(objectId, parentId, titleZh, titleSc, titleEn);
					shopRegionList.add(shopRegion);
					c.moveToNext();
				}

				c.close();
				c = null;

				if (shopRegionList.size() > 0) {
					return shopRegionList;
				}
				else {
					getShopRegionListWithId(id);
				}
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

	public List<ShopRegion> getShopRegionListWithId(String id) throws FanclException {
		LogController.log("getShopRegionListWithId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop_region WHERE id = '" + id + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<ShopRegion> shopRegionList = new ArrayList<ShopRegion>();
				ShopRegion shopRegion = null;

				String objectId;
				String parentId;
				String titleZh;
				String titleSc;
				String titleEn;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					parentId = c.getString(c.getColumnIndex("parent_id"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					shopRegion = new ShopRegion(objectId, parentId, titleZh, titleSc, titleEn);
					shopRegionList.add(shopRegion);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shopRegionList;
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
	public List<Shop> getFullShopList() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getFullShopList");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Shop> shopList = new ArrayList<Shop>();
				Shop shop = null;

				String objectId;
				String code;
				String regionId;
				String type;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String addressZh;
				String addressSc;
				String addressEn;
				String phoneNumber;
				String officeHourZh;
				String officeHourSc;
				String officeHourEn;
				String latitude;
				String longitude;
				String publishStatus;
				String isNew;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					regionId = c.getString(c.getColumnIndex("region_id"));
					type = c.getString(c.getColumnIndex("type"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					addressZh = c.getString(c.getColumnIndex("address_zh"));
					addressSc = c.getString(c.getColumnIndex("address_sc"));
					addressEn = c.getString(c.getColumnIndex("address_en"));
					phoneNumber = c.getString(c.getColumnIndex("phone"));
					officeHourZh = c.getString(c.getColumnIndex("office_hour_zh"));
					officeHourSc = c.getString(c.getColumnIndex("office_hour_sc"));
					officeHourEn = c.getString(c.getColumnIndex("office_hour_en"));
					latitude = c.getString(c.getColumnIndex("latitude"));
					longitude = c.getString(c.getColumnIndex("longitude"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					shop = new Shop(objectId, code, regionId, type, image, titleZh, titleSc, titleEn, addressZh, addressSc, addressEn, phoneNumber, officeHourZh, officeHourSc, officeHourEn, latitude, longitude, publishStatus, isNew, createDatetime);
					shopList.add(shop);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shopList;
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
	public List<Shop> getShopListForFancl(boolean isFancl, String id)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getShopListForFancl");
		String shopType = isFancl ? "fancl": "fanclfnh";

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop WHERE type = '" + shopType + "' AND region_id = '" + id + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<Shop> shopList = new ArrayList<Shop>();
				Shop shop = null;

				String objectId;
				String code;
				String regionId;
				String type;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String addressZh;
				String addressSc;
				String addressEn;
				String phoneNumber;
				String officeHourZh;
				String officeHourSc;
				String officeHourEn;
				String latitude;
				String longitude;
				String publishStatus;
				String isNew;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					regionId = c.getString(c.getColumnIndex("region_id"));
					type = c.getString(c.getColumnIndex("type"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					addressZh = c.getString(c.getColumnIndex("address_zh"));
					addressSc = c.getString(c.getColumnIndex("address_sc"));
					addressEn = c.getString(c.getColumnIndex("address_en"));
					phoneNumber = c.getString(c.getColumnIndex("phone"));
					officeHourZh = c.getString(c.getColumnIndex("office_hour_zh"));
					officeHourSc = c.getString(c.getColumnIndex("office_hour_sc"));
					officeHourEn = c.getString(c.getColumnIndex("office_hour_en"));
					latitude = c.getString(c.getColumnIndex("latitude"));
					longitude = c.getString(c.getColumnIndex("longitude"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					shop = new Shop(objectId, code, regionId, type, image, titleZh, titleSc, titleEn, addressZh, addressSc, addressEn, phoneNumber, officeHourZh, officeHourSc, officeHourEn, latitude, longitude, publishStatus, isNew, createDatetime);
					shopList.add(shop);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shopList;
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
	public Shop getShopDetailWithId(String shopId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getShopDetailWithId");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop WHERE id = '" + shopId + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				Shop shop = null;

				String objectId;
				String code;
				String regionId;
				String type;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String addressZh;
				String addressSc;
				String addressEn;
				String phoneNumber;
				String officeHourZh;
				String officeHourSc;
				String officeHourEn;
				String latitude;
				String longitude;
				String publishStatus;
				String isNew;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					regionId = c.getString(c.getColumnIndex("region_id"));
					type = c.getString(c.getColumnIndex("type"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					addressZh = c.getString(c.getColumnIndex("address_zh"));
					addressSc = c.getString(c.getColumnIndex("address_sc"));
					addressEn = c.getString(c.getColumnIndex("address_en"));
					phoneNumber = c.getString(c.getColumnIndex("phone"));
					officeHourZh = c.getString(c.getColumnIndex("office_hour_zh"));
					officeHourSc = c.getString(c.getColumnIndex("office_hour_sc"));
					officeHourEn = c.getString(c.getColumnIndex("office_hour_en"));
					latitude = c.getString(c.getColumnIndex("latitude"));
					longitude = c.getString(c.getColumnIndex("longitude"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					shop = new Shop(objectId, code, regionId, type, image, titleZh, titleSc, titleEn, addressZh, addressSc, addressEn, phoneNumber, officeHourZh, officeHourSc, officeHourEn, latitude, longitude, publishStatus, isNew, createDatetime);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shop;
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
	public Shop getShopDetailWithCode(String shopCode) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getShopDetailWithCode");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM shop WHERE code = '" + shopCode + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				Shop shop = null;

				String objectId;
				String code;
				String regionId;
				String type;
				String image;
				String titleZh;
				String titleSc;
				String titleEn;
				String addressZh;
				String addressSc;
				String addressEn;
				String phoneNumber;
				String officeHourZh;
				String officeHourSc;
				String officeHourEn;
				String latitude;
				String longitude;
				String publishStatus;
				String isNew;
				String createDatetime;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					regionId = c.getString(c.getColumnIndex("region_id"));
					type = c.getString(c.getColumnIndex("type"));
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					addressZh = c.getString(c.getColumnIndex("address_zh"));
					addressSc = c.getString(c.getColumnIndex("address_sc"));
					addressEn = c.getString(c.getColumnIndex("address_en"));
					phoneNumber = c.getString(c.getColumnIndex("phone"));
					officeHourZh = c.getString(c.getColumnIndex("office_hour_zh"));
					officeHourSc = c.getString(c.getColumnIndex("office_hour_sc"));
					officeHourEn = c.getString(c.getColumnIndex("office_hour_en"));
					latitude = c.getString(c.getColumnIndex("latitude"));
					longitude = c.getString(c.getColumnIndex("longitude"));
					publishStatus = c.getString(c.getColumnIndex("publish_status"));
					isNew = c.getString(c.getColumnIndex("is_new"));
					createDatetime = c.getString(c.getColumnIndex("create_datetime"));

					shop = new Shop(objectId, code, regionId, type, image, titleZh, titleSc, titleEn, addressZh, addressSc, addressEn, phoneNumber, officeHourZh, officeHourSc, officeHourEn, latitude, longitude, publishStatus, isNew, createDatetime);
					c.moveToNext();
				}

				c.close();
				c = null;

				return shop;
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
	public AboutFancl getFanclBackground(String aboutId) throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getFanclBackground");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl WHERE id = '" + aboutId + "';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = null;
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = null;
					descriptionSc = null;
					descriptionEn = null;

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFancl;
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
	public List<AboutFancl> getFanclBackgroundDescriptionWithType(String fanclType)
			throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getFanclBackgroundDescriptionWithType");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl_description WHERE type = '" + fanclType + "' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<AboutFancl> aboutFanclList = new ArrayList<AboutFancl>();
				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = null;
					titleSc = null;
					titleEn = null;
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					aboutFanclList.add(aboutFancl);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFanclList;
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
	public AboutFancl getFanclBackground() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getFanclBackground");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl WHERE type = 'background'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = null;
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = null;
					descriptionSc = null;
					descriptionEn = null;

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFancl;
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
	public List<AboutFancl> getFanclBackgroundDescription() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getFanclBackgroundDescription");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl_description WHERE type = 'background' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<AboutFancl> aboutFanclList = new ArrayList<AboutFancl>();
				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = null;
					titleSc = null;
					titleEn = null;
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					aboutFanclList.add(aboutFancl);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFanclList;
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
	public AboutFancl getLessIsMore() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getLessIsMore");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl WHERE type = 'lessIsMore'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = null;
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = null;
					descriptionSc = null;
					descriptionEn = null;

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFancl;
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
	public List<AboutFancl> getLessIsMoreDescription() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getLessIsMoreDescription");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl_description WHERE type = 'lessIsMore' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<AboutFancl> aboutFanclList = new ArrayList<AboutFancl>();
				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = null;
					titleSc = null;
					titleEn = null;
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					aboutFanclList.add(aboutFancl);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFanclList;
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
	
	public AboutFancl getHowToUse() throws FanclException {
		LogController.log("getHowToUse");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl WHERE type = 'howToUse'";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = null;
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));
					descriptionZh = null;
					descriptionSc = null;
					descriptionEn = null;

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFancl;
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
	
	public List<AboutFancl> getHowToUseDescription() throws FanclException {
		LogController.log("getHowToUseDescription");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM about_fancl_description WHERE type = 'howToUse' ORDER BY sequence;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<AboutFancl> aboutFanclList = new ArrayList<AboutFancl>();
				AboutFancl aboutFancl = null;

				String objectId;
				String type;
				String image;
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
					image = DataUtil.convertImageName(c.getString(c.getColumnIndex("image")));
					titleZh = null;
					titleSc = null;
					titleEn = null;
					descriptionZh = c.getString(c.getColumnIndex("description_zh"));
					descriptionSc = c.getString(c.getColumnIndex("description_sc"));
					descriptionEn = c.getString(c.getColumnIndex("description_en"));

					aboutFancl = new AboutFancl(objectId, type, image, titleZh, titleSc, titleEn, descriptionZh, descriptionSc, descriptionEn);
					aboutFanclList.add(aboutFancl);
					c.moveToNext();
				}

				c.close();
				c = null;

				return aboutFanclList;
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
	public ContactUs getContactUs() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getContactUs");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM contact_us;";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				ContactUs contactUs = null;

				String objectId;
				String officeHourZh;
				String officeHourSc;
				String officeHourEn;
				String addressZh;
				String addressSc;
				String addressEn;
				String phone;
				String email;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					officeHourZh = c.getString(c.getColumnIndex("office_hour_zh"));
					officeHourSc = c.getString(c.getColumnIndex("office_hour_sc"));
					officeHourEn = c.getString(c.getColumnIndex("office_hour_en"));
					addressZh = c.getString(c.getColumnIndex("address_zh"));
					addressSc = c.getString(c.getColumnIndex("address_sc"));
					addressEn = c.getString(c.getColumnIndex("address_en"));
					phone = c.getString(c.getColumnIndex("phone"));
					email = c.getString(c.getColumnIndex("email"));

					contactUs = new ContactUs(objectId, officeHourZh, officeHourSc, officeHourEn, addressZh, addressSc, addressEn, phone, email);
					c.moveToNext();
				}

				c.close();
				c = null;

				return contactUs;
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
	public List<QRCode> getQRCodeObjects() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getQRCodeObjects");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				String sql = "SELECT * FROM qrcode";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				List<QRCode> qRCodeList = new ArrayList<QRCode>();
				QRCode qRCode = null;

				String objectId;
				String type;
				String qrCodeId;
				String qrCodeString;

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					type = c.getString(c.getColumnIndex("type"));
					qrCodeId = c.getString(c.getColumnIndex("object_id"));
					qrCodeString = c.getString(c.getColumnIndex("code"));

					qRCode = new QRCode(objectId, qrCodeId, type, qrCodeString);
					qRCodeList.add(qRCode);
					c.moveToNext();
				}

				c.close();
				c = null;

				return qRCodeList;
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

	public List<AdBanner> getFrontAdObjects() throws FanclException {
		// TODO Auto-generated method stub
				LogController.log("getFrontAdObjects");
				Exception exception = null;
				SQLiteDatabase dB = null;
				Cursor c = null;

				try {
					dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
					if (dB != null)
					{	
						String sql = "SELECT * FROM ad;";
						LogController.log("sql:" + sql);
						c = dB.rawQuery(sql, null);

						List<AdBanner> adBannerList = new ArrayList<AdBanner>();
						AdBanner adBanner = null;

						String objectId;
						String itemId;
						String itemType;
						String imageZh;
						String imageSc;
						String imageEn;
						String image5Zh;
						String image5Sc;
						String image5En;
						String linkZh;
						String linkSc;
						String linkEn;
						String hitRate;

						c.moveToFirst();
						while (!c.isAfterLast()) {
							objectId = c.getString(c.getColumnIndex("id"));
							itemId = c.getString(c.getColumnIndex("item_id"));
							itemType = c.getString(c.getColumnIndex("item_type"));
							imageZh = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_zh")));
							imageSc = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_sc")));
							imageEn = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_en")));
							image5Zh = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_iphone5_zh")));
							image5Sc = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_iphone5_sc")));
							image5En = DataUtil.convertImageName(c.getString(c.getColumnIndex("image_iphone5_en")));
							linkZh = c.getString(c.getColumnIndex("link_zh"));
							linkSc = c.getString(c.getColumnIndex("link_sc"));
							linkEn = c.getString(c.getColumnIndex("link_en"));
							hitRate = c.getString(c.getColumnIndex("hit_rate"));

							adBanner = new AdBanner(objectId, itemId, itemType, imageZh, imageSc, imageEn, image5Zh, image5Sc, image5En, linkZh, linkSc, linkEn, hitRate);
							adBannerList.add(adBanner);
							c.moveToNext();
						}

						c.close();
						c = null;

						return adBannerList;
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
	public String getUnreadNumberWithType() throws FanclException {
		// TODO Auto-generated method stub
	    LogController.log("getUnreadNumberWithType");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
			    String sql = "SELECT count(*) FROM hot_item WHERE (type = 'campaign' OR type = 'shop' OR type = 'product' OR type = 'reading' OR type = 'promotion') AND is_read = 'N';";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				String unreadNumber = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					unreadNumber = c.getString(c.getColumnIndex("count(*)"));

					c.moveToNext();
				}

				c.close();
				c = null;

				return unreadNumber;
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

		return "";
	}

	@Override
	public String getUnreadChannel() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUnreadChannel");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
			    String sql = "SELECT count(*) FROM ichannel_magazine WHERE is_read = 'N' AND (type != 'intake' AND type !='skincare');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				String unreadNumber = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					unreadNumber = c.getString(c.getColumnIndex("count(*)"));

					c.moveToNext();
				}

				c.close();
				c = null;

				return unreadNumber;
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

		return "";
	}

	@Override
	public String getUnreadPromotion() throws FanclException {
		// TODO Auto-generated method stub
		LogController.log("getUnreadChannel");
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
			    String sql = "SELECT count(*) FROM promotion WHERE is_read = 'N' AND (promotion_type = 'redemption' OR promotion_type = 'latest');";
				LogController.log("sql:" + sql);
				c = dB.rawQuery(sql, null);

				String unreadNumber = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					unreadNumber = c.getString(c.getColumnIndex("count(*)"));

					c.moveToNext();
				}

				c.close();
				c = null;

				return unreadNumber;
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

		return "0";
	}
	
	public String getPromotionCount() throws FanclException {
		LogController.log("redeemICouponWithCode");

		String url = ApiConstant.getAPI(ApiConstant.PROMOTION_COUNT_API);

		String[] keys = new String[] { "fanclMemberId", "language", "systemType"};
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), CustomServiceFactory.getAccountService().currentLanguage(), "A"};

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
			return fanclResultParser.parsePromotionCount(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}
}
