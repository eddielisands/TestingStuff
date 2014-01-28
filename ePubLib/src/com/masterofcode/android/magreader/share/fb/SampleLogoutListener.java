package com.masterofcode.android.magreader.share.fb;

import com.masterofcode.android.magreader.share.fb.SessionEvents.LogoutListener;

public class SampleLogoutListener implements LogoutListener {
    public void onLogoutBegin() {
        //Log.v(this.getClass().getSimpleName().toString(),"Logging out...");
    }

    public void onLogoutFinish() {
        //Log.v(this.getClass().getSimpleName().toString(),"You have logged out! ");
    }
}