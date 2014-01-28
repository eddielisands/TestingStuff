package com.masterofcode.android.magreader.search;

import java.util.ArrayList;

import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.utils.constants.Constants;

public class SearchResultItem {
	private	int						searchResultType = Constants.SEARCH_TYPE_NONE;
	private FeedItem				feedItem;
	private LibraryItem				libraryItem;
	private boolean					libraryItemWholeBook;		// keyword is finded in more than SEARCH_IN_BOOK_ALL_BOOK_THRESHOLD topics
	private String					libraryItemTopicTitle;      // contains one topic title if 'libraryItemWholeBook' is 'false', else all topics titles with keywords, separated by comma
	private ArrayList<Integer>		libraryItemTopics = null;
	private int						libraryItemTopicIndex;      // topic index with first founded (for show)
	
	public void assignLibraryItem(LibraryItem item)
	{
		libraryItem = item;
		searchResultType = Constants.SEARCH_TYPE_LIBRARY;
	}

	public void assignFeedItem(FeedItem item)
	{
		feedItem = item;
		searchResultType = Constants.SEARCH_TYPE_FEEDS;
	}

	public int getSearchResultType()
	{
		return searchResultType;
	}

	public FeedItem getFeedItem()
	{
		return feedItem;
	}

	public LibraryItem getLibraryItem()
	{
		return libraryItem;
	}

	public boolean isLibraryItemWholeBook()
	{
		return libraryItemWholeBook;
	}

	public String getLibraryItemTopicTitle()
	{
		return libraryItemTopicTitle;
	}

	public void setLibraryItemWholeBook(boolean libraryItemWholeBook)
	{
		this.libraryItemWholeBook = libraryItemWholeBook;
	}

	public void setLibraryItemTopicTitle(String libraryItemTopicTitle)
	{
		this.libraryItemTopicTitle = libraryItemTopicTitle;
	}

	public ArrayList<Integer> getLibraryItemTopics()
	{
		return libraryItemTopics;
	}

	public void setLibraryItemTopics(ArrayList<Integer> libraryItemTopics)
	{
		this.libraryItemTopics = libraryItemTopics;
	}

	public int getLibraryItemTopicIndex()
	{
		return libraryItemTopicIndex;
	}

	public void setLibraryItemTopicIndex(int libraryItemTopicIndex)
	{
		this.libraryItemTopicIndex = libraryItemTopicIndex;
	}
}
