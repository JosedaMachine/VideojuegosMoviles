package com.gamelogic;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

public class SceneVictory implements SceneBase {

    private Button button;
    private IFont title, buttonFont;
    private final String victoryText = "VICTORY!";

    private Fade fade;

    private final Board checkBoard;
    public SceneVictory(Board checkboard) {
        this.checkBoard = checkboard;
    }

    @Override
    public void init(final IGame game, IGraphics graphics, final Audio audio) {
        loadResources(graphics, audio);

        //Fade In
        fade = new Fade(
                0, 0,
                graphics.getLogicWidth(), graphics.getLogicHeight(),
                1000, 1000, Fade.STATE_FADE.Out);
        fade.setColor(IColor.BLACK);

        int sizeX = 225, sizeY = 50;

        int posX = graphics.getLogicWidth()/2 - sizeX/2;
        int posY = graphics.getLogicHeight() - (int)(sizeY*2.5);

        //Boton vuelta al menu
        button = new Button("To Menu", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        audio.playSound("click.wav");

                        fade.triggerFade();

                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete()){
                    game.changeScene(new SceneTitle());
                }
            }
        };

        button.setFont(buttonFont);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(graphics.getImage("empty"));
    }

    @Override
    public void render(IGraphics graphics) {

        graphics.setFont(title);
        graphics.setColor(IColor.BLACK, 1.0f);

        //Texto de victoria
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()/8 + dime.second/2));

        //Tablero correcto
        checkBoard.drawBoard(graphics, graphics.getLogicWidth()/2 - checkBoard.getWidth()/2, graphics.getLogicHeight()/2 - checkBoard.getHeight()/2, true);

        //Boton de vuelta al menu
        button.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
    }

    @Override
    public void input(IGame game, Audio audio, TouchEvent event_) {
        button.input(event_);
    }

    @Override
    public void loadResources(IGraphics graphics, Audio audio) {
        graphics.newImage("crosssquare.png", "cross");

        graphics.newImage("emptysquare.png", "empty");

        title = graphics.newFont("arcade.TTF",75,true);

        buttonFont = graphics.newFont("arcade.TTF",50,true);
    }
}
