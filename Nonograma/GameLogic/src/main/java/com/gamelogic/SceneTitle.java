package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

public class SceneTitle implements SceneBase {

    private final Engine engine;

    private Button button;
    private IFont title;
    private String titleText = "Nonogram";
    public SceneTitle(Engine engine_) {

        this.engine = engine_;
    }

    @Override
    public void init() {
        loadResources(engine.getGraphics());

        int sizeX = 290, sizeY = 100;

        int posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        int posY = engine.getGraphics().getLogicHeight()/2 - sizeY/2;

        //Boton de play
        button = new Button("Play", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        engine.getGame().changeScene(new SceneLevels(engine));
                    }
                }
            }
        };

        button.setFont(title);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));

        //Musica en loop
        engine.getAudio().playSound("music.wav");
        engine.getAudio().getSound("music.wav").setLoop(true);
        engine.getAudio().getSound("music.wav").setVolume(-15);
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK);

        Pair<Double, Double> dime = graphics.getStringDimensions(titleText);
        //Texto del titulo
        graphics.drawText(titleText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()*0.25 + dime.second/2));

        //Boton
        button.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        //Vacio
    }

    @Override
    public void input(TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadResources(IGraphics graphics) {
        Image im = graphics.newImage("crosssquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "cross");

        im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        engine.getAudio().newSound("music.wav");
        engine.getAudio().newSound("click.wav");

        title = engine.getGraphics().newFont("arcade.TTF",80,true);
    }
}
