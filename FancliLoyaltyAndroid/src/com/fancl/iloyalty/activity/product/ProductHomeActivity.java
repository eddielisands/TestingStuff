package com.fancl.iloyalty.activity.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.R.color;
import com.fancl.iloyalty.activity.CustomSpinnerActivity;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.activity.login.SignUpFormActivity;
import com.fancl.iloyalty.adapter.ProductCategoryListAdapter;
import com.fancl.iloyalty.adapter.ProductSeasonalHomeListAdapter;
import com.fancl.iloyalty.adapter.ProductSubCategoryListAdapter;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.CustomTabBar;
import com.fancl.iloyalty.item.callback.CustomTabBarCallback;
import com.fancl.iloyalty.pojo.Product;
import com.fancl.iloyalty.pojo.ProductAnswer;
import com.fancl.iloyalty.pojo.ProductCategory;
import com.fancl.iloyalty.pojo.ProductQuestion;
import com.fancl.iloyalty.pojo.ProductSeries;
import com.fancl.iloyalty.pojo.Setting;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;


public class ProductHomeActivity extends MainTabActivity implements
CustomTabBarCallback {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 6.1, 6.1.1
	private int currentTabBarIndex = 0;
	private int currentSubTabBarIndex = 0;

	private CustomTabBar categoryTabBar;
	private CustomTabBar subCategoryTabBar;

	private String[] mainCateType = new String[] { "seasonal", "qna", "products" };

	private List<String> subCateList = new ArrayList<String>();
	private List<ProductCategory> parentCateList = new ArrayList<ProductCategory>();
	private List<ProductCategory> articleList = new ArrayList<ProductCategory>(); 
	
	private List<Product> articleQnaList = new ArrayList<Product>();
	
	private RelativeLayout productQnaLayout;
	private LinearLayout productQnaCompletedLayout;
	private LinearLayout productSeasonalLayout;
	
	private LocaleService localeService;
	
	Setting seasonalIntroList;
	private List<Map<String, String>> productTypeList = new ArrayList<Map<String, String>>();   
	private List<Map<String, String>> productList = new ArrayList<Map<String, String>>();
	
	private List<ProductCategory> seasonalProductTypeList;
	private List<Product> seasonalProductList;
	 
	List<Product> productSeasonal =new ArrayList<Product>();
	
	private ListView categoryListView;
	private ProductCategoryListAdapter productCategoryListViewAdapter;
	
	private List<ProductSeries> productSeriesList;
	
	private int productSeriesRow = 0;
	
	private List<ProductQuestion> productQuestionList;
	
	private ProductSubCategoryListAdapter productSubCategoryListViewAdapter;
	
	private RelativeLayout productCateContentLayout;
	
	private Boolean isCreateQna = false;
	private Boolean isCreateProductCate = false;
	private Boolean isCreateSeasonal = false;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        localeService = GeneralServiceFactory.getLocaleService();
        
        navigationBarSearchBtn.setVisibility(View.VISIBLE);
        navigationBarLeftBtn.setVisibility(View.VISIBLE);
        
        headerTitleTextView.setText(this.getResources().getString(
				R.string.menu_product_btn_title));

		this.setupSpaceLayout();

		this.setupMenuButtonListener(4, true);

    }

	private void setupSpaceLayout() {
	// TODO Auto-generated method stub
		// Space Layout
		LinearLayout productLayout = new LinearLayout(this);
		spaceLayout.addView(productLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		productLayout.setOrientation(LinearLayout.VERTICAL);

		// Tab Bar
		List<String> tabBarList = new ArrayList<String>();
		tabBarList.add(this.getResources().getString(
				R.string.product_seasonal_picks_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.product_qna_tab_bar_title));
		tabBarList.add(this.getResources().getString(
				R.string.product_catalogue_tab_bar_title));

		categoryTabBar = new CustomTabBar(this, currentTabBarIndex, tabBarList,
				DataUtil.dip2integerPx(this, 33), false, true,this);
		productLayout.addView(categoryTabBar, LayoutParams.MATCH_PARENT,
				DataUtil.dip2integerPx(this, 33));

		subCateList.add("1");
		subCateList.add("2");
		subCateList.add("3");
		subCateList.add("4");
		
		subCategoryTabBar = new CustomTabBar(this, 0, subCateList,
				DataUtil.dip2integerPx(this, 33), true,true, this);
		productLayout.addView(subCategoryTabBar, LayoutParams.MATCH_PARENT,
				DataUtil.dip2integerPx(this, 33));
		
		subCategoryTabBar.setVisibility(View.GONE);
		
		productCateContentLayout = new RelativeLayout(this);
		productLayout.addView(productCateContentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		

		//Seasonal page
		productSeasonalLayout = new LinearLayout(this);
		productCateContentLayout.addView(productSeasonalLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		try {
			seasonalIntroList = CustomServiceFactory.getProductService().getSesaonalDescription();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		ListView seasonalList = new ListView(this);
		seasonalList.setCacheColorHint(color.transparent);
		seasonalList.setDividerHeight(0);
		seasonalList.setScrollingCacheEnabled(false);
		productSeasonalLayout.addView(seasonalList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		seasonalList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.product_home_seasonal_intro, null));
		TextView seasonalTitletext = (TextView)findViewById(R.id.seasonal_title);
		TextView seasonalIntrotext = (TextView)findViewById(R.id.seasonal_intro);
		if(seasonalIntroList != null){
			
			seasonalTitletext.setText(localeService.textByLangaugeChooser(this, seasonalIntroList.getTitleEn(), seasonalIntroList.getTitleZh(), seasonalIntroList.getTitleSc()));
			seasonalIntrotext.setText(localeService.textByLangaugeChooser(this, seasonalIntroList.getDescriptionEn(), seasonalIntroList.getDescriptionZh(), seasonalIntroList.getDescriptionSc()));
		}
		
		try {
			seasonalProductTypeList = CustomServiceFactory.getProductService().getSeasonalProductCategory();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setData();
		ProductSeasonalHomeListAdapter adapter = new ProductSeasonalHomeListAdapter(this, productList, productTypeList);
		seasonalList.setAdapter(adapter); 
		seasonalList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub

//            	LogController.log("arg2:"+ arg2);
            	
            	if(arg2!=0){
            		Intent intent = new Intent(ProductHomeActivity.this, ProductDetailActivity.class);
            		intent.putExtra(Constants.PRODUCT_ITEM_KEY, productSeasonal.get(arg2-1));
            		startActivity(intent);
            	}

            }

        });
		
		navigationBarSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(ProductHomeActivity.this, ProductSearchActivity.class);
            	startActivity(intent);
			}
		});
		

}

	private void loadArticleFromApi(String subCate) {
		// TODO Auto-generated method stub
		try {
			articleList = CustomServiceFactory
					.getProductService().getProductSubCategoryListWithParentId(subCate);
			productCategoryListViewAdapter.setArticleList(articleList);
			categoryListView.setSelectionAfterHeaderView();
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private void setData() {
		// TODO Auto-generated method stub

		Map<String, String> mp;  
	

//		LogController.log("seasonalProductTypeList:"+ seasonalProductTypeList.size());

		for (int i = 0; i < seasonalProductTypeList.size(); i++) {

			if (seasonalProductTypeList.size() != 0) {
				ProductCategory seasonalType = seasonalProductTypeList.get(i);
				mp = new HashMap<String, String>();  
				mp.put("itemTitle", localeService.textByLangaugeChooser(this, seasonalType.getTitleEn(), seasonalType.getTitleZh(), seasonalType.getTitleSc()));  
				productList.add(mp);  
				productTypeList.add(mp); 
				productSeasonal.add(null);

				try {
					seasonalProductList = CustomServiceFactory.getProductService().getSeasonalProductWithCategoryId(seasonalType.getObjectId());
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				for (int j = 0; j < seasonalProductList.size(); j++) {  
					Product productItem = seasonalProductList.get(j);

					Map<String, String> map = new HashMap<String, String>();  
					if(productItem != null){
						map.put("itemTitleEn", productItem.getTitleEn());  
						map.put("itemTitleTc", localeService.textByLangaugeChooser(this, productItem.getTitleZh(), productItem.getTitleZh(), productItem.getTitleSc()));
						map.put("itemImage", productItem.getThumbnail());
						productSeasonal.add(productItem);
					}
					productList.add(map);  

					
				}
				
//				LogController.log("productTypeList:"+ productTypeList);
			}
		}

		
	}

	@Override
	public void clickedIndex(CustomTabBar customTabBar, int index) {
		// TODO Auto-generated method stub
		if (customTabBar.equals(categoryTabBar)) {
			currentTabBarIndex = index;
			currentSubTabBarIndex = 0;
			
			List<ProductAnswer> answerCheckList = null;
			try {
				answerCheckList = CustomServiceFactory.getProductService().getUserQnaAnswer();
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			if(index == 0){
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "Product Season Category", "", "", "", "ButtonClick", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				subCategoryTabBar.setVisibility(View.GONE);
				productSeasonalLayout.setVisibility(View.VISIBLE);
				if(isCreateProductCate)
					categoryListView.setVisibility(View.GONE);
				if(isCreateQna){
					productQnaLayout.setVisibility(View.GONE);
					productQnaCompletedLayout.setVisibility(View.GONE);
				}
			}else if (index == 1) {
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "Product Question", "", "", "", "ButtonClick", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				subCategoryTabBar.setVisibility(View.GONE);
				productSeasonalLayout.setVisibility(View.GONE);
				if(isCreateProductCate)
					categoryListView.setVisibility(View.GONE);
				if(isCreateQna){
					if(answerCheckList == null){
						productQnaCompletedLayout.setVisibility(View.GONE);
						productQnaLayout.setVisibility(View.VISIBLE);
					}else{
						productQnaCompletedLayout.setVisibility(View.VISIBLE);
						productQnaLayout.setVisibility(View.GONE);
					}
				}else{
					this.createQnaView();
				}
					
			}else{
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "Product Category", "", "", "", "ButtonClick", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				subCategoryTabBar.setVisibility(View.VISIBLE);
				productSeasonalLayout.setVisibility(View.GONE);
				if(isCreateQna){
					productQnaLayout.setVisibility(View.GONE);
					productQnaCompletedLayout.setVisibility(View.GONE);
				}
				if(isCreateProductCate)
					categoryListView.setVisibility(View.VISIBLE);
				else
					this.createProductCateView();
				
			}

			
		} else if (customTabBar.equals(subCategoryTabBar)) {
			currentSubTabBarIndex = index;
			

			loadArticleFromApi(parentCateList.get(currentSubTabBarIndex).getObjectId());
			
			try {
				CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "", "", "", parentCateList.get(currentSubTabBarIndex).getTitleEn(), "View", "");
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
	}

	private void loadSubCateListFromApi(String string) {
		// TODO Auto-generated method stub
		ProductSeries productSeries = productSeriesList.get(productSeriesRow);
		try {
			subCateList.clear();
			parentCateList = CustomServiceFactory
					.getProductService().getProductPartentCategoryListWithSeriesId(productSeries.getId());
			for (int i = 0; i < parentCateList.size(); i++) {


					subCateList.add(localeService.textByLangaugeChooser(this, parentCateList.get(i).getTitleEn(), parentCateList.get(i).getTitleZh(), parentCateList.get(i).getTitleSc()));

			}
			
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
		case (Constants.CUSTOM_SPINNER_PRODUCT) : { 
			if (resultCode == ProductHomeActivity.RESULT_OK) { 
				int newInt = data.getIntExtra(Constants.CUSTOM_SPINNER_RETURN_KEY, 0);

				// TODO Update your TextView.
//				LogController.log("ProductFormActivity onActivityResult : " + newInt);

				List<ProductSeries> productSeriesList = null;
				try {
					productSeriesList = CustomServiceFactory.getProductService().getProductSeriesList();
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



				productSeriesRow = newInt;

				loadSubCateListFromApi(mainCateType[2]);
				subCategoryTabBar.resetTabBar(subCateList, true);	


				this.getPickerView();

				subCategoryTabBar.setPickerText(productSeriesList.get(newInt));
				currentSubTabBarIndex = 0;
				loadArticleFromApi(parentCateList.get(currentSubTabBarIndex).getObjectId());
			}
			break; 
		}
		case (Constants.PRODUCT_QNA) : { 
			if (resultCode == ProductHomeActivity.RESULT_OK) { 
				LogController.log("qna complete reload product home page");
				
				productQnaCompletedLayout.setVisibility(View.VISIBLE);
				productQnaLayout.setVisibility(View.INVISIBLE);
				

				
				try {
					articleQnaList = CustomServiceFactory
									.getProductService().getUserQnaSuggestProduct();
				} catch (FanclException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
//				LogController.log("articleQnaList:"+articleQnaList.size());
				productSubCategoryListViewAdapter.setArticleList(articleQnaList);
			

				
				
//				SharedPreferences sharedPreferences = application.getSharedPreferences(
//						Constants.SHARED_PREFERENCE_APPLICATION_KEY,
//						Context.MODE_PRIVATE);
//				String qnaAnswer = sharedPreferences.getString(Constants.QNA_ANSWER_ID, null);
				List<ProductAnswer> answerList = null;
				try {
					answerList = CustomServiceFactory.getProductService().getUserQnaAnswer();
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				


				
				for (int i = 0; i < productQuestionList.size(); i++) {
					TextView answerText = (TextView) findViewById(i);
					
					answerText.setText("Question" + (i+1) + ": "+localeService.textByLangaugeChooser(this, answerList.get(i).getAnswerEn(), answerList.get(i).getAnswerZh(), answerList.get(i).getAnswerSc()));

				}
				

			}
			break; 
		}

		}


 
	}
	
	public void pickerAction() {
//		LogController.log("pickerAction");
	}
	
	private String[] formContentToStringArray(
			List<ProductSeries> productSeriesList) {
		// TODO Auto-generated method stub
	
		
		String[] currentLangContent = new String[productSeriesList.size()];
		
		for (int i = 0; i < productSeriesList.size(); i++) {
			ProductSeries productItem = productSeriesList.get(i);
			

				
				currentLangContent[i] = localeService.textByLangaugeChooser(this, productItem.getTitleEn(), productItem.getTitleZh(), productItem.getTitleSc());

		}
		
		return currentLangContent;
	}
	
	public void getPickerView() {
		RelativeLayout pickerView = (RelativeLayout) subCategoryTabBar.getTextView();
		pickerView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {

				LogController.log("pickerAction");
				
				Intent intent = new Intent(ProductHomeActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(productSeriesList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_PRODUCT);

			}

			
		});
		
	}
	
	public void createQnaView() {
		
		LogController.log("first createQnaView");

		isCreateQna = true;
		
		try {
			articleQnaList = CustomServiceFactory
					.getProductService().getUserQnaSuggestProduct();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//QnA page
		productQnaLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.product_home_qna_page, null);
		if(articleQnaList != null)
			productQnaLayout.setVisibility(View.GONE);
		productCateContentLayout.addView(productQnaLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Button startButton = (Button)findViewById(R.id.startButton);
		startButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProductHomeActivity.this, ProductQandAActivity.class);
				startActivityForResult(intent, Constants.PRODUCT_QNA);

			}         

		});

		//QnA Completed page
//		productQnaCompletedLayout = (RelativeLayout) this.getLayoutInflater().inflate(
//				R.layout.product_home_qna_complete_page, null);
		productQnaCompletedLayout = new LinearLayout(this);
		if(articleQnaList == null)
			productQnaCompletedLayout.setVisibility(View.GONE);
		productCateContentLayout.addView(productQnaCompletedLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

//		ListView qnaResultList = (ListView) findViewById(R.id.qnaListView);
		ListView qnaResultList = new ListView(this);
		qnaResultList.setCacheColorHint(color.transparent);
		qnaResultList.setDividerHeight(0);
		qnaResultList.setScrollingCacheEnabled(false);
		productQnaCompletedLayout.addView(qnaResultList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		qnaResultList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.product_home_qna_complete, null));

		productSubCategoryListViewAdapter = new ProductSubCategoryListAdapter(this, this, handler);
		qnaResultList.setAdapter(productSubCategoryListViewAdapter);

		qnaResultList.setSelectionAfterHeaderView();


		Button reStartButton = (Button)findViewById(R.id.reStartButton);
		reStartButton.setOnClickListener(new Button.OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProductHomeActivity.this, ProductQandAActivity.class);
				startActivityForResult(intent, Constants.PRODUCT_QNA);

			}         

		});

		qnaResultList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

//				LogController.log("arg2:"+ arg2);
				if(arg2!=0){

					Product product = articleQnaList.get(arg2-1);

					Intent intent = new Intent(ProductHomeActivity.this, ProductDetailActivity.class);
					intent.putExtra(Constants.PRODUCT_ITEM_KEY, product);
					startActivity(intent);
					
					try {
						CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "ProductDetail", "", product.getObjectId(),product.getTitleEn() , "View", "");
					} catch (FanclException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		});

		

		if(articleQnaList != null){
//			LogController.log("articleQnaList:"+articleQnaList.size());
			productSubCategoryListViewAdapter.setArticleList(articleQnaList);
		}

		List<ProductAnswer> answerList = null;
		try {
			answerList = CustomServiceFactory.getProductService().getUserQnaAnswer();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			productQuestionList = CustomServiceFactory.getProductService().getQnaProductQuestion();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for (int i = 0; i < productQuestionList.size(); i++) {
			LinearLayout answerContent = (LinearLayout)findViewById(R.id.qna_complete_answer_layout);
			TextView answerText = new TextView(this);
			if(answerList != null){
				if(localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)){
					answerText.setText(getString(R.string.qna_complete_question) + (i+1) + ": "+localeService.textByLangaugeChooser(this, answerList.get(i).getAnswerEn(), answerList.get(i).getAnswerZh(), answerList.get(i).getAnswerSc()));
				}else{
					answerText.setText(getString(R.string.qna_step) + (i+1) + getString(R.string.qna_complete_question)+": "+localeService.textByLangaugeChooser(this, answerList.get(i).getAnswerEn(), answerList.get(i).getAnswerZh(), answerList.get(i).getAnswerSc()));
				}
				
			}
			answerText.setId(i);
			answerText.setTextColor(getResources().getColor(R.color.Fancl_Grey));
			LinearLayout.LayoutParams answerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			answerLayoutParams.setMargins(20, 0, 0, 0);
			answerContent.addView(answerText, answerLayoutParams);
		}

	}

	public void createProductCateView() {
		
		LogController.log("first createProductCateView");

		isCreateProductCate = true;

		//Category page
		try {
			productSeriesList = CustomServiceFactory.getProductService().getProductSeriesList();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		productCategoryListViewAdapter = new ProductCategoryListAdapter(this, this, handler);
		categoryListView = new ListView(this);
		categoryListView.setCacheColorHint(color.transparent);
		categoryListView.setDividerHeight(0);
		categoryListView.setScrollingCacheEnabled(false);
		categoryListView.setAdapter(productCategoryListViewAdapter);
		//				categoryListView.setVisibility(View.GONE);
		categoryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text

//				LogController.log("position : " + position + "| id : " + id);
				List<ProductCategory> tmpArticleList = null;
				try {
					tmpArticleList = CustomServiceFactory
							.getProductService().getProductSubCategoryListWithParentId(parentCateList.get(currentSubTabBarIndex).getObjectId());
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ProductCategory productCate = tmpArticleList.get(position);

				Intent intent = new Intent(ProductHomeActivity.this, ProductCategoryListActivity.class);
				intent.putExtra("PRODUCT_CATE", productCate.getObjectId());
				intent.putExtra("PRODUCT_CATE_TITLE", localeService.textByLangaugeChooser(ProductHomeActivity.this, productCate.getTitleEn(), productCate.getTitleZh(), productCate.getTitleSc()));
				intent.putExtra("PRODUCT_CATE_DES", localeService.textByLangaugeChooser(ProductHomeActivity.this, productCate.getDescriptionEn(), productCate.getDescriptionZh(), productCate.getDescriptionSc()));
				startActivity(intent);
				
				try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "ProductCategoryTable", "", productCate.getObjectId(),productCate.getTitleEn() , "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		productCateContentLayout.addView(categoryListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


		loadSubCateListFromApi(mainCateType[0]);
		subCategoryTabBar.resetTabBar(subCateList, true);
		subCategoryTabBar.setPickerText(productSeriesList.get(0));
		RelativeLayout pickerView = (RelativeLayout) subCategoryTabBar.getTextView();
		pickerView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {

				LogController.log("pickerAction");

				Intent intent = new Intent(ProductHomeActivity.this, CustomSpinnerActivity.class);
				intent.putExtra(Constants.CUSTOM_SPINNER_STRING_ARRAY, formContentToStringArray(productSeriesList));
				startActivityForResult(intent, Constants.CUSTOM_SPINNER_PRODUCT);

			}


		});

		loadArticleFromApi(parentCateList.get(currentSubTabBarIndex).getObjectId());

	}
	
	public void createSeasonalView() {
		//Seasonal page
//		productSeasonalLayout = (RelativeLayout) this.getLayoutInflater().inflate(
//				R.layout.product_home_seasonal_page, null);
		productSeasonalLayout = new LinearLayout(this);
		productCateContentLayout.addView(productSeasonalLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		try {
			seasonalIntroList = CustomServiceFactory.getProductService().getSesaonalDescription();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		ListView seasonalList = (ListView) findViewById(R.id.seasonalListView);
		ListView seasonalList = new ListView(this);
		seasonalList.setCacheColorHint(color.transparent);
		seasonalList.setDividerHeight(0);
		seasonalList.setScrollingCacheEnabled(false);
		productSeasonalLayout.addView(seasonalList, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		seasonalList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.product_home_seasonal_intro, null));
		TextView seasonalTitletext = (TextView)findViewById(R.id.seasonal_title);
		TextView seasonalIntrotext = (TextView)findViewById(R.id.seasonal_intro);
		if(seasonalIntroList != null){
			
			seasonalTitletext.setText(localeService.textByLangaugeChooser(this, seasonalIntroList.getTitleEn(), seasonalIntroList.getTitleZh(), seasonalIntroList.getTitleSc()));
			seasonalIntrotext.setText(localeService.textByLangaugeChooser(this, seasonalIntroList.getDescriptionEn(), seasonalIntroList.getDescriptionZh(), seasonalIntroList.getDescriptionSc()));
		}
		
		try {
			seasonalProductTypeList = CustomServiceFactory.getProductService().getSeasonalProductCategory();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setData();
		ProductSeasonalHomeListAdapter adapter = new ProductSeasonalHomeListAdapter(this, productList, productTypeList);
		seasonalList.setAdapter(adapter); 
		seasonalList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub

            	
            	try {
					CustomServiceFactory.getSettingService().addUserLogWithSection("Product", "ProductDetail", "", productSeasonal.get(arg2-1).getObjectId(),productSeasonal.get(arg2-1).getTitleEn() , "View", "");
				} catch (FanclException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	Intent intent = new Intent(ProductHomeActivity.this, ProductDetailActivity.class);
            	intent.putExtra(Constants.PRODUCT_ITEM_KEY, productSeasonal.get(arg2-1));
            	startActivity(intent);
            	
            	

            }

        });
		
	}


	
}
