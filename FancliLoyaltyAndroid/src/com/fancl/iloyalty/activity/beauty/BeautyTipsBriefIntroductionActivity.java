package com.fancl.iloyalty.activity.beauty;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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

public class BeautyTipsBriefIntroductionActivity extends MainTabActivity {

	// WRT <<iloyalty_flow_V1.7_20120713.pdf>> screen 5.1.1, 5.1.2, 5.2.1, 5.2.2
	
	private LocaleService localeService;
	
	private RelativeLayout beautyDetailLayout;
	private RelativeLayout beautyDetailThumbnail;
	private RelativeLayout beautyDetailTextSize;
	private RelativeLayout beautyDetailReadMore;
	
	private TextView beautyDetailTitle;
	private TextView beautyDetailDescription;
	
	private float currentTextSize = 12;
	
	private IchannelMagazine ichannelMagazine;
	
	private List<IchannelMagazine> detailContentList = new ArrayList<IchannelMagazine>();
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ichannelMagazine = (IchannelMagazine)this.getIntent().getExtras().getSerializable(Constants.BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY);
        
        loadIChannelDescriptioinListFromDatabase(ichannelMagazine.getObjectId());

        localeService = GeneralServiceFactory.getLocaleService();
        
        headerTitleTextView.setText(this.getResources().getString(R.string.whats_hot_btn));
        
        this.setupSpaceLayout();
                
        this.setupMenuButtonListener(0, true);
    }
	
	private void setupSpaceLayout() {
		// Space Layout
		beautyDetailLayout = (RelativeLayout) this.getLayoutInflater().inflate(
				R.layout.beauty_ichannel_brief_intro_page, null);
		spaceLayout.addView(beautyDetailLayout, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		// Title
		beautyDetailTitle = (TextView) beautyDetailLayout.findViewById(R.id.beauty_detail_title);
		if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			beautyDetailTitle.setText(ichannelMagazine.getTitleEn());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			beautyDetailTitle.setText(ichannelMagazine.getTitleSc());
		} else if (localeService.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			beautyDetailTitle.setText(ichannelMagazine.getTitleZh());
		}
		
		// Thumbnail 
		beautyDetailThumbnail = (RelativeLayout) beautyDetailLayout.findViewById(R.id.beauty_detail_thumbnail);
		
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
		beautyDetailDescription = (TextView) beautyDetailLayout.findViewById(R.id.beauty_detail_description);
		beautyDetailDescription.setText(descriptionStr);
		
		// Text Size
		beautyDetailTextSize = (RelativeLayout) beautyDetailLayout.findViewById(R.id.beauty_detail_text_size);
		beautyDetailTextSize.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if (currentTextSize == 12) {
					currentTextSize = 18;
				} else {
					currentTextSize = 12;
				}
				beautyDetailDescription.setTextSize(currentTextSize);
			}
		});
		
		// Read More
		final Intent intent = new Intent(this, BeautyTipsDetailActivity.class);
		
		beautyDetailReadMore = (RelativeLayout) beautyDetailLayout.findViewById(R.id.beauty_detail_read_more);
		beautyDetailReadMore.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				
				startActivity(intent);
			}
		});
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
