package com.gamelogic.scenes;

import android.content.Intent;
import android.content.SharedPreferences;

import com.engineandroid.Audio;
import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.MESSAGE_TYPE;
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
    private Button button,
            shareButton,
            adButton;
    private Font title, buttonFont;
    private final String victoryText = "VICTORY!";

    private Fade fade;

    private final Board checkBoard;
    private int reward;

    public SceneVictory(Board checkboard, int reward) {
        this.checkBoard = checkboard;
        this.reward = reward;
    }

    @Override
    public void init(Engine engine) {
        Graphics graphics = engine.getGraphics();
        loadResources(graphics, engine.getAudio());
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        //Fade In
        fade = new Fade(engine,
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
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
                    if (shareButton.isInside(graphics, event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");
                        engine.getContext().startActivity(Intent.createChooser(
                                GameManager.instance().getTwitterIntent("Just beat a level on NONOGRAM!"), "Share in: "));
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };

        posX = (int)(logicWidth *0.75f - sizeX / 2);

        Message rewardMessage = new Message(MESSAGE_TYPE.REWARD_AD);
        rewardMessage.reward = this.reward;
        //Boton vuelta al menu
        adButton = new Button("X2 coin", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (adButton.isInside(graphics, event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");
                        AdManager.instance().showRewardedAd(rewardMessage);
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };

        posX = logicWidth / 2 - sizeX / 2;
        posY += logicHeight*0.05f + sizeY;

        //Cargamos el ad al inicio
        AdManager.instance().buildRewardedAd();

        //Boton vuelta al menu
        button = new Button("Menu", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (button.isInside(graphics, event_.getX_(), event_.getY_())) {
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

        //Parametros iniciales de botones
        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(graphics.getImage("buttonbox"));

        adButton.setFont(buttonFont);
        adButton.setColor(ColorWrap.BLACK);
        adButton.setBackgroundImage(graphics.getImage("buttonbox"));

        shareButton.setFont(buttonFont);
        shareButton.setColor(ColorWrap.BLACK);
        shareButton.setBackgroundImage(graphics.getImage("share"));

        GameManager.instance().addMoney(reward);
        GameManager.instance().updateInterface();
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto de victoria
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (logicWidth*0.05f), (int) (logicHeight / 9 + dime.second / 2));

        //Monedas
        String money = "+" + reward;
        dime = graphics.getStringDimensions(money);
        graphics.drawText(money, (int) (logicWidth - dime.first - dime.first*0.75), (int) (logicHeight / 9 + dime.second / 2));

        Image coin = graphics.getImage("coin");
        float coinScale = 55 / (float) coin.getWidth();
        float offsetX = coin.getWidth() * coinScale * 0.3f;
        float offsetY = coin.getHeight() * coinScale * 0.2f;

        graphics.drawImage(coin, (int) (logicWidth - coin.getWidth()* coinScale - offsetX), (int)(logicHeight / 9 - offsetY), coinScale, coinScale);

        //Tablero correcto
        checkBoard.drawBoard(graphics, logicWidth / 2 - checkBoard.getWidth() / 2, logicHeight / 2 - checkBoard.getHeight() / 2,
                true, GameManager.instance().getPalette().ordinal());

        //Boton de vuelta al menu
        button.render(graphics);
        adButton.render(graphics);
        shareButton.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(Engine e, double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
        adButton.update(deltaTime);
    }

    @Override
    public void input(Engine e, TouchEvent event) {
        shareButton.input(event);
        button.input(event);
        adButton.input(event);
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
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
    public void processMessage(Engine engine,Message msg) {
        //Duplicacion de recompensa
        if(msg.getType() == MESSAGE_TYPE.REWARD_AD){
            GameManager.instance().addMoney(msg.reward);
            this.reward *= 2;
            adButton.setBackgroundImage(engine.getGraphics().getImage("lockedbutt"));
            GameManager.instance().updateInterface();
        }
    }

    @Override
    public void orientationChanged(Graphics g, boolean isHorizontal) {

    }

    @Override
    public void save(Engine engine, String filename, SharedPreferences mPreferences) {
    }

    @Override
    public void restore(BufferedReader reader, SharedPreferences mPreferences) {
    }

    @Override
    public void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {

    }

    @Override
    public void verticalLayout(Graphics g, int logicWidth, int logicHeight) {

    }

}
