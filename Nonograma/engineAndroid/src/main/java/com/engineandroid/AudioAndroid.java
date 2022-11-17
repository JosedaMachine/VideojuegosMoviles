package com.engineandroid;

import android.content.res.AssetManager;

import com.engine.Audio;
import com.engine.Sound;

import java.util.HashMap;

public class AudioAndroid {

    String path = "sounds/";
    AssetManager assetManager;

    //Gestor de recursos
    //Dado un nombre devuelve su valor (instancia de sonido).
    //En caso de no estar el nombre devuelve null.
    HashMap<String, SoundAndroid> audioLoaded = new HashMap<>();

    AudioAndroid(AssetManager ass){
        assetManager = ass;
    }

    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }

    public Sound newSound(String name) {
        if(getSound(name) == null) {
            SoundAndroid sound = new SoundAndroid(assetManager, path + name);
            audioLoaded.put(name, sound);
            return sound;
        }
        return getSound(name);
    }

    public void playSound(String name) {
        getSound(name).play();
    }
}
