package com.masterofcode.android.magreader.search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.content.Context;

import com.masterofcode.android.EPubBook.EPubBook;
import com.masterofcode.android.EPubBook.EPubBookProcessingListener;
import com.masterofcode.android.EPubBook.EPubBookSearchResult;
import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedItem;
import com.masterofcode.android.magreader.db.entity.LibraryItem;
import com.masterofcode.android.magreader.library.LibraryManager;
import com.masterofcode.android.magreader.library.ResourceMagazinesCopyingListener;
import com.masterofcode.android.magreader.utils.constants.Constants;

public class SearchManager {
	private static SearchManager			instance = null;
	private FeedSearchDbAdapter feedSearchDbAdapter;
	private ActiveRecordBase _db;
	
	public SearchManager()
	{
		super();
	}

	static public SearchManager getInstance()
	{
		if(instance==null)
		{
			instance = new SearchManager();
		}
		return instance;
	}
	
	public ArrayList<SearchResultItem> searchWithType(Context context, int searchType, String keyword)
	{
		ArrayList<SearchResultItem> 	result = null;
		switch (searchType) {
			case Constants.SEARCH_TYPE_FEEDS:
				result =  searchInFeeds(context, keyword);
				break;

			case Constants.SEARCH_TYPE_LIBRARY:
				result =  searchInLibrary(context, keyword);
				break;

			case Constants.SEARCH_TYPE_EVERYWHERE:
				result =  searchEverywhere(context, keyword);
				break;

			default:
				break;
		}
		return result;
	}

	public ArrayList<SearchResultItem> searchInFeeds(Context context, String keyword)
	{
		ArrayList<SearchResultItem> feedSearchResults = null;
		try {
			feedSearchDbAdapter = new FeedSearchDbAdapter(context);
			feedSearchDbAdapter.open();
			List<FeedItem> feedItemSearchResults = new ArrayList<FeedItem>();
			HashSet<String> results = feedSearchDbAdapter.search(keyword);
			_db =  JtjApplication.getInstance().getDatabase();
			_db.open();
			for(String mGUID : results){
				for (FeedItem feedItem : _db.find(FeedItem.class, "GUID=?", new String[] { mGUID })){
					feedItemSearchResults.add(feedItem);
					break;
				}
			}
			for(FeedItem feedItem : feedItemSearchResults){
				SearchResultItem searchItem = new SearchResultItem();
				searchItem.assignFeedItem(feedItem);
				
				if(feedSearchResults==null)feedSearchResults = new ArrayList<SearchResultItem>();
				feedSearchResults.add(searchItem);
			}
			return feedSearchResults;
		} catch (ActiveRecordException e) {
			e.printStackTrace();
		}
		return feedSearchResults;
	}

	/*private void checkDefaultIsCopied(Context context)
	{
		if(LibraryManager.GetInstance().isAllMagazinesIsCopied(context)) return;
        LibraryManager.GetInstance().copyResourceMagazines(context, new ResourceMagazinesCopyingListener() {
      			@Override
       			public void onProgress(float copied) {
       			}
        	});
	}*/
	
	public ArrayList<SearchResultItem> searchInLibrary(Context context, String keyword)
	{

		//checkDefaultIsCopied(context);
		
		ArrayList<SearchResultItem>		result = null;
		List<LibraryItem> 				libraryItems = LibraryManager.GetInstance().queryLibraryItems();
		
		for(LibraryItem	currentLibraryItem : libraryItems)
		{
			
			// check if epub is downloaded 
			File			bookf = new File(currentLibraryItem.magazine_filepath);
			if(!bookf.canRead()) continue;
			
			EPubBook		currentBook = new EPubBook(context, currentLibraryItem.magazine_filepath, false);
			
			try {
				currentBook.load();

				// extract if need
				if(currentBook.isNeedExtract())
		    	{
					currentBook.extractContentToExternalStorage(new EPubBookProcessingListener() {
						@Override
						public void onProgress(long percentsExtracted) {
						}
					});
		    	}
				
				// process media if need
				if(currentBook.isNeedProcessingMedia())
		    	{
					currentBook.processMedia(new EPubBookProcessingListener() {
						@Override
						public void onProgress(long percentsExtracted) {
						}
					});
		    	}
				
				EPubBookSearchResult	searchResult = currentBook.isContainsKeyword(keyword);
				if(searchResult.isKeywordFinded())
				{
					if(result==null) result = new ArrayList<SearchResultItem>();
					
					ArrayList<Integer>		topics = searchResult.getTopicsWithKeyword();
					boolean					combineTopics = false;
	
					if(Constants.SEARCH_IN_LIBRARY_WITH_THRESHOLD)
					{
						if(((float)(topics.size())) / currentBook.getTopicsCount() > Constants.SEARCH_IN_LIBRARY_ALL_BOOK_THRESHOLD)
						{
							combineTopics = true;
						}
					}
					// check if topics with finded keywords bigger than thrashold, to interpreting
					// search result as whole book or no
					if(combineTopics)
					{
						SearchResultItem		newSearchResult = new SearchResultItem();
						
						newSearchResult.assignLibraryItem(currentLibraryItem);
						newSearchResult.setLibraryItemWholeBook(true);
						newSearchResult.setLibraryItemTopics(topics);
						if(topics.size()>0)	newSearchResult.setLibraryItemTopicIndex(topics.get(0).intValue());
						else newSearchResult.setLibraryItemTopicIndex(0);

						ArrayList<String>		topicTitles = searchResult.getTopicsTitles();
						String					resultTitles = null;
						for(int i=0; i<topicTitles.size(); i++)
						{
							if(resultTitles==null) resultTitles = topicTitles.get(i);
							else resultTitles = resultTitles + ", " + topicTitles.get(i);
						}
						newSearchResult.setLibraryItemTopicTitle(resultTitles);

						result.add(newSearchResult);
					} else {
						ArrayList<String>		topicTitles = searchResult.getTopicsTitles();
						for(int i=0; i<topics.size(); i++)
						{
							SearchResultItem		newSearchResult = new SearchResultItem();

							newSearchResult.assignLibraryItem(currentLibraryItem);
							newSearchResult.setLibraryItemTopics(topics);
							newSearchResult.setLibraryItemTopicTitle(topicTitles.get(i));
							newSearchResult.setLibraryItemTopicIndex(topics.get(i).intValue());
							newSearchResult.setLibraryItemWholeBook(false);
							result.add(newSearchResult);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ArrayList<SearchResultItem> searchEverywhere(Context context, String keyword)
	{
		ArrayList<SearchResultItem> feedSearchResults = searchInFeeds(context, keyword);
		ArrayList<SearchResultItem> librarySearchResults = searchInLibrary(context, keyword);
		ArrayList<SearchResultItem> result = feedSearchResults;
		
		if(result!=null)
		{
			if(librarySearchResults!=null)
			{
				result.addAll(librarySearchResults);
			}
		} else {
			result = librarySearchResults;
		}
		
		return result;
	}
}
