package com.masterofcode.android.magreader;

import java.util.ArrayList;
import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.masterofcode.android.magreader.db.entity.FeedType;
import com.masterofcode.android.magreader.utils.ApplicationUtils;
import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class SettingsActivity extends Activity {
	private static SharedPreferences prefs =null;
	private static String[] valuesTimeArray;
	private static TextView updateInterval;
	private TextView googleAccountName;
	private static SettingsActivity activity;
	private TextView subscriptionAccountName;
	
	private boolean		callSettingsFeeds = false; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		if(savedInstanceState==null)
		{
			Intent intent = this.getIntent();
			callSettingsFeeds = intent.getBooleanExtra(Constants.BUNDLE_KEY_SETTINGS_SELECT_FEEDS, false);
		} else {
			callSettingsFeeds = savedInstanceState.getBoolean(Constants.BUNDLE_KEY_SETTINGS_SELECT_FEEDS, false);
		}
		
		if(!Constants.SUBSCRIPTION_ENABLED)
		{
			RelativeLayout		settingsSubscriptionLayout = (RelativeLayout) findViewById(R.id.settings_subscription_account);
			settingsSubscriptionLayout.setVisibility(View.GONE);
		}
		
		updateStaticVariables();
		updateClickListeners();
		updateSubscriptionInfo();
		
		if(callSettingsFeeds)selectFeedsDirectly();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(Constants.BUNDLE_KEY_SETTINGS_SELECT_FEEDS, callSettingsFeeds);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constants.REUQEST_CODE_SUBSCRIPTION_ACTIVITY)
		{
			updateSubscriptionInfo();
			
			if(resultCode==RESULT_OK)
			{
				if(data!=null)
				{
					boolean		isLogged = data.getBooleanExtra(Constants.BUNDLE_KEY_SUBSCRIPTION_IS_LOGGED_IN, false);
					
					if(isLogged)
					{
						String		title = getString(R.string.subscription_dialog_title_logged_successfully);
						DialogFragment newFragment = SignInSuccessDialogFragment.newInstance(title);
						newFragment.show(getFragmentManager(), "signin_success_dialog");
					}
				}
			}
		}
	}

	private void updateStaticVariables() {
		googleAccountName = (TextView) findViewById(R.id.account_name);
		updateInterval = (TextView) findViewById(R.id.time_name);
		valuesTimeArray = getResources().getStringArray(R.array.time_to_update_in_ms);
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		activity = this;
		subscriptionAccountName = (TextView) findViewById(R.id.subscription_account_name);
	}
	
	private void updateSubscriptionInfo()
	{
		String subscriptionName = ApplicationUtils.getPrefPropertyString(this, Constants.PREFERENCE_SUBSCRIPTION_NAME, null);
		
		if(ApplicationUtils.getPrefPropertyBoolean(this, Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false) && subscriptionName!=null)
		{
			String subscriptionFormat = getResources().getString(R.string.settings_menu_subscription_current_account_template);
			subscriptionAccountName.setText(String.format(subscriptionFormat, subscriptionName));
		} else {
			subscriptionAccountName.setText(R.string.settings_menu_subscription_current_account_placeholder);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("noInetException", "SettingActivity -> onResume...start");
		checkGoogleAccount();
		Log.d("noInetException", "SettingActivity -> onResume...checkGoogleAccount()");
		updateTimeIntervalName();
		Log.d("noInetException", "SettingActivity -> onResume...updateTimeIntervalName()");
		updateCountOfManageFeeds();
		Log.d("noInetException", "SettingActivity -> onResume...updateCountOfManageFeeds()");
	}
	
	public static void updateCountOfManageFeeds() {
		ArrayList<FeedType> manageFeeds = ((JtjApplication) activity.getApplication()).getManageFeeds();
		int i = 0;
		for (FeedType manageFeed:manageFeeds) {
			if (!manageFeed.is_hide) {
				i++;
			}
		}
		String subtext = String.valueOf(i) + "/" + String.valueOf(manageFeeds.size()) + " feeds selected";
		TextView feedsSubtext = (TextView) activity.findViewById(R.id.feeds_name);
		feedsSubtext.setText(subtext);
	}
	
	public String getGoogleAccount(){
	    AccountManager manager = AccountManager.get(this); 
	    Account[] accounts = manager.getAccountsByType("com.google");
	    if (accounts.length == 0) {
	    	return null;
	    } else {
	    	return accounts[0].name;
    	}
	}
	
	public void updateClickListeners() {
		RelativeLayout timeUpdate = (RelativeLayout) findViewById(R.id.settings_time_interval);
		timeUpdate.setOnClickListener(timeUpdateClickListener);
		RelativeLayout mGoogleAccount = (RelativeLayout) findViewById(R.id.settings_account);
		mGoogleAccount.setOnClickListener(setGoogleAccountListener);
		RelativeLayout manageFeeds = (RelativeLayout) findViewById(R.id.settings_feeds);
		manageFeeds.setOnClickListener(settingsFeedsOnClickListener);
		RelativeLayout subscriptionManagmentLayout = (RelativeLayout) findViewById(R.id.settings_subscription_account);
		subscriptionManagmentLayout.setOnClickListener(subscriptionManagementOnClickListener);
}

	
	public void checkGoogleAccount(){
		if (!TextUtils.isEmpty(getGoogleAccount())){
			googleAccountName.setText(getGoogleAccount());
	    } else {
	    	googleAccountName.setText("No Account");
	    }
	}
	
	public static void updateTimeIntervalName() {
		long updateTime = prefs.getLong(Constants.PREFERENCE_UPDATE_FEEDS_TIME, Constants.DEFAULT_TIME_TO_UPDATE);
		if ((updateTime / Constants.DEFAULT_TIME_TO_UPDATE) < 1) {
			updateInterval.setText(String.valueOf(updateTime/60000) + " min");
		} else {
			updateInterval.setText(String.valueOf(updateTime/Constants.DEFAULT_TIME_TO_UPDATE) + " hour");
		}
	}
	
	OnClickListener timeUpdateClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int selectedItem = prefs.getInt(Constants.PREFERENCE_SELECTED_ITEM_TIME, Constants.DEFAULT_SELECTED_ITEM);
			DialogFragment newFragment = TimeUpdateDialogFragment.newInstance(R.string.update_interval, selectedItem);
		    newFragment.show(getFragmentManager(), "update_interval_dialog");
		}
	};
	
	OnClickListener settingsFeedsOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			selectFeeds();
		}
	};
	
	private void selectFeeds()
	{
		DialogFragment newFragment = FeedsManagmentSettingsDialog.newInstance(false);
		newFragment.show(getFragmentManager(), "feeds_manage");
	}
	
	OnClickListener subscriptionManagementOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showSubscriptionManagmentDialog();
		}
	};
	
	private void showSubscriptionManagmentDialog()
	{
		DialogFragment newFragment = SubscriptionManagmentDialogFragment.newInstance(ApplicationUtils.getPrefPropertyBoolean(this, Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false));
		newFragment.show(getFragmentManager(), "subscription_manage");
	}
	
	private void selectFeedsDirectly()
	{
		DialogFragment newFragment = FeedsManagmentSettingsDialog.newInstance(callSettingsFeeds);
		newFragment.show(getFragmentManager(), "feeds_manage");
	}
	
	OnClickListener setGoogleAccountListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(android.provider.Settings.ACTION_SYNC_SETTINGS);
			SettingsActivity.this.startActivity(intent);
		}
	};
	
	public static void updateTimeInterval(int selectedItem) {
		
		prefs.edit().putLong(Constants.PREFERENCE_UPDATE_FEEDS_TIME, Long.parseLong(valuesTimeArray[selectedItem])).commit();
		prefs.edit().putInt(Constants.PREFERENCE_SELECTED_ITEM_TIME, selectedItem).commit();
		updateTimeIntervalName();
	}

	public static class SubscriptionManagmentDialogFragment extends DialogFragment
	{
		public static SubscriptionManagmentDialogFragment newInstance(boolean isLogged)
		{
			SubscriptionManagmentDialogFragment frag = new SubscriptionManagmentDialogFragment();
			Bundle args = new Bundle();
			args.putBoolean(Constants.BUNDLE_KEY_SUBSCRIPTION_IS_LOGGED_IN, isLogged);
            frag.setArguments(args);
            return frag;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// prepare sub-menu
			boolean 			isLogged = getArguments().getBoolean(Constants.BUNDLE_KEY_SUBSCRIPTION_IS_LOGGED_IN);
			String  			items[] = getResources().getStringArray(R.array.subscription_account_settings_list);
			
			if(isLogged)
			{
				items = ApplicationUtils.removeItemFromStringsArray(items, getResources().getString(R.string.subscription_account_action_login));
			} else {
				items = ApplicationUtils.removeItemFromStringsArray(items, getResources().getString(R.string.subscription_account_action_logout));
			}

			final String			finalItems[] = items;
			
			return new AlertDialog.Builder(getActivity())
						.setTitle(R.string.settings_submenu_subscription_account_actions)
						.setItems(items, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								String selection = finalItems[which];
								SettingsActivity	settings = (SettingsActivity)getActivity(); 
								
								if(selection.equals(getResources().getString(R.string.subscription_account_action_login)))
								{
									settings.subscriptionAccountActionSelect(SubscriptionActivity.SUBSCRIPTION_ACTION_LOGIN);									
								} else 
									if(selection.equals(getResources().getString(R.string.subscription_account_action_logout)))
									{
										settings.subscriptionAccountActionSelect(SubscriptionActivity.SUBSCRIPTION_ACTION_LOGOUT);									
									} else
										if(selection.equals(getResources().getString(R.string.subscription_account_action_request_name)))
										{
											settings.subscriptionAccountActionSelect(SubscriptionActivity.SUBSCRIPTION_ACTION_REQUEST_NAME);									
										} else
											if(selection.equals(getResources().getString(R.string.subscription_account_action_request_password)))
											{
												settings.subscriptionAccountActionSelect(SubscriptionActivity.SUBSCRIPTION_ACTION_REQUEST_PASSWORD);									
											} else
												if(selection.equals(getResources().getString(R.string.subscription_account_action_register_new_account)))
												{
													settings.subscriptionAccountActionSelect(SubscriptionActivity.SUBSCRIPTION_ACTION_REGISTER);									
												}
							}
						})
						.create();
		}
	}
	
	public void subscriptionAccountActionSelect(int actionId)
	{
		if(actionId==SubscriptionActivity.SUBSCRIPTION_ACTION_LOGOUT)
		{
			ApplicationUtils.setPrefProperty(this, Constants.PREFERENCE_SUBSCRIPTION_ENABLED, false);
			updateSubscriptionInfo();
		} else {
			Intent			intent = new Intent(SettingsActivity.this, SubscriptionActivity.class);

			intent.putExtra(Constants.BUNDLE_KEY_SUBSCRIPTION_ACTION, actionId);
			startActivityForResult(intent, Constants.REUQEST_CODE_SUBSCRIPTION_ACTIVITY);
		}
	}
	
	public static class TimeUpdateDialogFragment extends DialogFragment {
		
		int selectedItem = -1;
		
		public static TimeUpdateDialogFragment newInstance(int title, int selectedItem) {
			TimeUpdateDialogFragment frag = new TimeUpdateDialogFragment();
			Bundle args = new Bundle();
            args.putInt("title", title);
            args.putInt("selected", selectedItem);
            frag.setArguments(args);
            return frag;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = getArguments().getInt("title");
            final int index = getArguments().getInt("selected");
            selectedItem = index;
			
			return new AlertDialog.Builder(getActivity())
						.setTitle(title)
						.setSingleChoiceItems(R.array.time_to_update, index, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedItem = which;
							}
						})
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (selectedItem != index) {
									SettingsActivity.updateTimeInterval(selectedItem);
								}
							}
						})
						.create();
		}
	}
	
	public static class FeedsManagmentSettingsDialog extends DialogFragment {
		
		boolean[] checkedItems;
		int numberOfFeeds;
		
		public static FeedsManagmentSettingsDialog newInstance(boolean closeAfterProcessing) {
			FeedsManagmentSettingsDialog frag = new FeedsManagmentSettingsDialog();
			Bundle args = new Bundle();
            args.putBoolean("closeAfterProcessing", closeAfterProcessing);
            frag.setArguments(args);
			return frag;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final boolean closeAfterProcessing = getArguments().getBoolean("closeAfterProcessing", false);
			
			final ArrayList<FeedType> manageFeeds = ((JtjApplication) getActivity().getApplication()).getManageFeeds();
			numberOfFeeds = manageFeeds.size();
			String[] feedsArray = new String[numberOfFeeds];
			boolean[] isChecked = new boolean[numberOfFeeds];
			int i = 0;
			for (FeedType manageFeed:manageFeeds) {
				feedsArray[i] = manageFeed.title;
				isChecked[i] = !manageFeed.is_hide;
				i++;
			}
			checkedItems = isChecked;
			
			AlertDialog		dialog = 
					new AlertDialog.Builder(getActivity())
					.setTitle(R.string.manage_feeds)
					.setMultiChoiceItems(feedsArray, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedItems[which] = isChecked;
						}
					})
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(ApplicationUtils.isOnline(activity)){
								new CheckIssuesAvail().execute();
							} else {
					        	ApplicationUtils.createNoInternetDialog(activity).show();
					        }
						}
					})
					.setCancelable(true)
					.create();
			
			dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode ==  KeyEvent.KEYCODE_BACK)
					{
						dialog.cancel();
						if(closeAfterProcessing)((SettingsActivity)getActivity()).finish();
						return true;
					}
					return false;
				}
			});
			return dialog;
		}
		
		private class CheckIssuesAvail extends AsyncTask<Void, Void, Boolean> {

			@Override
			protected Boolean doInBackground(Void...params) {
				// TODO Auto-generated method stub
				
				return ApplicationUtils.checkIssueIsAvail();
			}
			
			@Override
			protected void onPostExecute(Boolean issuesIsAvailable) {
				if (issuesIsAvailable){
					final ArrayList<FeedType> manageFeeds = ((JtjApplication) activity.getApplication()).getManageFeeds();
					if(((JtjApplication) activity.getApplication()).getManageFeeds() != null && ((JtjApplication) activity.getApplication()).getManageFeeds().size() > 0){
						updateFeeds(((JtjApplication) activity.getApplication()).getManageFeeds());
	    				activity.finish();
					}
				} else {
					ApplicationUtils.createNoInternetDialog(activity).show();
				}
			}
			
		}
		
		public void updateFeeds(ArrayList<FeedType> manageFeeds) {
			Log.d("noInetException", "in updateFeeds...");
			Intent intent = new Intent();
			ArrayList<Integer> feedsToUpdateList = new ArrayList<Integer>();
			ActiveRecordBase _db = ((JtjApplication) activity.getApplication()).getDatabase();
			for (int i=0; i<numberOfFeeds; i++) {
				if (!manageFeeds.get(i).is_hide != checkedItems[i]) {
					if (checkedItems[i]) feedsToUpdateList.add(manageFeeds.get(i).order_id);
					manageFeeds.get(i).is_hide = !checkedItems[i];
					((JtjApplication) activity.getApplication()).updateSelectedFeed(i, !checkedItems[i]);
					try {
						_db.open();
						List<FeedType> ft =  _db.find(FeedType.class, "TITLE=?", new String[] { manageFeeds.get(i).title });
						ft.get(0).is_hide = !checkedItems[i];
						ft.get(0).update();
					} catch (ActiveRecordException e) {
						e.printStackTrace();
					}
					_db.close();
					updateCountOfManageFeeds();
				}
			}
			intent.putExtra(Constants.FEEDS_TO_UPDATE, feedsToUpdateList);
			if (feedsToUpdateList.size() != 0){
				Log.d("noInetException", "before reloading service...");
				MainActivity.getInstance().reloadService(intent);
				Log.d("noInetException", "after reloading service...");
				//activity.setResult(RESULT_OK, intent);
			}
		}
	}
	
	public static class SignInSuccessDialogFragment extends DialogFragment {
		private final static String			TITLE_KEY = "title_key";

        public static SignInSuccessDialogFragment newInstance(String title) {
        	SignInSuccessDialogFragment frag = new SignInSuccessDialogFragment();
            Bundle args = new Bundle();
            args.putString(TITLE_KEY, title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString(TITLE_KEY);
            
            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                    )
                    .create();
        }
    }
}
