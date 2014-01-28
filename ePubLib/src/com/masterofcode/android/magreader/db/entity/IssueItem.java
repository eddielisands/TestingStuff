package com.masterofcode.android.magreader.db.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;

import android.text.TextUtils;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.utils.constants.JsonObjectConstants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class IssueItem extends ActiveRecordBase {
	public String appleAppStorePriceLevel;
	public String subtitle;
	public String type;
	public String version;
	public String publicationDate;
	public String publicationDateUTC;
	public boolean downloadable;
	public String coverPictureUrl;
	public String teaserPictureUrl;
	public String detailCoverUrl;
	public String shopPictureUrl;
	public String downloadUrl;
	public String shopDetailUrl;
	public String shopDetailTextUrlText;
	public String metaDataUrl;
	public String summary;
	public String categorTagA;
	public String categorTagB;
	public String categorTagC;
	public String categorTagD;
	public String categorTagE;
	public String volume;
	public String magazine;
	public String articles;
	public String title;
	public String id;
	public String androidreadergooglecheckoutprice;
	public String googlecheckoutid;
	public String issueID;
	public boolean isDownloaded;
	public boolean isBuyed;
	
	public IssueItem(){
	}
	
	public IssueItem(JSONObject jsonObject) {
		this.appleAppStorePriceLevel = jsonObject.optString(JsonObjectConstants.APPLEAPPSTOREPRICELEVEL);
		this.subtitle = jsonObject.optString(JsonObjectConstants.SUBTITLE);
		this.type = jsonObject.optString(JsonObjectConstants.TYPE);
		this.version = jsonObject.optString(JsonObjectConstants.VERSION);
		this.publicationDate = jsonObject.optString(JsonObjectConstants.PUBLICATIONDATE_ISSUE);
		this.publicationDateUTC = jsonObject.optString(JsonObjectConstants.PUBLICATIONDATEUTC_ISSUE);
		this.downloadable = jsonObject.optString(JsonObjectConstants.DOWNLOADABLE).equals("true") ? true : false;
		this.coverPictureUrl = jsonObject.optString(JsonObjectConstants.COVERPICTUREURL);
		this.teaserPictureUrl = jsonObject.optString(JsonObjectConstants.TEASERPICTUREURL);
		this.detailCoverUrl = jsonObject.optString(JsonObjectConstants.DETAILCOVERURL);
		//this.detailCoverUrl = jsonObject.optString(JsonObjectConstants.DETAILCOVERURL).contains("/240/0/Cover-Picture-JTJ-6-2011-png.png") ? "http://jaxenter.com/assets/240/320/Cover-Picture-JTJ-6-2011-png.png" : jsonObject.optString(JsonObjectConstants.DETAILCOVERURL);
		this.shopPictureUrl = jsonObject.optString(JsonObjectConstants.SHOPPICTUREURL);
		this.downloadUrl = jsonObject.optString(JsonObjectConstants.DOWNLOADURL);
		this.shopDetailUrl = jsonObject.optString(JsonObjectConstants.SHOPDETAILURL);
		this.shopDetailTextUrlText = jsonObject.optString(JsonObjectConstants.SHOPDETAILTEXTURLTEXT);
		this.metaDataUrl = jsonObject.optString(JsonObjectConstants.METADATEURL);
		this.summary = jsonObject.optString(JsonObjectConstants.SUMMARY);
		JSONArray categories;
		try {
			categories = jsonObject.getJSONArray("categories");
			this.categorTagA = !TextUtils.isEmpty(categories.getString(0)) ? categories.getString(0) : null ;
			this.categorTagB = !TextUtils.isEmpty(categories.getString(1)) ? categories.getString(1) : null ;
			this.categorTagC = !TextUtils.isEmpty(categories.getString(2)) ? categories.getString(2) : null ;
			this.categorTagD = !TextUtils.isEmpty(categories.getString(3)) ? categories.getString(3) : null ;
			this.categorTagE = !TextUtils.isEmpty(categories.getString(4)) ? categories.getString(4) : null ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		this.volume = jsonObject.optString(JsonObjectConstants.VOLUME);
		this.magazine = jsonObject.optString(JsonObjectConstants.MAGAZINE);
		this.articles = jsonObject.optString(JsonObjectConstants.ARTICLES);
		String issueTitleFromJSON = jsonObject.optString(JsonObjectConstants.TITLE_ISSUE);
		
		if ((issueTitleFromJSON != null) && (issueTitleFromJSON != "")) {
			issueTitleFromJSON = issueTitleFromJSON.replace("-", " ");
			//Eddie Li Magazine series prefix from string
			String magazineSeriesPrefix = JtjApplication.getContext().getString(R.string.magazine_series_prefix);
//			if ((Constants.MAGAZINE_SERIES_PREFIX.length() > 0) && (issueTitleFromJSON.startsWith(Constants.MAGAZINE_SERIES_PREFIX, 0))) {
//				issueTitleFromJSON = issueTitleFromJSON.substring(Constants.MAGAZINE_SERIES_PREFIX.length(), issueTitleFromJSON.length());
//			}
			if ((magazineSeriesPrefix.length() > 0) && (issueTitleFromJSON.startsWith(magazineSeriesPrefix, 0))) {
				issueTitleFromJSON = issueTitleFromJSON.substring(magazineSeriesPrefix.length(), issueTitleFromJSON.length());
			}
		}
		this.title = issueTitleFromJSON;
		this.issueID = jsonObject.optString(JsonObjectConstants.ID_ISSUE);
		this.androidreadergooglecheckoutprice = jsonObject.optString(JsonObjectConstants.ANDROIDREADERGOOGLECHECKOUTPRICE);
		this.googlecheckoutid = jsonObject.optString(JsonObjectConstants.GOOGLECHECKOUTID);
		this.isDownloaded = false;
		this.isBuyed = false;
	}
}
