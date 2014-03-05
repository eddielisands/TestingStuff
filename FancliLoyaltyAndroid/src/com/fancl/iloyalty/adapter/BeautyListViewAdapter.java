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

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.gt.snssharinglibrary.util.LogController;

public class BeautyListViewAdapter extends BaseAdapter  {

	private List<IchannelMagazine> articleList = new ArrayList<IchannelMagazine>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	public BeautyListViewAdapter (Context context, Activity activity, Handler handler) {
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
		IchannelMagazine ichannelMagazine;

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
			if (object instanceof IchannelMagazine)
			{
				ichannelMagazine = (IchannelMagazine) object;
				
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, ichannelMagazine);
				this.setListBarNewIcon(viewHolder.listBarNewIcon, ichannelMagazine.getIsNew());
				this.setListBarPlayIcon(viewHolder.listBarPlayIcon, ichannelMagazine);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, ichannelMagazine);
				this.setListBarUnreadIcon(viewHolder.listBarUnreadIcon, ichannelMagazine.getIsRead());

			}
		}
		
		return convertView;
	}

	public List<IchannelMagazine> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<IchannelMagazine> articleArray) {
		this.articleList = articleArray;
		
		this.notifyDataSetChanged();
	}
	

	class ViewHolder {
		RelativeLayout backgroundLayout;
		AsyncImageView listBarThumbnail;
		RelativeLayout listBarNewIcon;
		RelativeLayout listBarPlayIcon;
		TextView listBarDescriptionTextView;
		RelativeLayout listBarUnreadIcon;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.magazine_list_row, null);
		viewHolder.listBarThumbnail = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail);
		viewHolder.listBarNewIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_new_icon);
		viewHolder.listBarPlayIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_play_icon);
		viewHolder.listBarDescriptionTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_description_textview);
		viewHolder.listBarUnreadIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_arrow_btn);
	}

	private void setListBarThumbnail(View view, int position, IchannelMagazine ichannelMagazine) {
		((AsyncImageView)view).setImageBitmap(null);
		if (ichannelMagazine.getType().equals("video")) {
			String videoThumbnail = localeService.textByLangaugeChooser(context, ichannelMagazine.getVideoThumbnailEn(), ichannelMagazine.getVideoThumbnailZh(), 
					ichannelMagazine.getVideoThumbnailSc());
			((AsyncImageView)view).beautyVideoSetRequestingUrl(handler, videoThumbnail, Constants.IMAGE_FOLDER);
		}
		else {
			((AsyncImageView)view).setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + ichannelMagazine.getThumbnail()), Constants.IMAGE_FOLDER);
		}
	}
	
	private void setListBarNewIcon(View view, String isNew) {
		view.setVisibility((isNew.equalsIgnoreCase("Y")) ? View.VISIBLE : View.GONE);
	}
	
	private void setListBarPlayIcon(View view, IchannelMagazine magazine) {
		String videoLink = localeService.textByLangaugeChooser(context, magazine.getVideoLinkEn(), magazine.getVideoLinkZh(), magazine.getVideoLinkSc());
		if (magazine.getType().equals("video")) {
			view.setVisibility(View.VISIBLE);
		}
		else {
			if (videoLink != null) {
				if (videoLink.length() > 0) {
					view.setVisibility(View.VISIBLE);
				}
				else {
					view.setVisibility(View.GONE);
				}
			}
			else {
				view.setVisibility(View.GONE);
			}
		}
	}
	
	private void setListBarDescriptionTextView(View view, IchannelMagazine ichannelMagazine) {
		if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			((TextView)view).setText(ichannelMagazine.getTitleEn());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			((TextView)view).setText(ichannelMagazine.getTitleSc());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			((TextView)view).setText(ichannelMagazine.getTitleZh());
		}
	}
	
	private void setListBarUnreadIcon(View view, String isRead) {
//		view.setVisibility((isRead.equalsIgnoreCase("N")) ? View.VISIBLE : View.GONE);
	}
}
