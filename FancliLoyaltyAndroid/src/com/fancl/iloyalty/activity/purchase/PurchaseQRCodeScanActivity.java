package com.fancl.iloyalty.activity.purchase;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.CustomSpinnerActivity;
import com.fancl.iloyalty.activity.message.MessageHomeActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.ZxingRotationTextView;
import com.fancl.iloyalty.item.ZxingRotationTextView.AlignType;
import com.fancl.iloyalty.pojo.TillId;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

public class PurchaseQRCodeScanActivity extends CaptureActivity implements ActivityMonitorServiceCallback {
	protected AndroidProjectApplication application;
	protected Handler handler;
	protected ActivityMonitorService activityMonitorService;
	private List<TillId> tillList;
	
	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 4.3, 4.4, 4.6, 4.7

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		application = (AndroidProjectApplication)this.getApplication();
        handler = new Handler();
        
        activityMonitorService = GeneralServiceFactory.getActivityMonitorService();
        activityMonitorService.addCallbackListener(this);
        
        application.addActiveActivity(this);

		View view = this.getLayoutInflater().inflate(R.layout.qr_code_overlay_view, null);
		RelativeLayout rotatedHeaderLayout = (RelativeLayout) view.findViewById(R.id.rotated_header_layout);
		RelativeLayout leftBtnLayout = (RelativeLayout) view.findViewById(R.id.rotated_left_btn_layout);
		RelativeLayout rightBtnLayout = (RelativeLayout) view.findViewById(R.id.rotated_right_btn_layout);
		RelativeLayout maskLayout = (RelativeLayout) view.findViewById(R.id.mask_layout);

		ZxingRotationTextView headerTextView = new ZxingRotationTextView(this, AlignType.MIDDLE);
		headerTextView.setRototeDegree(270);
		headerTextView.setTextSize(18);

		RelativeLayout.LayoutParams headerLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rotatedHeaderLayout.addView(headerTextView, headerLayoutParams);

		headerTextView.setText(getResources().getString(R.string.purchase_title));
		leftBtnLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		rightBtnLayout.setVisibility(View.VISIBLE);
		rightBtnLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PurchaseQRCodeScanActivity.this, PurchasePOSCodeEnterActivity.class);
//				Intent intent = new Intent(PurchaseQRCodeScanActivity.this, PurchasePOSResponseActivity.class);
				startActivity(intent);
				finish();
			}
		});

		ZxingRotationTextView hintTextView = new ZxingRotationTextView(this, AlignType.MIDDLE);
		hintTextView.setRototeDegree(270);
		hintTextView.setTextSize(15);

		RelativeLayout.LayoutParams hintLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		hintLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		hintLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		hintLayoutParams.setMargins(DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10), DataUtil.dip2integerPx(this, 10));
		maskLayout.addView(hintTextView, hintLayoutParams);
		hintTextView.setText(getResources().getString(R.string.go_to_pos));

		setupOverlayView(view);
	}
	
	@Override
    protected void onResume () 
    {
		LogController.log("PurchaseQRCodeScanActivity on resume");
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnResumed(this);
    	}
    	
    	super.onResume();
    }
	
	@Override
    protected void onDestroy () 
    {
		LogController.log("PurchaseQRCodeScanActivity on onDestroy");
    	activityMonitorService.removeCallbackListener(this);
		application.removeActiveActivity(this);
    	super.onDestroy();
    }
	
	@Override
    protected void onStop () 
    {
		LogController.log("PurchaseQRCodeScanActivity on onStop");
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnStopped(this);
    	}
    	
    	super.onStop();
    }

	@Override
	public void handleDecode(Result rawResult, Bitmap barcode) {
		super.handleDecode(rawResult, barcode);

		LogController.log("PurchaseQRCodeScanActivity handleDecode : " + rawResult.getText());
		
		try {
			tillList = CustomServiceFactory.getPurchaseService().getStoreDetail(rawResult.getText());
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(tillList.size()>0){
			Intent intent = new Intent(PurchaseQRCodeScanActivity.this, PurchasePOSResponseActivity.class);
			intent.putExtra(Constants.POS_CODE_KEY, rawResult.getText());
			startActivity(intent);
		}else{
			Intent resultIntent = new Intent();
			setResult(PurchaseQRCodeScanActivity.RESULT_OK, resultIntent);

		}
		finish();
	}
	
	@Override
	public void applicationGoingToBackground() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applicationGoingToForeground() {
		// TODO Auto-generated method stub
		
	}

}
