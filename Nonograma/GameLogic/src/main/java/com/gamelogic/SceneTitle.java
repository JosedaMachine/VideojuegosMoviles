package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
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

        //NOTA
        //Al parecer el renderer no se ha inicializado, por lo que cuando ponemos las medidas con respecto a la pantalla
        // nos da 0 puesto que si width = 0 pues width/2 = 0

        loadImages(engine.getGraphics());
        title = engine.getGraphics().newFont("arcade.TTF",100,true);
        int sizeX = 290, sizeY = 100;
        int posX = engine.getGraphics().getWidth()/2;
        int posY = engine.getGraphics().getHeight()/2;

        button = new Button("Play", posX, posY,290, 100) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        engine.getGame().changeScene(new SceneLevels(engine));
                    }
                }
            }
        };

        button.setFont(title);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK);

        String title = "Nonograma";
        Pair<Double, Double> dime = graphics.getStringDimensions(title);
        graphics.drawText(title, (int) (graphics.getWidth()/2 - dime.first/2), (int) (graphics.getHeight()*0.25 + dime.second/2));

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
