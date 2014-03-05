package com.fancl.iloyalty.item;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog {
	
	private ProgressDialog pDialog;
	private Context mContext;
	
	// Constructor
	
	public LoadingDialog(Context context) {
		super(context);
		this.mContext = context;
		this.pDialog = new ProgressDialog(this.mContext);
	}
	
	public void loading() {
		pDialog.setMessage("Loading");
    	pDialog.setCancelable(false);
    	pDialog.show();
	}
		
	public void stop() {
		if (pDialog.isShowing()) pDialog.dismiss();
	}
}
