package com.engineandroid;

import android.content.res.AssetManager;
import android.media.SoundPool;

import java.util.HashMap;

public class Audio {

    String path = "sounds/";
    AssetManager assetManager;
    SoundPool soundPool = null;

    //Gestor de recursos
    //Dado un nombre devuelve su valor (instancia de sonido).
    //En caso de no estar el nombre devuelve null.
    HashMap<String, Sound> audioLoaded = new HashMap<>();
    Music music;

    Audio(AssetManager ass){
        assetManager = ass;
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
    }

    public Sound getSound(String name) {
        return audioLoaded.get(name);
    }
    public Music getMusic() {return music;}

    public Sound newSound(String name) {
        if(getSound(name) == null) {
            Sound sound = new Sound(soundPool, assetManager, path + name);
            audioLoaded.put(name, sound);
            return sound;
        }
        return getSound(name);
    }
    public Music setMusic(String name){
        if(music == null)
            music = new Music(assetManager, path + name);
        return music;
    }

    public void playSound(String name) {
        Sound sound = getSound(name);
        float volume = sound.getVolume();
        soundPool.play(sound.getId(), volume, volume, 1,
                sound.getLoop(), sound.getRate());
    }

    public void pauseSound(String name) {
        Sound sound = getSound(name);
        soundPool.pause(sound.getId());
    }

    public void pauseEverySound() {
        soundPool.autoPause();
    }

    public void resumeEverySound() {
        soundPool.autoResume();
    }
}

