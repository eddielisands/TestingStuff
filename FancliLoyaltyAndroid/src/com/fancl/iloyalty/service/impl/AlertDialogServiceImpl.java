package com.fancl.iloyalty.service.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.fancl.iloyalty.service.AlertDialogService;

public class AlertDialogServiceImpl implements AlertDialogService{
	
	@Override
	public void makeNativeDialog(final Activity activity, final String title, final String message, final String positiveBtnLabel, final DialogInterface.OnClickListener positiveBtnListener, final String negativeBtnLabel, final DialogInterface.OnClickListener negativeBtnListener, final boolean cancelable, final boolean isError)
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
