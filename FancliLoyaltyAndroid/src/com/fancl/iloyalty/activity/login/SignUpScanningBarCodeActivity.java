package com.fancl.iloyalty.activity.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.util.LogController;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

public class SignUpScanningBarCodeActivity extends CaptureActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		        
	}
	
	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {
		super.handleDecode(rawResult, barcode);
		
		LogController.log("SignUpScanningBarCodeActivity handleDecode : " + rawResult.getText());
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra(Constants.SIGN_UP_BAR_CODE_RETURN_KEY, rawResult.getText());
		setResult(SignUpScanningBarCodeActivity.RESULT_OK, resultIntent);
		finish();
	}
}
