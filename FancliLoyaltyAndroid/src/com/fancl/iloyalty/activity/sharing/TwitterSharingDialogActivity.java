package com.fancl.iloyalty.activity.sharing;

import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.AndroidProjectFrameworkActivity;
import com.fancl.iloyalty.asynctask.EarnCreditAsyncTask;
import com.fancl.iloyalty.asynctask.callback.EarnCreditAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.DetailContent;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.impl.TwitterServiceImpl;

public class TwitterSharingDialogActivity extends AndroidProjectFrameworkActivity implements SNSServiceCallback, EarnCreditAsyncTaskCallback {
	private final int MAX_COUNT = 140;
	
	private boolean fromProduct;
	private Product product;
	private DetailContent detailContent;
	private String sharingTitle;
	
	private LocaleService localeService;
	private SNSService twitterServiceImpl;
	
	private Button cancelButton;
	private Button shareButton;
	private TextView remainCounterTextView;
	private EditText shareEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Config.TWITTER_CONSUMER_KEY = "jvKys4Lr2afHg7WxvQ4M1g";
		Config.TWITTER_CONSUMER_SECRET = "Z9icVCBu2j1Da1O2mMEiggkBcXgmfk2OjAE7qvrV0A";
		Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.btn_cross;
		twitterServiceImpl = new TwitterServiceImpl(TwitterSharingDialogActivity.this);
		twitterServiceImpl.setSNSServiceCallback(TwitterSharingDialogActivity.this);
		
		localeService = GeneralServiceFactory.getLocaleService();
		
		if (getIntent().getStringExtra(Constants.SHARING_FORMAT_KEY).equals("product")) {
			fromProduct = true;
			product = (Product) getIntent().getSerializableExtra(Constants.SHARING_CONTENT_KEY);
			sharingTitle = localeService.textByLangaugeChooser(TwitterSharingDialogActivity.this, 
					product.getTitleEn(), product.getTitleZh(), product.getTitleSc());
		}
		else {
			fromProduct = false;
			detailContent = (DetailContent) getIntent().getSerializableExtra(Constants.SHARING_CONTENT_KEY);
			sharingTitle = detailContent.getTitleStr();
		}

		setContentView(R.layout.twitter_sharing_dialog);

		this.setupSpaceLayout();
	}

	private void setupSpaceLayout() {
		cancelButton = (Button) findViewById(R.id.tw_cancel_btn);
		cancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelAction();
			}
		});
		shareButton = (Button) findViewById(R.id.tw_share_btn);
		shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareAction();
			}
		});
		remainCounterTextView = (TextView) findViewById(R.id.tw_remain_text);
		shareEditText = (EditText) findViewById(R.id.tw_share_input);
		shareEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				remainCounterTextView.setText(String.valueOf((MAX_COUNT - shareEditText.getText().toString().length())));
			}
		});
		shareEditText.setText(sharingTitle);
		remainCounterTextView.setText(String.valueOf((MAX_COUNT - shareEditText.getText().toString().length())));
	}
	
	private void cancelAction() {
		finish();
	}
	
	private void shareAction() {
		if (shareEditText.getText().toString().length() > MAX_COUNT) {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					"Over 140",
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
			return;
		}
		
		String message = shareEditText.getText().toString();
		
		SNSShareDetail snsShareDetail = new SNSShareDetail(message);
		twitterServiceImpl.post(TwitterSharingDialogActivity.this, snsShareDetail);
	}

	@Override
	public void logginStatus(int snsCode, boolean isSuccessLogin,
			Object errorObject) {
		LogController.log("logginStatus >> "+ snsCode + " " + isSuccessLogin);
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> "+ snsCode + " " + isSuccessGetProfile);
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		LogController.log("loggoutStatus >> "+ snsCode + " " + isSuccessLogout);
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  "+ snsCode + " " + isSuccessPost);
		if (isSuccessPost) {
			String eventId = null;
			try {
				List<Event> events;
				if (fromProduct) {
					events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(product.getObjectId());
				}
				else {
					events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(detailContent.getDetailId());
				}
				for (int i = 0; i < events.size(); i++) {
					Event event = events.get(i);
					if (event.getEventType().equals("share")) {
						eventId = event.getEventId();
						break;
					}
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (eventId != null) {
				EarnCreditAsyncTask earnCreditAsyncTask = new EarnCreditAsyncTask(this);
				earnCreditAsyncTask.execute(eventId);
				return;
			}
			finish();
		}
	}

	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (results instanceof String) {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					("eraned credit: " + ((String)results)),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
		else {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, ((FanclGeneralResult) results).getErrMsgEn(), ((FanclGeneralResult) results).getErrMsgZh(), ((FanclGeneralResult) results).getErrMsgSc()),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
		finish();
	}
	
}
