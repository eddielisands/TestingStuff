package com.masterofcode.android.EPubBook;

public class OPFInfoSpineItem {
	private	String		idRef = null;		// ref to xhtml page
	private String		lanscapeShotRef = null;
	private String		portraitShotRef = null;
	private boolean		fullScreenFlag = false;
	private boolean		coverPageFlag = false;
	
	
	public OPFInfoSpineItem(String idRef, String lanscapeShotRef,
			String portraitShotRef, boolean fullScreenFlag,
			boolean coverPageFlag) {
		super();
		this.idRef = idRef;
		this.lanscapeShotRef = lanscapeShotRef;
		this.portraitShotRef = portraitShotRef;
		this.fullScreenFlag = fullScreenFlag;
		this.coverPageFlag = coverPageFlag;
	}

	// getters
	public String getIdRef()
	{
		return idRef;
	}
	
	public String getLanscapeShotRef()
	{
		return lanscapeShotRef;
	}
	
	public String getPortraitShotRef()
	{
		return portraitShotRef;
	}
	
	public boolean isFullScreen()
	{
		return fullScreenFlag;
	}
	
	public boolean isCoverPage()
	{
		return coverPageFlag;
	}
	
	public void setIdRef(String idRef)
	{
		this.idRef = idRef;
	}
	
	// setters
	public void setLanscapeShotRef(String lanscapeShotRef)
	{
		this.lanscapeShotRef = lanscapeShotRef;
	}
	
	public void setPortraitShotRef(String portraitShotRef)
	{
		this.portraitShotRef = portraitShotRef;
	}
	
	public void setFullScreenFlag(boolean fullscreenFlag)
	{
		this.fullScreenFlag = fullscreenFlag;
	}
	
	public void setCoverPageFlag(boolean coverpageFlag)
	{
		this.coverPageFlag = coverpageFlag;
	}
}
