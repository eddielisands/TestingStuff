package com.gt.snssharinglibrary.service;

import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;

import android.app.Activity;
import android.app.AlertDialog;

public abstract class SNSServiceImpl implements SNSService{
	
	public enum SNS_LOGIN_TYPE
	{
		LOGIN_TYPE_NORMAL_LOGIN,
		LOGIN_TYPE_LOGIN_AND_POST
	}
	
	protected SNSShareDetail snsShareDetail;
	protected SNS_LOGIN_TYPE currentLoginType;
	
	protected void showMessageDialog(final Activity activity, final String title, final String message)
	{
		if(activity != null)
		{
			if(!activity.isFinishing())
			{
				activity.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						AlertDialog.Builder alert = new AlertDialog.Builder(activity);
						
						if(title != null)
						{
							alert.setTitle(title);
						}
						
						if(message != null)
						{
							alert.setMessage(message);
						}
						
						alert.setPositiveButton(StringMapping.GENERAL_DIALOG_POSITIVE_BUTTON_LABEL, null);
						alert.show();
					}
				});
			}
		}
	}
	
}
