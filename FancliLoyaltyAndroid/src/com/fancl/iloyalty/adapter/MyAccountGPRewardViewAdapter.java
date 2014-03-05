package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardItem;
import com.fancl.iloyalty.service.LocaleService;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAccountGPRewardViewAdapter extends BaseAdapter{
	
private List<GPRewardItem> articleList = new ArrayList<GPRewardItem>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	public MyAccountGPRewardViewAdapter (Context context, Activity activity, Handler handler) {
		this.context = context;
		this.activity = activity;
		this.handler = handler;
		
		localeService = GeneralServiceFactory.getLocaleService();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return articleList.size();
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

		Object object = articleList.get(position);
		GPRewardItem gpReward;

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
			if (object instanceof GPRewardItem)
			{
				gpReward = (GPRewardItem) object;
				
				this.setListBarDate(viewHolder.listBarDateView, gpReward);
				this.setListBarRewardPoint(viewHolder.listBarRewardPointView, gpReward);
				this.setListBarRewardTypr(viewHolder.listBarRewardTypeView,gpReward);
				this.setListBarArrow(viewHolder.arrowView,gpReward);
				
				

			}
		}
		
		return convertView;
	}




	private void setListBarArrow(RelativeLayout arrowView, GPRewardItem gpReward) {
		// TODO Auto-generated method stub
		if(gpReward.getGiftInd().equals("N") && gpReward.getReceiptInd().equals("N")){
			arrowView.setVisibility(View.GONE);
		}else{
			arrowView.setVisibility(View.VISIBLE);
		}
		
	}

	private void setListBarRewardTypr(TextView listBarRewardTypeView,
			GPRewardItem gpReward) {
		// TODO Auto-generated method stub
		listBarRewardTypeView.setText(gpReward.getActionType()+":");
		
	}

	private void setListBarRewardPoint(TextView listBarRewardPointView,
			GPRewardItem gpReward) {
		// TODO Auto-generated method stub
		float gpPoint = gpReward.getPointAmount(); 
		int gpInt = (int) gpPoint; 
		String tmpGP = String.valueOf(gpInt);
		listBarRewardPointView.setText(tmpGP);
		
	}

	private void setListBarDate(TextView listBarDateView, GPRewardItem gpReward) {
		// TODO Auto-generated method stub
		listBarDateView.setText(gpReward.getTransactionDate());
		
	}

	public List<GPRewardItem> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<GPRewardItem> articleArray) {
		this.articleList = articleArray;
		
		this.notifyDataSetChanged();
	}
	

	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView listBarDateView;
		TextView listBarRewardPointView;
		TextView listBarRewardTypeView;
		RelativeLayout arrowView;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.my_account_gp_reward_list_row, null);
		viewHolder.listBarDateView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_date_textview);
		viewHolder.listBarRewardPointView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_reward_point_textview);
		viewHolder.listBarRewardTypeView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_reward_type_textview);
		viewHolder.arrowView = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_arrow_btn);

	}

}
