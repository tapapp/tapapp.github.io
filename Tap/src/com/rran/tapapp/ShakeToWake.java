package com.rran.tapapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;


public class ShakeToWake extends Activity {

    BroadcastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock mWakeLock = pm.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YourServie");
                mWakeLock.acquire();

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                mWakeLock.release();
            }

        };
    }

}
