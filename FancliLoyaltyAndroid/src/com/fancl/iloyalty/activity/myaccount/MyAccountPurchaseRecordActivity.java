package com.fancl.iloyalty.activity.myaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.asynctask.GetPurchaseHistoryReceiptAsyncTask;
import com.fancl.iloyalty.asynctask.GetUserProfileAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetPurchaseHistoryReceiptAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.FormContent;
import com.fancl.iloyalty.pojo.GPRewardItem;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.pojo.PurchaseHistoryReceipt;
import com.fancl.iloyalty.util.LogController;

public class MyAccountPurchaseRecordActivity extends MainTabActivity implements GetPurchaseHistoryReceiptAsyncTaskCallback{
	
	private View receiptLayout;
	private PurchaseHistory purchaseHistoryList;
	private GPRewardItem gpHostoryList;
	private List<PurchaseHistoryReceipt> purchaseHistory = new ArrayList<PurchaseHistoryReceipt>();
	private Boolean fromPush;
	private Boolean bonusPointRecord;


	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.2.5(Receipt)
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(R.string.my_account_electronic_receipt));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);
        
    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		receiptLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.my_account_ireceipt_page, null);
		spaceLayout.addView(receiptLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		Intent intent = this.getIntent();
		
		
		fromPush = intent.getBooleanExtra(Constants.IRECEIPT_FROM_PUSH_ITEM_KEY, false);
		bonusPointRecord = intent.getBooleanExtra(Constants.IRECEIPT_BONUS_POINT_RECORD_ITEM_KEY, false);
		
		if(fromPush==false && bonusPointRecord ==false){
			purchaseHistoryList = (PurchaseHistory) intent.getExtras().getSerializable(Constants.PURCHASE_ITEM_KEY);
		}else if(fromPush==false && bonusPointRecord ==true){
			gpHostoryList = (GPRewardItem) intent.getExtras().getSerializable(Constants.PURCHASE_ITEM_KEY);
		}
		
		if(fromPush){
			LogController.log("fromPush-Date:"+purchaseHistoryList.getPurchaseDate());
			GetPurchaseHistoryReceiptAsyncTask ireceiptAsyncTask = new GetPurchaseHistoryReceiptAsyncTask(this);
			ireceiptAsyncTask.execute(purchaseHistoryList.getPurchaseDate(), purchaseHistoryList.getSalesMemo(), purchaseHistoryList.getShopCode());
			
			if (checkLoadingDialog()) {
				loadingDialog.loading();
				
				final Timer t = new Timer();
				t.schedule(new TimerTask() {
					public void run() {
						if (loadingDialog != null) {
							loadingDialog.stop();
						}
						// when the task active then close the dialog
						t.cancel(); 
					}
				}, Constants.LOADING_DIALOG_TIMEOUT); 
			}
		}else{
			if(bonusPointRecord){
				LogController.log("notfromPush & bonusPointRecord-Date:"+gpHostoryList.getTransactionDatetime());
				GetPurchaseHistoryReceiptAsyncTask ireceiptAsyncTask = new GetPurchaseHistoryReceiptAsyncTask(this);
				ireceiptAsyncTask.execute(gpHostoryList.getTransactionDatetime(), gpHostoryList.getSalesMemo(), gpHostoryList.getShopCode());
				
				if (checkLoadingDialog()) {
					loadingDialog.loading();
					
					final Timer t = new Timer();
					t.schedule(new TimerTask() {
						public void run() {
							if (loadingDialog != null) {
								loadingDialog.stop();
							}
							// when the task active then close the dialog
							t.cancel(); 
						}
					}, Constants.LOADING_DIALOG_TIMEOUT); 
				}
			}else{
				LogController.log("notfromPush & notbonusPointRecord-Date:"+purchaseHistoryList.getPurchaseDatetime());
				GetPurchaseHistoryReceiptAsyncTask ireceiptAsyncTask = new GetPurchaseHistoryReceiptAsyncTask(this);
				ireceiptAsyncTask.execute(purchaseHistoryList.getPurchaseDatetime(), purchaseHistoryList.getSalesMemo(), purchaseHistoryList.getShopCode());
				
				if (checkLoadingDialog()) {
					loadingDialog.loading();
					
					final Timer t = new Timer();
					t.schedule(new TimerTask() {
						public void run() {
							if (loadingDialog != null) {
								loadingDialog.stop();
							}
							// when the task active then close the dialog
							t.cancel(); 
						}
					}, Constants.LOADING_DIALOG_TIMEOUT); 
				}
			}
		}
		

	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		purchaseHistory = (List<PurchaseHistoryReceipt>) results;
		
		LogController.log("iReceipt results:"+results);
		
		LinearLayout ireceiptView = (LinearLayout)findViewById(R.id.ireceipt_layout);
		
		if(purchaseHistory != null){
			for (int i = 0; i < purchaseHistory.size(); i++) {
			TextView item = new TextView(this);
			item.setText(purchaseHistory.get(i).getLineData());
			item.setTextColor(getResources().getColor(R.color.Black));
			item.setTextSize(8.6f);
			if (purchaseHistory.get(i).getLineAlign().equals("L")) {
				item.setGravity(Gravity.LEFT);
			}else if(purchaseHistory.get(i).getLineAlign().equals("C")){
				item.setGravity(Gravity.CENTER);
			}else{
				item.setGravity(Gravity.RIGHT);
			}
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(30, 0, 0, 0);
				
			ireceiptView.addView(item, layoutParams);

		}
			
		}


		
	}
	

	
}
