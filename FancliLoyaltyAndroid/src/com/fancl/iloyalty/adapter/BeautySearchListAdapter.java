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

public class BeautySearchListAdapter extends BaseAdapter{
	private List<IchannelMagazine> articleList = new ArrayList<IchannelMagazine>();

	private Context context;
	private Activity activity;
	private Handler handler;

	private LocaleService localeService;

	public BeautySearchListAdapter (Context context, Activity activity, Handler handler) {
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
		IchannelMagazine ichannel;

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
				ichannel = (IchannelMagazine) object;


				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, ichannel);
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, ichannel);

			}
		}

		return convertView;
	}



	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, IchannelMagazine ichannel) {
		// TODO Auto-generated method stub
		if(ichannel.getType().equals("video")){
			listBarThumbnail.beautyVideoSetRequestingUrl(handler, (localeService.textByLangaugeChooser(context, ichannel.getVideoThumbnailEn(), ichannel.getVideoThumbnailZh(), ichannel.getVideoLinkSc())), Constants.IMAGE_FOLDER);
		}else{
			listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + ichannel.getThumbnail()), Constants.IMAGE_FOLDER);
		}
		
	}

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, IchannelMagazine ichannel) {
		// TODO Auto-generated method stub
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, ichannel.getTitleEn(), ichannel.getTitleZh(), ichannel.getTitleSc()));
	}





	private void getLayout(ViewHolder viewHolder) {
		// TODO Auto-generated method stub
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.search_list_row, null);
		viewHolder.listBarDescriptionTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_search_description);
		viewHolder.listBarThumbnail = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.list_search_thumbnail);


	}

	class ViewHolder {
		RelativeLayout backgroundLayout;
		AsyncImageView listBarThumbnail;
		TextView listBarDescriptionTextView;

	}

	public void setArticleList(List<IchannelMagazine> articleArray) {
		// TODO Auto-generated method stub
		this.articleList = articleArray;

		this.notifyDataSetChanged();
	}

	public List<IchannelMagazine> getArticleList() {
		return articleList;
	}

}
