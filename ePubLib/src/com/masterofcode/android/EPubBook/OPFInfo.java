package com.masterofcode.android.EPubBook;

public class OPFInfo {
	private OPFInfoMetadata metadata;
	private OPFInfoManifest manifest;
	private OPFInfoSpine	spine;
	private OPFInfoGuide	guide;

	public OPFInfo()
	{
		metadata = new OPFInfoMetadata();
		manifest = new OPFInfoManifest();
		spine = new OPFInfoSpine();
		guide = new OPFInfoGuide();
	}

	public void strictCheck() throws Exception
	{
		metadata.strictCheck();
		spine.strictCheck(manifest);
	}

	public OPFInfoMetadata getMetadata()
	{
		return metadata;
	}

	public OPFInfoManifest getManifest()
	{
		return manifest;
	}

	public OPFInfoSpine getSpine()
	{
		return spine;
	}

	public OPFInfoGuide getGuide()
	{
		return guide;
	}
	
	public String getTOCFileName()
	{
		String				tocId = spine.getTocId();
		OPFInfoManifestItem item = manifest.getItemById(tocId);	
		return item.getHref();
	}
}
