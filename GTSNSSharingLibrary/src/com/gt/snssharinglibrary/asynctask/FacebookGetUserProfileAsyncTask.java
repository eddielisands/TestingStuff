package com.gt.snssharinglibrary.asynctask;

import org.json.JSONObject;

import com.facebook_2_0.android.Facebook;
import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.pojo.CusProgressDialog;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.facebook_2_0.FacebookSessionStore;
import com.gt.snssharinglibrary.util.LogController;

import android.content.Context;
import android.os.AsyncTask;

public class FacebookGetUserProfileAsyncTask extends AsyncTask<Void, Void, Boolean>{

	private Context context;
	private SNSService snsService;
	private Facebook facebook;
	
	private CusProgressDialog cusProgressDialog;
	
	public FacebookGetUserProfileAsyncTask(Context context, SNSService snsService, Facebook facebook)
	{
		this.context = context;
		this.snsService = snsService;
		this.facebook = facebook;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
		
		cusProgressDialog = new CusProgressDialog(context);
		cusProgressDialog.setMessage(StringMapping.FACEBOOK_LOGIN_LOADING_MESSAGE);
		cusProgressDialog.show();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String result = null;
		
		try
		{
			result = facebook.request("me");
			LogController.log("getProfile result >>> " + result);
			if (result != null)
			{
				JSONObject profileJSON = new JSONObject(result);

				String id = profileJSON.getString("id");

				String name = "";
				String email = "";

				try
				{
					name = profileJSON.getString("name");
					email = profileJSON.getString("email");
				}
				catch (Exception e)
				{
					result = null;
				}

				FacebookSessionStore.saveFacebookInfo(name, id, email, context);
			}
		}
		catch (Exception e)
		{
			result = null;
		}

		boolean success = false;

		if (result != null)
		{
			success = true;
		}

		return Boolean.valueOf(success);
	}
	
	@Override
	protected void onPostExecute (Boolean isSuccess) {
		super.onPostExecute(isSuccess);
		//process of thread ended(UI Thread)
		
		if(snsService != null)
		{
			snsService.getProfileStatus(context, isSuccess, null);
		}
		
		if(cusProgressDialog != null)
		{
			cusProgressDialog.dismiss();
		}
	}
}
