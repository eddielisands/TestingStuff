package com.fancl.iloyalty.adapter;


import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.util.LogController;

public class ViewFlowImageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ImageItemClickedListener imageItemClickedListener;
	private List<HotItem> hotItemList;

	private Handler handler = new Handler();

	public interface ImageItemClickedListener {
		public void onImageItemClicked(int position);
	}

	public ViewFlowImageAdapter(Context context, ImageItemClickedListener listener, List<HotItem> list) {
		mContext = context;
		imageItemClickedListener = listener;
		hotItemList = list;

		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;  
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		RelativeLayout mainLayout;

		if (convertView == null)
		{
			viewHolder = new ViewHolder();

			viewHolder.backgroundLayout = (RelativeLayout) mInflater.inflate(R.layout.view_flow_image_item, null);
			viewHolder.thumbnailIV = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.imgView);

			mainLayout = new RelativeLayout(mContext);
			mainLayout.addView(viewHolder.backgroundLayout);

			convertView = mainLayout;
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		HotItem tmpHotItem = hotItemList.get(position%hotItemList.size());
		viewHolder.thumbnailIV.setImageBitmap(null);
		viewHolder.thumbnailIV.setRequestingUrl(this.handler, ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + tmpHotItem.getHighlightImage(), Constants.IMAGE_FOLDER);
		LogController.log("hotitem link:"+ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + tmpHotItem.getHighlightImage());

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageItemClickedListener.onImageItemClicked(position%(hotItemList.size()*1000)%hotItemList.size());
			}
		});
		return convertView;
	}

	static class ViewHolder {
		RelativeLayout backgroundLayout;
		AsyncImageView thumbnailIV;
	}
}
