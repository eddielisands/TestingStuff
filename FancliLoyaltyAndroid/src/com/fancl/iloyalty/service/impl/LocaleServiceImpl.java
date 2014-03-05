package com.fancl.iloyalty.service.impl;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.NetworkConnective;

public class LocaleServiceImpl implements LocaleService{
	public final static String LANGUAGE_FOR_HTTP_EN = "en";
	public final static String LANGUAGE_FOR_HTTP_TC = "tc";
	public final static String LANGUAGE_FOR_HTTP_SC = "sc";

	public final static String LOCALE_EN_CODE = "en";
	public final static String LOCALE_TC_CODE = "zh-TW";
	public final static String LOCALE_SC_CODE = "zh-CN";

	public enum LANGUAGE_TYPE {
		EN, TC, SC
	}

	public LANGUAGE_TYPE currentLanguage = LANGUAGE_TYPE.EN;

	@Override
	public void loadUserPreferredLanguage(Context context) {

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String preferredLanguage = sharedPreferences.getString(
				Constants.SHARED_PREFERENCE_LANGUAGE_SETTING_KEY, null);

		if (preferredLanguage == null) {
			this.setLanguageByDeviceSetting(context);
		} else {
			if (preferredLanguage.equals(LOCALE_EN_CODE)) {
				setEnglish(context, true);
			} else if (preferredLanguage.equals(LOCALE_TC_CODE)) {
				setTChinese(context, true);
			} else if (preferredLanguage.equals(LOCALE_SC_CODE)) {
				setSChinese(context, true);
			} else {
				setEnglish(context, true);
			}
		}
	}

