package com.conference.app.lib.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.conference.app.lib.R;

public class SplashScreen extends Activity {
    private static final String TAG = SplashScreen.class.getName();
    private static final boolean DEBUG = false;

    private final int SPLASH_SCREEN_TIME_IN_MS = 3000;

    private Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(SPLASH_SCREEN_TIME_IN_MS);
                    }
                } catch (Exception e) {
                }

                startActivity(new Intent(SplashScreen.this, Dashboard.class));
                finish();
            }
        };

        splashThread.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            if (splashThread != null) {
                synchronized (splashThread) {
                    splashThread.notifyAll();
                }
            }
        }
        return true;
    }
}
