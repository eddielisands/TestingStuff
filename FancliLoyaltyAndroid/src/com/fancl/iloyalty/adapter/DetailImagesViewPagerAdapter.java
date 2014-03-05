package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.util.DeviceUtil;
import com.viewpagerindicator.IconPagerAdapter;

public class DetailImagesViewPagerAdapter extends PagerAdapter implements IconPagerAdapter {
	
	private Handler handler;
	private Activity activity;
	private List<RelativeLayout> mainLayoutList = new ArrayList<RelativeLayout>();
	private List<ImageView> itemImageList = new ArrayList<ImageView>();
	private List<String> imageList = new ArrayList<String>();
	private List<String> tagList = new ArrayList<String>();

	// For Bitmap
	private String imagePath;
	
	private boolean needAddPlayBtn = false;
	
	public DetailImagesViewPagerAdapter(Activity activity, List<String> imageList, Handler handler, String imagePath, boolean needAddPlayBtn)
	{
		this.imageList = imageList;
		this.activity = activity;
		this.handler = handler;
		this.imagePath = imagePath;
		this.needAddPlayBtn = needAddPlayBtn;

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
		int imageListIndex = 0;
		
		for (int i = 0; i < imageList.size(); i++) {
			this.addImageToView(imageListIndex, imageList.get(i));
			imageListIndex++;
		}		
	}
	
	private void addImageToView(int position, String imageStr) {
		itemImageList.add(getImageView());

		RelativeLayout relativeLayout = new RelativeLayout(activity);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		relativeLayout.addView(itemImageList.get(position));
		mainLayoutList.add(relativeLayout);

		tagList.add(imageStr);
		AsyncImageView tmpImageView = (AsyncImageView) itemImageList.get(position);
		tmpImageView.setImageBitmap(null);
		tmpImageView.setRequestingUrl(handler, (imagePath + imageStr), Constants.IMAGE_FOLDER);
		
		if (needAddPlayBtn && position == 0) {
			ImageButton playButton = new ImageButton(activity);
			playButton.setBackgroundColor(activity.getResources().getColor(R.color.Transparent));
			playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.btn_play_large));
			RelativeLayout.LayoutParams playButtonLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
			playButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
			relativeLayout.addView(playButton, playButtonLayoutParams);
			
			playButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Play video
				}
			});
		}
	}

	private ImageView getImageView() {
		int width = DeviceUtil.getDeviceWidth(activity);
		int height = 0;
		
		width = Math.round(width*0.95F);
		height = Math.round(((float)width/308F)*176F);
		
		AsyncImageView imageView = new AsyncImageView(activity);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setBackgroundColor(activity.getResources().getColor(R.color.LightGrey));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		return imageView;
	}

	@Override
	public int getIconResId(int index) {
//		return -1;
		return R.drawable.shop_detail_view_pager_indicator_dots;
	}

}

