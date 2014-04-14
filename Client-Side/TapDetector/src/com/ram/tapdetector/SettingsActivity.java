package com.ram.tapdetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {

    private String finalString;

    private RadioButton vibrationButton;
    private RadioButton musicButton;
    private RadioButton appSwitch;
    private RadioButton rejectCall;

    private Button finalCheck;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settings_layout);

        finalString = "App Switcher Settings";

        vibrationButton = (RadioButton) findViewById(R.id.bluetooth);
        musicButton = (RadioButton) findViewById(R.id.music);
        appSwitch = (RadioButton) findViewById(R.id.appSwitch);
        rejectCall = (RadioButton) findViewById(R.id.rejectCall);
        finalCheck = (Button) findViewById(R.id.openSettings);
        finalCheck.setText(finalString);

        vibrationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    finalString = "Vibration Settings";
                    finalCheck.setText(finalString);
                    musicButton.setChecked(false);
                    appSwitch.setChecked(false);
                    rejectCall.setChecked(false);
                }
            }
        });

        musicButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    finalString = "Music Settings";
                    finalCheck.setText(finalString);
                    appSwitch.setChecked(false);
                    rejectCall.setChecked(false);
                    vibrationButton.setChecked(false);
                }
            }
        });


        appSwitch.setChecked(true);
        appSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    finalString = "App Switcher Settings";
                    finalCheck.setText(finalString);
                    musicButton.setChecked(false);
                    rejectCall.setChecked(false);
                    vibrationButton.setChecked(false);
                }
            }
        });


        rejectCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    finalString = "Reject Call Settings";
                    finalCheck.setText(finalString);
                    musicButton.setChecked(false);
                    appSwitch.setChecked(false);
                    vibrationButton.setChecked(false);
                }
            }
        });

        finalCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Class mClass = null;

                if (finalString.equals("Reject Call Settings"))
                    mClass = RejectCallSettingsActivity.class;
                else if (finalString.equals("App Switcher Settings"))
                    mClass = AppSwitcherSettingsActivity.class;
                else if (finalString.equals("Music Settings"))
                    mClass = MusicSettingsActivity.class;
                else if (finalString.equals("Vibration Settings"))
                    mClass = VibrationSettingsActivity.class;

                if(mClass != null) {
                    Intent intent = new Intent(SettingsActivity.this, mClass);
                    startActivity(intent);
                }

            }
        });


    }
}