package com.ram.tapdetector;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import	android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.telephony.SmsManager;
import 	android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.AudioManager;

import static android.media.AudioManager.*;

public class Functions_Library implements AudioManager.OnAudioFocusChangeListener {

    ArrayList<HashMap<String,String>> song= new  ArrayList<HashMap<String,String>>();
    ArrayList<Integer> rec= new ArrayList<Integer>();
        private double Rv,Lv=100.0;

    AudioManager Audio;
    SongList songlist;
Context C;
    private int index;
    private boolean crap=false;
    private boolean shuffle = true;
    MediaPlayer mediaPlayer;


    public void onAudioFocusChange(int focusChange) {
    }

    public Functions_Library( ArrayList<HashMap<String,String>> s,Context c) {
        C=c;
        Audio =(AudioManager)C.getSystemService(Context.AUDIO_SERVICE);

        rec.add(5);
        song=s;

        mediaPlayer = new MediaPlayer();

    }
    private OnAudioFocusChangeListener al = new OnAudioFocusChangeListener(){
        public void onAudioFocusChange(int focusChange) {
            switch(focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    p();
                    break;

                case AudioManager.AUDIOFOCUS_GAIN: {
                    playSong(index);
                    Rv=Rv*10;
                    Lv=Lv*10;
                    mediaPlayer.setVolume((float)Rv,(float) Lv);
                }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK: {
                    Rv = Rv / 10;
                    Lv = Lv / 10;
                    mediaPlayer.setVolume((float) Rv, (float) Lv);
                }
                    break;


            }
        }
    };

    public void playSong(int songIndex)  {

        int result;
        result = Audio.requestAudioFocus(al,STREAM_MUSIC, AUDIOFOCUS_GAIN);

        if (result == AUDIOFOCUS_REQUEST_GRANTED) {

            index = songIndex;
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.get(songIndex).get("path"));
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setVolume((float)Rv,(float) Lv);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {

            stop();
        }
    }


    public void complete() {

        if(!shuffle) {
            if (index < (song.size() - 1)) {
                p();
                index++;
                playSong(index);

            } else {
                p();
                index = 0;
                playSong(index);
            }
        }
        else {
            if(rec.size()==song.size()) {
                for(int x=0;x<rec.size()-1;x++)
                    rec.remove(x);
            }
            int rand = (int) (Math.random() * song.size() - 1);
            for(int x=0;x<rec.size();x++)
            {
                if(rand!=rec.get(x))
                {
                    p();
                    playSong(rand);
                    rec.add(rand);
                }
            }

        }

    }
    public void changeShuffle()
    {
        if(shuffle) {
        shuffle=false;
        }
        else
        {
            shuffle=true;
        }

    }
    public void bluetooth(boolean s)
    {
        if(s)
        Audio.startBluetoothSco();
        else
            Audio.stopBluetoothSco();
    }
    public void outputCheck()
    {
        if (Audio.isBluetoothA2dpOn()) {
               Rv= Rv*10;
                  Lv=Lv*10;
            Audio.setBluetoothScoOn(true);
        } else if (Audio.isSpeakerphoneOn()) {
            Rv= Rv*5;
            Lv=Lv*5;
        } else if (Audio.isWiredHeadsetOn()) {
            Rv= Rv*2;
            Lv=Lv*2;
        }
      // Bluetooth  bluetooth(Audio.isBluetoothA2dpOn());
        mediaPlayer.setVolume((float)Rv,(float) Lv);
    }
    public boolean Playing()
    {
        return mediaPlayer.isPlaying();
    }
    public void stop()
    {
        mediaPlayer.stop();
    }
    public void p() {
        mediaPlayer.pause();
    }
    public void s() {
        mediaPlayer.start();
    }
    public int getDur()
    {
        return mediaPlayer.getDuration();
    }
    public int getPos() {

           return mediaPlayer.getCurrentPosition();
    }
    public void skip()
    {
           if(Playing()) {
               mediaPlayer.seekTo(((int) (getDur() - getPos()) + getPos()));
               complete();
           }

    }
    public void back()
    {
        if(Playing()) {
            if (getPos() < 100) {
                p();
                playSong(index - 1);
                index--;
            } else {
                p();
                playSong(index);
                mediaPlayer.seekTo(0);
            }
        }

    }

    public int getMusicStream()
    {
            return STREAM_MUSIC;
    }


    
    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }




}
