package com.fancl.iloyalty.adapter;

import java.util.List;
import java.util.Map;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.adapter.PromotionListViewAdapter.ViewHolder;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.util.LogController;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RelatedListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;  
	private Handler handler = new Handler();


	private List<Map<String, String>> listData;  

	private List<Map<String, String>> splitData;  
	

	public RelatedListAdapter(Context context,  

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
			LogController.log("set data related");

			
			if (listData.get(position).get("itemType").equals("magazine")){
				convertView = mInflater.inflate(R.layout.beauty_ichannel_list_row, null); 
				
				TextView textTcView = (TextView) convertView.findViewById(R.id.list_bar_description_textview);  
				textTcView.setText(listData.get(position).get("itemTitle")); 
				
				RelativeLayout listBarPlayIcon = (RelativeLayout) convertView.findViewById(R.id.list_bar_play_icon);
				listBarPlayIcon.setVisibility(View.GONE);
				
				AsyncImageView productView = (AsyncImageView) convertView.findViewById(R.id.list_bar_thumbnail);
				productView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + listData.get(position).get("itemImage")), Constants.IMAGE_FOLDER);
				
				RelativeLayout newIcon = (RelativeLayout) convertView.findViewById(R.id.list_bar_new_icon);
				newIcon.setVisibility(View.GONE);
			}else if(listData.get(position).get("itemType").equals("promotion")){
				convertView = mInflater.inflate(R.layout.my_promotion_list_row, null); 
				
				TextView listBarLuckyDrawText = (TextView) convertView.findViewById(R.id.list_bar_lucky_draw_text);
				listBarLuckyDrawText.setVisibility(View.GONE);
//				RotateAnimation rotate= (RotateAnimation)AnimationUtils.loadAnimation(mContext,R.drawable.rotate);
//				listBarLuckyDrawText.setAnimation(rotate);
				
				RelativeLayout listBarLuckyDrawIcon = (RelativeLayout) convertView.findViewById(R.id.list_bar_lucky_draw_icon);
				listBarLuckyDrawIcon.setVisibility(View.GONE);
				
				TextView listBarDatetimeTextView = (TextView) convertView.findViewById(R.id.list_bar_datetime_textview);
				listBarDatetimeTextView.setText(mContext.getString(R.string.promotion_detail_valid) + listData.get(position).get("itemDate"));
				
				TextView textTcView = (TextView) convertView.findViewById(R.id.list_bar_description_textview);  
				textTcView.setText(listData.get(position).get("itemDes")); 
				
				TextView titleView = (TextView) convertView.findViewById(R.id.list_bar_title_textview);
				titleView.setText(listData.get(position).get("itemTitle")); 
				
				AsyncImageView productView = (AsyncImageView) convertView.findViewById(R.id.list_bar_thumbnail);
				productView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PROMOTION_IMAGE_PATH) + listData.get(position).get("itemImage")), Constants.IMAGE_FOLDER);
			}else if(listData.get(position).get("itemType").equals("product")){
				convertView = mInflater.inflate(R.layout.product_list_row, null); 
				
				TextView textTcView = (TextView) convertView.findViewById(R.id.list_bar_description_tc_textview);  
				textTcView.setText(listData.get(position).get("itemTitleTc")); 
				
				TextView textEnView = (TextView) convertView.findViewById(R.id.list_bar_description_en_textview);  
				textEnView.setText(listData.get(position).get("itemTitle")); 
				
				AsyncImageView productView = (AsyncImageView) convertView.findViewById(R.id.list_bar_thumbnail);
				productView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + listData.get(position).get("itemImage")), Constants.IMAGE_FOLDER);
			}

		}  


		return convertView;  

	}  


}
