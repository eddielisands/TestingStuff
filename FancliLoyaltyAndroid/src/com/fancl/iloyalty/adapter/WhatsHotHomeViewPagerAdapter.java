package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.DeviceUtil;
import com.viewpagerindicator.IconPagerAdapter;

public class WhatsHotHomeViewPagerAdapter extends PagerAdapter implements IconPagerAdapter {

	private Handler handler;
	private Activity activity;
	private List<RelativeLayout> mainLayoutList = new ArrayList<RelativeLayout>();
	private List<ImageView> shopImageList = new ArrayList<ImageView>();
	private List<HotItem> pictureLinkList = new ArrayList<HotItem>();

	private int padding = (int)DataUtil.dip2px(AndroidProjectApplication.application, 3);

	public WhatsHotHomeViewPagerAdapter(Activity activity,
			List<HotItem> pictureLinkList, Handler handler)
	{
		this.pictureLinkList = pictureLinkList;
		this.activity = activity;
		this.handler = handler;

		setUpViews();
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mainLayoutList.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return mainLayoutList.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mainLayoutList.get(arg1), 0);
		return mainLayoutList.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	private void setUpViews() {
		for (int i = 0; i < pictureLinkList.size(); i++)
		{
			shopImageList.add(getImageView());

			RelativeLayout relativeLayout = new RelativeLayout(activity);
		    relativeLayout.setBackgroundColor(activity.getResources().getColor(R.color.Red));
			relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			relativeLayout.setPadding(padding, 0, padding, 0);
			relativeLayout.addView(shopImageList.get(i));
			mainLayoutList.add(relativeLayout);

			AsyncImageView tmpImageView = (AsyncImageView) shopImageList.get(i);
			tmpImageView.setBackgroundColor(activity.getResources().getColor(R.color.Orange));
			tmpImageView.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + pictureLinkList.get(i).getHighlightImage()), Constants.IMAGE_FOLDER);
		}
	}

	private ImageView getImageView() {
		int width = DeviceUtil.getDeviceWidth(activity);
		int height = 0;
		
		width = Math.round(width*0.95F);
		height = Math.round(((float)width/308F)*176F);
		
		AsyncImageView imageView = new AsyncImageView(activity);
		imageView.setBackgroundColor(activity.getResources().getColor(R.color.LightGrey));
		imageView.setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.setMargins(10, 0, 0, 0);
		imageView.setLayoutParams(params);
		imageView.setPadding(padding, padding, padding, padding);
		return imageView;
	}

	@Override
	public int getIconResId(int index) {
//		return -1;
		return R.drawable.shop_detail_view_pager_indicator_dots;
	}

}
