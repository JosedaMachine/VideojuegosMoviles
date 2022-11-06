package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class SceneLevels implements SceneBase {

    IFont title;
    Engine engine;
    Button button;

    public SceneLevels(Engine engine_) {
        this.engine = engine_;
    }

    List<Button> levels;

    @Override
    public void init() {

        loadResources(engine.getGraphics());

        levels = new ArrayList<>();

        int numButtons = 6;
        int offsetX = 50, offsetY = 50;

        int sizeX = 150, sizeY = 100;

        int posX = engine.getGraphics().getWidth()/3 - sizeX/2;
        int posY = engine.getGraphics().getHeight()/3 -  sizeY/2;
        levels.add(createLevel("4x4", posX, posY, sizeX, sizeY, 4, 4));

        posX = engine.getGraphics().getWidth()/2 - sizeX/2;
        levels.add(createLevel("5x5", posX, posY, sizeX, sizeY, 5, 5));

        posX = (int) (engine.getGraphics().getWidth()/1.5 - sizeX/2);
        levels.add(createLevel("5x10", posX, posY, sizeX, sizeY, 5, 10));

        posY = engine.getGraphics().getHeight()/2 -  sizeY/2;
        posX = engine.getGraphics().getWidth()/3 - sizeX/2;
        levels.add(createLevel("8x8", posX, posY, sizeX, sizeY, 8, 8) );

        posX = engine.getGraphics().getWidth()/2 - sizeX/2;
        levels.add(createLevel("10x10", posX, posY, sizeX, sizeY, 10, 10) );

        posX = (int) (engine.getGraphics().getWidth()/1.5 - sizeX/2);
        levels.add(createLevel("10x15", posX, posY, sizeX, sizeY, 10, 15) );
    }

    private Button createLevel(String text, int x, int y, int sizeX, int sizeY, final int i, final int j){
        final Button button = new Button(text, x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        engine.getGame().changeScene(new SceneGame(engine , i, j));
                    }
                }
            }
        };
        button.setFont(title);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));

        return button;
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK);

        String title = "Select puzzle size";
        Pair<Double, Double> dime = graphics.getStringDimensions(title);
        graphics.drawText(title, (int) (graphics.getWidth()/2 - dime.first/2), (int) (graphics.getHeight()*0.15 + dime.second/2));

        for(int i = 0; i < levels.size(); i++){
            levels.get(i).render(graphics);
        }
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void input(TouchEvent event) {
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).input(event);
        }
    }

    @Override
    public void loadResources(IGraphics graphics) {
        Image im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        title = engine.getGraphics().newFont("arcade.TTF",50,true);
    }
}
