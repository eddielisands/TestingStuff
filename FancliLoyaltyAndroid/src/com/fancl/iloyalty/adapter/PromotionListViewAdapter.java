package com.fancl.iloyalty.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.AsyncImageView;
import com.fancl.iloyalty.pojo.Promotion;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.LogController;

public class PromotionListViewAdapter extends BaseAdapter {

	private List<Promotion> promotionList = new ArrayList<Promotion>();
	
	private Context context;
	private Activity activity;
	private Handler handler;
	
	private LocaleService localeService;
	
	private int promotionIndex = 4; 
	
	public PromotionListViewAdapter (Context context, Activity activity, Handler handler) {
		this.context = context;
		this.activity = activity;
		this.handler = handler;
		
		localeService = GeneralServiceFactory.getLocaleService();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return promotionList.size();
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

		Object object = promotionList.get(position);
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
			if (object instanceof Promotion)
			{
				promotion = (Promotion) object;
				
				this.setListBarThumbnail(viewHolder.listBarThumbnail, position, promotion);
				this.setListBarNewIcon(viewHolder.listBarNewIcon, promotion.getIsNew());
				this.setListBarLuckyDrawIcon(viewHolder.listBarLuckyDrawIcon, viewHolder.listBarLuckyDrawText, promotion.getIsLuckyDraw(), promotion.getIsParticipated(), promotion.getLuckyDrawType(), promotion.getCouponStatusCode(), promotion.getCouponStatus());
//				this.setListBarRedeemedIcon(viewHolder.listBarRedeemedIcon, promotion.getIs);
				this.setListBarTitleTextView(viewHolder.listBarTitleTextView, promotion);
				this.setListBarDescriptionTextView(viewHolder.listBarDescriptionTextView, promotion);
				this.setListBarDatetimeTextView(viewHolder.listBarDatetimeTextView, promotion);
				if(promotionIndex == 2 || promotionIndex == 3){
					String tmpRead = promotion.getIsNew();
					if(tmpRead.equalsIgnoreCase("Y"))
						this.setListBarUnreadIcon(viewHolder.listBarUnreadIcon, "N");
					else
						this.setListBarUnreadIcon(viewHolder.listBarUnreadIcon, "Y");
					
				}else{
					this.setListBarUnreadIcon(viewHolder.listBarUnreadIcon, promotion.getIsRead());
				}
				this.setListBarParticipate(viewHolder.listBarParticipate, promotion.getIsLuckyDraw(), promotion.getIsParticipated());
			}
		}
		