	@Override
	public boolean saveLanguagePrefer(Context context, String languageCode) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		return sharedPreferences
				.edit()
				.putString(Constants.SHARED_PREFERENCE_LANGUAGE_SETTING_KEY,
						languageCode).commit();
	}

	@Override
	public void setLanguageByDeviceSetting(Context context) {
		Locale userSystemPreferLanguage = Locale.getDefault();
		if (userSystemPreferLanguage.equals(Locale.ENGLISH)) {
			setEnglish(context, true);
		} else if (userSystemPreferLanguage.equals(Locale.CHINESE)) {
			setTChinese(context, true);
		} else if (userSystemPreferLanguage.equals(Locale.CHINA)) {
			setSChinese(context, true);
		} else if (userSystemPreferLanguage.equals(Locale.TRADITIONAL_CHINESE)) {
			setTChinese(context, true);
		} else if (userSystemPreferLanguage.equals(Locale.TAIWAN)) {
			setTChinese(context, true);
		} else if (userSystemPreferLanguage.equals(Locale.SIMPLIFIED_CHINESE)) {
			setSChinese(context, true);
		} else {
			setEnglish(context, true);
		}
	}

	@Override
	public Locale getCurrentLocale() {
		if (currentLanguage.equals(LANGUAGE_TYPE.EN)) {
			return Locale.ENGLISH;
		} else if (currentLanguage.equals(LANGUAGE_TYPE.TC)) {
			return Locale.TRADITIONAL_CHINESE;
		} else if (currentLanguage.equals(LANGUAGE_TYPE.SC)) {
			return Locale.SIMPLIFIED_CHINESE;
		} else {
			return Locale.ENGLISH;
		}
	}

	@Override
	public LANGUAGE_TYPE getCurrentLanguageType() {
		if (currentLanguage.equals(LANGUAGE_TYPE.EN)) {
			return LANGUAGE_TYPE.EN;
		} else if (currentLanguage.equals(LANGUAGE_TYPE.TC)) {
			return LANGUAGE_TYPE.TC;
		} else if (currentLanguage.equals(LANGUAGE_TYPE.SC)) {
			return LANGUAGE_TYPE.SC;
		} else {
			return LANGUAGE_TYPE.EN;
		}
	}

	@Override
	public void resetLanguage(Context context) {
		Locale currentLocale = getCurrentLocale();
		if (currentLocale.equals(Locale.ENGLISH)) {
			setEnglish(context, false);
		} else if (currentLocale.equals(Locale.TRADITIONAL_CHINESE)) {
			setTChinese(context, false);
		} else if (currentLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
			setSChinese(context, false);
		} else {
			setEnglish(context, false);
		}
	}

	@Override
	public void setEnglish(Context context, boolean callToServer) {
		LogController.log("preferredLanguage >>>> setEnglish");
		if (!(context.getResources().getConfiguration().locale.toString()
				.equals(LocaleServiceImpl.LOCALE_EN_CODE))) {
			Configuration config = new Configuration();
			config.locale = Locale.ENGLISH;
			currentLanguage = LANGUAGE_TYPE.EN;
			Locale.setDefault(Locale.ENGLISH);
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			context.getResources().updateConfiguration(config, dm);

			this.saveLanguagePrefer(context, LOCALE_EN_CODE);

			if (callToServer) {
				sendLanguageSettingToServer(context, LANGUAGE_TYPE.EN);
			}
		}
	}

	@Override
	public void setTChinese(Context context, boolean callToServer) {
		LogController.log("preferredLanguage >>>> setTChinese");
		if (!(context.getResources().getConfiguration().locale.toString()
				.equals(LocaleServiceImpl.LOCALE_TC_CODE))) {
			Configuration config = new Configuration();
			config.locale = Locale.TRADITIONAL_CHINESE;
			currentLanguage = LANGUAGE_TYPE.TC;
			Locale.setDefault(Locale.TRADITIONAL_CHINESE);
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			context.getResources().updateConfiguration(config, dm);

			this.saveLanguagePrefer(context, LOCALE_TC_CODE);

			if (callToServer) {
				sendLanguageSettingToServer(context, LANGUAGE_TYPE.TC);
			}
		}
	}

	@Override
	public void setSChinese(Context context, boolean callToServer) {
		LogController.log("preferredLanguage >>>> setSChinese");
		if (!(context.getResources().getConfiguration().locale.toString()
				.equals(LocaleServiceImpl.LOCALE_SC_CODE))) {
			Configuration config = new Configuration();
			config.locale = Locale.SIMPLIFIED_CHINESE;
			currentLanguage = LANGUAGE_TYPE.SC;
			Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			context.getResources().updateConfiguration(config, dm);

			this.saveLanguagePrefer(context, LOCALE_SC_CODE);

			if (callToServer) {
				sendLanguageSettingToServer(context, LANGUAGE_TYPE.SC);
			}
		}
	}

	@Override
	public synchronized void sendLanguageSettingToServer(Context context,
			LANGUAGE_TYPE languageType) {
		if (NetworkConnective.checkNetwork(context)) {
			// process of send new language setting to server side
		}
	}

	@Override
	public String languageStringForHttp(LANGUAGE_TYPE languageType) {
		if (languageType == null) {
			return LocaleServiceImpl.LANGUAGE_FOR_HTTP_EN;
		}

		if (languageType.equals(LANGUAGE_TYPE.EN)) {
			return LocaleServiceImpl.LANGUAGE_FOR_HTTP_EN;
		} else if (languageType.equals(LANGUAGE_TYPE.TC)) {
			return LocaleServiceImpl.LANGUAGE_FOR_HTTP_TC;
		} else if (languageType.equals(LANGUAGE_TYPE.SC)) {
			return LocaleServiceImpl.LANGUAGE_FOR_HTTP_SC;
		} else {
			return LocaleServiceImpl.LANGUAGE_FOR_HTTP_EN;
		}
	}

	@Override
	public LANGUAGE_TYPE languageStrForApplication(String languageStrFromHttp) {
		if (languageStrFromHttp == null) {
			return LANGUAGE_TYPE.EN;
		}

		if (languageStrFromHttp.equals(LocaleServiceImpl.LANGUAGE_FOR_HTTP_EN)) {
			return LANGUAGE_TYPE.EN;
		} else if (languageStrFromHttp
				.equals(LocaleServiceImpl.LANGUAGE_FOR_HTTP_TC)) {
			return LANGUAGE_TYPE.TC;
		} else if (languageStrFromHttp
				.equals(LocaleServiceImpl.LANGUAGE_FOR_HTTP_SC)) {
			return LANGUAGE_TYPE.SC;
		} else {
			return LANGUAGE_TYPE.EN;
		}
	}

	@Override
	public String textByLangaugeChooser(Context context, String textEn,
			String textTc, String textSc) {
		if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			return textEn;
		} else if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			return checkLangaugeTextNotNull(textEn, textTc);
		} else if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			return checkLangaugeTextNotNull(textEn, textSc);
		} else {
			return textEn;
		}
	}

	@Override
	public String errorMessaeByLangaugeChooser(GeneralException generalException) {
		if (generalException == null) {
			return null;
		}

		if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.EN)) {
			return generalException.getErrMsgEn();
		} else if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.TC)) {
			return generalException.getErrMsgTc();
		} else if (this.getCurrentLanguageType().equals(LANGUAGE_TYPE.SC)) {
			return generalException.getErrMsgSc();
		} else {
			return generalException.getErrMsgEn();
		}
	}

	@Override
	public String checkLangaugeTextNotNull(String textEN, String textUnknow) {
		if (textUnknow == null || textUnknow.trim().equals(""))
			return textEN;
		else
			return textUnknow;
	}
}
