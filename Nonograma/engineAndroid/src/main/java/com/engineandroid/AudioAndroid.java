package com.engineandroid;

import android.content.res.AssetManager;

import com.engine.Audio;
import com.engine.Sound;

import java.util.HashMap;

public class AudioAndroid implements Audio {

    String path = "sounds/";
    AssetManager assetManager;

    HashMap<String, SoundAndroid> audioLoaded = new HashMap<>();

    AudioAndroid(AssetManager ass){
        assetManager = ass;
    }

    @Override
    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }

    @Override
    public Sound newSound(String name) {
        SoundAndroid sound = new SoundAndroid(assetManager, path + name);
        audioLoaded.put(name, sound);
        return sound;
    }

    @Override
    public void playSound(String name) {
        getSound(name).play();
    }
}
