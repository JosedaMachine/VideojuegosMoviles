package com.engineandroid;

import android.content.res.AssetManager;

import java.util.HashMap;

public class Audio {

    String path = "sounds/";
    AssetManager assetManager;

    //Gestor de recursos
    //Dado un nombre devuelve su valor (instancia de sonido).
    //En caso de no estar el nombre devuelve null.
    HashMap<String, Sound> audioLoaded = new HashMap<>();

    Audio(AssetManager ass){
        assetManager = ass;
    }

    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }

    public Sound newSound(String name) {
        if(getSound(name) == null) {
            Sound sound = new Sound(assetManager, path + name);
            audioLoaded.put(name, sound);
            return sound;
        }
        return getSound(name);
    }

    public void playSound(String name) {
        getSound(name).play();
    }
}
