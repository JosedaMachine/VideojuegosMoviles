package com.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import java.io.IOException;

public class Sound {

    int soundId;
    float volume;
    int loop;
    float rate;

    Sound(SoundPool sPool, AssetManager ass, String file){
        soundId = -1;
        try {
            AssetFileDescriptor assetDescriptor = ass.openFd(file);
            soundId = sPool.load(assetDescriptor, 1);
            volume = 1;
            loop = 0;
            rate = 1;

        }catch (IOException e) {
            throw new RuntimeException("Couldn't load sound.");
        }

    }

    public void setLoop(int l) {
        loop = l;
    }
    public int getLoop() {
        return loop;
    }
    public void setVolume(float vol) {
        volume = vol;
    }
    public float getVolume() {
        return volume;
    }
    public int getId() {
        return soundId;
    }
    public float getRate(){
        return rate;
    }
}
