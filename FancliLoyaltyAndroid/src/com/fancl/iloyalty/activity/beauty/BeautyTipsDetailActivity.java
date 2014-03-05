package com.fancl.iloyalty.activity.beauty;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.MainTabActivity;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.viewpagerindicator.IconPageIndicator;

public class BeautyTipsDetailActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 5.1.3, 5.2.3
	private LocaleService localeService;
	
	private IchannelMagazine ichannelMagazine;
	
	private List<IchannelMagazine> detailContentList;
	
	private RelativeLayout beautyTipsDetailLayout;
	private RelativeLayout beautyTipsDetailTextSize;
	private RelativeLayout beautyTipsDetailRelatedProduct;
	
	private TextView beautyTipsDetailTitle;
	private TextView beautyTipsDetailDescription;
	
	private ViewPager beautyTipsDetailViewPager;
	
	private IconPageIndicator beautyTipsDetailIconIndicator;
	
	private int currentTextSize = 12;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ichannelMagazine = (IchannelMagazine)this.getIntent().getExtras().getSerializable(Constants.BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY);
        
        loadIChannelDescriptioinListFromDatabase(ichannelMagazine.getObjectId());

        localeService = GeneralServiceFactory.getLocaleService();
        
        headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_btn));
        
        this.setupSpaceLayout();
        
        this.setupThumbnail();
                
        this.setupMenuButtonListener(0, true);
    }
	
	private void setupSpaceLayout() {
		// Space Layout
		beautyTipsDetailLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.beauty_ichannel_detail_page, null);
		spaceLayout.addView(beautyTipsDetailLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Title
		beautyTipsDetailTitle = (TextView) beautyTipsDetailLayout.findViewById(R.id.beauty_tips_detail_title);
		if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			beautyTipsDetailTitle.setText(ichannelMagazine.getTitleEn());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			beautyTipsDetailTitle.setText(ichannelMagazine.getTitleSc());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			beautyTipsDetailTitle.setText(ichannelMagazine.getTitleZh());
		}
		
		// Thumbnail View Pager
		beautyTipsDetailViewPager = (ViewPager) beautyTipsDetailLayout.findViewById(R.id.beauty_tips_detail_view_pager);
		
		// Descriptioin
		String descriptionStr = "";
		for (int i = 0; i < detailContentList.size(); i++) {
			IchannelMagazine tmpIchannelMagazine = detailContentList.get(i);
			if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
				descriptionStr = descriptionStr + tmpIchannelMagazine.getDescriptionEn();
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
				descriptionStr = descriptionStr + tmpIchannelMagazine.getDescriptionSc();
			} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
				descriptionStr = descriptionStr + tmpIchannelMagazine.getDescriptionZh();
			}			
		}
		beautyTipsDetailDescription = (TextView) beautyTipsDetailLayout.findViewById(R.id.beauty_tips_detail_description);
		beautyTipsDetailDescription.setText(descriptionStr);
		
		// Text Size
		beautyTipsDetailTextSize = (RelativeLayout) beautyTipsDetailLayout.findViewById(R.id.beauty_tips_detail_text_size);
		beautyTipsDetailTextSize.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if (currentTextSize == 12) {
					currentTextSize = 18;
				} else {
					currentTextSize = 12;
				}
				beautyTipsDetailDescription.setTextSize(currentTextSize);
			}
		});
		
		// Read More
//		final Intent intent = new Intent(this, BeautyRelatedProductActivity.class);
		
		beautyTipsDetailRelatedProduct = (RelativeLayout) beautyTipsDetailLayout.findViewById(R.id.beauty_tips_detail_related_product);
		beautyTipsDetailRelatedProduct.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				
//				startActivity(intent);
			}
		});
	}
	
	private void setupThumbnail() {
//		beautyTipsDetailViewPager.setAdapter(new WhatsHotHomeViewPagerAdapter(this, bannerList, handler));
		beautyTipsDetailViewPager.setCurrentItem(0);

		beautyTipsDetailIconIndicator = (IconPageIndicator) findViewById(R.id.viewpager_icon_indicator);
		beautyTipsDetailIconIndicator.setViewPager(beautyTipsDetailViewPager);
	}
	
	private void loadIChannelDescriptioinListFromDatabase(String objectId) {
		try {
			detailContentList = CustomServiceFactory.getPromotionService()
					.getIchannelDescriptionWithIchannelId(objectId);
		} catch (GeneralException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
