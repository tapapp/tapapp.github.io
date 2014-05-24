package com.rran.tapapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class VibrationSettingsActivity extends Activity {

    // Bluetooth
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<BluetoothDevice> mArrayAdapter;
    private BluetoothDevice device;
    private static final UUID MY_UUID = UUID.fromString("09D215E0-C32B-11E3-9C1A-0800200C9A66");

    // Request Code
    private static final int REQUEST_ENABLE_BT = 3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this,"Your Device Does Not Support Bluetooth",Toast.LENGTH_LONG).show();
            finish();
        } else {

            Toast.makeText(this, "This feature is incomplete", Toast.LENGTH_LONG).show();
            finish();

            getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.vibrations_layout);

            // Initialize Bluetooth
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
                finish();
            }

        }

    }

    public void findPairedDevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> pairedDevicesList = new ArrayList<BluetoothDevice>(pairedDevices);

        // TODO: if empty, in app pairing

        if (pairedDevices.size() <= 0) {
            Toast toast = Toast.makeText(this,"Please Pair to a Bluetooth Device",Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        ArrayList<String> deviceNames = new ArrayList<String>();
        for (int i = 0; i < pairedDevicesList.size(); i++) {
            deviceNames.add(pairedDevicesList.get(i).getName());
        }

        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,deviceNames);

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(mArrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VibrationSettingsActivity.this,TapDetectorActivity.class);
                intent.putExtra("Mode",Mode.BLUETOOTH_VIBRATION);
                intent.putExtra("Address",((BluetoothDevice) parent.getItemAtPosition(position)).getAddress());
                startActivity(intent);
            }
        });

        // Server
        AcceptThread thread = new AcceptThread();
        thread.run();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else
            findPairedDevices();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            findPairedDevices();
        } else {
            Toast.makeText(this,"Feature Requires Bluetooth",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Tap", MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {

            BluetoothSocket socket = null;

            while (true) {

                try {
                    socket = mmServerSocket.accept();
                    device = socket.getRemoteDevice();

                    AlertDialog.Builder alert = new AlertDialog.Builder(VibrationSettingsActivity.this);

                    alert.setTitle("New Post");

                    TextView view = new TextView(VibrationSettingsActivity.this);
                    view.setText("Do you want to connect to "+device.getName());
                    alert.setView(view);

                    alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent intent = new Intent(VibrationSettingsActivity.this,TapDetectorActivity.class);
                            intent.putExtra("Mode",Mode.BLUETOOTH_VIBRATION);
                            intent.putExtra("Address",device.getAddress());
                            startActivity(intent);

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            AcceptThread.this.cancel();

                            (new AcceptThread()).run();

                        }
                    });

                    alert.show();


                } catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }

        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}