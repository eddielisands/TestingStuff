package com.masterofcode.android.magreader.bookmarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.EntitiesHelper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.BookmarksItem;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.utils.constants.Constants;

public class BookmarksManager<curBookmark> {
	private static BookmarksManager			instance = null;
	
	private ActiveRecordBase				dbBookmarks;
	
	public BookmarksManager()
	{
		super();
		this.dbBookmarks = JtjApplication.getInstance().getBookmarksDatabase();
	}

	static public BookmarksManager GetInstance()
	{
		if(instance==null)
		{
			instance = new BookmarksManager();
		}
		return instance;
	}

	public boolean removeBookmark(BookmarksItem bookmark)
	{
		if (bookmark==null) return false;
		
		if(bookmark.item_type==Constants.BOOKMARKS_ITEM_TYPE_FEED)
		{
			FeedItem		feedItem = bookmark.feedItem;
			if (feedItem==null) return false;
			
			feedItem.isBookmarked = false;
			
			try {
				ActiveRecordBase		_db = JtjApplication.getInstance().getDatabase();
				if(!_db.isOpen())_db.open();
				feedItem.update();
				//_db.close();
			} catch (ActiveRecordException e1) {
				e1.printStackTrace();
				return false;
			}
		} else if(bookmark.item_type==Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE)
				{
			
				} else return false;
		
		// remove bookmark from db
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			bookmark.delete();
			dbBookmarks.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void removeBookmarks(Context context, List<BookmarksItem> bookmarks)
	{
		if (bookmarks==null) return;
		// remove bookmarks from db
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			
			for(BookmarksItem curBookmark : bookmarks)
			{
				curBookmark.delete();
			}
			dbBookmarks.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
	}

	
	public void BoomarkFeed(Context context, FeedItem feedItem)
	{
		BookmarksItem			newBookmark = new BookmarksItem(feedItem.channel_id, feedItem.guid);

		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			BookmarksItem			newDbBookmark = dbBookmarks.newEntity(BookmarksItem.class);
			EntitiesHelper.copyFieldsWithoutID(newDbBookmark, newBookmark);
			newDbBookmark.save();
			dbBookmarks.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}

//		Log.i("---", "bookmark feed with title="+feedItem.title);
		
		feedItem.isBookmarked = true;
		feedItem.bookmarking_date = newBookmark.bookmarking_date;
		try {
			feedItem.update();
		} catch (ActiveRecordException e1) {
			e1.printStackTrace();
		}
		
		setFeedsBookmarkedStatus(context, feedItem.guid, newBookmark.bookmarking_date);
	}
	
	private void setFeedsBookmarkedStatus(Context context, String guid, long date)
	{
		// TODO: all feeds with this uuid will be marked as bookmarked
		ActiveRecordBase		database;
		
		database = ((JtjApplication) ((Activity)context).getApplication()).getDatabase();
		
		try {
			List<FeedItem>		feeds = database.find(FeedItem.class, "GUID=?", new String [] { String.valueOf(guid) });
			for(FeedItem  current : feeds)
			{
				current.isBookmarked = true;
				current.bookmarking_date = date;
				current.update();
			}
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
	}

	public void BookmarkMagazine(String magazine_title, String magazine_name, String magazine_path, String magazine_cover_path, String magazine_topic_path, String magazine_topic_title, int topic_index, float topic_offset)
	{
		BookmarksItem			newBookmark = new BookmarksItem(magazine_title, magazine_name, magazine_path, magazine_cover_path, magazine_topic_path, magazine_topic_title, topic_index, topic_offset);

		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			BookmarksItem			newDbBookmark = dbBookmarks.newEntity(BookmarksItem.class);
			EntitiesHelper.copyFieldsWithoutID(newDbBookmark, newBookmark);
			newDbBookmark.save();
			dbBookmarks.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}

		Log.i("---", "bookmark topic path="+magazine_topic_path);
	}
	
	public ArrayList<Integer> queryBookmarkedTopics(String magazineName)
	{
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			List<BookmarksItem> bookmarks = dbBookmarks.find(BookmarksItem.class, false, "ITEMTYPE=? AND MAGAZINENAME=?", new String[] { String.valueOf(Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE), magazineName }, null, null, "BOOKMARKINGDATE DESC", null);
			dbBookmarks.close();

			ArrayList<Integer>		result = new ArrayList<Integer>();
			for(BookmarksItem cur : bookmarks)
			{
				result.add(Integer.valueOf(cur.magazine_topic_index));
			}
			
			return result;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return null;
	}
			
