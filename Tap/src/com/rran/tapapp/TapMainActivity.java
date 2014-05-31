package com.rran.tapapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.*;
import android.widget.Button;

public class TapMainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_layout);

        /*TouchView view = new TouchView(this);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.add(view);*/

        Button settingButton = (Button) findViewById(R.id.settings_btn);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	
                Intent intent = new Intent(TapMainActivity.this,SettingsActivity.class);
                startActivity(intent);

            }
        });

    }





}