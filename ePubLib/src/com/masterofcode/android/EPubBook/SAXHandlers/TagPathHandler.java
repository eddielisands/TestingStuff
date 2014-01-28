package com.masterofcode.android.EPubBook.SAXHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TagPathHandler extends DefaultHandler {
    private String					currentTagPath = null;
    private StringBuilder			builder = null;

	public String getCurrentTagPath()
	{
		return currentTagPath;
	}

	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		currentTagPath = "";
        builder = new StringBuilder();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(uri, localName, qName, attributes);
		currentTagPath += "/" + localName;
		processStartElement(currentTagPath, uri, localName, qName, attributes);
		builder.setLength(0);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		super.endElement(uri, localName, qName);

		String		content = builder.toString();
		processElementContent(currentTagPath, content);

		processEndElementBeforeStrippingTagPath(currentTagPath, uri, localName, qName);

		int idx = currentTagPath.lastIndexOf("/" + localName);
		if(idx != -1)
		{
			currentTagPath = currentTagPath.substring(0, idx);
		}

		processEndElementAfterStrippingTagPath(currentTagPath, uri, localName, qName);
	}
	
	public void processStartElement(String tagPath, String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
	}

	public void processEndElementBeforeStrippingTagPath(String tagPath, String uri, String localName, String qName) throws SAXException
	{
	}

	public void processEndElementAfterStrippingTagPath(String tagPath, String uri, String localName, String qName) throws SAXException
	{
	}

	public void processElementContent(String tagPath, String content) throws SAXException
	{
	}
}
