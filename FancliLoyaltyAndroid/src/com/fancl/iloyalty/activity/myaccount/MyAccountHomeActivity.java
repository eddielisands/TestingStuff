package com.fancl.iloyalty.activity.myaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.CustomSpinnerActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.login.LoginForgetPasswordActivity;
import com.fancl.iloyalty.activity.login.SignUpFormActivity;
import com.fancl.iloyalty.adapter.MyAccountGPRewardViewAdapter;
import com.fancl.iloyalty.adapter.MyAccountPurchaseViewAdapter;
import com.fancl.iloyalty.asynctask.GetGPRewardAsyncTask;
import com.fancl.iloyalty.asynctask.GetGPRewardHistoryItemAsyncTask;
import com.fancl.iloyalty.asynctask.GetPurchaseHistoryAsyncTask;
import com.fancl.iloyalty.asynctask.GetUserProfileAsyncTask;
import com.fancl.iloyalty.asynctask.LoginAsyncTask;
import com.fancl.iloyalty.asynctask.callback.GetGPRewardAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.GetGPRewardHistoryItemAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.GetPurchaseHistoryAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.GetUserProfileAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.LoginAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.CustomTabBar;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.FormContent;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class MyAccountHomeActivity extends MainTabActivity implements CustomTabBarCallback ,GetGPRewardAsyncTaskCallback,GetPurchaseHistoryAsyncTaskCallback,GetUserProfileAsyncTaskCallback,LoginAsyncTaskCallback,GetGPRewardHistoryItemAsyncTaskCallback{


	private int currentTabBarIndex = 0;
	private CustomTabBar categoryTabBar;
	private RelativeLayout myAccountCateContentLayout;
	private RelativeLayout myAccountPurchaseLayout;
	private LinearLayout myAccountGPRewardLayout;
	private LinearLayout myAccountDetailLayout;

	private Boolean isCreatePurchase = false;
	private Boolean isCreateGPReward = false;
	private Boolean isCreateAccountDetail = false;

	private MyAccountPurchaseViewAdapter myAccountPurchaseViewAdapter;
	private MyAccountGPRewardViewAdapter myAccountGPRewardViewAdapter;

	private GPReward gpRewardItemList;
	private List<PurchaseHistory> purchaseHistoryList= new ArrayList<PurchaseHistory>();

	private List<FormContent> monthList = new ArrayList<FormContent>();
	private List<FormContent> genderList = new ArrayList<FormContent>();
	private List<FormContent> skinTypeList = new ArrayList<FormContent>();
	private List<FormContent> countryList = new ArrayList<FormContent>();

	private RelativeLayout genderArrow;
	private RelativeLayout skinTypeArrow;
	private RelativeLayout countryArrow;
	private RelativeLayout changePasswordArrow;

	private EditText cityEditText;
	private EditText address1EditText;
	private EditText address2EditText;
	private EditText address3EditText;
	private RelativeLayout myAccountGenderBtn;
	private RelativeLayout myAccountSkinTypeBtn;
	private RelativeLayout myAccountLivingCountryBtn;
	private TextView genderText;
	private TextView skinTypeText;
	private TextView countryText;
	private RelativeLayout changePasswordBtn;

	private int genderIndex= -1;
	private int skinIndex= -1;
	private int countryIndex= -1;

	private User userProfile = new User();

	private Boolean isEdit = false;
	
	private TextView noRecordText;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.2, 6.2.4

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		navigationBarLeftBtn.setVisibility(View.VISIBLE);

		headerTitleTextView.setText(this.getResources().getString(
				R.string.menu_my_account_btn_title));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);

	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		LinearLayout myAccountLayout = new LinearLayout(this);
		spaceLayout.addView(myAccountLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		myAccountLayout.setOrientation(LinearLayout.VERTICAL);

		// Tab Bar
		List<String> tabBarList = new ArrayList<String>();
		tabBarList.add(this.getResources().getString(
				R.string.my_account_record_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.my_account_gp_reward_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.my_account_detail_tab_bar_title));

		categoryTabBar = new CustomTabBar(this, currentTabBarIndex, tabBarList,
				DataUtil.dip2integerPx(this, 33), false, true,this);
		myAccountLayout.addView(categoryTabBar, LayoutParams.MATCH_PARENT,
				DataUtil.dip2integerPx(this, 33));

		myAccountCateContentLayout = new RelativeLayout(this);
		myAccountLayout.addView(myAccountCateContentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.createPurchaseView();

		GetPurchaseHistoryAsyncTask getPurchaseAsyncTask = new GetPurchaseHistoryAsyncTask(this);
		getPurchaseAsyncTask.execute();


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

		navigationBarRightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(isEdit){

					isEdit = false;

					genderArrow.setVisibility(View.GONE);
					skinTypeArrow.setVisibility(View.GONE);
					countryArrow.setVisibility(View.GONE);
					changePasswordArrow.setVisibility(View.GONE);

					cityEditText.setEnabled(false);
					address1EditText.setEnabled(false);
					address2EditText.setEnabled(false);
					address3EditText.setEnabled(false);

					myAccountGenderBtn.setEnabled(false);
					myAccountSkinTypeBtn.setEnabled(false);
					myAccountLivingCountryBtn.setEnabled(false);
					changePasswordBtn.setEnabled(false);

					navigationBarCancelBtn.setVisibility(View.GONE);

					//update user profile
					if (genderIndex != -1) {
						FormContent formContent = genderList.get(genderIndex);
						userProfile.setGender(formContent.getCode());
					}
					if (skinIndex != -1) {
						FormContent formContent = skinTypeList.get(skinIndex);
						userProfile.setSkinType(formContent.getCode());
					}
					if (countryIndex != -1) {
						FormContent formContent = countryList.get(countryIndex);
						userProfile.setCountry(formContent.getCode());
					}

					userProfile.setCity(cityEditText.getText().toString());
					userProfile.setAddress1(address1EditText.getText().toString());
					userProfile.setAddress2(address2EditText.getText().toString());
					userProfile.setAddress3(address3EditText.getText().toString());

					try {
						CustomServiceFactory.getAccountService().updateUserProfile(userProfile);
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					navigationBarRightTextView.setText("");
					navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "My Account Detail", "", "", "", "EditDone", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}else{

					AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountHomeActivity.this);
					builder.setTitle(R.string.my_account_edit_profile);
					builder.setMessage(R.string.my_account_please_enter_password);

					// Set up the input
					final EditText input = new EditText(MyAccountHomeActivity.this);
					// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
					input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					builder.setView(input);

					// Set up the buttons
					builder.setPositiveButton(R.string.my_account_forget_password, new DialogInterface.OnClickListener() { 
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MyAccountHomeActivity.this, LoginForgetPasswordActivity.class);
							startActivity(intent);

							dialog.cancel();
						}
					});
					builder.setNegativeButton(R.string.cancel_btn_title, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

					builder.setNeutralButton(R.string.ok_btn_title, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String currentMemberId = CustomServiceFactory.getAccountService().currentMemberId();

							LoginAsyncTask loginAsyncTask = new LoginAsyncTask(MyAccountHomeActivity.this);
							loginAsyncTask.execute(currentMemberId, input.getText().toString());

							MyAccountHomeActivity.this.runOnUiThread(new Runnable() {
								public void run() {
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
							});

							dialog.cancel();
						}

					});

					builder.show();

				}


			}
		});


	}

	@Override
	public void clickedIndex(CustomTabBar customTabBar, int index) {
		// TODO Auto-generated method stub
		noRecordText.setVisibility(View.GONE);
		
		if (customTabBar.equals(categoryTabBar)) {
			if (currentTabBarIndex == index) {
				return;
			}
			currentTabBarIndex = index;


			if(index == 0){
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "Purchase Record", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
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
					


				GetPurchaseHistoryAsyncTask getPurchaseAsyncTask = new GetPurchaseHistoryAsyncTask(this);
				getPurchaseAsyncTask.execute();

				navigationBarRightBtn.setVisibility(View.GONE);
				navigationBarCancelBtn.setVisibility(View.GONE);

				myAccountPurchaseLayout.setVisibility(View.VISIBLE);
				if(isCreateGPReward)
					myAccountGPRewardLayout.setVisibility(View.GONE);
				if(isCreateAccountDetail){
					myAccountDetailLayout.setVisibility(View.GONE);
				}

				if (genderArrow != null) {
					cancelEdit();
				}
			}else if (index == 1) {
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "GP Reward", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


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
				


				GetGPRewardAsyncTask getGPRewardAsyncTask = new GetGPRewardAsyncTask(this);
				getGPRewardAsyncTask.execute();

				navigationBarRightBtn.setVisibility(View.GONE);
				navigationBarCancelBtn.setVisibility(View.GONE);

				myAccountPurchaseLayout.setVisibility(View.GONE);
				if(isCreateAccountDetail)
					myAccountDetailLayout.setVisibility(View.GONE);
				if(isCreateGPReward)
					myAccountGPRewardLayout.setVisibility(View.VISIBLE);
				else
					this.createGPRewardView();

				if (genderArrow != null) {
					cancelEdit();
				}


			}else{
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "My Account Detail", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				
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
					


				GetUserProfileAsyncTask getUserAsyncTask = new GetUserProfileAsyncTask(this);
				getUserAsyncTask.execute();

				navigationBarRightBtn.setVisibility(View.VISIBLE);
				navigationBarRightTextView.setText("");
				navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit_2);

				myAccountPurchaseLayout.setVisibility(View.GONE);
				if(isCreateGPReward)
					myAccountGPRewardLayout.setVisibility(View.GONE);
				if(isCreateAccountDetail)
					myAccountDetailLayout.setVisibility(View.VISIBLE);
				else
					this.createAccountDetailView();

			}
		}




	}

	public void createPurchaseView() {

		isCreatePurchase = true;

		myAccountPurchaseLayout = new RelativeLayout(this);
		myAccountCateContentLayout.addView(myAccountPurchaseLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		ListView purchaseList = new ListView(this);
		purchaseList.setCacheColorHint(color.transparent);
		purchaseList.setDividerHeight(0);
		purchaseList.setScrollingCacheEnabled(false);
		myAccountPurchaseLayout.addView(purchaseList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		noRecordText = new TextView(this);
		noRecordText.setText(R.string.my_account_purchase_no_receipt_available);
		noRecordText.setTextColor(getResources().getColor(R.color.Fancl_Blue));
		noRecordText.setVisibility(View.GONE);
		noRecordText.setTextSize(16);
		noRecordText.setGravity(Gravity.CENTER_HORIZONTAL);
		myAccountPurchaseLayout.addView(noRecordText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		myAccountPurchaseViewAdapter = new MyAccountPurchaseViewAdapter(this, this, handler);
		purchaseList.setAdapter(myAccountPurchaseViewAdapter);

		purchaseList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if(purchaseHistoryList.get(arg2).getReceiptInd().equals("Y")){
					Intent intent = new Intent(MyAccountHomeActivity.this, MyAccountPurchaseRecordActivity.class);
					intent.putExtra(Constants.PURCHASE_ITEM_KEY, purchaseHistoryList.get(arg2));
					intent.putExtra(Constants.IRECEIPT_FROM_PUSH_ITEM_KEY, false);
					intent.putExtra(Constants.IRECEIPT_BONUS_POINT_RECORD_ITEM_KEY, false);
					startActivity(intent);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "Electronic Receipt", "", "", "", "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}


			}

		});
	}

	public void createGPRewardView() {

		isCreateGPReward = true;

		myAccountGPRewardLayout = new LinearLayout(this);
		myAccountCateContentLayout.addView(myAccountGPRewardLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		ListView gpRewardList = new ListView(this);
		gpRewardList.setCacheColorHint(color.transparent);
		gpRewardList.setDividerHeight(0);
		gpRewardList.setScrollingCacheEnabled(false);
		myAccountGPRewardLayout.addView(gpRewardList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		gpRewardList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.my_account_gp_reward_header, null));

		myAccountGPRewardViewAdapter = new MyAccountGPRewardViewAdapter(this, this, handler);
		gpRewardList.setAdapter(myAccountGPRewardViewAdapter);

		gpRewardList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if(arg2!=0){
					if(gpRewardItemList.getItemList().get(arg2-1).getReceiptInd().equals("Y") && gpRewardItemList.getItemList().get(arg2-1).getGiftInd().equals("N")){
						Intent intent = new Intent(MyAccountHomeActivity.this, MyAccountPurchaseRecordActivity.class);
						intent.putExtra(Constants.PURCHASE_ITEM_KEY, gpRewardItemList.getItemList().get(arg2-1));
						intent.putExtra(Constants.IRECEIPT_FROM_PUSH_ITEM_KEY, false);
						intent.putExtra(Constants.IRECEIPT_BONUS_POINT_RECORD_ITEM_KEY, true);
						startActivity(intent);
						
						try {
							CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "Electronic Receipt", "", "", "", "View", "");
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}



					}else if(gpRewardItemList.getItemList().get(arg2-1).getReceiptInd().equals("N") && gpRewardItemList.getItemList().get(arg2-1).getGiftInd().equals("Y")){
//						LogController.log("gpRewardItemList.getItemList:"+gpRewardItemList.getItemList());
						
						GetGPRewardHistoryItemAsyncTask gpHistoryItemAsyncTask = new GetGPRewardHistoryItemAsyncTask(MyAccountHomeActivity.this);
						gpHistoryItemAsyncTask.execute(gpRewardItemList.getItemList().get(arg2-1).getTransactionDatetime(), gpRewardItemList.getItemList().get(arg2-1).getSalesMemo(),gpRewardItemList.getItemList().get(arg2-1).getShopCode(),gpRewardItemList.getItemList().get(arg2-1).getItemCode());

						try {
							CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "GP Reward", "", "", "", "View", "");
						} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}

		});
	}

	public void createAccountDetailView() {

		isCreateAccountDetail = true;


		myAccountDetailLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.my_account_detail_page, null);
		myAccountDetailLayout.findViewById(R.layout.my_account_detail_page);
		myAccountCateContentLayout.addView(myAccountDetailLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		genderArrow = (RelativeLayout)findViewById(R.id.my_account_gender_arrow);
		genderArrow.setVisibility(View.GONE);
		skinTypeArrow = (RelativeLayout)findViewById(R.id.my_account_skin_type_arrow);
		skinTypeArrow.setVisibility(View.GONE);
		countryArrow = (RelativeLayout)findViewById(R.id.my_account_living_country_arrow);
		countryArrow.setVisibility(View.GONE);
		changePasswordArrow = (RelativeLayout)findViewById(R.id.my_account_change_password_arrow);
		changePasswordArrow.setVisibility(View.GONE);

		cityEditText = (EditText)findViewById(R.id.city_edit_text);
		cityEditText.setEnabled(false);
		address1EditText = (EditText)findViewById(R.id.address1_edit_text);
		address1EditText.setEnabled(false);
		address2EditText = (EditText)findViewById(R.id.address2_edit_text);
		address2EditText.setEnabled(false);
		address3EditText = (EditText)findViewById(R.id.address3_edit_text);
		address3EditText.setEnabled(false);

		genderText = (TextView)findViewById(R.id.my_account_gender_content);
		skinTypeText = (TextView)findViewById(R.id.my_account_skin_type_content);
		countryText = (TextView)findViewById(R.id.my_account_living_country_content);


		// Gender
		myAccountGenderBtn = (RelativeLayout)findViewById(R.id.sign_up_form_gender_button);
		myAccountGenderBtn.setEnabled(false);
		myAccountGenderBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyAccountHomeActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(genderList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_GENDER);
			}
		});

		// Skin Type
		myAccountSkinTypeBtn = (RelativeLayout)findViewById(R.id.sign_up_form_skin_type_button);
		myAccountSkinTypeBtn.setEnabled(false);
		myAccountSkinTypeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyAccountHomeActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(skinTypeList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_SKIN);
			}
		});

		// Living Country
		myAccountLivingCountryBtn = (RelativeLayout)findViewById(R.id.sign_up_form_living_country_button);
		myAccountLivingCountryBtn.setEnabled(false);
		myAccountLivingCountryBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyAccountHomeActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(countryList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_SIGN_UP_COUNTRY);
			}
		});


		changePasswordBtn = (RelativeLayout)findViewById(R.id.my_account_change_passward_btn);
		changePasswordBtn.setEnabled(false);
		changePasswordBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyAccountHomeActivity.this, MyAccountChangePasswordActivity.class);
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "Change Password Page", "", "", "", "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});


		navigationBarCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				cancelEdit();

				//				GetUserProfileAsyncTask getUserAsyncTask = new GetUserProfileAsyncTask(MyAccountHomeActivity.this);
				//				getUserAsyncTask.execute();
				//				
				//				genderArrow.setVisibility(View.GONE);
				//				skinTypeArrow.setVisibility(View.GONE);
				//				countryArrow.setVisibility(View.GONE);
				//				changePasswordArrow.setVisibility(View.GONE);
				//				
				//				cityEditText.setEnabled(false);
				//				address1EditText.setEnabled(false);
				//				address2EditText.setEnabled(false);
				//				address3EditText.setEnabled(false);
				//				
				//				myAccountGenderBtn.setEnabled(false);
				//				myAccountSkinTypeBtn.setEnabled(false);
				//				myAccountLivingCountryBtn.setEnabled(false);
				//				changePasswordBtn.setEnabled(false);
				//				
				//				navigationBarCancelBtn.setVisibility(View.GONE);
				//				
				//				isEdit = false;
			}
		});


		try {
			monthList = CustomServiceFactory.getAccountService().getFormContentWithType("month");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			genderList = CustomServiceFactory.getAccountService().getFormContentWithType("gender");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			skinTypeList = CustomServiceFactory.getAccountService().getFormContentWithType("skin");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			countryList = CustomServiceFactory.getAccountService().getFormContentWithType("country");
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		if (results == null) {
			noRecordText.setVisibility(View.VISIBLE);
			return;
		}

		if(currentTabBarIndex == 0){
			purchaseHistoryList = (List<PurchaseHistory>) results;

			if (purchaseHistoryList == null) {
				return;
			}

			myAccountPurchaseViewAdapter.setArticleList(purchaseHistoryList);
			
			if(purchaseHistoryList.size()==0)
				noRecordText.setVisibility(View.VISIBLE);
			

		}else if(currentTabBarIndex == 1){
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


	}

	@Override
	public void onPostExecuteCallback(User results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		if (results == null) {
			return;
		}

		LogController.log("user profile result:"+results);

		userProfile = results;

		TextView nameText = (TextView)findViewById(R.id.my_account_detail_member);
		nameText.setText(results.getLastName()+" "+results.getFirstName());
		TextView memberIdText = (TextView)findViewById(R.id.my_account_detail_member_id);
		memberIdText.setText(results.getFanclMemberId());
		TextView membershipGradeText = (TextView)findViewById(R.id.my_account_detail_membership_grade);
		membershipGradeText.setText(results.getVipGradeName());
		TextView currentGiftPointText = (TextView)findViewById(R.id.my_account_detail_current_point);
		currentGiftPointText.setText(results.getGpBalance());
		TextView currentDiscountPointText = (TextView)findViewById(R.id.my_account_detail_current_discount_point);
		currentDiscountPointText.setText(results.getDpBalance());
		TextView pointExpiryDateText = (TextView)findViewById(R.id.my_account_detail_expiry_date);
		pointExpiryDateText.setText(results.getExpiryDate());

		//table info
		TextView firstNameText = (TextView)findViewById(R.id.name_edit_text);
		firstNameText.setText(results.getFirstName());
		TextView lastNameText = (TextView)findViewById(R.id.surname_edit_text);
		lastNameText.setText(results.getLastName());
		TextView emailAddressText = (TextView)findViewById(R.id.email_edit_text);
		emailAddressText.setText(results.getEmail());
		TextView reEmailAddressText = (TextView)findViewById(R.id.retype_email_edit_text);
		reEmailAddressText.setText(results.getEmail());
		TextView mobileText = (TextView)findViewById(R.id.mobile_edit_text);
		mobileText.setText(results.getMobile());
		TextView monthBirthText = (TextView)findViewById(R.id.month_birth_edit_text);
		monthBirthText.setText(results.getMonthOfBirth());
		TextView YearBirthText = (TextView)findViewById(R.id.year_birth_edit_text);
		YearBirthText.setText(results.getYearOfBirth());


		for (int i = 0; i < genderList.size(); i++) {
			if(genderList.get(i).getCode().equals(results.getGender())){
				genderText.setText(genderList.get(i).getTitleEn());
			}
		}

		for (int i = 0; i < skinTypeList.size(); i++) {
			if(skinTypeList.get(i).getCode().equals(results.getSkinType())){
				skinTypeText.setText(skinTypeList.get(i).getTitleEn());
			}
		}

		for (int i = 0; i < countryList.size(); i++) {
			if(countryList.get(i).getCode().equals(results.getCountry())){
				countryText.setText(countryList.get(i).getTitleEn());
			}
		}


		cityEditText.setText(results.getCity());
		address1EditText.setText(results.getAddress1());
		address2EditText.setText(results.getAddress2());
		address3EditText.setText(results.getAddress3());

	}

	private String[] formContentToStringArray(List<FormContent> formContentList) {
		LogController.log("formContentToStringArray");
		LogController.log("formContentList.size() = " + formContentList.size());

		String[] currentLangContent = new String[formContentList.size()];
		LocaleService localeService = GeneralServiceFactory.getLocaleService();

		for (int i = 0; i < formContentList.size(); i++) {
			FormContent formContent = formContentList.get(i);
			currentLangContent[i] = localeService.textByLangaugeChooser(MyAccountHomeActivity.this, formContent.getTitleEn(), formContent.getTitleZh(), formContent.getTitleSc());
			LogController.log("currentLangContent[i] = " + currentLangContent[i]);
		}

		return currentLangContent;		
	}

	private String formContentToString(FormContent formContent) {
		String returnString = "";
		LocaleService localeService = GeneralServiceFactory.getLocaleService();
		returnString = localeService.textByLangaugeChooser(MyAccountHomeActivity.this, formContent.getTitleEn(), formContent.getTitleZh(), formContent.getTitleSc());
		return returnString;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 


		case (Constants.CUSTOM_SPINNER_SIGN_UP_GENDER) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.

				genderIndex = newInt;
				genderText.setText(formContentToString(genderList.get(newInt)));
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_SKIN) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.

				skinIndex = newInt;
				skinTypeText.setText(formContentToString(skinTypeList.get(newInt)));
			} 
			break; 
		}
		case (Constants.CUSTOM_SPINNER_SIGN_UP_COUNTRY) : { 
			if (resultCode == SignUpFormActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.

				countryIndex = newInt;
				countryText.setText(formContentToString(countryList.get(newInt)));
			} 
			break; 
		}
		}
	}

	@Override
	public void onPostExecuteCallback(FanclGeneralResult results) {
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		if (results == null) {
			return;
		}

		if (results.getStatus() == 0) {
			LogController.log("Status : 0 is success");

			isEdit = true;

			genderArrow.setVisibility(View.VISIBLE);
			skinTypeArrow.setVisibility(View.VISIBLE);
			countryArrow.setVisibility(View.VISIBLE);
			changePasswordArrow.setVisibility(View.VISIBLE);

			cityEditText.setEnabled(true);
			address1EditText.setEnabled(true);
			address2EditText.setEnabled(true);
			address3EditText.setEnabled(true);

			myAccountGenderBtn.setEnabled(true);
			myAccountSkinTypeBtn.setEnabled(true);
			myAccountLivingCountryBtn.setEnabled(true);
			changePasswordBtn.setEnabled(true);

			navigationBarCancelBtn.setVisibility(View.VISIBLE);

			navigationBarRightTextView.setText(getResources().getString(R.string.done_btn_title));
			navigationBarRightBtn.setBackgroundResource(R.drawable.btn_edit);


			 try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("My Account", "My Account Detail", "", "", "", "EditBegin", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


		} else if (results.getStatus() == 1) {
			LogController.log("Status : 1 is fail");

			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, results.getErrMsgEn(), results.getErrMsgZh(), results.getErrMsgSc()),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					dialog.cancel();
				}
			},
			"", null, false, false);
		}

	}

	@Override
	public void onPostExecuteCallback(GPRewardHistoryItem results) {
		// TODO Auto-generated method stub
		if (loadingDialog != null) {
			loadingDialog.stop();
		}

		if (results == null) {
			return;
		}

		startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(results, MyAccountHomeActivity.this, false, "GP Reward", 4));

	}

	public void cancelEdit(){
		GetUserProfileAsyncTask getUserAsyncTask = new GetUserProfileAsyncTask(MyAccountHomeActivity.this);
		getUserAsyncTask.execute();

		genderArrow.setVisibility(View.GONE);
		skinTypeArrow.setVisibility(View.GONE);
		countryArrow.setVisibility(View.GONE);
		changePasswordArrow.setVisibility(View.GONE);

		cityEditText.setEnabled(false);
		address1EditText.setEnabled(false);
		address2EditText.setEnabled(false);
		address3EditText.setEnabled(false);

		myAccountGenderBtn.setEnabled(false);
		myAccountSkinTypeBtn.setEnabled(false);
		myAccountLivingCountryBtn.setEnabled(false);
		changePasswordBtn.setEnabled(false);

		navigationBarCancelBtn.setVisibility(View.GONE);

		isEdit = false;

	}



}
