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
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.service.LocaleService;

public class ProductSearchListAdapter extends BaseAdapter{
	private List<Product> articleList = new ArrayList<Product>();

	private Context context;
	private Activity activity;
	private Handler handler;

	private LocaleService localeService;

	public ProductSearchListAdapter (Context context, Activity activity, Handler handler) {
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
		Product product;

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
			if (object instanceof Product)
			{
				product = (Product) object;


				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, product);
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, product);

			}
		}

		return convertView;
	}



	private void setListBarThumbnail(AsyncImageView listBarThumbnail,
			int position, Product product) {
		// TODO Auto-generated method stub
		listBarThumbnail.setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PRODUCT_IMAGE_PATH) + product.getThumbnail()), Constants.IMAGE_FOLDER);
		
	}

	private void setListBarDescriptionTextView(
			TextView listBarDescriptionTextView, Product product) {
		// TODO Auto-generated method stub

		listBarDescriptionTextView.setText(localeService.textByLangaugeChooser(context, product.getTitleEn(), product.getTitleZh(), product.getTitleSc()));
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

	public void setArticleList(List<Product> articleArray) {
		// TODO Auto-generated method stub
		this.articleList = articleArray;

		this.notifyDataSetChanged();
	}

	public List<Product> getArticleList() {
		return articleList;
	}

}
