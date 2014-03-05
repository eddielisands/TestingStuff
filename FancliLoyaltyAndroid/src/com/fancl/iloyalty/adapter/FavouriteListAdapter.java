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
import com.fancl.iloyalty.pojo.AboutFancl;
import com.fancl.iloyalty.pojo.HotItem;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.pojo.Shop;
import com.fancl.iloyalty.service.LocaleService;


public class FavouriteListAdapter extends BaseAdapter{
	private List<Object> articleList = new ArrayList<Object>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	private Boolean isShowDeleteBtn = false;
	
	DeleteBtnClickedListener deleteBtnClickedListener;
	
	public FavouriteListAdapter (Context context, Activity activity, Handler handler) {
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
		Product product;
		HotItem hotItem;
		AboutFancl aboutFancl;
		Shop shop;
		Promotion promotion;

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
			if (object instanceof IchannelMagazine){
				ichannelMagazine = (IchannelMagazine) object;
				
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, ichannelMagazine);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, ichannelMagazine);

			}
			else if (object instanceof Product) {
				product = (Product) object;
				
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, product);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, product);
				
			}
			else if (object instanceof HotItem) {
				hotItem = (HotItem) object;
				
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, hotItem);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, hotItem);
				
			}
			else if(object instanceof AboutFancl) {
				aboutFancl = (AboutFancl) object;
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, aboutFancl);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, aboutFancl);
				
			}
			else if(object instanceof Shop) {
				shop = (Shop) object;
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, shop);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, shop);
			}
			else if(object instanceof Promotion){
				promotion = (Promotion) object;
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, promotion);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, promotion);
			}
			
			
			if(isShowDeleteBtn)
				this.setListBarDeleteBtn(viewHolder.deleteBtnView, true);
			else
				this.setListBarDeleteBtn(viewHolder.deleteBtnView, false);
			
			
			this.setDeleteBtn(viewHolder.deleteBtnView,position);
			

			
			
		}
		
		return convertView;
	}

	

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, Promotion promotion) {
		// TODO Auto-generated method stub
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, promotion.getTitleEn(), promotion.getTitleZh(), promotion.getTitleSc()));
		
	}

	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, Promotion promotion) {
		// TODO Auto-generated method stub
		listBarThumbnail.setImageBitmap(null);
		listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + promotion.getThumbnail()), Constants.IMAGE_FOLDER);
		listBarThumbnail.setVisibility(View.VISIBLE);
		
	}

	private void setDeleteBtn(RelativeLayout deleteBtnView, final int position) {
		// TODO Auto-generated method stub
		deleteBtnView.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v) {
//				LogController.log("position:"+position);
				deleteBtnClickedListener.deleteBtnClicked(position);
				

			}
		});		
	}

	private void setListBarDeleteBtn(RelativeLayout deleteBtnView, boolean isShow) {
		// TODO Auto-generated method stub
		if(isShow)
			deleteBtnView.setVisibility(View.VISIBLE);
		else
			deleteBtnView.setVisibility(View.GONE);
		
	}

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, Shop shop) {
		// TODO Auto-generated method stub
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, shop.getTitleEn(), shop.getTitleZh(), shop.getTitleSc()));
		
	}

	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, Shop shop) {
		// TODO Auto-generated method stub
		listBarThumbnail.setImageBitmap(null);
		listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + shop.getImage()), Constants.IMAGE_FOLDER);
		listBarThumbnail.setVisibility(View.VISIBLE);
		
	}

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, AboutFancl aboutFancl) {
		// TODO Auto-generated method stub
		
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, aboutFancl.getTitleEn(), aboutFancl.getTitleZh(), aboutFancl.getTitleSc()));
		
	}

	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, AboutFancl aboutFancl) {
		// TODO Auto-generated method stub
		listBarThumbnail.setImageBitmap(null);
//		listBarThumbnail.setVisibility(View.INVISIBLE);
		
		
	}



	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, HotItem hotItem) {
		// TODO Auto-generated method stub
		
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, hotItem.getTitleEn(), hotItem.getTitleZh(), hotItem.getTitleSc()));
		
	}

	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, HotItem hotItem) {
		// TODO Auto-generated method stub
		listBarThumbnail.setImageBitmap(null);
		listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.HOT_ITEM_IMAGE_PATH) + hotItem.getThumbnail()), Constants.IMAGE_FOLDER);
		listBarThumbnail.setVisibility(View.VISIBLE);
		
	}

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, Product product) {
		// TODO Auto-generated method stub
		
		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, product.getTitleEn(), product.getTitleZh(), product.getTitleSc()));
		
	}

	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, Product product) {
		// TODO Auto-generated method stub
		listBarThumbnail.setImageBitmap(null);
		listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getThumbnail()), Constants.IMAGE_FOLDER);
		listBarThumbnail.setVisibility(View.VISIBLE);
		
	}

	public List<Object> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Object> articleArray) {
		this.articleList = articleArray;
		
		this.notifyDataSetChanged();
	}
	
	public void setShowDeleteBtn(Boolean isShow){
		isShowDeleteBtn = isShow;
		
		this.notifyDataSetChanged();
	}
	

	class ViewHolder {
		RelativeLayout backgroundLayout;
		AsyncImageView listBarThumbnail;
		TextView listBarDescriptionTextView;
		RelativeLayout deleteBtnView;

		
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.favourite_list_row, null);
		viewHolder.listBarThumbnail = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail);
		viewHolder.listBarDescriptionTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_description_textview);
		viewHolder.deleteBtnView = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_delete_btn);

	}
	


	private void setListBarThumbnail(View view, int position, IchannelMagazine ichannelMagazine) {
		((AsyncImageView)view).setImageBitmap(null);
		if(ichannelMagazine.getType().equals("video"))
			((AsyncImageView)view).setRequestingUrl(handler, (localeService.textByLangaugeChooser(context, ichannelMagazine.getVideoThumbnailEn(), ichannelMagazine.getVideoLinkZh(), ichannelMagazine.getVideoThumbnailSc())), Constants.IMAGE_FOLDER);
		else
			((AsyncImageView)view).setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.ICHANNEL_IMAGE_PATH) + ichannelMagazine.getThumbnail()), Constants.IMAGE_FOLDER);

	}
	
	
	private void setListBarDescriptionTextView(View view, IchannelMagazine ichannelMagazine) {

		((TextView)view).setText(localeService.textByLangaugeChooser(context, ichannelMagazine.getTitleEn(), ichannelMagazine.getTitleZh(), ichannelMagazine.getTitleSc()));

	}
	
	public interface DeleteBtnClickedListener {
		public void deleteBtnClicked(int deleteRow);
	}
	
	
	public void setListener(DeleteBtnClickedListener listener){
		this.deleteBtnClickedListener = listener;
		
	}

}
