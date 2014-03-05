package com.fancl.iloyalty.item;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.ProductSeries;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.DeviceUtil;
import com.fancl.iloyalty.util.LogController;

public class CustomTabBar extends RelativeLayout {
	
	private Context context;
	private List<String> tabBarList;
	private List<RelativeLayout> tabBarViewList;
	
	private int tabBarHeight;
	private boolean isScrollable;
	private int currentTabBarIndex;
	
	private HorizontalScrollView sv;
	
	private Drawable tabBtnBgDrawable;
	private Drawable tabBtnHlDrawable;
	
	private RelativeLayout backgroundTab;
	private RelativeLayout highlightTab;
	
	private CustomTabBarCallback customTabBarCallback;
	
	private Runnable scrollerTask;
	
	private int initialPosition;
	private int newCheck = 100;
	
	final Drawable highlightTabImg = this.getResources().getDrawable(R.drawable.sub_cat_on);
	private boolean isProduct;
	
	private TextView pickerTitle;
	private RelativeLayout picker;
	
	private LocaleService localeService = GeneralServiceFactory.getLocaleService();

	public CustomTabBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomTabBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomTabBar (Context context, int currentTabBarIndex, List<String> tabBarList, int tabBarHeight, boolean isScrollable, boolean isProduct, CustomTabBarCallback customTabBarCallback) {
		super(context);
		
		this.context = context;
		this.tabBarList = tabBarList;
		this.tabBarHeight = tabBarHeight;
		this.isScrollable = isScrollable;
		this.isProduct = isProduct;
		this.currentTabBarIndex = currentTabBarIndex;
		
		this.customTabBarCallback = customTabBarCallback;
		
		this.setBackgroundColor(0xFF00FF0);
		
		tabBtnHlDrawable = this.getResources().getDrawable(R.drawable.btn_cat_rgt_on);
		tabBtnBgDrawable = this.getResources().getDrawable(R.drawable.btn_cat_rgt_off);
				
		createView();
	}
	
	public void setConstructors(Context context, int currentTabBarIndex, List<String> tabBarList, int tabBarHeight, boolean isScrollable, boolean isProduct, CustomTabBarCallback customTabBarCallback) {
		this.context = context;
		this.tabBarList = tabBarList;
		this.tabBarHeight = tabBarHeight;
		this.isScrollable = isScrollable;
		this.isProduct = isProduct;
		this.currentTabBarIndex = currentTabBarIndex;
		
		this.customTabBarCallback = customTabBarCallback;
		
		this.setBackgroundColor(0xFF00FF0);
		
		tabBtnHlDrawable = this.getResources().getDrawable(R.drawable.btn_cat_rgt_on);
		tabBtnBgDrawable = this.getResources().getDrawable(R.drawable.btn_cat_rgt_off);
				
		createView();
	}
	
	private void createView() {
		if (isScrollable) {
			scrollerTask = new Runnable() {

		        public void run() {

		            int newPosition = getScrollX();
		            if(initialPosition - newPosition == 0){//has stopped

		            	LogController.log("scrollerTask ");
		            	int scrollToIndex = ((sv.getScrollX() + (highlightTabImg.getMinimumWidth() / 2)) / highlightTabImg.getMinimumWidth());
		            	sv.smoothScrollTo((scrollToIndex * highlightTabImg.getMinimumWidth()), 0);
		            	
		            	
		            	tabBarOnClick(scrollToIndex);
		            	
		            	for (int i = 0; i < tabBarList.size(); i++) {
		            		TextView tabTitle = (TextView) findViewById(i);
		            		if(i == scrollToIndex)
		            			tabTitle.setTextColor(context.getResources().getColor(R.color.white));
		            		else
		            			tabTitle.setTextColor(context.getResources().getColor(R.color.Fancl_Grey));
		            	}
		            	
		            }else{
		                initialPosition = getScrollY();
		                CustomTabBar.this.postDelayed(scrollerTask, newCheck);
		            }
		        }
		    };
			
			this.setupTabBarWithScroll();
		} else {
			this.setupTabBarWithoutScroll();
			
		}
	}
	
