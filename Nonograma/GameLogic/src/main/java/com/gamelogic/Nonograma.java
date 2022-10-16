package com.gamelogic;

import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;

public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;

    public Nonograma(Engine engine){
        this.engine = engine;
    }

    @Override
    public void changeScene() {

    }

    @Override
    public int getScene() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double elapsedTime) {

    }

    @Override
    public void render(IGraphics graphics) {

    }

    @Override
    public void processInput(TouchEvent event) {

    }

    @Override
    public void loadImages(IGraphics graphics) {

    }
}
