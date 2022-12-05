package com.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import java.io.IOException;

public class Sound {

    MediaPlayer sound = null;

    Sound(AssetManager ass, String file){
        sound = new MediaPlayer();
        sound.reset();
        try {
            AssetFileDescriptor afd = ass.openFd(file);
            sound.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            sound.prepare();
            sound.setLooping(false);

            afd.close();
        } catch (IOException e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }
    }

    public void play() {
        sound.start();
    }

    public void pause() {
        sound.stop();
    }

    public boolean isLoaded() {
        return sound != null;
    }

    public void setLoop(boolean l) {
        sound.setLooping(l);
    }

    public void setVolume(int vol) {
        sound.setVolume(vol,vol);
    }

    public boolean alreadyPlaying() {
        return sound.isPlaying();
    }
}
