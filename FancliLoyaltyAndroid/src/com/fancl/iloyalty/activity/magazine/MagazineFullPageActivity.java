package com.fancl.iloyalty.activity.magazine;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainNoTabActivity;
import com.fancl.iloyalty.adapter.MagazineViewFlowAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.graphics.FastBitmapDrawable;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.item.CustomMagazineViewPager;
import com.fancl.iloyalty.item.CustomMagazineViewPager.OnPageSelectedListener;
import com.fancl.iloyalty.item.ImageViewTouch;
import com.fancl.iloyalty.pojo.MagazineImage;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;

public class MagazineFullPageActivity extends MainNoTabActivity {
	private View magazineLayout;
	private View espoirLayoutButton;
	private View fhLayoutButton;

	private List<MagazineImage> magazineImageList;
	private CustomMagazineViewPager magazineViewFlow;
	private MagazineViewFlowAdapter magazineViewFlowAdapter;
	private LinearLayout magazineIndexLayout;
	private List<AsyncImageView> magazineThumbnailsList;

	private boolean isEspoirSelected = true;

	private ImageViewTouch imageViewTouch;
	
	private LocaleService localeService;
	
	private LinearLayout.LayoutParams smallLayoutParams;
	private LinearLayout.LayoutParams bigLayoutParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		smallLayoutParams = new LinearLayout.LayoutParams(DataUtil.dip2integerPx(this, 24), DataUtil.dip2integerPx(this, 25));
		smallLayoutParams.setMargins(DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5));
		smallLayoutParams.gravity = Gravity.CENTER;
		bigLayoutParams = new LinearLayout.LayoutParams(DataUtil.dip2integerPx(this, 34), DataUtil.dip2integerPx(this, 35));
		bigLayoutParams.setMargins(DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5), DataUtil.dip2integerPx(this, 5));
		bigLayoutParams.gravity = Gravity.CENTER;

		localeService = GeneralServiceFactory.getLocaleService();
		headerTitleTextView.setText(this.getResources().getString(R.string.menu_fancl_magazine_btn_title));

		this.setupSpaceLayout();
		setupViewFlowEvent();
		setupMagazineThumbnails();
		onPageSelectedListener.onPageSelected(0);
		
		navigationBarLeftBtn.setVisibility(View.VISIBLE);
		navigationBarListBtn.setVisibility(View.VISIBLE);
		navigationBarListBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MagazineFullPageActivity.this, MagazineHomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void setupSpaceLayout() {
		// TODO Auto-generated method stub
		// Space Layout
		magazineLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.magazine_full_page, null);
		spaceLayout.addView(magazineLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		espoirLayoutButton = (RelativeLayout) findViewById(R.id.espoir_layout);
		espoirLayoutButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogController.log("espoirLayout");
				if (isEspoirSelected) {
					return;
				}
				isEspoirSelected = true;

				espoirLayoutButton.setBackgroundResource(R.drawable.btn_magazine_cat_lft_on);
				fhLayoutButton.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_off);
				
				if(imageViewTouch != null)
				{
					if(imageViewTouch.getFastBitmapDrawable() != null)
					{
						FastBitmapDrawable fastBitmapDrawable = imageViewTouch.getFastBitmapDrawable();
						imageViewTouch.setImageBitmap(null);

						Bitmap bitmapInFast = fastBitmapDrawable.getBitmap();

						if(bitmapInFast != null)
						{
							fastBitmapDrawable.setBitmap(null);
							if(!bitmapInFast.isRecycled())
							{
								bitmapInFast.recycle();
							}
						}
					}
				}

				magazineViewFlowAdapter = null;
				getMagazineImageListWithType("skincare");
				setupMagazineThumbnails();
				onPageSelectedListener.onPageSelected(0);
			}

		});

		fhLayoutButton = (RelativeLayout) findViewById(R.id.fh_layout);
		fhLayoutButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogController.log("fhLayout");
				if (!isEspoirSelected) {
					return;
				}
				isEspoirSelected = false;

				espoirLayoutButton.setBackgroundResource(R.drawable.btn_magazine_cat_lft_off);
				fhLayoutButton.setBackgroundResource(R.drawable.btn_magazine_cat_rgt_on);
				
				if(imageViewTouch != null)
				{
					if(imageViewTouch.getFastBitmapDrawable() != null)
					{
						FastBitmapDrawable fastBitmapDrawable = imageViewTouch.getFastBitmapDrawable();
						imageViewTouch.setImageBitmap(null);

						Bitmap bitmapInFast = fastBitmapDrawable.getBitmap();

						if(bitmapInFast != null)
						{
							fastBitmapDrawable.setBitmap(null);
							if(!bitmapInFast.isRecycled())
							{
								bitmapInFast.recycle();
							}
						}
					}
				}

				magazineViewFlowAdapter = null;
				getMagazineImageListWithType("intake");
				setupMagazineThumbnails();
				onPageSelectedListener.onPageSelected(0);
			}

		});

		magazineViewFlow = (CustomMagazineViewPager) findViewById(R.id.magazine_view_flow);

		magazineIndexLayout = (LinearLayout) findViewById(R.id.magazine_index_layout);

		getMagazineImageListWithType("skincare");
	}

	private void getMagazineImageListWithType(String type) {
		try {
			magazineImageList = CustomServiceFactory.getPromotionService().getMagazineImageWithMagazineType(type);

			if (magazineViewFlowAdapter == null) {
				magazineViewFlowAdapter = new MagazineViewFlowAdapter(new Handler(), this, magazineImageList);
				magazineViewFlow.setAdapter(magazineViewFlowAdapter);
			}
			else {
				magazineViewFlowAdapter.resetMagazineItemList(magazineImageList);
				magazineViewFlowAdapter.notifyDataSetChanged();
			}
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private OnPageSelectedListener onPageSelectedListener;
	
	private void setupViewFlowEvent() {
		onPageSelectedListener = new OnPageSelectedListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				LogController.log("onSwitched");
				
				magazineViewFlow.setPagingEnabled(false);
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						magazineViewFlow.setPagingEnabled(true);
					}
				}, 500);

				setThumbnailSize(position);
				if(imageViewTouch != null)
				{
					if(imageViewTouch.getFastBitmapDrawable() != null)
					{
						FastBitmapDrawable fastBitmapDrawable = imageViewTouch.getFastBitmapDrawable();
						imageViewTouch.setImageBitmap(null);

						Bitmap bitmapInFast = fastBitmapDrawable.getBitmap();

						if(bitmapInFast != null)
						{
							fastBitmapDrawable.setBitmap(null);
							if(!bitmapInFast.isRecycled())
							{
								bitmapInFast.recycle();
							}
						}
					}
				}

				if(magazineViewFlowAdapter != null)
				{
					ImageViewTouch newImageViewTouch = magazineViewFlowAdapter.getImageViewTouchByPosition(position);
					imageViewTouch = newImageViewTouch;

					MagazineImage magazineImage = magazineImageList.get(position);

					String image = localeService.textByLangaugeChooser(MagazineFullPageActivity.this, magazineImage.getImageEn(), magazineImage.getImageZh(), magazineImage.getImageSc());
					newImageViewTouch.setRequestingUrl(handler, ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + image, Constants.IMAGE_FOLDER);
//					newImageViewTouch.setRequestingUrl(handler, "http://download.gtomato.com/FANCL_iLoyalty_Android/test_2050.png", Constants.IMAGE_FOLDER);
				}
			}
		};
		
		magazineViewFlow.setOnPageSelectedListener(onPageSelectedListener);
	}
	
	
	
	private void setupMagazineThumbnails() {
		
		magazineThumbnailsList = new ArrayList<AsyncImageView>();
		
		if (magazineIndexLayout.getChildCount() > 0) {
			magazineIndexLayout.removeAllViews();
		} 
		
		for (int i = 0; i < magazineImageList.size(); i++) {
			MagazineImage magazineImage = magazineImageList.get(i);
			String thumbnail = localeService.textByLangaugeChooser(this, magazineImage.getThumbnail_En(), magazineImage.getThumbnail_Zh(), magazineImage.getThumbnail_Sc());
			
			AsyncImageView thumbnailImageView = new AsyncImageView(this);
			thumbnailImageView.setId(i);
			thumbnailImageView.setRequestingUrl(handler, ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + thumbnail, Constants.IMAGE_FOLDER);
			thumbnailImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setThumbnailSize(v.getId());
					magazineViewFlow.setCurrentItem(v.getId());
				}
			});
			
			if (i == 0) {
				magazineIndexLayout.addView(thumbnailImageView, bigLayoutParams);
			}
			else {
				magazineIndexLayout.addView(thumbnailImageView, smallLayoutParams);
			}
			
			magazineThumbnailsList.add(thumbnailImageView);
		}
	}
	
	private void setThumbnailSize(int index) {
		for (int i = 0; i < magazineThumbnailsList.size(); i++) {
			AsyncImageView image = magazineThumbnailsList.get(i);
			if (i == index) {
				image.setLayoutParams(bigLayoutParams);
			}
			else {
				image.setLayoutParams(smallLayoutParams);
			}
		}
	}
}
