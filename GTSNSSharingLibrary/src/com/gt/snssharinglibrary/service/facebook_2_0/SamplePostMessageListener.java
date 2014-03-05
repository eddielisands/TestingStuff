package com.gt.snssharinglibrary.service.facebook_2_0;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook_2_0.android.AsyncFacebookRunner;
import com.facebook_2_0.android.FacebookError;
import com.gt.snssharinglibrary.util.LogController;

public class SamplePostMessageListener implements AsyncFacebookRunner.RequestListener {

	@Override
	public void onComplete(String response, Object state) {
		LogController.log("SamplePostMessageListener >>> onComplete " + response);
	}
	@Override
	public void onIOException(IOException e, Object state) {
		LogController.log("SamplePostMessageListener >>> onIOException " + e.toString());
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		LogController.log("SamplePostMessageListener >>> onFileNotFoundException " + e.toString());
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		LogController.log("SamplePostMessageListener >>> onMalformedURLException " + e.toString());
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		LogController.log("SamplePostMessageListener >>> onFacebookError " + e.toString());
	}

	// private SamplePostMessageCallback samplePostMessageCallback;
	//
	// public SamplePostMessageListener(SamplePostMessageCallback
	// samplePostMessageCallback)
	// {
	// this.samplePostMessageCallback = samplePostMessageCallback;
	// }
	//
	// @Override
	// public void onComplete(String response, Object state) {
	// LogController.log("SamplePostMessageListener >>> onComplete " +
	// response);
	// try
	// {
	// JSONObject json = Util.parseJson(response);
	// final String id = json.getString("id");
	//
	// LogController.log("id " + id);
	//
	// if(!K11StringUtil.isStringEmpty(id))
	// {
	// if(samplePostMessageCallback != null)
	// {
	// LogController.log("TEST - samplePostMessageCallback");
	// samplePostMessageCallback.postMessageResult(true, state);
	// return;
	// }
	// }
	// }
	// catch(JSONException e)
	// {
	//
	// }
	// catch(FacebookError e)
	// {
	//
	// }
	//
	// if(samplePostMessageCallback != null)
	// {
	// samplePostMessageCallback.postMessageResult(false, state);
	// return;
	// }
	// }
	//
	// @Override
	// public void onIOException(IOException e, Object state) {
	// LogController.log("SampleUploadListener >>> onIOException");
	// if(samplePostMessageCallback != null)
	// {
	// samplePostMessageCallback.postMessageResult(false, state);
	// }
	// }
	//
	// @Override
	// public void onFileNotFoundException(FileNotFoundException e, Object
	// state) {
	// LogController.log("SampleUploadListener >>> onFileNotFoundException");
	// if(samplePostMessageCallback != null)
	// {
	// samplePostMessageCallback.postMessageResult(false, state);
	// }
	// }
	//
	// @Override
	// public void onMalformedURLException(MalformedURLException e, Object
	// state) {
	// LogController.log("SampleUploadListener >>> onMalformedURLException");
	// if(samplePostMessageCallback != null)
	// {
	// samplePostMessageCallback.postMessageResult(false, state);
	// }
	// }
	//
	// @Override
	// public void onFacebookError(FacebookError e, Object state) {
	// if(samplePostMessageCallback != null)
	// {
	// samplePostMessageCallback.postMessageResult(false, state);
	// }
	// }

}
