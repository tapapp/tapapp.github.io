package com.ram.tapdetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class CallManager extends BroadcastReceiver {

    private ITelephony telephonyService;
    private TelephonyManager tManager;

    private Context currentContext;
    private Intent currentIntent;

    private boolean called;

    public CallManager() {
        super();
        called = false;

    }

    public void tapped() {

        Log.i("tManager",tManager+"");
        if (tManager != null)
            Log.i("tManager",tManager.getCallState()+"");
        Log.i("Ringing Constant",TelephonyManager.CALL_STATE_RINGING+"");

        if (tManager != null && tManager.getCallState() == TelephonyManager.CALL_STATE_RINGING)
            endCall();

    }

    public void endCall() {

        Log.i("here","here");

        try {
            Class c = Class.forName(tManager.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tManager);
            Bundle bundle = currentIntent.getExtras();
            String phoneNumber = bundle.getString("incoming_number");
            Log.d("INCOMING", phoneNumber);
            if ((phoneNumber != null)) {
                telephonyService.endCall();
                Log.d("HANG UP", phoneNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        called = false;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Log.i("tm",tManager+"");
        currentContext = context;
        currentIntent = intent;

    }
    
}