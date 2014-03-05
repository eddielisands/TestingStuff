package com.fancl.iloyalty.activity.purchase;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainNoTabActivity;
import com.fancl.iloyalty.asynctask.ILoyaltyTPCSocketCancelAsyncTask;
import com.fancl.iloyalty.asynctask.ILoyaltyTPCSocketConnectAsyncTask;
import com.fancl.iloyalty.asynctask.ILoyaltyTPCSocketDisconnectAsyncTask;
import com.fancl.iloyalty.asynctask.ILoyaltyTPCSocketTillIdAsyncTask;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.pojo.TillId;
import com.fancl.iloyalty.service.ILoyaltyTCPSocketService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.callback.ILoyaltyTCPSocketServiceCallback;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.NetworkConnective;

public class PurchasePOSResponseActivity extends MainNoTabActivity implements ILoyaltyTCPSocketServiceCallback {
	private String scannedCode;
	private ILoyaltyTCPSocketService iLoyaltyTCPSocketService;
	protected AndroidProjectApplication application;
	private boolean isOpenThreadPool = false;
	private Timer timer;
	private int failCounts = 0;

	private View waitingLayout;
	private View failLayout;
	private View tryAgainFailLayout;
	private View successLayout;

	private RelativeLayout successButton;
	private RelativeLayout tryAgainButton;
	private RelativeLayout securityCheckButton;
	private RelativeLayout cancelButton;

	private ExecutorService executorService;

	private ILoyaltyTPCSocketConnectAsyncTask connectAsyncTask;
	private ILoyaltyTPCSocketTillIdAsyncTask tillIdAsyncTask;
	private ILoyaltyTPCSocketCancelAsyncTask cancelAsyncTask;
	private ILoyaltyTPCSocketDisconnectAsyncTask disconnectAsyncTask;

	private Shop shop;
	private List<TillId> tillList;

	private TextView waitingText;

	private LocaleService localeService;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 4.1

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		localeService = GeneralServiceFactory.getLocaleService();

		scannedCode = getIntent().getStringExtra(Constants.POS_CODE_KEY);

