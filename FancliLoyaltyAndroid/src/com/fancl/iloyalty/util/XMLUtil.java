package com.fancl.iloyalty.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtil {
	
	private static final String SUB_TAG = "XMLUtil";
	
	private static void log(String message, Exception e)
	{
		LogController.log(SUB_TAG + " >>> " + message, e);
	}
	
	public static Document toDocument(InputStream inputStream) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(inputStream);
			
			return document;
		} catch (ParserConfigurationException e) {
			XMLUtil.log("ParserConfigurationException", e);
		} catch (SAXException e) {
			XMLUtil.log("SAXException", e);
		} catch (IOException e) {
			XMLUtil.log("IOException", e);
		}
		
		return null;
	}
}
