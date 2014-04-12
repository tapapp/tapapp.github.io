package com.ram.tapdetector;
import android.media.AudioManager;

public class Functions_Library extends AudioManager{

    public final int STREAM_MUSIC=2;
    public final int ADJUST_RAISE=1;
    public final int ADJUST_LOWER=1;



    public Functions_Library()
    {

    
    }

    public int getMusicStream()
    {
            return STREAM_MUSIC;
    }
    public void Volume(int i ,int j)
    {
        if(i>0) {
            adjustVolume(ADJUST_RAISE, j);
        }
        if(i<0) {

            adjustVolume(ADJUST_LOWER, j);
        }
    }
    public void Volume(int q, int i ,int j)
    {
        if(i>0) {
            adjustStreamVolume(q, i, j);
        }
        if(i<0) {

            adjustStreamVolume(q, i*-1, j);
        }
    }
    public int getMAX(int q)
    {
        return getStreamMaxVolume(q);
    }
    public int getCurr(int q) {

    return getStreamVolume(q);
    }
    public void setVol(int q,int i, int j,int max) {
        if(i<max) {
            setStreamVolume(q,i,j);
        }
    }
    
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
    }




}
