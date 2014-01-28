package com.masterofcode.android.magreader.share.fb;

import com.masterofcode.android.magreader.share.fb.SessionEvents.AuthListener;

public class SampleAuthListener implements AuthListener {

    public void onAuthSucceed() {
        //Log.v(this.getClass().getSimpleName().toString(),"You have logged in! ");
    }

    public void onAuthFail(String error) {
    	//Log.v(this.getClass().getSimpleName().toString(),"Login Failed: ");
    }
}

