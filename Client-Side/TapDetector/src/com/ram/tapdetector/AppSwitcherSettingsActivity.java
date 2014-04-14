package com.ram.tapdetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AppSwitcherSettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.appswitcher_layout);

        Button start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppSwitcherSettingsActivity.this,TapDetectorActivity.class);
                intent.putExtra("Mode",Mode.APP_SWITCHER);
                startActivity(intent);
            }
        });

    }

}