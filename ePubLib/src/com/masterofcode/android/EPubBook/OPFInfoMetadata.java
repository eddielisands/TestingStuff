package com.masterofcode.android.EPubBook;

import java.util.HashMap;

public class OPFInfoMetadata {
	private String					title = null;
	private String					language = null;
	private String					identifier = null;
	private String					publisher = null;
	private String					date = null;
	private HashMap<String, String>	additionalMetadata = null;

	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getLanguage()
	{
		return language;
	}
	
	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	public String getIdentifier()
	{
		return identifier;
	}
	
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}
	
	public String getPublisher()
	{
		return publisher;
	}
	
	public void setPublisher(String publisher)
	{
		this.publisher = publisher;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public void strictCheck() throws Exception
	{
		if(title==null || language==null || identifier==null) throw new Exception("EPub OPF has invalid 'metadata' section");
	}

	public HashMap<String, String> getAdditionalMetadata()
	{
		return additionalMetadata;
	}
	
	public String getAdditionalMetadataContentForName(String name)
	{
		if(additionalMetadata != null) return additionalMetadata.get(name);
		return null;
	}

	public void addAdditionalMetadata(String name, String content)
	{
		if(additionalMetadata==null) additionalMetadata = new HashMap<String, String>();
		additionalMetadata.put(name, content);
	}
}
