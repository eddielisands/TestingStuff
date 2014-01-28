package com.masterofcode.android.EPubBook.SAXHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import com.masterofcode.android.EPubBook.OPFInfo;

public class OPFHandler extends TagPathHandler {
    static final String PACKAGE 							= "package";
    static final String METADATA							= "/" + PACKAGE + "/metadata";
    static final String MANIFEST							= "/" + PACKAGE + "/manifest";
    static final String SPINE								= "/" + PACKAGE + "/spine";
    static final String GUIDE								= "/" + PACKAGE + "/guide";
    static final String METADATA_TITLE						= METADATA + "/title";
    static final String METADATA_LANGUAGE					= METADATA + "/language";
    static final String METADATA_IDENTIFIER 				= METADATA + "/identifier";
    static final String METADATA_DATE						= METADATA + "/date";
    static final String METADATA_PUBLISHER					= METADATA + "/publisher";
    static final String METADATA_META						= METADATA + "/meta";
    static final String METADATA_META_ATTR_CONTENT			= "content";
    static final String METADATA_META_ATTR_NAME 			= "name";
    static final String MANIFEST_ITEM						= MANIFEST + "/item";
    static final String MANIFEST_ITEM_ATTR_ID 				= "id";
    static final String MANIFEST_ITEM_ATTR_HREF 			= "href";
    static final String MANIFEST_ITEM_ATTR_MEDIATYPE		= "media-type";
    static final String SPINE_TAG_ATTR_TOC					= "toc";
    static final String SPINE_ITEM							= SPINE + "/itemref";
    static final String SPINE_ITEM_ATTR_IDREF				= "idref";
    static final String SPINE_ITEM_ATTR_LANDSCAPE_SHOT		= "landscape_shot";
    static final String SPINE_ITEM_ATTR_PORTRAIT_SHOT		= "portrait_shot";
    static final String SPINE_ITEM_ATTR_COVERPAGE			= "coverpage";
    static final String SPINE_ITEM_ATTR_FULLSCREEN			= "fullscreen";
    static final String GUIDE_ITEM							= GUIDE + "/reference";
    static final String GUIDE_ITEM_ATTR_TYPE				= "type";
    static final String GUIDE_ITEM_ATTR_TITLE				= "title";
    static final String GUIDE_ITEM_ATTR_HREF				= "href";

    OPFInfo					opfInfo;
    boolean					isStrictCheck = false;

	public OPFHandler(boolean strictCheck) {
		opfInfo = new OPFInfo();
		isStrictCheck = strictCheck;
    }

	@Override
	public void processStartElement(String tagPath, String uri,	String localName, String qName, Attributes attributes) throws SAXException
	{
		super.processStartElement(tagPath, uri, localName, qName, attributes);
		
		if(tagPath.equalsIgnoreCase(METADATA_META))
		{
			String		name = attributes.getValue(METADATA_META_ATTR_NAME);
			String		content = attributes.getValue(METADATA_META_ATTR_CONTENT);
			
			if(name != null && content != null) opfInfo.getMetadata().addAdditionalMetadata(name, content);
			return;
		}

		if(tagPath.equalsIgnoreCase(MANIFEST_ITEM))
		{
			String		id = attributes.getValue(MANIFEST_ITEM_ATTR_ID);
			String		href = attributes.getValue(MANIFEST_ITEM_ATTR_HREF);
			String		mediaType = attributes.getValue(MANIFEST_ITEM_ATTR_MEDIATYPE);
			
			if(id != null && href != null && mediaType != null) opfInfo.getManifest().addItem(id, href, mediaType);
			return;
		}

		if(tagPath.equalsIgnoreCase(SPINE))
		{
			if (opfInfo.getSpine().getTocId()==null)
			{
				String		toc = attributes.getValue(SPINE_TAG_ATTR_TOC);
				if(toc != null) opfInfo.getSpine().setTocId(toc);
				return;
			} else {
				if(isStrictCheck) throw new SAXException("EPub OPF has more than one 'spine' section");
			}
		}

		if(tagPath.equalsIgnoreCase(SPINE_ITEM))
		{
			String		idRef = attributes.getValue(SPINE_ITEM_ATTR_IDREF);
			String		lanscapeShot = attributes.getValue(SPINE_ITEM_ATTR_LANDSCAPE_SHOT);
			String		portraitShot = attributes.getValue(SPINE_ITEM_ATTR_PORTRAIT_SHOT);
			String		coverpage = attributes.getValue(SPINE_ITEM_ATTR_COVERPAGE);
			String		fullscreen = attributes.getValue(SPINE_ITEM_ATTR_FULLSCREEN);
			boolean		isCoverpage = false, isFullscreen = false;
			
			if(coverpage != null)
			{
				if(coverpage.equalsIgnoreCase("true")) isCoverpage = true;
			}

			if(fullscreen != null)
			{
				if(fullscreen.equalsIgnoreCase("true")) isFullscreen = true;
			}

			if(idRef != null) opfInfo.getSpine().addItem(idRef, lanscapeShot, portraitShot, isFullscreen, isCoverpage);
			return;
		}

		if(tagPath.equalsIgnoreCase(GUIDE_ITEM))
		{
			String		type = attributes.getValue(GUIDE_ITEM_ATTR_TYPE);
			String		title = attributes.getValue(GUIDE_ITEM_ATTR_TITLE);
			String		href = attributes.getValue(GUIDE_ITEM_ATTR_HREF);
			
			if(type != null && title != null && href != null) opfInfo.getGuide().addItem(type, title, href);
			return;
		}
	}

	@Override
	public void processElementContent(String tagPath, String content) throws SAXException
	{
		super.processElementContent(tagPath, content);
		
		if(tagPath.equalsIgnoreCase(METADATA_TITLE))
		{
			// take first title
			if(opfInfo.getMetadata().getTitle()==null)opfInfo.getMetadata().setTitle(content);
			return;
		}
		if(tagPath.equalsIgnoreCase(METADATA_LANGUAGE))
		{
			opfInfo.getMetadata().setLanguage(content);
			return;
		}
		if(tagPath.equalsIgnoreCase(METADATA_IDENTIFIER))
		{
			opfInfo.getMetadata().setIdentifier(content);
			return;
		}
		if(tagPath.equalsIgnoreCase(METADATA_PUBLISHER))
		{
			opfInfo.getMetadata().setPublisher(content);
			return;
		}
		if(tagPath.equalsIgnoreCase(METADATA_DATE))
		{
			opfInfo.getMetadata().setDate(content);
			return;
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
		opfInfo.getSpine().checkReferencedMediaTypes(opfInfo.getManifest());
		opfInfo.getSpine().checkToc(opfInfo.getManifest());
	}

	public OPFInfo getOpfInfo() {
		return opfInfo;
	}
}
