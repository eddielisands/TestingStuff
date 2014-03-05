package com.fancl.iloyalty.service;

import org.w3c.dom.Document;

import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.exception.GeneralException;
import com.longevitysoft.android.xml.plist.domain.PList;

public interface HttpConnectionService {
	public Document downloadXML(String url, String[] keys, String[] values, HTTP_CALLING_METHOD callingMethod) throws GeneralException;
	public PList downloadPList(String url, String[] keys, String[] values, HTTP_CALLING_METHOD callingMethod) throws GeneralException;
}
