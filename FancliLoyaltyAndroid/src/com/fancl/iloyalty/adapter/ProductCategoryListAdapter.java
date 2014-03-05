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

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.ProductCategory;
import com.fancl.iloyalty.service.LocaleService;

public class ProductCategoryListAdapter extends BaseAdapter{

	private List<ProductCategory> articleList = new ArrayList<ProductCategory>();

	private Context context;
	private Activity activity;
	private Handler handler;

	private LocaleService localeService;

	public ProductCategoryListAdapter (Context context, Activity activity, Handler handler) {
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
		ProductCategory productCategory;

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
			if (object instanceof ProductCategory)
			{
				productCategory = (ProductCategory) object;


				this.setListBarDescriptionEnTextView(viewHolder.listBarDescriptionEnTextView, productCategory);
				this.setListBarDescriptionTcTextView(viewHolder.listBarDescriptionTcTextView, productCategory);
			}
		}

		return convertView;
	}



	private void setListBarDescriptionTcTextView(
			TextView listBarDescriptionTcTextView,
			ProductCategory productCategory) {
		// TODO Auto-generated method stub

		listBarDescriptionTcTextView.setText(localeService.textByLangaugeChooser(context, productCategory.getTitleZh(), productCategory.getTitleZh(), productCategory.getTitleSc()));
		


	}

	private void setListBarDescriptionEnTextView(
			TextView listBarDescriptionEnTextView,
			ProductCategory productCategory) {
		// TODO Auto-generated method stub
		listBarDescriptionEnTextView.setText(productCategory.getTitleEn());

	}

	private void getLayout(ViewHolder viewHolder) {
		// TODO Auto-generated method stub
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.product_home_category_list_row, null);
		viewHolder.listBarDescriptionEnTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_description_en_textview);
		viewHolder.listBarDescriptionTcTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_description_tc_textview);

	}

	class ViewHolder {
		RelativeLayout backgroundLayout;
		TextView listBarDescriptionEnTextView;
		TextView listBarDescriptionTcTextView;
	}

	public void setArticleList(List<ProductCategory> articleArray) {
		// TODO Auto-generated method stub
		this.articleList = articleArray;

		this.notifyDataSetChanged();
	}

	public List<ProductCategory> getArticleList() {
		return articleList;
	}

}
