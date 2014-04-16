package com.ram.tapdetector;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TapDetectorActivity extends Activity implements SensorEventListener {

    // App State
    private int appMode;

    // App Switcher
    private static final int MAX_TASKS = 50;

    // Call
    private CallManager manager;

    // UI Elements
    private TextView tv;

    // Music
    Functions_Library functions_library;
    private boolean musicShuffle;

    // Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter mArrayAdapter;
    private BluetoothDevice mDevice;
    private static final UUID MY_UUID = UUID.fromString("09D215E0-C32B-11E3-9C1A-0800200C9A66");
    private ConnectedThread cThread;

    // Sensors
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    // Sensor Values
    private float[] gravity;
    private float[] acceleration;
    private int[] currentWindow;

    // Sensor Threshold Values
    private static final float THRESHOLD_LOW = 0.5f;
    private static final float THRESHOLD_MID = 0.7f;

    // Sensor Counters
    private boolean initOver;
    private int sensorCount;
    private int numSpikes;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            appMode = bundle.getInt("Mode");
        else
            appMode = Mode.APP_SWITCHER;

        if (appMode == Mode.MUSIC) {
            musicShuffle = false;
            if (bundle.getBoolean("Shuffle"))
                musicShuffle = true;

            initApp();

        }

        if (appMode == Mode.APP_SWITCHER) {
            initApp();
        }

        if (appMode == Mode.REJECT_CALLS) {
            initApp();
        }

        if (appMode == Mode.BLUETOOTH_VIBRATION) {

            String address = bundle.getString("Address");

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            ArrayList<BluetoothDevice> pairedDevicesList = new ArrayList<BluetoothDevice>(pairedDevices);

            for (BluetoothDevice device : pairedDevicesList) {
                if (device.getAddress().equals(address)) {
                    mDevice = device;
                    initApp();
                }
            }

        }

    }

    @Override
    public void onBackPressed() {

        if (functions_library != null)
            functions_library.p();

        super.onBackPressed();
    }

    public void initApp() {

        if (appMode == Mode.REJECT_CALLS) {

            manager = new CallManager();

        }

        if (appMode == Mode.MUSIC) {
            // Start Music
            SongList songs = new SongList();
            functions_library = new Functions_Library(songs.getSongs(),this);
            if (musicShuffle)
                functions_library.changeShuffle();
            functions_library.playSong(0);
        }

        if (appMode == Mode.BLUETOOTH_VIBRATION) {

            // Connect Bluetooth
            ConnectThread thread = new ConnectThread(mDevice);
            thread.run();

        }

        // Initialize Data
        gravity = new float[3];
        acceleration = new float[3];
        currentWindow = new int[3];
        for (int i = 0; i < currentWindow.length; i++) {
            currentWindow[i] = 0;
        }
        sensorCount = 0;
        initOver = false;
        numSpikes = 0;

        // Initialize Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
                functions_library.outputCheck();
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                cThread = new ConnectedThread(mmSocket);
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {

                    bytes = mmInStream.read(buffer);

                    String read = new String(buffer,"UTF-8");
                    if (read.equals("1")) {
                        Vibrator v = (Vibrator) TapDetectorActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void changeApp(ComponentName newComponent) {

        Intent intent = getPackageManager().getLaunchIntentForPackage(newComponent.getPackageName());
        startActivity(intent);

    }

    public void next() {

        ActivityManager am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(MAX_TASKS);

        ArrayList<String> classNames = new ArrayList<String>();
        for (ActivityManager.RunningTaskInfo task : tasks) {
            classNames.add(task.topActivity.getClassName());
        }

        ComponentName currentComponent = am.getRunningTasks(1).get(0).topActivity;

        String currentClassName = currentComponent.getClassName();

        int index = classNames.indexOf(currentClassName);

        ArrayList<Integer> taskListRemovers = new ArrayList<Integer>();

        for (int i = 0; i < tasks.size(); i++) {
            String className = tasks.get(i).topActivity.getClassName();
            if (className.startsWith("com.android.systemui") || className.startsWith("com.android.launcher")) {
                if (!className.equals(currentClassName)) {
                    classNames.remove(className);
                    taskListRemovers.add(i);
                }
            }
        }

        for (int i = 0; i < taskListRemovers.size(); i++) {
            tasks.remove((int) taskListRemovers.get(i)-i);
        }

        if (index < 0)
            return;

        if(index+1 < classNames.size())
        {

            ComponentName newComponent = null;

            newComponent = tasks.get(index+1).topActivity;

            if (newComponent != null)
                changeApp(newComponent);

        }

    }

    public void previous() {

        ActivityManager am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(MAX_TASKS);

        ArrayList<String> classNames = new ArrayList<String>();
        for (ActivityManager.RunningTaskInfo task : tasks) {
            classNames.add(task.topActivity.getClassName());
        }

        ComponentName currentComponent = am.getRunningTasks(1).get(0).topActivity;

        String currentClassName = currentComponent.getClassName();

        int index = classNames.indexOf(currentClassName);

        ArrayList<Integer> taskListRemovers = new ArrayList<Integer>();

        for (int i = 0; i < tasks.size(); i++) {
            String className = tasks.get(i).topActivity.getClassName();
            if (className.startsWith("com.android.systemui") || className.startsWith("com.android.launcher")) {
                if (!className.equals(currentClassName)) {
                    classNames.remove(className);
                    taskListRemovers.add(i);
                }
            }
        }

        for (int i = 0; i < taskListRemovers.size(); i++) {
            tasks.remove((int) taskListRemovers.get(i)-i);
        }

        if (index < 0)
            return;

        if(index-1 > -1)
        {

            ComponentName newComponent = null;

            newComponent = tasks.get(index+1).topActivity;

            if (newComponent != null)
                changeApp(newComponent);

        }

    }

    public void translateWindow(int num) {

        for (int i = 0; i < currentWindow.length-1; i++) {
            currentWindow[i] = currentWindow[i+1];
        }

        currentWindow[currentWindow.length-1] = num;

    }

    public int countSpikes(int[] array) {

        int count = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1)
                count++;
        }

        return count;

    }

    public boolean xRightTap() {
        return acceleration[0] > THRESHOLD_MID && Math.abs(acceleration[1]) < THRESHOLD_LOW && Math.abs(acceleration[2]) < THRESHOLD_LOW;
    }

    public boolean xLeftTap() {
        return acceleration[0] > THRESHOLD_MID && Math.abs(acceleration[1]) < THRESHOLD_LOW && Math.abs(acceleration[2]) < THRESHOLD_LOW;
    }

    public boolean zTap() {
        return Math.abs(acceleration[0]) < THRESHOLD_LOW && Math.abs(acceleration[1]) < THRESHOLD_LOW && Math.abs(acceleration[2]) > THRESHOLD_MID;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            final float alpha = 0.8f;

            // Find the Gravity
            gravity[0] = alpha*gravity[0] + (1-alpha)*event.values[0];
            gravity[1] = alpha*gravity[1] + (1-alpha)*event.values[1];
            gravity[2] = alpha*gravity[2] + (1-alpha)*event.values[2];

            // Find the Acceleration
            acceleration[0] = event.values[0] - gravity[0];
            acceleration[1] = event.values[1] - gravity[1];
            acceleration[2] = event.values[2] - gravity[2];

            //Log.i("acceleration",acceleration[0]+", "+acceleration[1]+", "+acceleration[2]);

            sensorCount++;

            if (sensorCount > 20)
                initOver = true;

            if (appMode == Mode.REJECT_CALLS || appMode == Mode.BLUETOOTH_VIBRATION) {

                if (zTap() && initOver) {

                    translateWindow(1);

                    if (countSpikes(currentWindow) == 1) {

                        Log.i("Tap","Z-TAP");

                        if (appMode == Mode.REJECT_CALLS) {
                            manager.tapped();
                        }

                        if (appMode == Mode.BLUETOOTH_VIBRATION) {
                            cThread.write("1".getBytes());
                        }

                    }


                } else if (initOver) {
                    translateWindow(0);
                }

            }

            if (appMode == Mode.APP_SWITCHER || appMode == Mode.MUSIC) {

                if (xRightTap() && initOver) {

                    translateWindow(1);

                    if (countSpikes(currentWindow) == 1) {

                        if (appMode == Mode.APP_SWITCHER) {
                            Log.i("Tap","Right Tap");
                            next();
                        }

                        if (appMode == Mode.MUSIC)
                            functions_library.skip();

                    }

                } else if (xLeftTap()) {

                    translateWindow(1);

                    if (countSpikes(currentWindow) == 1) {

                        if (appMode == Mode.APP_SWITCHER) {
                            Log.i("Tap","Left Tap");
                            previous();
                        }

                        if (appMode == Mode.MUSIC)
                            functions_library.back();

                    }

                } else if (initOver)
                    translateWindow(0);

            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}