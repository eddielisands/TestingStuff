package com.fancl.iloyalty.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;


public class CustomSpinnerAdapter extends BaseAdapter {

	private Context context;
	private Activity activity;
	private Handler handler;
	private String[] spinnerOptionList;
	
	public CustomSpinnerAdapter(Context context, Activity activity, Handler handler, String[] spinnerOptionList) {
		this.context = context;
		this.activity = activity;
		this.handler = handler;
		this.spinnerOptionList = spinnerOptionList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return spinnerOptionList.size();
		return spinnerOptionList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		RelativeLayout mainLayout;

		String object = spinnerOptionList[position];
		
		if (convertView == null)
		{
			viewHolder = new ViewHolder();

			this.getLayout(viewHolder);

			mainLayout = new RelativeLayout(activity);
			mainLayout.addView(viewHolder.backgroundLayout);

			convertView = mainLayout;
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (object != null) {
			this.setTitle(viewHolder.title, object);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView title;
		RelativeLayout radioBtn;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.custom_spinner_row, null);
		viewHolder.title = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_title);
		viewHolder.radioBtn = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.radio_btn);
	}

	private void setTitle(View view, String title) {
		((TextView)view).setText(title);
	}
	
}
