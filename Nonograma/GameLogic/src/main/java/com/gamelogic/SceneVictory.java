package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

public class SceneVictory implements SceneBase {

    private final Engine engine;

    private Button button;
    private IFont title, buttonFont;
    private final String victoryText = "VICTORY!";

    private Fade fade;

    private final Board checkBoard;
    public SceneVictory(Engine engine_, Board checkboard) {
        this.checkBoard = checkboard;
        this.engine = engine_;
    }

    @Override
    public void init() {
        loadResources(engine.getGraphics());

        //Fade In
        fade = new Fade(engine,
                0, 0,
                engine.getGraphics().getLogicWidth(), engine.getGraphics().getLogicHeight(),
                1000, 1000, Fade.STATE_FADE.Out);
        fade.setColor(IColor.BLACK);

        int sizeX = 225, sizeY = 50;

        int posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        int posY = engine.getGraphics().getLogicHeight() - (int)(sizeY*2.5);

        //Boton vuelta al menu
        button = new Button("To Menu", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");

                        fade.triggerFade();

                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete()){
                    engine.getGame().changeScene(new SceneTitle(engine));
                }
            }
        };

        button.setFont(buttonFont);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    @Override
    public void render(IGraphics graphics) {

        graphics.setFont(title);
        graphics.setColor(IColor.BLACK, 1.0f);

        //Texto de victoria
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()/8 + dime.second/2));

        //Tablero correcto
        checkBoard.drawBoard(engine, graphics.getLogicWidth()/2 - checkBoard.getWidth()/2, graphics.getLogicHeight()/2 - checkBoard.getHeight()/2, true);

        //Boton de vuelta al menu
        button.render(graphics);
        fade.render();
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
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

        title = engine.getGraphics().newFont("arcade.TTF",75,true);

        buttonFont = engine.getGraphics().newFont("arcade.TTF",50,true);
    }
}
