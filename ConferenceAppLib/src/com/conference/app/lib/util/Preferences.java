package com.conference.app.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Preferences {
    private static final String TAG = Preferences.class.getName();
    private static final boolean DEBUG = false;

    private static final String VERSION = "version";
    private static final String FIRST_START = "firststart";
    private static final String DOWNLOAD_FINISHED = "downloadfinished";

    private final SharedPreferences sharedPrefs;

    public Preferences(final Context ctx) {
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void setVersion(final int version) {
        sharedPrefs.edit().putInt(VERSION, version).commit();
    }

    public int getVersion() {
        return sharedPrefs.getInt(VERSION, -1);
    }

    public void setDownloadFinished(final boolean isFinished) {
        sharedPrefs.edit().putBoolean(DOWNLOAD_FINISHED, isFinished).commit();
    }

    public boolean isDownloadFinished() {
        return sharedPrefs.getBoolean(DOWNLOAD_FINISHED, false);
    }

    public void setFirstStart(final boolean isFirstStart) {
        sharedPrefs.edit().putBoolean(FIRST_START, isFirstStart).commit();
    }

    public boolean isFirstStart() {
        return sharedPrefs.getBoolean(FIRST_START, true);
    }
}
