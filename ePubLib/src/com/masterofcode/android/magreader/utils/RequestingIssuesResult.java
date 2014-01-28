package com.masterofcode.android.magreader.utils;

import org.apache.http.HttpResponse;
import org.json.JSONArray;

public class RequestingIssuesResult {
	private JSONArray 	jsonResult;
	private boolean		isAuthValid;
	private boolean		isIOError;
	private HttpResponse httpResponse;
	
	public JSONArray getJsonResult()
	{
		return jsonResult;
	}
	
	public boolean isAuthValid()
	{
		return isAuthValid;
	}
	
	public boolean isIOError()
	{
		return isIOError;
	}
	
	public void setJsonResult(JSONArray jsonResult)
	{
		this.jsonResult = jsonResult;
	}
	
	public void setAuthValid(boolean isAuthValid)
	{
		this.isAuthValid = isAuthValid;
	}
	
	public void setIOError(boolean isIOError)
	{
		this.isIOError = isIOError;
	}
	
	public HttpResponse getHttpResponse()
	{
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse httpResponse)
	{
		this.httpResponse = httpResponse;
	}

	public RequestingIssuesResult(JSONArray jsonResult, boolean isAuthValid, boolean isIOError, HttpResponse response)
	{
		super();
		this.jsonResult = jsonResult;
		this.isAuthValid = isAuthValid;
		this.isIOError = isIOError;
		this.httpResponse = response;
	}
}
