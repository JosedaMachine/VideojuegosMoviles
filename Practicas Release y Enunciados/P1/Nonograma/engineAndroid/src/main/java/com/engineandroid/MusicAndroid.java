package com.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MusicAndroid {
    MediaPlayer music = null;

    MusicAndroid(AssetManager ass, String file){
        music = new MediaPlayer();
        music.reset();
        try {
            AssetFileDescriptor afd = ass.openFd(file);
            music.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            music.prepare();
            music.setLooping(false);

            afd.close();
        } catch (IOException e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }
    }

    public void play() {
        music.start();
    }

    public void pause() {
        music.pause();
    }

    public boolean isLoaded() {
        return music != null;
    }

    public void setLoop(boolean l) {
        music.setLooping(l);
    }

    public void setVolume(int vol) {
        music.setVolume(vol,vol);
    }

    public boolean alreadyPlaying() {
        return music.isPlaying();
    }
}
