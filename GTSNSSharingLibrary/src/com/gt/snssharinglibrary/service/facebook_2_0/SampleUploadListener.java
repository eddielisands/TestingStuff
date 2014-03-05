package com.gt.snssharinglibrary.service.facebook_2_0;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook_2_0.android.AsyncFacebookRunner;
import com.facebook_2_0.android.FacebookError;
import com.facebook_2_0.android.Util;
import com.gt.snssharinglibrary.util.LogController;
import com.gt.snssharinglibrary.util.StringUtil;

public class SampleUploadListener implements AsyncFacebookRunner.RequestListener{

	private SampleUploadCallback sampleUploadCallback;
	
	public SampleUploadListener(SampleUploadCallback sampleUploadCallback)
	{
		this.sampleUploadCallback = sampleUploadCallback;
	}
	
	public void setSampleUploadCallback(SampleUploadCallback sampleUploadCallback)
	{
		this.sampleUploadCallback = sampleUploadCallback;
	}
	
	@Override
	public void onComplete(String response, Object state) {
		LogController.log("SampleUploadListener >>> onComplete " + response);
		try
		{
			JSONObject json = Util.parseJson(response);
			final String src = json.getString("src");
			
			LogController.log("src " + src);
			
			if(!StringUtil.isStringEmpty(src))
			{	
				if(sampleUploadCallback != null)
				{
					sampleUploadCallback.uploadResult(true, state);
					return;
				}
			}
		}
		catch(JSONException e)
		{
			
		}
		catch(FacebookError e)
		{
			
		}
		
		if(sampleUploadCallback != null)
		{
			sampleUploadCallback.uploadResult(false, state);
			return;
		}
	}

	@Override
	public void onIOException(IOException e, Object state) {
		LogController.log("SampleUploadListener >>> onIOException");
		if(sampleUploadCallback != null)
		{
			sampleUploadCallback.uploadResult(false, state);
		}
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		LogController.log("SampleUploadListener >>> onFileNotFoundException");
		if(sampleUploadCallback != null)
		{
			sampleUploadCallback.uploadResult(false, state);
		}
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		LogController.log("SampleUploadListener >>> onMalformedURLException");
		if(sampleUploadCallback != null)
		{
			sampleUploadCallback.uploadResult(false, state);
		}
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		if(sampleUploadCallback != null)
		{
			sampleUploadCallback.uploadResult(false, state);
		}
	}

}
