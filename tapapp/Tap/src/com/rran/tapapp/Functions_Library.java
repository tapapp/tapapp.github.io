package com.rran.tapapp;
import	android.media.MediaPlayer;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

public class Functions_Library {

    ArrayList<HashMap<String,String>> song= new  ArrayList<HashMap<String,String>>();
    ArrayList<Integer> rec= new ArrayList<Integer>();
    public final int STREAM_MUSIC=2;
    SongList songlist;
    public final int ADJUST_RAISE=1;
    public final int ADJUST_LOWER=1;
    private int index;
    private boolean crap=false;
    private boolean shuffle = true;
    MediaPlayer mediaPlayer;

    public Functions_Library( ArrayList<HashMap<String,String>> s) {
        rec.add(5);
        song=s;

        mediaPlayer = new MediaPlayer();

    }
    public void playSong(int songIndex)  {

        index=songIndex;
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.get(songIndex).get("path"));
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setVolume(100.0f,10.0f);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
