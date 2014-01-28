package com.masterofcode.android.magreader.db.entity;

import org.kroz.activerecord.ActiveRecordBase;

import com.masterofcode.android.magreader.utils.constants.Constants;

public class BookmarksItem extends ActiveRecordBase implements Comparable<BookmarksItem> {
	// common parts
	public int				item_type = Constants.BOOKMARKS_ITEM_TYPE_NONE;
	public long				bookmarking_date;
	
	// Feed part
	public String 			channel_id = null;
    public String 			guid = null;

    // Magazine part
    public String 			magazine_title = null;
    public String 			magazine_name = null;
    public String 			magazine_path = null;
    public String 			magazine_cover_path = null;
    public String 			magazine_topic_path = null;
    public String 			magazine_topic_title = null;
    public int				magazine_topic_index = 0;
    public float			magazine_topic_offset = 0;
    
    // show part
    public FeedItem			feedItem = null;


    public BookmarksItem()
    {
    }
    
    public BookmarksItem(String channel_id, String guid)
    {
    	item_type =  Constants.BOOKMARKS_ITEM_TYPE_FEED;
		this.channel_id = channel_id;
		this.guid = guid;
		setBookmaringDate();
	}

	public BookmarksItem(String magazine_title, String magazine_name, String magazine_path, String magazine_cover_path, String magazine_topic_path, String magazine_topic_title, int topic_index, float topic_offset)
	{
    	item_type =  Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE;
    	this.magazine_title = magazine_title;
		this.magazine_name = magazine_name;
		this.magazine_path = magazine_path;
		this.magazine_cover_path = magazine_cover_path;
		this.magazine_topic_path = magazine_topic_path;
		this.magazine_topic_title = magazine_topic_title;
		this.magazine_topic_index = topic_index;
		this.magazine_topic_offset = topic_offset;
		setBookmaringDate();
	}
	
	private void setBookmaringDate()
	{
		bookmarking_date = System.currentTimeMillis();
	}
	
    public FeedItem getFeedItem()
    {
		return feedItem;
	}

	public void setFeedItem(FeedItem feedItem)
	{
		this.feedItem = feedItem;
	}

	@Override
	public int compareTo(BookmarksItem compareObject) {
		if (bookmarking_date < compareObject.bookmarking_date)
			return -1;
		else if (bookmarking_date == compareObject.bookmarking_date)
				return 0;
			 else return 1;
	}
}
