package com.ram.tapdetector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class TapDetectorActivity extends Activity implements SensorEventListener {

    // App State
    private boolean appStarted;

    // UI Elements
    private TextView tv;

    // Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter mArrayAdapter;

    // Request Code
    private static final int REQUEST_ENABLE_BT = 3;

    // Sensors
    private SensorManager mSensorManager;
    private Sensor accelerometer;

    // Sensor Values
    private float[] gravity;
    private float[] acceleration;
    private int[] currentWindow;

    // Sensor Threshold Value
    private static final float THRESHOLD = 0.75f;

    // Sensor Counters
    private boolean initOver;
    private int sensorCount;
    private int numSpikes;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        appStarted = false;

        // Initialize Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void initApp() {

        appStarted = true;

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

        // Initialize UI
        tv = (TextView) findViewById(R.id.textView);
        tv.setText("Spike "+numSpikes);

        // Initialize Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            mArrayAdapter = new ArrayAdapter();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    Log.i("Device: ",device.getName());
                }
            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (appStarted)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appStarted)
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ENABLE_BT) {

                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth Enabled
                    initApp();
                } else {
                    // Bluetooth Not Enabled
                    Toast.makeText(this, "App Requires Bluetooth", Toast.LENGTH_SHORT).show();
                    finish();
                }
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

            sensorCount++;

            if (sensorCount > 20)
                initOver = true;

            if (acceleration[0] < THRESHOLD && acceleration[1] < THRESHOLD && acceleration[2] > THRESHOLD && initOver){

                translateWindow(1);

                if (countSpikes(currentWindow) == 1) {

                    numSpikes++;
                    Log.i("Spike ",""+numSpikes);
                    tv.setText("Spike " + numSpikes);

                }

            } else if (initOver)
                translateWindow(0);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
