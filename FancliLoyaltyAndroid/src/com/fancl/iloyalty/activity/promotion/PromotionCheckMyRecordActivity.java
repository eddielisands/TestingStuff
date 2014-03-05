package com.fancl.iloyalty.activity.promotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.myaccount.MyAccountHomeActivity;
import com.fancl.iloyalty.activity.myaccount.MyAccountPurchaseRecordActivity;
import com.fancl.iloyalty.adapter.MyAccountGPRewardViewAdapter;
import com.fancl.iloyalty.asynctask.GetGPRewardAsyncTask;
import com.fancl.iloyalty.asynctask.GetGPRewardHistoryItemAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetGPRewardAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.GetGPRewardHistoryItemAsyncTaskCallback;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.item.LoadingDialog;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;


public class PromotionCheckMyRecordActivity extends MainTabActivity implements GetGPRewardAsyncTaskCallback,GetGPRewardHistoryItemAsyncTaskCallback{
	
	private MyAccountGPRewardViewAdapter myAccountGPRewardViewAdapter;
	private GPReward gpRewardItemList;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        navigationBarLeftBtn.setVisibility(View.VISIBLE);
	        
	        headerTitleTextView.setText(this.getResources().getString(R.string.promotion_btn));
	        
	        this.setupSpaceLayout();
	        
	        this.setupMenuButtonListener(1, true);
	        
	    }

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		
		LinearLayout checkMyRecordLayout = new LinearLayout(this);
		spaceLayout.addView(checkMyRecordLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		checkMyRecordLayout.setOrientation(LinearLayout.VERTICAL);

		ListView gpRewardList = new ListView(this);
		gpRewardList.setCacheColorHint(color.transparent);
		gpRewardList.setDividerHeight(0);
		gpRewardList.setScrollingCacheEnabled(false);
		checkMyRecordLayout.addView(gpRewardList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		gpRewardList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.my_account_gp_reward_header, null));

		myAccountGPRewardViewAdapter = new MyAccountGPRewardViewAdapter(this, this, handler);
		gpRewardList.setAdapter(myAccountGPRewardViewAdapter);
		
		GetGPRewardAsyncTask getGPRewardAsyncTask = new GetGPRewardAsyncTask(this);
		getGPRewardAsyncTask.execute();
		
		this.runOnUiThread(new Runnable() {
			  public void run() {
				  if (checkLoadingDialog()) {
						loadingDialog.loading();
				}
			  }
			});
		
		gpRewardList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	
            	if(arg2!=0){
            		if(gpRewardItemList.getItemList().get(arg2-1).getReceiptInd().equals("Y") && gpRewardItemList.getItemList().get(arg2-1).getGiftInd().equals("N")){
            			Intent intent = new Intent(PromotionCheckMyRecordActivity.this, MyAccountPurchaseRecordActivity.class);
                		intent.putExtra(Constants.PURCHASE_ITEM_KEY, gpRewardItemList.getItemList().get(arg2-1));
                		intent.putExtra(Constants.IRECEIPT_FROM_PUSH_ITEM_KEY, false);
                		intent.putExtra(Constants.IRECEIPT_BONUS_POINT_RECORD_ITEM_KEY, true);
                		startActivity(intent);
  
            			
            		}else if(gpRewardItemList.getItemList().get(arg2-1).getReceiptInd().equals("N") && gpRewardItemList.getItemList().get(arg2-1).getGiftInd().equals("Y")){
            			GetGPRewardHistoryItemAsyncTask gpHistoryItemAsyncTask = new GetGPRewardHistoryItemAsyncTask(PromotionCheckMyRecordActivity.this);
            			gpHistoryItemAsyncTask.execute(gpRewardItemList.getItemList().get(arg2-1).getTransactionDatetime(), gpRewardItemList.getItemList().get(arg2-1).getSalesMemo(),gpRewardItemList.getItemList().get(arg2-1).getShopCode(),gpRewardItemList.getItemList().get(arg2-1).getItemCode());

            		}
            		
            	}

            }

        });
		
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		
		if (loadingDialog != null) {
			loadingDialog.stop();
		}
		
		gpRewardItemList = (GPReward) results;
		
		TextView nameText = (TextView)findViewById(R.id.account_name);
		nameText.setText(gpRewardItemList.getName());
		TextView memberIdText = (TextView)findViewById(R.id.member_id);
		memberIdText.setText(gpRewardItemList.getFanclMemberId());
		TextView membershipGradeText = (TextView)findViewById(R.id.membership_grade);
		membershipGradeText.setText(gpRewardItemList.getVipGradeName());
		TextView currentGiftPointText = (TextView)findViewById(R.id.current_point);
		currentGiftPointText.setText(gpRewardItemList.getGpBalance());
		TextView pointExpiryDateText = (TextView)findViewById(R.id.expiry_date);
		pointExpiryDateText.setText(gpRewardItemList.getExpireDate());
		
		myAccountGPRewardViewAdapter.setArticleList(gpRewardItemList.getItemList());
		
	}

	@Override
	public void onPostExecuteCallback(GPRewardHistoryItem results) {
		// TODO Auto-generated method stub
		
		if (results == null) {
			return;
		}
		
		startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(results, PromotionCheckMyRecordActivity.this, false, "GP Reward", 4));
		
	}



}
