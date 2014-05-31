package com.rran.tapapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AppSwitcherSettingsActivity extends Activity {

    private boolean mStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.appswitcher_layout);

        mStart = false;

        ToggleButton toggle = (ToggleButton) findViewById(R.id.shuffleBtn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mStart = true;
                else
                    mStart = false;
            }
        });

        Button start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStart == true) {
                    Intent intent = new Intent(AppSwitcherSettingsActivity.this, TapDetectorActivity.class);
                    intent.putExtra("Mode", Mode.APP_SWITCHER);
                    startActivity(intent);
                } else {
                    Toast.makeText(AppSwitcherSettingsActivity.this,"App Switcher is Disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}