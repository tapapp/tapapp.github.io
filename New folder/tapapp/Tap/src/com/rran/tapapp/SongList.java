package com.rran.tapapp;

import android.os.Environment;
import android.util.Log;

import java.io.FileFilter;
import java.util.HashMap;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class SongList {

    ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> song;

    public SongList() {

    }

    public ArrayList<HashMap<String, String>> getSongs() {
        File base = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_MUSIC).getAbsolutePath());

        if (base.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : base.listFiles(new FileExtensionFilter())) {
                song = new HashMap<String, String>();
                song.put("SongTitle", file.getName().substring(0, file.getName().length() - 4));
                Log.i("fileName",file.getName());
                song.put("path", file.getPath());
                songsList.add(song);
            }
        }

        return songsList;
    }

    class FileExtensionFilter implements FilenameFilter {

        @Override
        public boolean accept(File base, String s) {
            return ((s.endsWith(".mp3")) || (s.endsWith(".MP3)")));
        }

    }
}