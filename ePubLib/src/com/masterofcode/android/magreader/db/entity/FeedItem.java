package com.masterofcode.android.magreader.db.entity;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.kroz.activerecord.ActiveRecordBase;

import android.text.TextUtils;
import android.util.Log;

import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.ImageUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.masterofcode.android.magreader.utils.constants.JsonObjectConstants;
import com.twmacinta.util.MD5;

public class FeedItem extends ActiveRecordBase {
	
	public String channel_id;
    public String title;
    public String link;
    public String description;
    public String guid;
    public String author;
    public String publication_date;
    public String sandspicture;
    public String thumbnailslink;
    public boolean highlighted;
    public boolean isRead;
    public boolean isBookmarked;
	public long bookmarking_date;
	
	public FeedItem() {		
	}
	
	public FeedItem(JSONObject jsonObject, String channelId) {
		
		this.channel_id = channelId;
		this.title = jsonObject.optString(JsonObjectConstants.TITLE);
		this.link = jsonObject.optString(JsonObjectConstants.LINK);
		this.description = jsonObject.optString(JsonObjectConstants.DESCRIPTION);
		/*if (jsonObject.optJSONObject(JsonObjectConstants.GUID) != null) {
			this.guid = jsonObject.optJSONObject(JsonObjectConstants.GUID).optString("content");
		} else {
			this.guid = jsonObject.optString(JsonObjectConstants.GID);
			if (this.guid.equals("")) {
				this.guid = jsonObject.optString(JsonObjectConstants.GUID);
			}
		}*/
		this.author = jsonObject.optString(JsonObjectConstants.AUTHOR);
		this.publication_date = ApplicationUtils.formatDateForDb(jsonObject.optString(JsonObjectConstants.PUBLICATIONDATE));
		this.sandspicture = jsonObject.optString(JsonObjectConstants.SANDSPICTURE);
		this.thumbnailslink = TextUtils.isEmpty(ImageUtils.getThumbnailsLink(description)) ? "" : Constants.THUMBNAILS_URL_SCRIPT + ImageUtils.getThumbnailsLink(description);
		if (jsonObject.optString(JsonObjectConstants.HIGHLIGHT).equals("1")) {
			this.highlighted = true;
		} else {
			this.highlighted = false;
		}
		this.isRead = false;
		this.isBookmarked = false;
		
		MD5 md5 = new MD5();
		try {
			md5.Update(this.link, null);
			this.guid = md5.asHex();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
