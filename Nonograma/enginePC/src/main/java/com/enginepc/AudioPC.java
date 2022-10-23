package com.enginepc;

import com.engine.Audio;
import com.engine.Sound;

import java.util.HashMap;

public class AudioPC implements Audio {

    String path = "appDesktop/assets/sounds";

    HashMap<String, SoundPC> audioLoaded = new HashMap<>();

    @Override
    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }

    @Override
    public Sound newSound(String name) {
        SoundPC sound = new SoundPC(path + name);
        audioLoaded.put(name, sound);
        return sound;
    }

    @Override
    public void playSound(String name) {
        getSound(name).play();
    }
}
