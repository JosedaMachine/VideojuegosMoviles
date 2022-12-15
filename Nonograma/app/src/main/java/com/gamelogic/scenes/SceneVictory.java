package com.gamelogic.scenes;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.engineandroid.AdManager;
import com.gamelogic.Board;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;
import com.gamelogic.managers.GameManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;

public class SceneVictory implements SceneBase {

    private final Engine engine;

    private Button button,
            shareButton,
            adButton;
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
                Graphics.ConstraintX.LEFT, Graphics.ConstraintY.TOP,
                Graphics.ConstraintX.RIGHT, Graphics.ConstraintY.BOTTOM,
                500, 500, Fade.STATE_FADE.Out);
        fade.setColor(ColorWrap.BLACK);

        int sizeX = 225, sizeY = 50;

        int posX = (int)(logicWidth *0.25f - sizeX / 2);
        int posY = logicHeight - (int) (sizeY * 2.5);

        //Boton vuelta al menu
        shareButton = new Button("", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (shareButton.isInside(event_.getX_(), event_.getY_())) {
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

        posX = (int)(logicWidth *0.75f - sizeX / 2);

        //Boton vuelta al menu
        adButton = new Button("X2 coin", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (adButton.isInside(event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");
                        AdManager.instance().buildAndShowRewardAd();
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };

        posX = logicWidth / 2 - sizeX / 2;
        posY += logicHeight*0.05f + sizeY;

        //Boton vuelta al menu
        button = new Button("Menu", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (button.isInside(event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");
                        fade.triggerFade();
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete()){
                    //We know that our scene level selection is 2 Scene below current one.
                    int i = 2;
                    while(i-- > 0){
                        engine.getGame().previousScene();
                    }
                }
            }
        };

        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        adButton.setFont(buttonFont);
        adButton.setColor(ColorWrap.BLACK);
        adButton.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        shareButton.setFont(buttonFont);
        shareButton.setColor(ColorWrap.BLACK);
        shareButton.setBackgroundImage(engine.getGraphics().getImage("share"));

        //TODO: cambiar de 20 a lo que toque
        GameManager.instance().addMoney(20);
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto de victoria
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (logicWidth / 2 - dime.first / 2), (int) (logicHeight / 9 + dime.second / 2));

        //Monedas
        String money = "+" + 20;
        dime = graphics.getStringDimensions(money);
        graphics.drawText(money, (int) (logicWidth / 2 - dime.first / 2), (int) (logicHeight / 5 + dime.second / 2));

        Image coin = graphics.getImage("coin");
        float coinScale = 55 / (float) coin.getWidth();
        float offsetX = coin.getWidth() * coinScale * 0.3f;
        float offsetY = coin.getHeight() * coinScale * 0.2f;

        graphics.drawImage(coin, (int) (logicWidth / 2 + dime.first / 2 + offsetX), (int)(logicHeight / 5 - offsetY), coinScale, coinScale);

        //Tablero correcto
        checkBoard.drawBoard(engine, logicWidth / 2 - checkBoard.getWidth() / 2, logicHeight / 2 - checkBoard.getHeight() / 2,
                true, GameManager.instance().getPalette().ordinal());

        //Boton de vuelta al menu
        button.render(graphics);
        adButton.render(graphics);
        shareButton.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
        adButton.update(deltaTime);
    }

    @Override
    public void input(TouchEvent event) {
        shareButton.input(event);
        button.input(event);
        adButton.input(event);
    }

    @Override
    public void loadResources(Graphics graphics) {
        graphics.newImage("share.png", "share");

        title = graphics.newFont("arcade.TTF", 75, true);

        buttonFont = graphics.newFont("arcade.TTF", 50, true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void orientationChanged(boolean isHorizontal) {

    }

    @Override
    public void save(FileOutputStream file) {
        GameManager.instance().save(file);
    }

    @Override
    public void restore(BufferedReader reader) {
        GameManager.instance().restore(reader);
    }


}
