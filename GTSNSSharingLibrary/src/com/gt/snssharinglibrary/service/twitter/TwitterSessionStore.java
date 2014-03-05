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

package com.gt.snssharinglibrary.service.twitter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.sugree.twitter.Twitter;

public class TwitterSessionStore {
    
    private static final String ACCESS_TOKEN = "access_token";
    private static final String SECRET_TOKEN = "expires_in";
    private static final String KEY = "twitter-session";


    public static boolean save(Twitter session, Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(ACCESS_TOKEN, session.getAccessToken());
        editor.putString(SECRET_TOKEN, session.getSecretToken());
        return editor.commit();
    }

    public static boolean restore(Twitter session, Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(ACCESS_TOKEN, null));
        session.setSecretToken(savedSession.getString(SECRET_TOKEN, null));
        return session.isSessionValid();
    }
    
    public static void clear(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
