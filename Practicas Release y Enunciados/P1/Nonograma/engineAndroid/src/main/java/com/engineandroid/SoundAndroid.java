package com.engineandroid;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;

import com.engine.Sound;

import java.io.IOException;

public class SoundAndroid implements Sound {

    int soundId;
    float volume;
    int loop;
    float rate;

    SoundAndroid(SoundPool sPool, AssetManager ass, String file){
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

    // Obsoleto por uso de SoundPool
    @Override
    public void play() {

    }

    @Override
    public void setLoop(boolean l) {
        if(l)
            loop = 0;
        else
            loop = -1;
    }
    public int getLoop() {
        return loop;
    }
    @Override
    public void setVolume(int vol) {
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
