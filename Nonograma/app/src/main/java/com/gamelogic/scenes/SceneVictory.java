package com.gamelogic.scenes;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.Board;
import com.gamelogic.Button;
import com.gamelogic.Fade;
import com.gamelogic.GameManager;
import com.gamelogic.TextElement;

public class SceneVictory implements SceneBase {

    private final Engine engine;

    private Button button;
    private Button shareButton;
    private Font title, buttonFont;
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
        int logicWidth = engine.getGraphics().getLogicWidth();
        int logicHeight = engine.getGraphics().getLogicHeight();

        //Fade In
        fade = new Fade(engine,
                0, 0,
                logicWidth, logicHeight,
                500, 500, Fade.STATE_FADE.Out);
        fade.setColor(ColorWrap.BLACK);

        int sizeX = 225, sizeY = 50;

        int posX = logicWidth/2 - sizeX/2;
        int posY = logicHeight - (int)(sizeY*2.5);

        //Boton vuelta al menu
        button = new Button("Menu", posX, posY,sizeX, sizeY) {
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
                    //TODO: al titulo si quick game -> a story si historia
                    engine.getGame().pushScene(new SceneTitle(engine));
                }
            }
        };

        shareButton = new Button("", posX, posY + sizeY + 15,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(shareButton.isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        engine.getContext().startActivity(
                                GameManager.instance().getTwitterIntent("Just beat a level on NONOGRAM!"));
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };

        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
        shareButton.setFont(buttonFont);
        shareButton.setColor(ColorWrap.BLACK);
        shareButton.setBackgroundImage(engine.getGraphics().getImage("share"));

        GameManager.instance().addMoney(20);
        if(engine.getGame().getUserInterface().getElement(0) != null){
            TextElement ui = (TextElement) engine.getGame().getUserInterface().getElement(0);
            ui.setText(GameManager.instance().getTextMoney());
        }
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto de victoria
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (logicWidth/2 - dime.first/2), (int) (logicHeight/8 + dime.second/2));

        //Tablero correcto
        checkBoard.drawBoard(engine, logicWidth/2 - checkBoard.getWidth()/2, logicHeight/2 - checkBoard.getHeight()/2, true);

        //Boton de vuelta al menu
        button.render(graphics);
        shareButton.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
    }

    @Override
    public void input(TouchEvent event) {
        shareButton.input(event);
        button.input(event);
    }

    @Override
    public void loadResources(Graphics graphics) {
        graphics.newImage("crosssquare.png", "cross");
        graphics.newImage("emptysquare.png", "empty");
        graphics.newImage("share.png", "share");

        title = graphics.newFont("arcade.TTF",75,true);

        buttonFont = graphics.newFont("arcade.TTF",50,true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


}
