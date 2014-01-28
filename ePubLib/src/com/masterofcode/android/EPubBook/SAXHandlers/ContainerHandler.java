package com.masterofcode.android.EPubBook.SAXHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class ContainerHandler extends TagPathHandler {
	static final String ROOTFILE_PATH = "/container/rootfiles/rootfile";
	static final String ROOTFILE_TAG_FULL_PATH = "full-path";
	static final String ROOTFILE_TAG_MEDIA_TYPE = "media-type";
	
	String		rootFilePath = null;
	String		rootFileMediaType = null;
	

    public String getRootFilePath()
    {
    	return rootFilePath;
    }

    public String getRootFileMediaType()
    {
    	return rootFileMediaType;
    }

	@Override
	public void processStartElement(String tagPath, String uri,	String localName, String qName, Attributes attributes)	throws SAXException {
		super.processStartElement(tagPath, uri, localName, qName, attributes);
		
		if (tagPath.equalsIgnoreCase(ROOTFILE_PATH))
		{
			String		fullPath = attributes.getValue(ROOTFILE_TAG_FULL_PATH);
			String		mediaType = attributes.getValue(ROOTFILE_TAG_MEDIA_TYPE);
			
			rootFilePath = fullPath;
			rootFileMediaType = mediaType;
		}
		
	}
}
