package com.fancl.iloyalty.service;

import android.app.Activity;
import android.content.DialogInterface;

public interface AlertDialogService {
	public void makeNativeDialog(final Activity activity, final String title, final String message, final String positiveBtnLabel, final DialogInterface.OnClickListener positiveBtnListener, final String negativeBtnLabel, final DialogInterface.OnClickListener negativeBtnListener, final boolean cancelable, final boolean isError);
}
