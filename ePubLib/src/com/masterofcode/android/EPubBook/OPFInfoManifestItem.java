package com.masterofcode.android.EPubBook;

public class OPFInfoManifestItem {
	private String 		id = null;
	private String 		href = null;
	private String 		mediaType = null;

	public OPFInfoManifestItem(String id, String href, String mediaType) {
		super();
		this.id = id;
		this.href = href;
		this.mediaType = mediaType;
	}

	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getHref()
	{
		return href;
	}
	
	public void setHref(String href)
	{
		this.href = href;
	}
	
	public String getMediaType()
	{
		return mediaType;
	}
	
	public void setMediaType(String mediaType)
	{
		this.mediaType = mediaType;
	}
}
