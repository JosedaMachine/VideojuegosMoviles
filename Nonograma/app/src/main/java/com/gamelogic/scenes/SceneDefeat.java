package com.gamelogic.scenes;

import android.content.SharedPreferences;

import com.engineandroid.Audio;
import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.io.BufferedReader;

public class SceneDefeat implements SceneBase {
    private Button button;
    private Font title, buttonFont;
    private final String victoryText = "DEFEAT!";

    private Fade fade;

    public SceneDefeat() {
    }

    @Override
    public void init(Engine engine) {
        loadResources(engine.getGraphics(), engine.getAudio());
        int logicWidth = engine.getGraphics().getLogicWidth();
        int logicHeight = engine.getGraphics().getLogicHeight();

        //Fade In
        fade = new Fade(engine,
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
                500, 500, Fade.STATE_FADE.Out);
        fade.setColor(ColorWrap.BLACK);

        int sizeX = 225, sizeY = 50;

        int posX = logicWidth/2 - sizeX/2;
        int posY = logicHeight - (int)(sizeY*2.5);

        //Boton vuelta al menu
        button = new Button("Levels", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(engine.getGraphics(), event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");

                        fade.triggerFade();

                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete()){
                    //Sabemos que la escena de niveles esta 2 escenas por debajo de ésta.
                    int i = 2;
                    while(i-- > 0){
                        engine.getGame().previousScene();
                    }
                }
            }
        };

        //Params iniciales de boton
        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));
    }

    @Override
    public void render(Graphics graphics) {

        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto de derrota
        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (logicWidth/2 - dime.first/2), (int) (logicHeight/8 + dime.second/2));

        //Imagen Derrota
        Image im = graphics.getImage("skull");

        float scale = 0.8f;
        graphics.drawImage(im, logicWidth/2 - (int)((im.getWidth()*scale)/2),
                logicHeight/2 - (int)((im.getHeight()*scale)/2), scale, scale);

        //Boton de vuelta al menu
        button.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(Engine engine, double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
    }

    @Override
    public void input(Engine engine,TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        graphics.newImage("skull.png", "skull");

        title = graphics.newFont("arcade.TTF",75,true);

        buttonFont = graphics.newFont("arcade.TTF",50,true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void processMessage(Engine e, Message msg) {

    }

    @Override
    public void orientationChanged(Graphics g,boolean isHorizontal) {

    }

    @Override
    public void save(Engine e, String filename, SharedPreferences mPreferences) {

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
