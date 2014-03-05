package com.gt.snssharinglibrary.pojo;

import com.gt.snssharinglibrary.Config;

import android.app.ProgressDialog;
import android.content.Context;

public class CusProgressDialog {

	private ProgressDialog progressDialog;
	
	public CusProgressDialog(Context context)
	{
		if(Config.SHOW_LOADING_DIALOG)
		{
			progressDialog = new ProgressDialog(context);
		}
	}
	
	public void setMessage(String message)
	{
		if(progressDialog != null)
		{
			progressDialog.setMessage(message);
		}
	}
	
	public void show()
	{
		if(progressDialog != null)
		{
			progressDialog.show();
		}
	}
	
	public void dismiss()
	{
		if(progressDialog != null)
		{
			try
			{
				progressDialog.dismiss();
			}
			catch (Exception e) {
				
			}
		}
	}
	
}
