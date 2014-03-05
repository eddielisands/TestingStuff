package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.ImageViewTouch;
import com.fancl.iloyalty.pojo.MagazineImage;
import com.fancl.iloyalty.service.LocaleService;

public class MagazineViewFlowAdapter extends PagerAdapter {
	private Handler mHandler;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<MagazineImage> magazineItemList;
	private LocaleService localeService;

	private List<View> mainLayoutList = new ArrayList<View>();
	private List<ImageViewTouch> itemImageList = new ArrayList<ImageViewTouch>();

	private Map<Integer, ImageViewTouch> imageViewTouch = new HashMap<Integer, ImageViewTouch>();

	public MagazineViewFlowAdapter(Handler mHandler, Context mContext,
			List<MagazineImage> magazineItemList)
	{
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.magazineItemList = magazineItemList;

		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		localeService = GeneralServiceFactory.getLocaleService();

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
		return magazineItemList.size();
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
		mainLayoutList.clear();
		itemImageList.clear();
		int imageListIndex = 0;

		for (int i = 0; i < magazineItemList.size(); i++) {
			this.addImageToView(imageListIndex);
			imageListIndex++;
		}		
	}

	private void addImageToView(int position) {
		View convertView = mInflater.inflate(R.layout.magazine_view_flow_item, null);
		ImageViewTouch magazineImageView = (ImageViewTouch) convertView.findViewById(R.id.magazine_image_view);
		magazineImageView.setImageBitmap(null);
		itemImageList.add(magazineImageView);
		mainLayoutList.add(convertView);
	}

	public ImageViewTouch getImageViewTouchByPosition(int position)
	{
		return itemImageList.get(position);
	}
	
	public void resetMagazineItemList(List<MagazineImage> list) {
		magazineItemList = list;
		setUpViews();
	}
}
