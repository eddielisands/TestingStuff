package com.fancl.iloyalty.item;

import android.app.ProgressDialog;
import android.content.Context;

public class FanclProgressDialog extends ProgressDialog{

	public FanclProgressDialog(Context context) {
		super(context);
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();	
		}
		catch (Exception e) {
			
		}
	}
}
