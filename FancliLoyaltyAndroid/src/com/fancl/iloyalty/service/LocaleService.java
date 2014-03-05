package com.fancl.iloyalty.service;

import java.util.Locale;

import android.content.Context;

import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;

public interface LocaleService {
	public void loadUserPreferredLanguage(Context context);

	public boolean saveLanguagePrefer(Context context, String languageCode);

	public void setLanguageByDeviceSetting(Context context);
	
	public Locale getCurrentLocale();
	
	public LANGUAGE_TYPE getCurrentLanguageType();

	public void resetLanguage(Context context);

	public void setEnglish(Context context, boolean callToServer);

	public void setTChinese(Context context, boolean callToServer);

	public void setSChinese(Context context, boolean callToServer);

	public void sendLanguageSettingToServer(Context context, LANGUAGE_TYPE languageType);

	public String languageStringForHttp(LANGUAGE_TYPE languageType);

	public LANGUAGE_TYPE languageStrForApplication(String languageStrFromHttp);

	public String textByLangaugeChooser(Context context, String textEn, String textTc, String textSc);
	
	public String errorMessaeByLangaugeChooser(GeneralException generalException);

	public String checkLangaugeTextNotNull(String textEN, String textUnknow);
}