	public List<BookmarksItem> queryBookmarkedMagazines(Context context)
	{
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			List<BookmarksItem> bookmarks = dbBookmarks.find(BookmarksItem.class, false, "ITEMTYPE=?", new String[] { String.valueOf(Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE) }, null, null, "BOOKMARKINGDATE DESC", null);
			dbBookmarks.close();

			return bookmarks;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<BookmarksItem> queryBookmarkedFeedsAndMagazines(Context context)
	{
		List<BookmarksItem>			bookmarkedFeeds = queryBookmarkedFeeds(context);
		List<BookmarksItem>			bookmarkedMagazines = queryBookmarkedMagazines(context);
		
		if(bookmarkedFeeds!=null)
		{
			if(bookmarkedMagazines!=null)
			{
				bookmarkedFeeds.addAll(bookmarkedMagazines);
				Collections.sort(bookmarkedFeeds);
				return bookmarkedFeeds;
			} else {
				return bookmarkedFeeds;	// there is no bookmaked magazines
			}
		} else {
			return bookmarkedMagazines; // if no bookmarked feeds, returns bookmarked magazines (or null, if no magazines)
		}
	}
	
	public List<BookmarksItem> queryBookmarkedFeeds(Context context)
	{
		ActiveRecordBase		_db;
		List<FeedItem>			bookmarkedFeeds = null;
		List<BookmarksItem>		bookmarks = null;
		
		// getting bookmarked feeds from feeds database 
		_db = JtjApplication.getInstance().getDatabase();
		try {
			if(!_db.isOpen())_db.open();
			bookmarkedFeeds = _db.find(FeedItem.class, false, "IS_BOOKMARKED=?", new String[] { "true" }, null, null, "BOOKMARKINGDATE DESC", null);
			//_db.close();
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}

		// getting bookmarked objects from bookmarks database 
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			bookmarks = dbBookmarks.find(BookmarksItem.class, false, "ITEMTYPE=?", new String[] { String.valueOf(Constants.BOOKMARKS_ITEM_TYPE_FEED) }, null, null, "BOOKMARKINGDATE DESC", null);
			dbBookmarks.close();
			
			for(BookmarksItem cur : bookmarks)
			{
				Log.i("---", "bookmarked feed, channel_id="+cur.channel_id+", guid="+cur.guid);
			}
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		
		// set feed objects for according bookmarks objects 
		if(bookmarkedFeeds!=null && bookmarks!=null)
		{
			int				normalItemsCount = 0;

			for(BookmarksItem curBookmark : bookmarks)
			{
				FeedItem		appropriateFeedItem = null;
				
				for(FeedItem curFeedItem : bookmarkedFeeds)
				{
					if(curFeedItem.channel_id.equalsIgnoreCase(curBookmark.channel_id) &&
					   curFeedItem.guid.equalsIgnoreCase(curBookmark.guid))
					{
						appropriateFeedItem = curFeedItem;
						break;
					}
				}
				
				if(appropriateFeedItem!=null)
				{
					curBookmark.setFeedItem(appropriateFeedItem);
					normalItemsCount++;
				}
			}
			
			if(normalItemsCount==bookmarks.size() && normalItemsCount>0) return bookmarks;
			
			if(normalItemsCount>0)
			{
				List<BookmarksItem>		result = new ArrayList<BookmarksItem>(normalItemsCount);
				
				for(BookmarksItem curBookmark : bookmarks)
				{
					if(curBookmark.getFeedItem()!=null) result.add(curBookmark);
				}
				
				return result;
			}
		}
		
		return null;
	}
	
	public boolean isMagazineHasBookmarks(Context context, String magazineName)
	{
		
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			List<BookmarksItem> bookmarks = dbBookmarks.find(BookmarksItem.class, false, "ITEMTYPE=? AND MAGAZINENAME=?", new String[] { String.valueOf(Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE), magazineName }, null, null, "BOOKMARKINGDATE DESC", null);
			dbBookmarks.close();

			if(bookmarks!=null)
			{
				if(bookmarks.size()>0)
				{
					return true;
				}
			}
			return false;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void removeMagazineBookmarks(Context context, String magazineName)
	{
		
		try {
			if(!dbBookmarks.isOpen()) dbBookmarks.open();
			List<BookmarksItem> bookmarks = dbBookmarks.find(BookmarksItem.class, false, "ITEMTYPE=? AND MAGAZINENAME=?", new String[] { String.valueOf(Constants.BOOKMARKS_ITEM_TYPE_MAGAZINE), magazineName }, null, null, "BOOKMARKINGDATE DESC", null);
			dbBookmarks.close();
			
			removeBookmarks(context, bookmarks);
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
	}
}
