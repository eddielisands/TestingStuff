package com.fancl.iloyalty.adapter;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;


public class ShopListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;  



	private List<Map<String, String>> listData;  



	private List<Map<String, String>> splitData;  



	public ShopListAdapter(Context context,  

			List<Map<String, String>> listData,  

			List<Map<String, String>> splitData) {  

		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);  

		this.listData = listData;  

		this.splitData = splitData;  

	}  



	public int getCount() {  

		return listData.size();  

	}  





	public Object getItem(int position) {  

		return listData.get(position);  

	}  





	public long getItemId(int position) {  

		return position;  

	}  





	public boolean isEnabled(int position) {  

		if (splitData.contains(listData.get(position))) {  

			return false;  

		}  

		return super.isEnabled(position);  

	}  





	public View getView(final int position, View convertView, ViewGroup parent) {  

		if (splitData.contains(listData.get(position))) {  

			convertView = mInflater.inflate(R.layout.shop_location_list_section_header, null);  

		} else {  

			convertView = mInflater.inflate(R.layout.shop_location_list_section_item, null);  

			RelativeLayout shop = (RelativeLayout) convertView.findViewById(R.id.myListItem);
			if (position%2 == 0) {
				shop.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
			}else{
				shop.setBackgroundColor(mContext.getResources().getColor(R.color.LightGrey));
			}

		}  



		TextView textView = (TextView) convertView.findViewById(R.id.itemTitle);  

		textView.setText(listData.get(position).get("itemTitle"));  



		return convertView;  

	}  


}
