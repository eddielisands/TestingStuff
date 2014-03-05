package com.gt.snssharinglibrary.service.facebook_2_0;

import java.util.Set;

import android.os.Bundle;

import com.facebook_2_0.android.DialogError;
import com.facebook_2_0.android.Facebook.DialogListener;
import com.facebook_2_0.android.FacebookError;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.util.LogController;

public class PostFeedListener implements DialogListener {

	private SNSService snsService;

	public PostFeedListener(SNSService snsService)
	{
		this.snsService = snsService;
	}

	public void onComplete(Bundle values) {

		LogController.log("PostFeedListener onComplete");

		Set<String> keys = values.keySet();

		if (keys.size() > 0)
		{
			if (snsService != null)
			{
				snsService.postStatus(true, values);
			}
		}
	}

	public void onFacebookError(FacebookError error) {
		LogController.log("PostFeedListener onFacebookError");
		if (snsService != null)
		{
			snsService.postStatus(false, error);
		}
	}

	public void onError(DialogError error) {
		LogController.log("PostFeedListener onError");
		if (snsService != null)
		{
			snsService.postStatus(false, error);
		}
	}

	public void onCancel() {
		LogController.log("PostFeedListener onCancel");
		// no need to call if it is onCancel
	}
}
