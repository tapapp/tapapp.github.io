public class CallManager extends BroadcastReceiver {

    public abstract void onReceive(Context context, Intent intent) {
        
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
           try {
             Class c = Class.forName(tm.getClass().getName());
             Method m = c.getDeclaredMethod("getITelephony");
             m.setAccessible(true);
             telephonyService = (ITelephony) m.invoke(tm);
             Bundle bundle = intent.getExtras();
             String phoneNumber = bundle.getString("incoming_number");
             Log.d("INCOMING", phoneNumber);
             if ((phoneNumber != null)) { 
                telephonyService.endCall();
                Log.d("HANG UP", phoneNumber);
             }
        
           } catch (Exception e) {
             e.printStackTrace();
        }
    }   
}