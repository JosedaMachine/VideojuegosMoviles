package com.enginepc;

import com.engine.Audio;
import com.engine.Sound;

import java.util.HashMap;

public class AudioPC implements Audio {
    String path = "appDesktop/assets/sounds/";

    //Gestor de recursos
    //Dado un nombre devuelve su valor (instancia de sonido).
    //En caso de no estar el nombre devuelve null.
    HashMap<String, SoundPC> audioLoaded = new HashMap<>();

    @Override
    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }

    @Override
    public Sound newSound(String name) {
        if(getSound(name) == null) {
            SoundPC sound = new SoundPC(path + name);
            audioLoaded.put(name, sound);
            return sound;
        }
        return getSound(name);
    }

    @Override
    public void playSound(String name) {
        getSound(name).play();
    }
}
