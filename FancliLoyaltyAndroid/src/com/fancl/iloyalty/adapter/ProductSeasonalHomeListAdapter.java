package com.fancl.iloyalty.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.adapter.PromotionListViewAdapter.ViewHolder;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.util.LogController;

public class ProductSeasonalHomeListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;  
	private Handler handler = new Handler();


	private List<Map<String, String>> listData;  

	private List<Map<String, String>> splitData;  
	

	public ProductSeasonalHomeListAdapter(Context context,  

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
		
		ViewHolder viewHolder;

		if (splitData.contains(listData.get(position))) {  

			convertView = mInflater.inflate(R.layout.product_home_seasonal_header_row, null);  
			
			TextView textView = (TextView) convertView.findViewById(R.id.itemTitle);  
			textView.setText(listData.get(position).get("itemTitle")); 

		} else {  
			LogController.log("set data seasonal");

			convertView = mInflater.inflate(R.layout.product_list_row, null); 
			
			TextView textTcView = (TextView) convertView.findViewById(R.id.list_bar_description_tc_textview);  
			textTcView.setText(listData.get(position).get("itemTitleTc")); 
			
			TextView textEnView = (TextView) convertView.findViewById(R.id.list_bar_description_en_textview);  
			textEnView.setText(listData.get(position).get("itemTitleEn")); 
			
			AsyncImageView productView = (AsyncImageView) convertView.findViewById(R.id.list_bar_thumbnail);
			productView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + listData.get(position).get("itemImage")), Constants.IMAGE_FOLDER);


		}  


		return convertView;  

	}  


}
