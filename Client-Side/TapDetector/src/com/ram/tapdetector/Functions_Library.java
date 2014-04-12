package com.ram.tapdetector;
import android.media.AudioManager;

public class Functions_Library extends AudioManager{

    public final int STREAM_MUSIC=2;
    public final int ADJUST_RAISE=1;
    public final int ADJUST_LOWER=1;



   public Functions_Library()
   {

    
   }






    public int getMusicStream()
    {
            return STREAM_MUSIC;
    }
    public void Volume(int i ,int j)
    {
        if(i>0) {
            adjustVolume(ADJUST_RAISE, j);
        }
        if(i<0) {

            adjustVolume(ADJUST_LOWER, j);
        }
    }
    public void Volume(int q, int i ,int j)
    {
        if(i>0) {
            adjustStreamVolume(q, i, j);
        }
        if(i<0) {

            adjustStreamVolume(q, i*-1, j);
        }
    }
    public int getMAX(int q)
    {
        return getStreamMaxVolume(q);
    }
    public int getCurr(int q) {

    return getStreamVolume(q);
    }
    public void setVol(int q,int i, int j,int max) {
        if(i<max) {
            setStreamVolume(q,i,j);
        }
    }




}
