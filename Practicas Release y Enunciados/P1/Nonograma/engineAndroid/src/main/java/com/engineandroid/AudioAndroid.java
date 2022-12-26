package com.engineandroid;

import android.content.res.AssetManager;
import android.media.SoundPool;

import com.engine.Audio;
import java.util.HashMap;

public class AudioAndroid implements Audio {

    String path = "sounds/";
    AssetManager assetManager;
    SoundPool soundPool = null;

    //Gestor de recursos
    //Dado un nombre devuelve su valor (instancia de sonido).
    //En caso de no estar el nombre devuelve null.
    HashMap<String, SoundAndroid> audioLoaded = new HashMap<>();
    MusicAndroid music;

    AudioAndroid(AssetManager ass){
        assetManager = ass;
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
    }

    @Override
    public SoundAndroid getSound(String name) {
        return audioLoaded.get(name);
    }
    public MusicAndroid getMusic() {return music;}

    @Override
    public SoundAndroid newSound(String name) {
        if(getSound(name) == null) {
            SoundAndroid sound = new SoundAndroid(soundPool, assetManager, path + name);
            audioLoaded.put(name, sound);
            return sound;
        }
        return getSound(name);
    }

    @Override
    public void setMusic(String name){
        if(music == null)
            music = new MusicAndroid(assetManager, path + name);
    }

    @Override
    public void playSound(String name) {
        SoundAndroid sound = getSound(name);

        if(sound == null) return;

        float volume = sound.getVolume();

        soundPool.play(sound.getId(), volume, volume, 1,
                sound.getLoop(), sound.getRate());
    }

    @Override
    public void startMusic() {
        if(!music.alreadyPlaying()) {
            music.play();
            music.setLoop(true);
            music.setVolume(-15);
        }
    }

    @Override
    public void pauseAll() {
        music.pause();
        pauseEverySound();
    }

    @Override
    public void resumeAll() {
        music.play();
        resumeEverySound();
    }

    public void pauseSound(String name) {
        SoundAndroid sound = getSound(name);
        if(sound == null) return;
        soundPool.pause(sound.getId());
    }

    public void resumeSound(String name){
        SoundAndroid sound = getSound(name);
        if(sound == null) return;
        soundPool.resume(sound.getId());
    }

    public void pauseEverySound() {
        soundPool.autoPause();
    }

    public void resumeEverySound() {
        soundPool.autoResume();
    }
}