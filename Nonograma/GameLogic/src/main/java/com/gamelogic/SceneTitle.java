package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.awt.Panel;
import java.awt.TextField;

//TODO Implemtar render y update para scene Game
//TODO Dejar la carrera
public class SceneTitle implements SceneBase {

    private Engine engine;

    private Button button;
    IFont title;
    public SceneTitle(Engine engine_) {
        this.engine = engine_;
    }

    @Override
    public void init() {
        loadImages(engine.getGraphics());
        title = engine.getGraphics().newFont("RamadhanMubarak.ttf",100,true);
        button = new Button("Jugar", engine.getGraphics().getWidth()/2, engine.getGraphics().getHeight()/2 - 200,
                200, 100, engine.getGame(), engine);

        button.setFont(title);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    @Override
    public void render(IGraphics graphics) {
        Image im = graphics.getImage("cross");
        int size = 400;
        graphics.drawImage(im, (graphics.getWidth()/2) - (size/2), graphics.getHeight()/2, size, size);
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK);
        graphics.drawText("Nonograma", graphics.getWidth()/2 - 100, 100);

        button.render(graphics);

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void input(TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadImages(IGraphics graphics) {
        Image im = graphics.newImage("crosssquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "cross");

        im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");
    }


}