		try {
			tillList = CustomServiceFactory.getPurchaseService().getStoreDetail(scannedCode);
		} catch (FanclException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(tillList.size()>0){
			try {
				shop = CustomServiceFactory.getAboutFanclService().getShopDetailWithCode(tillList.get(0).getStoreCode());
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (android.os.Build.VERSION.SDK_INT >= 11) {
			isOpenThreadPool = true;
			executorService = Executors.newFixedThreadPool(5);
		}
		else {
			isOpenThreadPool = false;
		}
		iLoyaltyTCPSocketService = CustomServiceFactory.getILoyaltyTCPSocketService();
		iLoyaltyTCPSocketService.addCallbackListener(this);

		headerTitleTextView.setText(this.getResources().getString(R.string.purchase_title));

		this.setupSpaceLayout();

		navigationBarLeftBtn.setVisibility(View.VISIBLE);

		connectAsyncTask = new ILoyaltyTPCSocketConnectAsyncTask();
		if (isOpenThreadPool) {
			connectAsyncTask.executeOnExecutor(executorService);
		}
		else {
			connectAsyncTask.execute();
		}

		connectToPOS();
	}

	@Override
	protected void onDestroy () 
	{
		LogController.log("PurchasePOSResponseActivity on onDestroy");
//		timer.cancel();
//		timer.purge();
		timer = null;

		iLoyaltyTCPSocketService.removeCallbackListener(this);

		if (tillIdAsyncTask != null) {
			tillIdAsyncTask.cancel(true);
		}
		if (connectAsyncTask != null) {
			connectAsyncTask.cancel(true);
		}
		if (cancelAsyncTask != null) {
			cancelAsyncTask.cancel(false);
		}
		if (disconnectAsyncTask != null) {
			disconnectAsyncTask.cancel(false);
		}

		if (isOpenThreadPool) {
			executorService.shutdown();
		}

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				disconnectAsyncTask = new ILoyaltyTPCSocketDisconnectAsyncTask();
				if (isOpenThreadPool) {
					disconnectAsyncTask.executeOnExecutor(executorService);
				}
				else {
					disconnectAsyncTask.execute();
				}
			}
		}, 500);

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				callFinish();
			}
		}, 1500);
	}

	private void callFinish() {
		finish();
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		waitingLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_waiting_response_page, null);
		failLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_fail_to_connect_to_pos_page, null);
		tryAgainFailLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_try_again_fail_page, null);
		successLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_connect_to_pos_successfully_page, null);

		waitingText =(TextView)waitingLayout.findViewById(R.id.connect_waiting_text);
		String waitingTitle = getResources().getString(R.string.purchase_waiting_response_title);
		String waitingBraanch = getResources().getString(R.string.purchase_waiting_response_shop_title);
		if(shop != null){
			waitingText.setText(waitingTitle + localeService.textByLangaugeChooser(PurchasePOSResponseActivity.this, shop.getTitleEn(), shop.getTitleZh(), shop.getTitleSc()) + " " + waitingBraanch);
		}else{
			waitingText.setText(waitingTitle + tillList.get(0).getStoreName() + " " + waitingBraanch);
		}

		cancelButton = (RelativeLayout) waitingLayout.findViewById(R.id.cancel_btn);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelAsyncTask = new ILoyaltyTPCSocketCancelAsyncTask();
				if (isOpenThreadPool) {
					cancelAsyncTask.executeOnExecutor(executorService);
				}
				else {
					cancelAsyncTask.execute();
				}
				onBackPressed();
			}
		});
		securityCheckButton = (RelativeLayout) tryAgainFailLayout.findViewById(R.id.security_check_btn);
		securityCheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				failCounts = 0;
				Intent intent = new Intent(PurchasePOSResponseActivity.this, PurchasePOSCodeEnterActivity.class);
				startActivity(intent);
				onBackPressed();
			}
		});
		tryAgainButton = (RelativeLayout) failLayout.findViewById(R.id.try_again_btn);
		tryAgainButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				connectToPOS();
				spaceLayout.removeAllViews();
				spaceLayout.addView(waitingLayout, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
		});

		TextView successText =(TextView)successLayout.findViewById(R.id.connect_success_text);
		String successTitle = getResources().getString(R.string.purchase_connect_to_pos_successfully_title);
		String successConfirm = getResources().getString(R.string.purchase_connect_to_pos_successfully_shop_title);
		if(shop != null){
			successText.setText(successTitle + " " + localeService.textByLangaugeChooser(PurchasePOSResponseActivity.this, shop.getTitleEn(), shop.getTitleZh(), shop.getTitleSc()) + successConfirm);
		}else{
			successText.setText(successTitle + " " + tillList.get(0).getStoreName() + successConfirm);
		}
		successButton = (RelativeLayout) successLayout.findViewById(R.id.connect_success_btn);
		successButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		spaceLayout.addView(waitingLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void connectToPOS() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tillIdAsyncTask = new ILoyaltyTPCSocketTillIdAsyncTask();
				if (isOpenThreadPool) {
					tillIdAsyncTask.executeOnExecutor(executorService, scannedCode);
				}
				else {
					tillIdAsyncTask.execute(scannedCode);
				}
			}
		}, 500);

		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						connectionFailed();
					}
				});
			}
		};
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		timer = new Timer();
		if (NetworkConnective.checkNetwork(this)) {
			timer.schedule(timerTask, 60000);
		}
		else {
			timer.schedule(timerTask, 2000);
		}
	}

	private void connectionSuccess() {
		// to success view
		timer.cancel();
		timer.purge();
		timer = null;
		
		//cancel pos

		spaceLayout.removeAllViews();
		spaceLayout.addView(successLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void connectionFailed() {
		//cancel pos 
		failCounts += 1;
		if (failCounts > 2) {
			// to manual connect view
			spaceLayout.removeAllViews();
			spaceLayout.addView(tryAgainFailLayout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			LogController.log("connection fail-need enter code");
		}
		else {
			// to try again view

			spaceLayout.removeAllViews();
			spaceLayout.addView(failLayout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			LogController.log("connection fail");
		}
	}

	@Override
	public void didReceiveTCPSocketResult(String string) {
		// TODO Auto-generated method stub
		LogController.log("didReceiveTCPSocketResult: " + string);
	}

	@Override
	public void didReceiveTCPSocketIsSuccess(final Boolean isSuccess) {
		// TODO Auto-generated method stub
		LogController.log("didReceiveTCPSocketIsSuccess: " + isSuccess);

		handler.post(new Runnable() {

			@Override
			public void run() {

				if(isSuccess){
					connectionSuccess();
				}else{
					connectionFailed();
				}
			}

		});

	}
}