	private void setupTabBarWithoutScroll() {
		LinearLayout tabBarLayout = new LinearLayout(context);
		tabBarLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		this.addView(tabBarLayout, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		
		tabBarViewList = new ArrayList<RelativeLayout>();
		
		for (int i = 0; i < tabBarList.size(); i++) {
			final int tabBarIndex = i;
			RelativeLayout singleTab = new RelativeLayout(context);
			if (i == currentTabBarIndex) {
				singleTab.setBackgroundDrawable(tabBtnHlDrawable);
			} else {
				singleTab.setBackgroundDrawable(tabBtnBgDrawable);
			}
			tabBarLayout.addView(singleTab, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
			tabBarViewList.add(singleTab);
			
			String titleStr = tabBarList.get(i);
			TextView tabTitle = new TextView(context);
			tabTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tabTitle.setText(titleStr);
			tabTitle.setTextColor(this.getResources().getColor(R.color.Fancl_Grey));
			tabTitle.setTextSize(12);
			singleTab.addView(tabTitle, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
			singleTab.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					tabBarOnClick(tabBarIndex);
				}
			});
		}
	}
	
	private void setupTabBarWithScroll() {
//		backgroundTab = new RelativeLayout(context);
//		backgroundTab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sub_cat_bar));
//		this.addView(backgroundTab, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		
		if(isProduct){
			LinearLayout subCat = new LinearLayout(context);
			subCat.setOrientation(LinearLayout.HORIZONTAL);
			
			picker = new RelativeLayout(context);
			picker.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_product_series));
			subCat.addView(picker, new LayoutParams(LayoutParams.WRAP_CONTENT, tabBarHeight-1));

			
			pickerTitle = new TextView(context);
			pickerTitle.setGravity(Gravity.CENTER);
//			pickerTitle.setText("skincare");
			pickerTitle.setTextColor(this.getResources().getColor(R.color.white));
			pickerTitle.setTextSize(12);
			picker.addView(pickerTitle, 130, LayoutParams.MATCH_PARENT);
			
			backgroundTab = new RelativeLayout(context);
			backgroundTab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sub_cat_bar));
			subCat.addView(backgroundTab, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
			
			this.addView(subCat, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));

		}else{
			backgroundTab = new RelativeLayout(context);
			backgroundTab.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sub_cat_bar));
			this.addView(backgroundTab, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		}
		
		highlightTab = new RelativeLayout(context);
//		final Drawable highlightTabImg = this.getResources().getDrawable(R.drawable.sub_cat_on);
		highlightTab.setBackgroundDrawable(highlightTabImg);
		RelativeLayout.LayoutParams params  = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_HORIZONTAL);
		params.addRule(CENTER_VERTICAL);
		highlightTab.setGravity(CENTER_HORIZONTAL | CENTER_VERTICAL);
		highlightTab.setLayoutParams(params);
		this.addView(highlightTab);
		
		sv = new HorizontalScrollView(context);
		sv.setHorizontalScrollBarEnabled(false);
//		sv.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sub_cat_bar));

//		this.addView(sv, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		if(isProduct){
			RelativeLayout.LayoutParams params2  = new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight);
            params2.setMargins(25, 0, 30, 0);
			backgroundTab.addView(sv, params2);
//			backgroundTab.addView(sv, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		}else{
			this.addView(sv, new LayoutParams(LayoutParams.MATCH_PARENT, tabBarHeight));
		}
		
		sv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
				
					startScrollerTask();
				}
				return false;
			}
		});
		
		
		LinearLayout tmpLinearLayout = new LinearLayout(context);
		tmpLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		sv.addView(tmpLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, tabBarHeight));

//		RelativeLayout startSpaceLayout = new RelativeLayout(context);
//		tmpLinearLayout.addView(startSpaceLayout, ((DeviceUtil.getDeviceWidth(context) - highlightTabImg.getMinimumWidth()) / 2), tabBarHeight);	
		
		if(!isProduct){
			RelativeLayout startSpaceLayout = new RelativeLayout(context);
			tmpLinearLayout.addView(startSpaceLayout, ((DeviceUtil.getDeviceWidth(context) - highlightTabImg.getMinimumWidth()) / 2), tabBarHeight);	
		}
		
		tabBarViewList = new ArrayList<RelativeLayout>();
		
		for (int i = 0; i < tabBarList.size(); i++) {
//			final int tabBarIndex = i;
			
			RelativeLayout singleTab = new RelativeLayout(context);
			tmpLinearLayout.addView(singleTab, new LayoutParams(highlightTabImg.getMinimumWidth(), tabBarHeight));
//			tabBarViewList.add(singleTab);
			
			String titleStr = tabBarList.get(i);
			TextView tabTitle = new TextView(context);
			tabTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
			tabTitle.setText(titleStr);
			tabTitle.setId(i);
			tabTitle.setTextColor(this.getResources().getColor(R.color.Fancl_Grey));
			if(i==0)
				tabTitle.setTextColor(this.getResources().getColor(R.color.white));
			tabTitle.setTextSize(12);
			singleTab.addView(tabTitle, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			
//			singleTab.setOnClickListener(new View.OnClickListener()
//			{
//				@Override
//				public void onClick(View v) {
//					tabBarOnClick(tabBarIndex);
//				}
//			});
		}
		
		RelativeLayout endSpaceLayout = new RelativeLayout(context);
		tmpLinearLayout.addView(endSpaceLayout, ((DeviceUtil.getDeviceWidth(context) - highlightTabImg.getMinimumWidth()) / 2), tabBarHeight);
		
		RelativeLayout leftArrowLayout = new RelativeLayout(context);
		leftArrowLayout.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ico_lft_arrow));
		RelativeLayout.LayoutParams leftArrowParams  = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftArrowParams.addRule(ALIGN_PARENT_LEFT);
		leftArrowParams.addRule(CENTER_VERTICAL);
		leftArrowParams.setMargins(10, 0, 0, 0);
		leftArrowLayout.setLayoutParams(leftArrowParams);
