package com.fancl.iloyalty.activity.purchase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainNoTabActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.service.LocaleService;
import com.gt.snssharinglibrary.util.LogController;

public class PurchasePOSCodeEnterActivity extends MainNoTabActivity {
	private LocaleService localeService;

	private View posInputLayout;

	private TextView digit1TextView;
	private TextView digit2TextView;
	private TextView digit3TextView;
	private TextView digit4TextView;

	private RelativeLayout numberButton0;
	private RelativeLayout numberButton1;
	private RelativeLayout numberButton2;
	private RelativeLayout numberButton3;
	private RelativeLayout numberButton4;
	private RelativeLayout numberButton5;
	private RelativeLayout numberButton6;
	private RelativeLayout numberButton7;
	private RelativeLayout numberButton8;
	private RelativeLayout numberButton9;
	private RelativeLayout numberButtonDelete;

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 4.8

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		headerTitleTextView.setText(this.getResources().getString(R.string.purchase_title));

		this.setupSpaceLayout();
		
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		navigationBarRightBtn.setVisibility(View.VISIBLE);
		navigationBarRightTextView.setText(getResources().getString(R.string.confirm_btn_title));
		navigationBarRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				confirmButtonClicked();
			}
		});
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		posInputLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_enter_4_digit_key_page, null);
		spaceLayout.addView(posInputLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		digit1TextView = (TextView) this.findViewById(R.id.digit_1_textview);
		digit2TextView = (TextView) this.findViewById(R.id.digit_2_textview);
		digit3TextView = (TextView) this.findViewById(R.id.digit_3_textview);
		digit4TextView = (TextView) this.findViewById(R.id.digit_4_textview);

		numberButton0 = (RelativeLayout) this.findViewById(R.id.keyboard_0);
		numberButton0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(0);
			}
		});
		numberButton1 = (RelativeLayout) this.findViewById(R.id.keyboard_1);
		numberButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(1);
			}
		});
		numberButton2 = (RelativeLayout) this.findViewById(R.id.keyboard_2);
		numberButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(2);
			}
		});
		numberButton3 = (RelativeLayout) this.findViewById(R.id.keyboard_3);
		numberButton3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(3);
			}
		});
		numberButton4 = (RelativeLayout) this.findViewById(R.id.keyboard_4);
		numberButton4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(4);
			}
		});
		numberButton5 = (RelativeLayout) this.findViewById(R.id.keyboard_5);
		numberButton5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(5);
			}
		});
		numberButton6 = (RelativeLayout) this.findViewById(R.id.keyboard_6);
		numberButton6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(6);
			}
		});
		numberButton7 = (RelativeLayout) this.findViewById(R.id.keyboard_7);
		numberButton7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(7);
			}
		});
		numberButton8 = (RelativeLayout) this.findViewById(R.id.keyboard_8);
		numberButton8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(8);
			}
		});
		numberButton9 = (RelativeLayout) this.findViewById(R.id.keyboard_9);
		numberButton9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(9);
			}
		});
		numberButtonDelete = (RelativeLayout) this.findViewById(R.id.keyboard_delete);
		numberButtonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyboardOnClickedAction(-1);
			}
		});

	}

	private void keyboardOnClickedAction(int clickedKeyNum) {
		TextView currentTextView = getNextInputTextView();

		switch (clickedKeyNum) {
		case 0: {
			currentTextView.setText("0");
		}
		break;
		case 1: {
			currentTextView.setText("1");
		}
		break;
		case 2: {
			currentTextView.setText("2");
		}
		break;
		case 3: {
			currentTextView.setText("3");
		}
		break;
		case 4: {
			currentTextView.setText("4");
		}
		break;
		case 5: {
			currentTextView.setText("5");
		}
		break;
		case 6: {
			currentTextView.setText("6");
		}
		break;
		case 7: {
			currentTextView.setText("7");
		}
		break;
		case 8: {
			currentTextView.setText("8");
		}
		break;
		case 9: {
			currentTextView.setText("9");
		}
		break;
		case -1: {
			if (digit4TextView.getText().toString().length() > 0) {
				digit4TextView.setText(null);
			}
			else if (digit3TextView.getText().toString().length() > 0) {
				digit3TextView.setText(null);
			}
			else if (digit2TextView.getText().toString().length() > 0) {
				digit2TextView.setText(null);
			}
			else if (digit1TextView.getText().toString().length() > 0) {
				digit1TextView.setText(null);
			}
		}
		break;

		default:
			break;
		}
	}

	private TextView getNextInputTextView() {
		if (digit1TextView.getText().toString().length() > 0 && digit2TextView.getText().toString().length() > 0 && digit3TextView.getText().toString().length() > 0) {
			return digit4TextView;
		}
		else if (digit1TextView.getText().toString().length() > 0 && digit2TextView.getText().toString().length() > 0) {
			return digit3TextView;
		}
		else if (digit1TextView.getText().toString().length() > 0) {
			return digit2TextView;
		}
		else {
			return digit1TextView;
		}
	}

	private void confirmButtonClicked() {
		String inputtedCode = "";
		
		if (digit1TextView.getText().toString().length() > 0 &&
				digit2TextView.getText().toString().length() > 0 &&
				digit3TextView.getText().toString().length() > 0 &&
				digit4TextView.getText().toString().length() > 0) {

			inputtedCode = digit1TextView.getText().toString() + digit2TextView.getText().toString() + digit3TextView.getText().toString() + digit4TextView.getText().toString();
			
			if (inputtedCode.length() == 4) {
				String posCode = ""; 
				try {
					posCode = CustomServiceFactory.getPurchaseService().getSecurityCodeWithInput(inputtedCode);
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LogController.log("posCode:"+posCode);
				Intent intent = new Intent(PurchasePOSCodeEnterActivity.this, PurchasePOSCodeDisplayActivity.class);
				intent.putExtra(Constants.POS_CODE_KEY, posCode);
				startActivity(intent);
				finish();
				
			}
		}
		else {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					"Input Error",
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
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(PurchasePOSCodeEnterActivity.this, PurchaseQRCodeScanActivity.class);
		startActivity(intent);
	}
}

