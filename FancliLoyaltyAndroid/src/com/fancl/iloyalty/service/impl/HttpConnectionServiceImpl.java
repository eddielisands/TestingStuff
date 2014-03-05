package com.fancl.iloyalty.service.impl;

import java.io.ByteArrayInputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.w3c.dom.Document;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.util.HttpUtil;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.NetworkConnective;
import com.fancl.iloyalty.util.XMLUtil;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.PList;

public class HttpConnectionServiceImpl implements HttpConnectionService{
	
	private AndroidProjectApplication application;
	
	public HttpConnectionServiceImpl(AndroidProjectApplication application)
	{
		this.application = application;
	}
	
	@Override
	public Document downloadXML(String url, String[] keys, String[] values, HTTP_CALLING_METHOD callingMethod) throws GeneralException {
		if(!NetworkConnective.checkNetwork(this.application))
		{
			throw this.makeGeneralNoNetworkGeneralException();
		}
		
		try {
			if(callingMethod != null)
			{
				if(callingMethod.equals(HTTP_CALLING_METHOD.POST))
				{
					byte[] byteArray = HttpUtil.downloadXMLFromPost(url, keys, values);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
					Document document = XMLUtil.toDocument(byteArrayInputStream);
					return document;
				}
				else if(callingMethod.equals(HTTP_CALLING_METHOD.GET))
				{
					byte[] byteArray = HttpUtil.downloadXMLFromGet(url, keys, values);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
					Document document = XMLUtil.toDocument(byteArrayInputStream);
					return document;
				}
			}
			
			byte[] byteArray = HttpUtil.downloadXMLFromPost(url, keys, values);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			Document document = XMLUtil.toDocument(byteArrayInputStream);
			
			if(document != null)
			{
				if(document.getDocumentElement() != null)
				{
					document.getDocumentElement().normalize();
				}				
			}
			
			return document;
		} catch (ConnectTimeoutException e) {
			LogController.log("HttpConnectionManager downloadXML ConnectTimeoutException " + e);
			throw makeGeneralException();
		}
		catch (ClientProtocolException e)
		{
			LogController.log("HttpConnectionManager downloadXML ClientProtocolException " + e);
			throw makeGeneralException();
		}
		catch (Exception e)
		{
			LogController.log("HttpConnectionManager downloadXML Exception " + e);
			e.printStackTrace();
			throw makeGeneralException();
		}
	}
	
	@Override
	public PList downloadPList(String url, String[] keys, String[] values, HTTP_CALLING_METHOD callingMethod) throws GeneralException {
		if(!NetworkConnective.checkNetwork(this.application))
		{
			throw this.makeGeneralNoNetworkGeneralException();
		}
		
		try {
			if(callingMethod != null)
			{
				if(callingMethod.equals(HTTP_CALLING_METHOD.POST))
				{
					byte[] byteArray = HttpUtil.downloadXMLFromPost(url, keys, values);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
					
					PListXMLParser parser = new PListXMLParser();
			        PListXMLHandler handler = new PListXMLHandler();
			        parser.setHandler(handler);
			        parser.parse(byteArrayInputStream);
			        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
			        
					return actualPList;
				}
				else if(callingMethod.equals(HTTP_CALLING_METHOD.GET))
				{
					byte[] byteArray = HttpUtil.downloadXMLFromGet(url, keys, values);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
					
					PListXMLParser parser = new PListXMLParser();
			        PListXMLHandler handler = new PListXMLHandler();
			        parser.setHandler(handler);
			        parser.parse(byteArrayInputStream);
			        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
			        
					return actualPList;
				}
			}
			
			byte[] byteArray = HttpUtil.downloadXMLFromPost(url, keys, values);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			
			PListXMLParser parser = new PListXMLParser();
	        PListXMLHandler handler = new PListXMLHandler();
	        parser.setHandler(handler);
	        parser.parse(byteArrayInputStream);
	        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
	        
			return actualPList;
		} catch (ConnectTimeoutException e) {
			LogController.log("HttpConnectionManager downloadPList ConnectTimeoutException " + e);
			throw makeGeneralException();
		}
		catch (ClientProtocolException e)
		{
			LogController.log("HttpConnectionManager downloadPList ClientProtocolException " + e);
			throw makeGeneralException();
		}
		catch (Exception e)
		{
			LogController.log("HttpConnectionManager downloadPList Exception " + e);
			e.printStackTrace();
			throw makeGeneralException();
		}
	}

	private GeneralException makeGeneralException() {
		return new GeneralException(Constants.GERENAL_STATUS_FAIL_LOCAL_FAIL, application.getResources().getString(R.string.no_network_connection));
	}

	private GeneralException makeGeneralNoNetworkGeneralException() {
		return new GeneralException(Constants.GERENAL_STATUS_FAIL_LOCAL_FAIL, application.getResources().getString(R.string.no_network_connection));
	}
}
