package com.enginepc;

import com.engine.Sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPC implements Sound {
    Clip clip = null;

    public SoundPC(String file){
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(file));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void pause() {
        clip.stop();
    }

    @Override
    public boolean isLoaded() {
        return clip != null;
    }

    @Override
    public void setLoop(boolean l) {
        if(l)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(0);
    }

    public void setVolume(int vol) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(vol); // Reduce volume by 10 decibels.
    }
}
