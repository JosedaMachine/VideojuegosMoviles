package com.gamelogic;


import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;

import java.util.Stack;

public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;

    public Nonograma(Engine engine){
        this.engine = engine;
    }

    //Iniciar nueva escena
    @Override
    public void changeScene(SceneBase newScene) {
        currScene = newScene;
        currScene.init(this, engine.getGraphics(), engine.getAudio());
    }

    @Override
    public SceneBase getScene() {
        return currScene;
    }

    //Escena de titulo inicial
    @Override
    public void init() {
        changeScene(new SceneTitle());
    }

    @Override
    public void update(double elapsedTime) {
        currScene.update(elapsedTime);
    }

    @Override
    public void render(IGraphics graphics) {
        currScene.render(graphics);
    }

    @Override
    public void processInput(TouchEvent event) {
        currScene.input(this, engine.getAudio(), event);
    }

    @Override
    public void loadImages(IGraphics graphics) {
        currScene.loadResources(graphics, engine.getAudio());
    }

    @Override
    public void onResume() {
        engine.getAudio().resumeAll();
    }

    @Override
    public void onPause() {
        engine.getAudio().pauseAll();
    }
}
