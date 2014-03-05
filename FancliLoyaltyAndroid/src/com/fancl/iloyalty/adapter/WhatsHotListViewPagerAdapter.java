package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;

public class WhatsHotListViewPagerAdapter extends BaseAdapter {

	private List<HotItem> whatsHotList = new ArrayList<HotItem>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	public WhatsHotListViewPagerAdapter(Context context, Activity activity, Handler handler, List<HotItem> whatsHotList) {
		this.context = context;
		this.activity = activity;
		this.handler = handler;
		this.whatsHotList = whatsHotList;
		
		localeService = GeneralServiceFactory.getLocaleService();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return whatsHotList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		LogController.log("getItem");
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

		Object object = whatsHotList.get(position);
		HotItem hotItem;

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
			if (object instanceof HotItem)
			{
				hotItem = (HotItem) object;
				LogController.log("hotItem : " + hotItem.getTitleEn());
				
				this.setTitle(viewHolder.title, hotItem);
				this.setRowBottomBarBreakline(viewHolder.rowBottomBreakline, position);
				this.setThumbnailIV(viewHolder.thumbnailIV, position, hotItem);
				this.setListBarThumbnailNewTag(viewHolder.listBarThumbnailNewTag, hotItem.getIsRead());
				this.setListBarPlayIcon(viewHolder.playIcon, hotItem.getLinkType());
			}
		}
		
		return convertView;
	}

	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView title;
		RelativeLayout rowBottomBreakline;
		AsyncImageView thumbnailIV;
		RelativeLayout listBarThumbnailNewTag;
		ImageView playIcon;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.whats_hot_list_row, null);
		viewHolder.title = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_title);
		viewHolder.rowBottomBreakline = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_breakline);
		viewHolder.thumbnailIV = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail);
//		viewHolder.listBarThumbnailNewTag = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail_new_tag);
		viewHolder.listBarThumbnailNewTag = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_arrow_btn);
		viewHolder.playIcon = (ImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_play_icon);
	}
	
	private void setTitle(View view, HotItem hotItem) {
		((TextView)view).setText(localeService.textByLangaugeChooser(context, hotItem.getTitleEn(), hotItem.getTitleZh(), hotItem.getTitleSc()));
	}
	
	private void setRowBottomBarBreakline(View view, int position) {
//		view.setVisibility((childPosition == (whatsHotList.get(groupPosition).size() - 1)) ? View.INVISIBLE : View.VISIBLE);
		view.setVisibility(View.VISIBLE);
	}
	
	private void setThumbnailIV(View view, int position, HotItem hotItem) {
		((AsyncImageView)view).setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + hotItem.getThumbnail()), Constants.IMAGE_FOLDER);
	}
	
	private void setListBarThumbnailNewTag(View view, String isRead) {
		view.setVisibility((isRead.equalsIgnoreCase("N")) ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void setListBarPlayIcon(View view, String linkType) {
		view.setVisibility(View.GONE);
		
		if (linkType != null) {
			if (linkType.equals("video") || linkType.equals("link")) {
				view.setVisibility(View.VISIBLE);
			}
			else {
				view.setVisibility(View.GONE);
			}
		}
	}
	
	public void setList(List<HotItem> list) {
		whatsHotList = list;
		this.notifyDataSetChanged();
	}
}
