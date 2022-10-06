package com.enginepc;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.Graphics;
import com.engine.Input;
import javax.swing.JFrame;

public class EnginePC implements Engine{

    public EnginePC(JFrame renderView){}

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Input getInput() {
        return null;
    }

    @Override
    public Audio getAudio() {
        return null;
    }

    @Override
    public void setScene(com.gamelogic.NonogramaGame scene) {

    }
}