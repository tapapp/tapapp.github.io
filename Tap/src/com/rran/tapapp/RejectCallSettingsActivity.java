package com.rran.tapapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class RejectCallSettingsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Toast.makeText(this,"Your Device Does Not Support Phone Calls",Toast.LENGTH_LONG).show();
            finish();
        } else {

            Toast.makeText(this, "This feature is incomplete", Toast.LENGTH_LONG).show();
            finish();

            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.rejectcall_layout);

            Button start = (Button) findViewById(R.id.button);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RejectCallSettingsActivity.this, TapDetectorActivity.class);
                    intent.putExtra("Mode", Mode.REJECT_CALLS);
                    startActivity(intent);
                }
            });

        }

    }
}