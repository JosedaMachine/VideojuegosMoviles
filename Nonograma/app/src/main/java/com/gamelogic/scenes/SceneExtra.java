package com.gamelogic.scenes;

import android.content.SharedPreferences;

import com.engineandroid.Audio;
import com.engineandroid.ColorWrap;
import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Message;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.io.BufferedReader;

public class SceneExtra implements SceneBase {

    private Fade fade;
    private Button quickButton;
    private Font title;

    public SceneExtra(){
    }


    @Override
    public void init(Engine engine) {
        Graphics graphics = engine.getGraphics();;
        loadResources(graphics, engine.getAudio());
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        //Fade In
        fade = new Fade(engine,
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
                500, 500, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        //Posicion y tamanyo de boton (290x100)
        int sizeX = (int)(logicWidth * 0.8f),
                sizeY = (int)(logicHeight * 0.111f);

        int posX = logicWidth/2 - sizeX/2,
                posY = logicHeight/2 - sizeY/2 + 200;

        //Boton de play
        quickButton = new Button("Quick Game", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (quickButton.isInside(graphics, event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("wrong.wav");
                        setSelected(true);

                        //Trigger Fade Out
                        if (fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {

            }
        };

        quickButton.setFont(title);
        quickButton.setColor(ColorWrap.BLACK);
        quickButton.setBackgroundImage(graphics.getImage("buttonbox"));
    }

    @Override
    public void render(Graphics graphics) {
        quickButton.render(graphics);
    }

    @Override
    public void update(Engine engine, double deltaTime) {
        fade.update(deltaTime);
        quickButton.update(deltaTime);
    }

    @Override
    public void input(Engine e, TouchEvent event) {
        quickButton.input(event);
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        title = graphics.newFont("arcade.TTF",(int)(graphics.getLogicHeight() * 0.088f),true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void processMessage(Engine engine, Message msg) {

    }

    @Override
    public void orientationChanged(Graphics graphics, boolean isHorizontal) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        if(isHorizontal){
            horizontalLayout(graphics,logicWidth, logicHeight);
        }else{
            verticalLayout(graphics, logicWidth, logicHeight);
        }
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
