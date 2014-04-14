package com.ram.tapdetector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MusicSettingsActivity extends Activity {

    private boolean shuffle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_layout);

        shuffle = false;

        ToggleButton shuffleButton = (ToggleButton) findViewById(R.id.shuffleBtn);
        shuffleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    shuffle = true;
                else
                    shuffle = false;
            }
        });

        Button start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicSettingsActivity.this,TapDetectorActivity.class);
                intent.putExtra("Mode",Mode.MUSIC);
                intent.putExtra("Shuffle",shuffle);
                startActivity(intent);
            }
        });

    }
}