/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gt.snssharinglibrary.service.facebook_2_0;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Session;
import com.facebook_2_0.android.Facebook;
import com.gt.snssharinglibrary.util.StringUtil;;

public class FacebookSessionStore {
    
    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-session";

	// store name and id
	private static final String INFO_KEY = "facebook-info";
	private static final String USERNAME = "facebook-username";
	private static final String FACEBOOK_ID = "facebook-id";
	private static final String FACEBOOK_EMAIL = "facebook-email";
	private static final String FACEBOOK_FIRST_NAME = "facebook-first-name";
	private static final String FACEBOOK_MIDDLE_NAME = "facebook-middle-name";
	private static final String FACEBOOK_LAST_NAME = "facebook-last-name";
	private static final String FACEBOOK_GENDER = "facebook-gender";
	private static final String FACEBOOK_BIRTHDAY = "facebook-birthday";
	
	private static final String FACEBOOK_LOGIN_STATUS_KEY = "facebook-login-status-key";
	private static final String	FACEBOOK_LOGIN_STATUS = "facebook-login-status";

    public static boolean save(Facebook session, Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        editor.putLong(EXPIRES, session.getAccessExpires());
        return editor.commit();
    }
    
    public static boolean save(Session session, Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        
        if(session.getExpirationDate() != null)
		{
			editor.putLong(EXPIRES, session.getExpirationDate().getTime());
		}
		
        return editor.commit();
    }

    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(TOKEN, null));
        session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
        return session.isSessionValid();
    }
    
	public static boolean saveFacebookInfo(String facebookUsername,
			String facebookId, String email, Context context) {
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(USERNAME, facebookUsername);
		editor.putString(FACEBOOK_ID, facebookId);
		editor.putString(FACEBOOK_EMAIL, email);
		return editor.commit();
	}
	
	public static boolean saveFacebookFirstName(Context context, String firstName)
	{
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_FIRST_NAME, firstName);
		return editor.commit();
	}
	
	public static boolean saveFacebookMiddleName(Context context, String middleName)
	{
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_MIDDLE_NAME, middleName);
		return editor.commit();
	}
	
	public static boolean saveFacebookLastName(Context context, String lastName)
	{
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_LAST_NAME, lastName);
		return editor.commit();
	}
	
	public static boolean saveFacebookBirthday(Context context, String birthday)
	{
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_BIRTHDAY, birthday);
		return editor.commit();
	}
	
	public static boolean saveFacebookGender(Context context, String gender)
	{
		Editor editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(FACEBOOK_GENDER, gender);
		return editor.commit();
	}

	public static String restoreAccessToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		return savedSession.getString(TOKEN, null);
	}
	
	public static String restoreUsername(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(USERNAME, null);
	}

	public static String restoreFacebookId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_ID, null);
	}

	public static String restoreFacebookEmail(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_EMAIL, null);
	}
	
	public static String restoreFacebookFirstName(Context context)
	{
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_FIRST_NAME, null);
	}
	
	public static String restoreFacebookMiddleName(Context context)
	{
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_MIDDLE_NAME, null);
	}
	
	public static String restoreFacebookLastName(Context context)
	{
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_LAST_NAME, null);
	}
	
	public static String restoreFacebookGender(Context context)
	{
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_GENDER, null);
	}
	
	public static String restoreFacebookBirthday(Context context)
	{
		SharedPreferences savedSession = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE);
		return savedSession.getString(FACEBOOK_BIRTHDAY, null);
	}

	public static boolean isHasFacebookToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		String token = savedSession.getString(TOKEN, null);
         if(StringUtil.isStringEmpty(token))
         {
        	 return false;
         }
         else
         {
        	 return true;
         }
    }
    
    public static void clear(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        
        editor = context.getSharedPreferences(INFO_KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
	
	public static boolean saveUserLoggedToFacebook(Context context, boolean loggedOn) {
		Editor editor = context.getSharedPreferences(FACEBOOK_LOGIN_STATUS_KEY, Context.MODE_PRIVATE).edit();
		editor.putBoolean(FACEBOOK_LOGIN_STATUS, loggedOn);
		return editor.commit();
	}

	public static boolean isUserLoggedOnFacebook(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(FACEBOOK_LOGIN_STATUS_KEY, Context.MODE_PRIVATE);
		return savedSession.getBoolean(FACEBOOK_LOGIN_STATUS, false);
	}
}
