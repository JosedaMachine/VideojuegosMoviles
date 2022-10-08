package com.enginepc;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.Graphics;
import com.engine.Input;
import com.engine.SceneBase;

import javax.swing.JFrame;

public class EnginePC implements Engine, Runnable{


    JFrame view;
    GraphicsPC graphics;
    SceneBase currScene;

    public EnginePC(JFrame renderView){
        view = renderView;
        graphics = new GraphicsPC(renderView);
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
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
    public void setScene(SceneBase scene) {
        currScene = scene;
            currScene.init();
    }

    @Override
    public void resume() {

    }

    @Override
    public void run() {

    }
}