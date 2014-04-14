package com.ram.tapdetector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

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