package com.masterofcode.android.magreader.db.entity;

import org.kroz.activerecord.ActiveRecordBase;

import com.masterofcode.android.magreader.utils.constants.Constants;

public class FeedType extends ActiveRecordBase {
	
    public String title;
    public String url;
    public String language;
    public int order_id;
    public int unread_count;
    public boolean is_hide;
    public boolean is_default = false;
    

    public FeedType() {    	
    }

    public FeedType(String title, String url, String language, int order_id, int unread_count) {
        this.title = title;
        this.url = url;
        this.language = language;
        this.order_id = order_id;
        this.unread_count = unread_count;
        if (title.equals(Constants.FEED_ALL_NAME) || title.equals(Constants.FEED_NEWS_NAME) || title.equals(Constants.FEED_VIDEOS_NAME)) {
        	this.is_hide = false;
        } else {
        	this.is_hide = true;
        }
    }
}
