package com.engineandroid;

import com.engine.Sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import java.io.IOException;

public class SoundAndroid implements Sound {

    MediaPlayer sound = null;

    SoundAndroid(AssetManager ass, String file){
        sound = new MediaPlayer();
        sound.reset();
        try {
            AssetFileDescriptor afd = ass.openFd(file);
            sound.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            sound.prepare();
            sound.setLooping(true);
        } catch (IOException e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        sound.start();
    }

    @Override
    public void pause() {
        sound.stop();
    }

    @Override
    public boolean isLoaded() {
        return sound != null;
    }

    @Override
    public void setLoop(boolean l) {
        //TODO
    }
}
