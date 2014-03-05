package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.service.LocaleService;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAccountPurchaseViewAdapter extends BaseAdapter{

private List<PurchaseHistory> articleList = new ArrayList<PurchaseHistory>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	public MyAccountPurchaseViewAdapter (Context context, Activity activity, Handler handler) {
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
		PurchaseHistory history;

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
			if (object instanceof PurchaseHistory)
			{
				history = (PurchaseHistory) object;
				
				this.setListBarDate(viewHolder.listBarDateView, history);
				this.setListBarShopName(viewHolder.listBarShopNameView, history);
				this.setListBarTotalPoint(viewHolder.listBarTotalPointView, history);
				this.setListBarArrow(viewHolder.arrowView,history);
				
				

			}
		}
		
		return convertView;
	}



	private void setListBarArrow(RelativeLayout arrowView,
			PurchaseHistory history) {
		// TODO Auto-generated method stub
		if(history.getReceiptInd().equals("N"))
			arrowView.setVisibility(View.GONE);
		else
			arrowView.setVisibility(View.VISIBLE);
		
		
	}

	private void setListBarTotalPoint(TextView listBarTotalPointView,
			PurchaseHistory history) {
		// TODO Auto-generated method stub
		float totalPoint = history.getTotalAmount();
		int totalInt = (int) totalPoint; 
		String tmpPoint =String.valueOf(totalInt);
		listBarTotalPointView.setText(tmpPoint);
		
	}

	private void setListBarShopName(TextView listBarShopNameView,
			PurchaseHistory history) {
		// TODO Auto-generated method stub
		listBarShopNameView.setText(history.getShopNameEn());
		
	}

	private void setListBarDate(TextView listBarDateView,
			PurchaseHistory history) {
		// TODO Auto-generated method stub
		listBarDateView.setText(history.getPurchaseDate());
		
	}

	public List<PurchaseHistory> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<PurchaseHistory> articleArray) {
		this.articleList = articleArray;
		
		this.notifyDataSetChanged();
	}
	

	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView listBarDateView;
		TextView listBarShopNameView;
		TextView listBarTotalPointView;
		RelativeLayout arrowView;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.my_account_purchase_record_list_row, null);
		viewHolder.listBarDateView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_date_textview);
		viewHolder.listBarShopNameView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_shop_name_textview);
		viewHolder.listBarTotalPointView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_purchase_point_textview);
		viewHolder.arrowView = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_arrow_btn);
	}

	

}
