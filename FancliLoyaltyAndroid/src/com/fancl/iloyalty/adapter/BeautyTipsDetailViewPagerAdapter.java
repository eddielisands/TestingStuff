package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.runnable.ImageReceiver;
import com.fancl.iloyalty.runnable.ImageReceiverCallback;
import com.fancl.iloyalty.service.ThreadService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.DeviceUtil;
import com.fancl.iloyalty.util.ImageCahceController;
import com.fancl.iloyalty.util.LogController;
import com.viewpagerindicator.IconPagerAdapter;

public class BeautyTipsDetailViewPagerAdapter extends PagerAdapter implements
ImageReceiverCallback, IconPagerAdapter {

	private Handler handler;
	private Activity activity;
	private List<RelativeLayout> mainLayoutList = new ArrayList<RelativeLayout>();
	private List<ImageView> shopImageList = new ArrayList<ImageView>();
	private List<String> tagList = new ArrayList<String>();
	private IchannelMagazine ichannelMagazine;

	// For Bitmap
	private ImageReceiver imageReceiver;
	private ThreadService threadService;
	private ImageCahceController imageCahceController;

	private int padding = (int)DataUtil.dip2px(AndroidProjectApplication.application, 3);
	
	public BeautyTipsDetailViewPagerAdapter(Activity activity, IchannelMagazine ichannelMagazine, Handler handler)
	{
//		this.pictureLinkList = pictureLinkList;
		this.ichannelMagazine = ichannelMagazine;
		this.activity = activity;
		this.handler = handler;

		imageCahceController = new ImageCahceController(10);
		threadService = GeneralServiceFactory.getThreadService();

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
		int shopImageListIndex = 0;
		
//		for (int i = 0; i < 5; i++) {
//			String imageStr = "";
//			if (i == 0) { 
//				imageStr = ichannelMagazine.getImage1();
//			} else if (i == 1) {
//				imageStr = hotItem.getImage2();
//			} else if (i == 2) {
//				imageStr = hotItem.getImage3();
//			} else if (i == 3) {
//				imageStr = hotItem.getImage4();
//			} else if (i == 4) {
//				imageStr = hotItem.getImage5();
//			}
//			if (!StringUtil.isStringEmpty(imageStr)) {
//				this.addImageToView(shopImageListIndex, imageStr);
//				shopImageListIndex++;
//			}			
//		}		
	}
	
	private void addImageToView(int position, String imageStr) {
		shopImageList.add(getImageView());

		RelativeLayout relativeLayout = new RelativeLayout(activity);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		relativeLayout.setPadding(padding, 0, padding, 0);
		relativeLayout.addView(shopImageList.get(position));
		mainLayoutList.add(relativeLayout);

		shopImageList.get(position).setTag(imageStr);
		tagList.add(imageStr);
		getBitmapFromURL(position, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + imageStr));
	}

	private ImageView getImageView() {
		int width = DeviceUtil.getDeviceWidth(activity);
		int height = 0;
		
		width = Math.round(width*0.95F);
		height = Math.round(((float)width/308F)*176F);
		
		ImageView imageView = new ImageView(activity);
		imageView.setScaleType(ScaleType.FIT_XY);
//		imageView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.detail_default_pic_directory));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		imageView.setPadding(padding, padding, padding, padding);
		return imageView;
	}

	private void getBitmapFromURL(int position, String src) {

		imageReceiver = new ImageReceiver(activity, src, Constants.IMAGE_FOLDER, this, position, false);
		threadService.executImageRunnable(imageReceiver);
	}

	@Override
	public void updateToView(final Bitmap bitmap, final int position,
			final String url) {
		imageCahceController.thumbnailCacheController(url, bitmap);

		if (bitmap != null)
		{
			handler.post(new Runnable()
			{

				@Override
				public void run() {
					ImageView imageView = shopImageList.get(position);
					imageView.setImageBitmap(null);
					if (imageView != null && imageView.getTag().equals(tagList.get(position)))
					{
						LogController.log("updateToView: pos " + position + " , url " + url);
						imageView.setImageBitmap(bitmap);
					}
				}
			});

		}
	}

	public void destory() {
		if (imageCahceController != null)
		{
			imageCahceController.clearThumbnailCache();
		}
	}

	@Override
	public int getIconResId(int index) {
//		return -1;
		return R.drawable.shop_detail_view_pager_indicator_dots;
	}
}
