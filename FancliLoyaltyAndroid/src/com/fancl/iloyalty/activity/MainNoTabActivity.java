package com.fancl.iloyalty.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.item.LoadingDialog;

public class MainNoTabActivity extends AndroidProjectFrameworkActivity {

	public View navigationBarLeftBtn = null;
	public View navigationBarRightBtn = null;
	public View navigationBarSearchBtn = null;
	public View navigationBarListBtn = null;
	public View navigationBarFullPageBtn = null;
	public View navigationBarDoneBtn = null;
	public View navigationBarShareBtn = null;
	public View navigationBarEditBtn = null;
	public View navigationBarCancelBtn = null;
	public TextView headerTitleTextView = null;
	public TextView navigationBarRightTextView = null;
	public TextView navigationBarDoneTextView = null;

	protected RelativeLayout spaceLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_no_tab);

		spaceLayout = (RelativeLayout) this.findViewById(R.id.space_layout);
		navigationBarLeftBtn = (RelativeLayout) this.findViewById(R.id.header_bar_left_btn_title);
		navigationBarRightBtn = (RelativeLayout) this.findViewById(R.id.header_bar_right_btn_title);
		navigationBarSearchBtn = (RelativeLayout) this.findViewById(R.id.header_bar_search_btn_title);
		navigationBarListBtn = (RelativeLayout) this.findViewById(R.id.header_bar_list_btn_title);
		navigationBarFullPageBtn = (RelativeLayout) this.findViewById(R.id.header_bar_fullpage_btn_title);
		navigationBarDoneBtn = (RelativeLayout) this.findViewById(R.id.header_bar_done_btn_title);
		navigationBarShareBtn = (RelativeLayout) this.findViewById(R.id.header_bar_share_btn_title);
		navigationBarEditBtn = (RelativeLayout) this.findViewById(R.id.header_bar_edit_btn_title);
		navigationBarCancelBtn = (RelativeLayout) this.findViewById(R.id.header_bar_cancel_btn_title);
		headerTitleTextView = (TextView) this.findViewById(R.id.header_navigation_title);
		navigationBarRightTextView = (TextView) this.findViewById(R.id.header_bar_right_btn_text);
		navigationBarDoneTextView = (TextView) this.findViewById(R.id.header_bar_done_btn_text);
		
		navigationBarLeftBtn.setVisibility(View.GONE);
		navigationBarRightBtn.setVisibility(View.GONE);
		navigationBarSearchBtn.setVisibility(View.GONE);
		navigationBarListBtn.setVisibility(View.GONE);
		navigationBarFullPageBtn.setVisibility(View.GONE);
		navigationBarDoneBtn.setVisibility(View.GONE);
		navigationBarShareBtn.setVisibility(View.GONE);
		navigationBarEditBtn.setVisibility(View.GONE);
		navigationBarCancelBtn.setVisibility(View.GONE);

		navigationBarLeftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
}
