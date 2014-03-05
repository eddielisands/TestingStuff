package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.ProductChoice;

public class ProductChoiceViewFlowAdapter extends BaseAdapter {
	private Handler mHandler;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<ProductChoice> productChoiceList;
	private int pagesDisplay;
	private HashMap<String, List<ProductChoice>> productChoicesMap;
	private ChoiceItemClickedListener choiceItemClickedListener;
	private List<ImageView> imageBackgroundList;

	private final int NO_OF_CHOICES_IN_PAGE = 6;

	public interface ChoiceItemClickedListener {
		public void onChoiceItemClicked(ProductChoice productChoice);
	}

	public ProductChoiceViewFlowAdapter(Handler handler, Context context, List<ProductChoice> list, ChoiceItemClickedListener listener) {
		mHandler = handler;
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		productChoiceList = list;
		pagesDisplay = Math.round(((float)(productChoiceList.size() / NO_OF_CHOICES_IN_PAGE) + 0.5f));
		productChoicesMap = new HashMap<String, List<ProductChoice>>();
		choiceItemClickedListener = listener;
		imageBackgroundList = new ArrayList<ImageView>();

		for (int i = 0; i < pagesDisplay; i++) {
			String hashMapKey = String.valueOf(i);
			List<ProductChoice> tmpList = new ArrayList<ProductChoice>();
			for (int j = 0; j < NO_OF_CHOICES_IN_PAGE; j++) {
				if (i * NO_OF_CHOICES_IN_PAGE + j >= productChoiceList.size()) {
					break;
				}
				ProductChoice tmpChoice = productChoiceList.get(i * NO_OF_CHOICES_IN_PAGE + j);
				tmpList.add(tmpChoice);
			}
			productChoicesMap.put(hashMapKey, tmpList);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pagesDisplay;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;            

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.product_detail_view_page_item, null);
			holder = new ViewHolder();                

			holder.imageLayout1 = (RelativeLayout) convertView.findViewById(R.id.image_layout1);
			holder.imageBg1 = (ImageView) convertView.findViewById(R.id.selective_image_1_bg);
			holder.image1 = (AsyncImageView) convertView.findViewById(R.id.selective_image_1);

			holder.imageLayout2 = (RelativeLayout) convertView.findViewById(R.id.image_layout2);
			holder.imageBg2 = (ImageView) convertView.findViewById(R.id.selective_image_2_bg);
			holder.image2 = (AsyncImageView) convertView.findViewById(R.id.selective_image_2);

			holder.imageLayout3 = (RelativeLayout) convertView.findViewById(R.id.image_layout3);
			holder.imageBg3 = (ImageView) convertView.findViewById(R.id.selective_image_3_bg);
			holder.image3 = (AsyncImageView) convertView.findViewById(R.id.selective_image_3);

			holder.imageLayout4 = (RelativeLayout) convertView.findViewById(R.id.image_layout4);
			holder.imageBg4 = (ImageView) convertView.findViewById(R.id.selective_image_4_bg);
			holder.image4 = (AsyncImageView) convertView.findViewById(R.id.selective_image_4);

			holder.imageLayout5 = (RelativeLayout) convertView.findViewById(R.id.image_layout5);
			holder.imageBg5 = (ImageView) convertView.findViewById(R.id.selective_image_5_bg);
			holder.image5 = (AsyncImageView) convertView.findViewById(R.id.selective_image_5);

			holder.imageLayout6 = (RelativeLayout) convertView.findViewById(R.id.image_layout6);
			holder.imageBg6 = (ImageView) convertView.findViewById(R.id.selective_image_6_bg);
			holder.image6 = (AsyncImageView) convertView.findViewById(R.id.selective_image_6);

			convertView.setTag(holder);            
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		String hashMapKey = String.valueOf(position);
		List<ProductChoice> tmpList = productChoicesMap.get(hashMapKey);

		setUpViewHolderContent(holder, tmpList);

		return convertView;
	}

	private void setUpViewHolderContent(ViewHolder holder, List<ProductChoice> list) {
		for (int i = 0; i < list.size(); i++) {
			final ProductChoice choice = list.get(i);
			String url = ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + choice.getImage();
			switch (i) {
			case 0:
				holder.imageLayout1.setVisibility(View.VISIBLE);
				holder.imageBg1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg1);
				holder.imageBg1.setImageBitmap(null);
				holder.image1.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			case 1:
				holder.imageLayout2.setVisibility(View.VISIBLE);
				holder.imageBg2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg2);
				holder.imageBg2.setImageBitmap(null);
				holder.image2.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			case 2:
				holder.imageLayout3.setVisibility(View.VISIBLE);
				holder.imageBg3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg3);
				holder.imageBg3.setImageBitmap(null);
				holder.image3.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			case 3:
				holder.imageLayout4.setVisibility(View.VISIBLE);
				holder.imageBg4.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg4);
				holder.imageBg4.setImageBitmap(null);
				holder.image4.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			case 4:
				holder.imageLayout5.setVisibility(View.VISIBLE);
				holder.imageBg5.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg5);
				holder.imageBg5.setImageBitmap(null);
				holder.image5.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			case 5:
				holder.imageLayout6.setVisibility(View.VISIBLE);
				holder.imageBg6.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						choiceClicked(v, choice);
					}
				});
				imageBackgroundList.add(holder.imageBg6);
				holder.imageBg6.setImageBitmap(null);
				holder.image6.setRequestingUrl(mHandler, url, Constants.IMAGE_FOLDER);
				break;

			default:
				break;
			}
		}
	}
	
	public void autoSelectFirstItem() {
		if (imageBackgroundList.size() > 0) {
			choiceClicked(imageBackgroundList.get(0), productChoiceList.get(0));
		}
	}

	private void choiceClicked(View view, ProductChoice choice) {
		for (int i = 0; i < imageBackgroundList.size(); i++) {
			ImageView tmpImageView = imageBackgroundList.get(i);
			tmpImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_option));
		}
		ImageView imageBg = (ImageView) view;
		imageBg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bg_option_selected));
		choiceItemClickedListener.onChoiceItemClicked(choice);
	}

	static class ViewHolder {            
		RelativeLayout imageLayout1;  
		ImageView imageBg1;
		AsyncImageView image1;
		RelativeLayout imageLayout2;         
		ImageView imageBg2;
		AsyncImageView image2;
		RelativeLayout imageLayout3;
		ImageView imageBg3;
		AsyncImageView image3;
		RelativeLayout imageLayout4;
		ImageView imageBg4;
		AsyncImageView image4;
		RelativeLayout imageLayout5;
		ImageView imageBg5;
		AsyncImageView image5;
		RelativeLayout imageLayout6;
		ImageView imageBg6;
		AsyncImageView image6;
	}
}
