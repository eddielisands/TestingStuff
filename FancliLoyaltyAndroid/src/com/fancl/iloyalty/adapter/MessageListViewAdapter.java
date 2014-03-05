package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.Notification;
import com.fancl.iloyalty.service.LocaleService;

public class MessageListViewAdapter extends BaseAdapter {
	
	private List<Notification> messageList = new ArrayList<Notification>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	public MessageListViewAdapter(Context context, Activity activity, Handler handler) {
		this.context = context;
		this.activity = activity;
		this.handler = handler;

		localeService = GeneralServiceFactory.getLocaleService();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageList.size();
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

		Object object = messageList.get(position);
		Notification notification;

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
			if (object instanceof Notification)
			{
				notification = (Notification) object;
				
				this.setTitle(viewHolder.title, notification);
				this.setDate(viewHolder.date, notification);
			}
		}
		
		return convertView;
	}
	
	private void setDate(TextView date, Notification notification) {
		// TODO Auto-generated method stub
		date.setText(notification.getCreateDatetime());
		
	}

	private void setTitle(TextView title, Notification notification) {
		// TODO Auto-generated method stub
		title.setText(notification.getContent());
		
	}

	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView title;
		TextView date;
	}


	private void getLayout(ViewHolder viewHolder) {
		// TODO Auto-generated method stub
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.message_home_list_row, null);
		viewHolder.title = (TextView) viewHolder.backgroundLayout.findViewById(R.id.content_Textview);
		viewHolder.date = (TextView) viewHolder.backgroundLayout.findViewById(R.id.date_Textview);
		
	}
	
	public void setArticleList(List<Notification> articleArray) {
		// TODO Auto-generated method stub
		this.messageList = articleArray;

		this.notifyDataSetChanged();
	}

	public List<Notification> getArticleList() {
		return messageList;
	}

}