		return convertView;
	}

	private void setListBarParticipate(RelativeLayout listBarParticipate,
			String isLuckyDraw, String isParticipated) {
		// TODO Auto-generated method stub
		listBarParticipate.setVisibility(View.GONE);
		listBarParticipate.getBackground().setAlpha(80);
		
		if(promotionIndex == 2 || promotionIndex == 3){
			if(isLuckyDraw.equalsIgnoreCase("Y") && isParticipated.equalsIgnoreCase("Y")){
				listBarParticipate.setVisibility(View.VISIBLE);
			}
		}
		
	}

	public List<Promotion> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
		
		this.notifyDataSetChanged();
	}
	
	class ViewHolder {
		RelativeLayout backgroundLayout;
		AsyncImageView listBarThumbnail;
		RelativeLayout listBarNewIcon;
		RelativeLayout listBarLuckyDrawIcon;
		RelativeLayout listBarRedeemedIcon;
		TextView listBarTitleTextView;
		TextView listBarDescriptionTextView;
		TextView listBarDatetimeTextView;
		RelativeLayout listBarUnreadIcon;
		TextView listBarLuckyDrawText;
		RelativeLayout listBarParticipate;
	}

	private void getLayout(ViewHolder viewHolder) {
		viewHolder.backgroundLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.my_promotion_list_row, null);
		viewHolder.listBarThumbnail = (AsyncImageView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_thumbnail);
		viewHolder.listBarNewIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_new_icon);
		viewHolder.listBarLuckyDrawIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_lucky_draw_icon);
		viewHolder.listBarRedeemedIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_redeemed_icon);
		viewHolder.listBarTitleTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_title_textview);
		viewHolder.listBarDescriptionTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_description_textview);
		viewHolder.listBarDatetimeTextView = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_datetime_textview);
		viewHolder.listBarUnreadIcon = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_arrow_btn);
		viewHolder.listBarLuckyDrawText = (TextView) viewHolder.backgroundLayout.findViewById(R.id.list_bar_lucky_draw_text);
		viewHolder.listBarParticipate = (RelativeLayout) viewHolder.backgroundLayout.findViewById(R.id.list_bar_dim);
	}
	
	private void setListBarThumbnail(View view, int position, Promotion promotion) {
		((AsyncImageView)view).setImageBitmap(null);
		((AsyncImageView)view).setRequestingUrl(handler, (ApiConstant.getAPI(ApiConstant.PROMOTION_IMAGE_PATH) + promotion.getThumbnail()), Constants.IMAGE_FOLDER);
	}
	
	private void setListBarNewIcon(View view, String isNew) {
		if (isNew == null) {
			view.setVisibility(View.GONE);
			return;
		}
		view.setVisibility((isNew.equalsIgnoreCase("Y")) ? View.VISIBLE : View.GONE);
	}
	
	private void setListBarLuckyDrawIcon(View view, TextView listBarLuckyDrawText, String isLuckyDraw, String isParticipated, String luckyDrawType, String couponStatusCode, String couponStatus) {
		if (isLuckyDraw == null) {
			view.setVisibility(View.GONE);
			return;
		}
		view.setVisibility(view.VISIBLE);
		
		RotateAnimation rotate= (RotateAnimation)AnimationUtils.loadAnimation(context,R.drawable.rotate);
		listBarLuckyDrawText.setAnimation(rotate);
		
		if(isLuckyDraw.equalsIgnoreCase("Y")){
			if(isParticipated.equalsIgnoreCase("Y")){
				listBarLuckyDrawText.setText("Used");
				view.setBackgroundResource(R.drawable.ico_redeemed);
			}else{
				view.setBackgroundResource(R.drawable.ico_lucky_draw);
				if(luckyDrawType.equalsIgnoreCase("luckyDraw"))
					listBarLuckyDrawText.setText("luckyDraw");
				else if(luckyDrawType.equalsIgnoreCase("game"))
					listBarLuckyDrawText.setText("Game");
			}
	
		}else{
			if(isParticipated.equalsIgnoreCase("Y")){
				view.setBackgroundResource(R.drawable.ico_redeemed);
				listBarLuckyDrawText.setText("Redeemed");
			}else{
				view.setVisibility(view.GONE);
			}
			
		}
		
		if(promotionIndex == 2){
			if(couponStatusCode.equalsIgnoreCase("90")){
				view.setBackgroundResource(R.drawable.ico_redeemed);
				listBarLuckyDrawText.setText(couponStatus);
				view.setVisibility(view.VISIBLE);
			}
		}
		
	}
	
	private void setListBarRedeemedIcon(View view, String isRedeemed) {
		if (isRedeemed == null) {
			view.setVisibility(View.GONE);
			return;
		}
		view.setVisibility((isRedeemed.equalsIgnoreCase("Y")) ? View.VISIBLE : View.GONE);
	}
	
	private void setListBarTitleTextView(final View view, Promotion promotion) {
		if (promotionIndex == 4) {
			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				((TextView)view).setText(promotion.getGp() + " Point Gift");
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				((TextView)view).setText(promotion.getGp() + " Point Gift");
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				((TextView)view).setText(promotion.getGp() + " Point Gift");
			}
		}
		else {
			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				((TextView)view).setText(promotion.getTitleEn());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				((TextView)view).setText(promotion.getTitleSc());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				((TextView)view).setText(promotion.getTitleZh());
			}
		}
		
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				ViewTreeObserver obs = view.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if(((TextView) view).getLineCount() > 1)
				{
					int lineEndIndex = ((TextView) view).getLayout().getLineEnd(0);
					String text = ((TextView) view).getText().subSequence(0, lineEndIndex-3) +"...";
					((TextView) view).setText(text);
				}
			}
		});
	}
	
	private void setListBarDescriptionTextView(final View view, Promotion promotion) {
		if (promotionIndex == 4) {
			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				((TextView)view).setText(promotion.getTitleEn());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				((TextView)view).setText(promotion.getTitleSc());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				((TextView)view).setText(promotion.getTitleZh());
			}
		}
		else {
			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				((TextView)view).setText(promotion.getDescriptionEn());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				((TextView)view).setText(promotion.getDescriptionSc());
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				((TextView)view).setText(promotion.getDescriptionZh());
			}
		}
		
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				ViewTreeObserver obs = view.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if(((TextView) view).getLineCount() > 2)
				{
					int lineEndIndex = ((TextView) view).getLayout().getLineEnd(1);
					String text = ((TextView) view).getText().subSequence(0, lineEndIndex-3) +"...";
					((TextView) view).setText(text);
				}
			}
		});
	}
	
	private void setListBarDatetimeTextView(View view, Promotion promotion) {
		if (promotionIndex != 4) {
			view.setVisibility(View.VISIBLE);
			((TextView)view).setText(context.getString(R.string.promotion_detail_valid) + promotion.getPromotionEndDatetime());
		}
		else {
			view.setVisibility(View.GONE);
		}
	}
	
	private void setListBarUnreadIcon(View view, String isRead) {
		
		view.setVisibility((isRead.equalsIgnoreCase("N")) ? View.VISIBLE : View.GONE);
	}
	
	public void setPromotionIndex(int index) {
		this.promotionIndex = index;
	}
}
