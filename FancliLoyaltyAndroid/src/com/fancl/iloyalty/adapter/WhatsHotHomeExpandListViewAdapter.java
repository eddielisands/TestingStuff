package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.whatshot.WhatsHotListActivity;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.runnable.ImageReceiver;
import com.fancl.iloyalty.runnable.ImageReceiverCallback;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.ThreadService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.ImageCahceController;
import com.fancl.iloyalty.util.LogController;

public class WhatsHotHomeExpandListViewAdapter extends BaseExpandableListAdapter implements
ImageReceiverCallback {
	
	private Handler handler;
	private Activity activity;
	
	private List<ImageView> shopImageList = new ArrayList<ImageView>();
	
	private Context context;
	private String[] titles = new String[] { "campaign,shop", "product", "reading", "promotion", "magazine" };
	private List<List<HotItem>> whatsHotList = new ArrayList<List<HotItem>>();
	private List<HotItem> expendedList = new ArrayList<HotItem>();
	
	private LocaleService localeService;
	
	// For Bitmap
	private ImageReceiver imageReceiver;
	private ThreadService threadService;
	private ImageCahceController imageCahceController;
	
	private int selectedGroupIndex = -1;
	private int selectedChildIndex = -1;
	
	public WhatsHotHomeExpandListViewAdapter(Context context, Activity activity, Handler handler)
	{
		this.context = context;
		this.activity = activity;
		this.handler = handler;
		
		imageCahceController = new ImageCahceController(10);
		threadService = GeneralServiceFactory.getThreadService();
		localeService = GeneralServiceFactory.getLocaleService();
		
		for (int i = 0; i < titles.length; i++) {
			whatsHotList.add(loadWhatsHotListFromDatabase(titles[i]));
		}
	}
	
	public void resetListContent() {
		whatsHotList.clear();
		for (int i = 0; i < titles.length; i++) {
			whatsHotList.add(loadWhatsHotListFromDatabase(titles[i]));
		}
		this.notifyDataSetChanged();
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		RelativeLayout mainLayout;

		Object object = whatsHotList.get(groupPosition).get(childPosition);
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
				
				this.setTitle(viewHolder.title, hotItem);
				this.setRowBottomBreakline(viewHolder.rowBottomBreakline, groupPosition, childPosition);
				this.setThumbnailIV(viewHolder.thumbnailIV, childPosition);
				this.setListBarThumbnailNewTag(viewHolder.listBarThumbnailNewTag, hotItem.getIsRead());				
			}
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		shopImageList.clear();
		return whatsHotList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		GroupViewHolder groupViewHolder;
		RelativeLayout mainLayout;

		Object object = whatsHotList.get(groupPosition);

		if (convertView == null)
		{
			groupViewHolder = new GroupViewHolder();

			this.getGroupViewLayout(groupViewHolder);

			mainLayout = new RelativeLayout(activity);
			mainLayout.addView(groupViewHolder.backgroundLayout);

			convertView = mainLayout;
			convertView.setTag(groupViewHolder);
		}
		else
		{
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		
		if (object != null) {
			this.setGroupViewTitle(groupViewHolder.title, groupPosition);
			this.setHeaderBottomBreakline(groupViewHolder.headerBottomBreakline, isExpanded, groupPosition);
			
			ArrayList<HotItem> childList = (ArrayList<HotItem>) whatsHotList.get(groupPosition);
			int newNumberIndex = 0;
			for (int i = 0; i < childList.size(); i++) {
				HotItem hotItem = childList.get(i);
				if (hotItem.getIsRead().equalsIgnoreCase("N")) {
					newNumberIndex++;
				}
			}
			
			this.setNewNumberIndicatorBg(groupViewHolder.newNumberIndicatorBg, newNumberIndex);
			this.setNewNumberIndicatorTextView(groupViewHolder.newNumberIndicatorTextView, newNumberIndex);
			this.setArrowIcon(groupViewHolder.headerArrowIcon, groupPosition);
		}
		
		return convertView;

	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		LogController.log("isChildSelectable groupPosition : " + groupPosition + " childPosition : " + childPosition);
		
		if (selectedGroupIndex != groupPosition && selectedChildIndex != childPosition) {
			selectedGroupIndex = groupPosition;
			selectedChildIndex = childPosition;
			
			HotItem hotItem = whatsHotList.get(groupPosition).get(childPosition);
			
			activity.startActivity(CustomServiceFactory.getDetailContentService().getDetailContentActivity(hotItem, activity, true, null, 0));
		}
		
		return true;
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
	    int len = titles.length;
	    ExpandableListView list = (ExpandableListView) activity.findViewById(R.id.whats_hot_home_page_list_view);
	    for (int i = 0; i < len; i++) {
	        if (i != groupPosition) {
	            list.collapseGroup(i);
	        }
	    }
	    expendedList = whatsHotList.get(groupPosition);
	}
	
	private List<HotItem> loadWhatsHotListFromDatabase(String type) {
		List<HotItem> tmpItemList = new ArrayList<HotItem>();
		try {
			tmpItemList = CustomServiceFactory.getPromotionService()
					.getHighlightListWithType(type, false);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tmpItemList;
	}
	
	private void getBitmapFromURL(int position, String src) {
		LogController.log("position : " + position + " | src : " + src);
		imageReceiver = new ImageReceiver(activity, src, Constants.IMAGE_FOLDER, this, position, false);
		threadService.executImageRunnable(imageReceiver);
	}

	@Override
	public void updateToView(final Bitmap bitmap, final int position,
			final String url) {
		LogController.log("updateToView");
		imageCahceController.thumbnailCacheController(url, bitmap);

		if (bitmap != null)
		{
			handler.post(new Runnable()
			{

				@Override
				public void run() {
					ImageView imageView = shopImageList.get(position);
					if (imageView != null && imageView.getTag().equals(expendedList.get(position)))
					{
						LogController.log("updateToView: pos " + position + " , url " + url);
						imageView.setImageBitmap(bitmap);
					}
				}
			});

		}
	}
	
	class GroupViewHolder {
		RelativeLayout backgroundLayout;
		TextView title;
		RelativeLayout headerBottomBreakline;
		RelativeLayout newNumberIndicatorBg;
		TextView newNumberIndicatorTextView;
		RelativeLayout headerArrowIcon;
	}

	private void getGroupViewLayout(GroupViewHolder groupViewHolder) {
		groupViewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.whats_hot_home_list_category, null);
		groupViewHolder.title = (TextView) groupViewHolder.backgroundLayout.findViewById(R.id.category_title_textview);
		groupViewHolder.headerBottomBreakline = (RelativeLayout) groupViewHolder.backgroundLayout.findViewById(R.id.header_bottom_breakline);
		groupViewHolder.newNumberIndicatorBg = (RelativeLayout) groupViewHolder.backgroundLayout.findViewById(R.id.new_number_indicator_bg);
		groupViewHolder.newNumberIndicatorTextView = (TextView) groupViewHolder.backgroundLayout.findViewById(R.id.new_number_indicator_textview);
		groupViewHolder.headerArrowIcon = (RelativeLayout) groupViewHolder.backgroundLayout.findViewById(R.id.header_arrow_icon);
	}
	
	private void setGroupViewTitle(View view, int groupPosition) {
		if (groupPosition == 0) {
			((TextView)view).setText(context.getResources().getString(R.string.whats_hot_category_new_campaign));
		} else if (groupPosition == 1) {
			((TextView)view).setText(context.getResources().getString(R.string.whats_hot_category_new_product));
		} else if (groupPosition == 2) {
			((TextView)view).setText(context.getResources().getString(R.string.whats_hot_category_new_reading));
		} else if (groupPosition == 3) {
			((TextView)view).setText(context.getResources().getString(R.string.whats_hot_category_new_promotion));
		} else if (groupPosition == 4) {
			((TextView)view).setText(context.getResources().getString(R.string.whats_hot_category_new_magazine));
		}
	}
	
	private void setHeaderBottomBreakline(View view, boolean isExpanded, int groupPosition) {
		if (!isExpanded && (groupPosition != (titles.length - 1))) {
			view.setVisibility(View.INVISIBLE);
		} else {
			if ((whatsHotList.get(groupPosition).size() > 0) || (groupPosition == (titles.length - 1))) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void setNewNumberIndicatorBg(View view, int newNumberIndex) {
		view.setVisibility((newNumberIndex > 0) ? View.VISIBLE : View.INVISIBLE);
	}
	
	private void setNewNumberIndicatorTextView(View view, int newNumberIndex) {
		((TextView)view).setText("" + newNumberIndex);
	}
	
	private void setArrowIcon(View view, int groupPosition) {
		final int tmpGroupPosition = groupPosition;
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, WhatsHotListActivity.class);
				intent.putExtra(Constants.WHATS_HOT_HOT_ITEM_TYPE_KEY, titles[tmpGroupPosition]);
				activity.startActivity(intent);
			}
		});
	}
	
	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView title;
		RelativeLayout rowBottomBreakline;
		ImageView thumbnailIV;
		RelativeLayout listBarThumbnailNewTag;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.whats_hot_list_row, null);
		viewHolder.title = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_title);
		viewHolder.rowBottomBreakline = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_breakline);
		viewHolder.thumbnailIV = (ImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail);
		viewHolder.listBarThumbnailNewTag = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail_new_tag);
	}

	private void setTitle(View view, HotItem hotItem) {
		if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			((TextView)view).setText(hotItem.getTitleEn());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			((TextView)view).setText(hotItem.getTitleSc());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			((TextView)view).setText(hotItem.getTitleZh());
		}
	}
	
	private void setRowBottomBreakline(View view, int groupPosition, int childPosition) {
		view.setVisibility((childPosition == (whatsHotList.get(groupPosition).size() - 1)) ? View.INVISIBLE : View.VISIBLE);
	}
	
	private void setThumbnailIV(View view, int childPosition) {
		((ImageView)view).setImageBitmap(null);
		shopImageList.add((ImageView)view);
		
		shopImageList.get(childPosition).setTag(expendedList.get(childPosition));
		this.getBitmapFromURL(childPosition, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + expendedList.get(childPosition).getHighlightImage()));
	}
	
	private void setListBarThumbnailNewTag(View view, String isRead) {
		view.setVisibility((isRead.equalsIgnoreCase("N")) ? View.VISIBLE : View.INVISIBLE);
	}
	
}
