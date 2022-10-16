package com.gamelogic;

import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;

public class Nonograma implements IGame {
    Engine engine;

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
    public void processInput() {

    }
}
