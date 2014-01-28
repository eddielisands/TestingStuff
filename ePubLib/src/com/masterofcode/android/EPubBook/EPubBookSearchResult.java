package com.masterofcode.android.EPubBook;

import java.util.ArrayList;

public class EPubBookSearchResult {
	private	boolean				isKeywordFinded = false;
	private ArrayList<Integer>	topicsWithKeyword = null;
	private ArrayList<String>	topicsTitles = null;
	
	public boolean isKeywordFinded()
	{
		return isKeywordFinded;
	}
	
	public ArrayList<Integer> getTopicsWithKeyword()
	{
		return topicsWithKeyword;
	}

	public void setKeywordFinded(boolean isKeywordFinded)
	{
		this.isKeywordFinded = isKeywordFinded;
	}
	
	public void setTopicsWithKeyword(ArrayList<Integer> topicsWithKeyword)
	{
		this.topicsWithKeyword = topicsWithKeyword;
	}

	public ArrayList<String> getTopicsTitles()
	{
		return topicsTitles;
	}

	public void setTopicsTitles(ArrayList<String> topicsTitles)
	{
		this.topicsTitles = topicsTitles;
	}
}
