package com.gt.snssharinglibrary.service.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.service.SNSServiceCallback;

public class SNSServiceSuperImpl {
	protected SNSServiceCallback snsServiceCallback;

	protected boolean stopShowDialog = false;
	
	protected void showMessageDialog(final Activity activity, final String title, final String message)
	{
		if(!stopShowDialog)
		{
			stopShowDialog = false;
			String positiveBtnLabel = StringMapping.GENERAL_DIALOG_POSITIVE_BUTTON_LABEL;
			makeNativeDialog(activity, title, message, positiveBtnLabel, null, null, null, true, false);
		}
	}

	private void makeNativeDialog(final Activity activity, final String title, final String message, final String positiveBtnLabel, final DialogInterface.OnClickListener positiveBtnListener, final String negativeBtnLabel, final DialogInterface.OnClickListener negativeBtnListener, final boolean cancelable, final boolean isError)
	{
		if(activity != null)
		{
			if(!activity.isFinishing())
			{
				activity.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						AlertDialog.Builder alert = new AlertDialog.Builder(activity);
						
						alert.setCancelable(cancelable);
						
						//set title
						if(title != null)
						{
							alert.setTitle(title);
						}
						
						//set message
						String showMessage = "";
						if(message != null)
						{
							showMessage = message;
						}
						
						alert.setMessage(showMessage);
						
						//set icon
						if(isError)
						{
							alert.setIcon(android.R.drawable.ic_dialog_alert);
						}
						else
						{
							alert.setIcon(android.R.drawable.ic_dialog_info);
						}
						
						// set button listener
						if(positiveBtnLabel != null)
						{
							alert.setPositiveButton(positiveBtnLabel, positiveBtnListener);
						}
						
						if(negativeBtnLabel != null)
						{
							alert.setNegativeButton(negativeBtnLabel, negativeBtnListener);
						}
						
						alert.show();
					}
				});
			}
		}
	}
}