//		this.addView(leftArrowLayout);
		
		RelativeLayout rightArrowLayout = new RelativeLayout(context);
		rightArrowLayout.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.ico_rgt_arrow));
		RelativeLayout.LayoutParams rightArrowParams  = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightArrowParams.addRule(ALIGN_PARENT_RIGHT);
		rightArrowParams.addRule(CENTER_VERTICAL);
		rightArrowParams.setMargins(0, 0, 10, 0);
		rightArrowLayout.setLayoutParams(rightArrowParams);
//		this.addView(rightArrowLayout);
		
		if(isProduct){
			backgroundTab.addView(leftArrowLayout);
			backgroundTab.addView(rightArrowLayout);
		}else{
			this.addView(leftArrowLayout);
			this.addView(rightArrowLayout);
		}
	}
	
	public void resetTabBar(List<String> tabBarList, boolean isScrollable) {
		this.removeAllViews();
		
		this.tabBarList = tabBarList;
		this.isScrollable = isScrollable;
		
		createView();
	}
	
	public void setScrollViewBackgroundImg(Drawable bgDrawable) {
		sv.setBackgroundDrawable(bgDrawable);
	}
	
	public void setTabButtonBackgroundImg(Drawable tabBtnBgDrawable) {
		this.tabBtnBgDrawable = tabBtnBgDrawable;
		
		if (!isScrollable) {
			for (int i = 0; i < tabBarViewList.size(); i++) {
				RelativeLayout singleTab = tabBarViewList.get(i);
				if (i == currentTabBarIndex) {
					singleTab.setBackgroundDrawable(this.tabBtnHlDrawable);
				} else {
					singleTab.setBackgroundDrawable(this.tabBtnBgDrawable);
				}
			}
		}
	}
	
	public void setTabButtonHighlightImg(Drawable tabBtnHlDrawable) {
		this.tabBtnHlDrawable = tabBtnHlDrawable;
		
		if (isScrollable) {
			backgroundTab.setBackgroundDrawable(this.tabBtnHlDrawable);
		} else {
			for (int i = 0; i < tabBarViewList.size(); i++) {
				RelativeLayout singleTab = tabBarViewList.get(i);
				if (i == currentTabBarIndex) {
					singleTab.setBackgroundDrawable(this.tabBtnHlDrawable);
				} else {
					singleTab.setBackgroundDrawable(this.tabBtnBgDrawable);
				}
			}
		}
	}
	
	private void tabBarOnClick(int index) {
		currentTabBarIndex = index;
		
		for (int i = 0; i < tabBarViewList.size(); i++) {
			RelativeLayout singleTab = tabBarViewList.get(i);
			if (i == index) {
				singleTab.setBackgroundDrawable(this.tabBtnHlDrawable);
			} else {
				singleTab.setBackgroundDrawable(this.tabBtnBgDrawable);
			}
		}
		
		if (customTabBarCallback != null)
		{
			customTabBarCallback.clickedIndex(this, index);
		}
	}
	
	private void startScrollerTask(){

	    initialPosition = getScrollX();
	    CustomTabBar.this.postDelayed(scrollerTask, newCheck);
	}
	

	public View getTextView(){
		return picker;
		
	}
	
	public void setPickerText(ProductSeries productSeries) {

		pickerTitle.setText(localeService.textByLangaugeChooser(context, productSeries.getTitleEn(), productSeries.getTitleZh(), productSeries.getTitleSc()));
		
	}
	
	
	
}
