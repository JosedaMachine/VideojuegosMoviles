package com.gamelogic;

import com.engine.Engine;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;
import com.engine.TouchEvent;

public class SceneLevels implements SceneBase {


    Engine engine;

    public SceneLevels(Engine engine_) {
        this.engine = engine_;
    }

    @Override
    public void init() {
        loadImages(engine.getGraphics());
    }

    @Override
    public void render(IGraphics graphics) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void input(TouchEvent event) {

    }

    @Override
    public void loadImages(IGraphics graphics) {

        Image im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");
    }
}
