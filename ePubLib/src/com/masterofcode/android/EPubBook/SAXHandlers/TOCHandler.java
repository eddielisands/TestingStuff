package com.masterofcode.android.EPubBook.SAXHandlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.masterofcode.android.EPubBook.TOCInfo;
import com.masterofcode.android.EPubBook.TOCInfoNavMap;
import com.masterofcode.android.EPubBook.TOCInfoNavPoint;

import android.util.Log;

public class TOCHandler extends TagPathHandler {
	private final String			DOC_TITLE_TEXT = "/ncx/docTitle/text";
	private final String			NAVPOINT_LABEL = "/navPoint/navLabel/text";
	private final String			NAVPOINT_CONTENT = "/navPoint/content";
	private final String			NAVPOINT_TAG = "navPoint";
	private final String			NAVPOINT_CONTENT_ATTR_SRC = "src";
	private final String			NAVPOINT_ATTR_ID = "id";
	private final String			NAVPOINT_ATTR_PLAY_ORDER = "playOrder";
	private TOCInfo					tocInfo;
	private	TOCInfoNavPoint			currentNavPoint = null;
	private	TOCInfoNavMap			navMap = null;

	public TOCHandler()
	{
		super();
		
		tocInfo = new TOCInfo();
		navMap = new TOCInfoNavMap();
	}

	@Override
	public void processStartElement(String tagPath, String uri,	String localName, String qName, Attributes attributes) throws SAXException
	{
		super.processStartElement(tagPath, uri, localName, qName, attributes);

		if(localName.equalsIgnoreCase(NAVPOINT_TAG))
		{
			TOCInfoNavPoint newNavPoint = new TOCInfoNavPoint();
			String			id = attributes.getValue(NAVPOINT_ATTR_ID);
			String			playOrder = attributes.getValue(NAVPOINT_ATTR_PLAY_ORDER);
			
			if(id!=null) newNavPoint.setId(id);
			if(playOrder!=null) newNavPoint.setPlayOrder(Integer.parseInt(playOrder));
				
			newNavPoint.setParent(currentNavPoint);
			if(currentNavPoint==null)
			{
				navMap.addNavPoint(newNavPoint);
			} else {
				currentNavPoint.addChild(newNavPoint);
			}
			currentNavPoint = newNavPoint;
			return;
		}
		
		int		idx = tagPath.lastIndexOf(NAVPOINT_CONTENT);
		if (idx!=-1)
		{
			String		src = attributes.getValue(NAVPOINT_CONTENT_ATTR_SRC);
			
			if(src!=null)
			{
				if(currentNavPoint!=null)
				{
					currentNavPoint.setContentSrc(src);
				}
			}
			return;
		}
	}

	@Override
	public void processEndElementAfterStrippingTagPath(String tagPath, String uri, String localName, String qName) throws SAXException
	{
		super.processEndElementAfterStrippingTagPath(tagPath, uri, localName, qName);

		if(localName.equalsIgnoreCase(NAVPOINT_TAG))
		{
			currentNavPoint = currentNavPoint.getParent();
		}
	}

	@Override
	public void processElementContent(String tagPath, String content) throws SAXException
	{
		super.processElementContent(tagPath, content);

		if(tagPath.equalsIgnoreCase(DOC_TITLE_TEXT))
		{
			tocInfo.setDocTitle(content);
			return;
		}
		
		int		idx = tagPath.lastIndexOf(NAVPOINT_LABEL);
		if (idx!=-1)
		{
			currentNavPoint.setNavLabel(content);
		}

	}

	public TOCInfo getTocInfo()
	{
		return tocInfo;
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
/*		
		Log.i("TOC---", "navPoint in navMap="+navMap.getNavPoints().size());
		for(int i=0; i<navMap.getNavPoints().size(); i++)
		{
			Log.i("TOC---", "root navPoint["+i+"]");
			inspectNavPoint("    ", navMap.getNavPoints().get(i));
		}*/
	}
	
	public void inspectNavPoint(String padding, TOCInfoNavPoint navPoint)
	{
		Log.i("TOC---", padding + "id=" + navPoint.getId());
		Log.i("TOC---", padding + "playOrder=" + navPoint.getPlayOrder());
		Log.i("TOC---", padding + "label=" + navPoint.getNavLabel());
		Log.i("TOC---", padding + "src=" + navPoint.getContentSrc());
		if(navPoint.getChilds().size()>0)
		{
			Log.i("TOC---", padding + "childs:");
			for(int i=0; i<navPoint.getChilds().size(); i++)
			{
				Log.i("TOC---", padding + padding + "child["+i+"]:");
				inspectNavPoint(padding + padding + padding, navPoint.getChilds().get(i));
			}
		}
	}
}
