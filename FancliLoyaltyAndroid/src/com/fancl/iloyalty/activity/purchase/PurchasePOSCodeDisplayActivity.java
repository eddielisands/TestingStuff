package com.fancl.iloyalty.activity.purchase;

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
import com.fancl.iloyalty.service.LocaleService;

public class PurchasePOSCodeDisplayActivity extends MainNoTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 4.9

	private LocaleService localeService;

	private View posDisplayLayout;
	private String posCode;

	private TextView posCodeTextView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		posCode = getIntent().getStringExtra(Constants.POS_CODE_KEY);

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
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		posDisplayLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.purchase_9_digit_security_code_page, null);
		spaceLayout.addView(posDisplayLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		posCodeTextView = (TextView) this.findViewById(R.id.pos_code_textview);

		posCodeTextView.setText(posCode);

		RelativeLayout confirmButton = (RelativeLayout) this.findViewById(R.id.confirm_button);
		confirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(PurchasePOSCodeDisplayActivity.this, PurchaseQRCodeScanActivity.class);
		startActivity(intent);
	}
}
