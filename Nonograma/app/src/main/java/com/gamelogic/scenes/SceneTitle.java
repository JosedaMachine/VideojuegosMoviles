package com.gamelogic.scenes;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.engineandroid.AdManager;
import com.engineandroid.Audio;
import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.GyroscopeSensor;
import com.engineandroid.LightSensor;
import com.engineandroid.MESSAGE_TYPE;
import com.engineandroid.MagnetometerSensor;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.Music;
import com.engineandroid.TouchEvent;
import com.engineandroid.AccelerometerSensor;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;
import com.engineandroid.UserInterface;
import com.gamelogic.managers.GameManager;
import com.gamelogic.utils.ImageElement;
import com.gamelogic.utils.TextElement;

import java.io.BufferedReader;
import java.io.FileOutputStream;

public class SceneTitle implements SceneBase {
    private Fade fade;
    private Button quickButton, storyButton, paletteButton;
    private Font title;
    private String titleText = "Nonogram";

    private MagnetometerSensor magnetometerSensor;


    public SceneTitle() {    }

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
                posY = logicHeight/2 - sizeY/2;

        Message interMessage = new Message(MESSAGE_TYPE.INTERST_AD);

        //Boton de play
        quickButton = new Button("Quick Game", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(quickButton.isInside(graphics, event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        setSelected(true);

                        //Trigger Fade Out
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                //Cambio de escena al terminar fade
                if(fade.getFadeOutComplete() && isSelected()){

                    setSelected(false);

                    AdManager.instance().showInterstitialAd(interMessage);
//                    engine.getGame().pushScene(new SceneQuickLevels());
                    fade.reset();
                    fade.setState(Fade.STATE_FADE.In);
                    fade.triggerFade();
                    graphics.setClearColor(ColorWrap.WHITE);
                }
            }
        };

        //Cargamos el ad al inicio
        AdManager.instance().buildInterestingAd();

        posY += logicHeight * 0.2f;

        //Boton de play
        storyButton = new Button("Story Mode", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(storyButton.isInside(graphics, event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        setSelected(true);

                        //Trigger Fade Out
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                            graphics.setClearColor(ColorWrap.WHITE);
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                //Cambio de escena al terminar fade
                if(fade.getFadeOutComplete() && isSelected()){
                    engine.getGame().pushScene(new SceneStoryCategories());
                    setSelected(false);
                    fade.reset();
                    fade.setState(Fade.STATE_FADE.In);
                    fade.triggerFade();
                    graphics.setClearColor(ColorWrap.WHITE);
                }
            }
        };

        posY += logicHeight * 0.2f;

        //Boton de paletas
        paletteButton = new Button("Palettes", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(paletteButton.isInside(graphics, event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        setSelected(true);

                        //Trigger Fade Out
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                //Cambio de escena al terminar fade
                if(fade.getFadeOutComplete() && isSelected()){

                    setSelected(false);

                    engine.getGame().pushScene(new ScenePalettes());
                    fade.reset();
                    fade.setState(Fade.STATE_FADE.In);
                    fade.triggerFade();
                    graphics.setClearColor(ColorWrap.WHITE);
                }
            }
        };

        quickButton.setFont(title);
        quickButton.setColor(ColorWrap.BLACK);
        quickButton.setBackgroundImage(graphics.getImage("buttonbox"));

        storyButton.setFont(title);
        storyButton.setColor(ColorWrap.BLACK);
        storyButton.setBackgroundImage(graphics.getImage("buttonbox"));

        paletteButton.setFont(title);
        paletteButton.setColor(ColorWrap.BLACK);
        paletteButton.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        if(graphics.orientationHorizontal()){
            horizontalLayout(graphics, logicWidth, logicHeight);
        }

        //Musica en loop
        Music music = engine.getAudio().getMusic();

        if(!music.alreadyPlaying()) {
            music.play();
            music.setLoop(true);
            music.setVolume(-15);
        }

        //Acceleremoter
        magnetometerSensor = new MagnetometerSensor(graphics.getContext());


        GameManager.instance().updateInterface();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        Pair<Double, Double> dime = graphics.getStringDimensions(titleText);
        //Texto del titulo
        graphics.drawText(titleText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()*0.25 + dime.second/2));

        //Boton
        quickButton.render(graphics);
        storyButton.render(graphics);
        paletteButton.render(graphics);

        fade.render(graphics);
    }

    public void update(Engine e,double deltaTime) {
        updateBackground(e.getGraphics());
        //Vacio
        fade.update(deltaTime);
        quickButton.update(deltaTime);
        storyButton.update(deltaTime);
        paletteButton.update(deltaTime);
    }

    @Override
    public void input(Engine e,TouchEvent event) {
        quickButton.input(event);
        storyButton.input(event);
        paletteButton.input(event);
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        graphics.newImage("buttonbox.png", "buttonbox");
        graphics.newImage("coin.png", "coin");

        audio.setMusic("music.wav");
        audio.newSound("click.wav");
        //0.88 es el porcentaje que ocupa la fuente arcade en alto de pantalla lógica, es decir un 8%
        title = graphics.newFont("arcade.TTF",(int)(graphics.getLogicHeight() * 0.088f),true);
    }

    @Override
    public void onResume() {
        magnetometerSensor.onResume();
    }

    @Override
    public void onPause() {
        magnetometerSensor.onPause();
    }

    @Override
    public void processMessage(Engine e, Message msg) {
    }

    @Override
    public void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {
        int sizeX = (int)(logicWidth * 0.8f),
                sizeY = (int)(logicHeight * 0.111f * 1.5f) ;
        //Izquierda
        quickButton.setSize(sizeX, sizeY);
        quickButton.setX(-logicWidth/2  - sizeX/4);
        quickButton.setY(logicHeight/2);

        //Derecha
        storyButton.setSize(sizeX, sizeY);
        storyButton.setX((int) (logicWidth + logicWidth/2  - sizeX/1.25f));
        storyButton.setY(logicHeight/2);

        //Medio
        paletteButton.setSize(sizeX, sizeY);
        paletteButton.setX(logicWidth/2  - sizeX/2);
        paletteButton.setY((int) (logicHeight/2 - sizeY/2 + logicHeight * 0.4f));

        title = g.newFont("arcade.TTF",(int)(g.getLogicHeight() * 0.088f * 2  ),true);
    }

    @Override
    public void verticalLayout(Graphics g, int logicWidth, int logicHeight) {
        int sizeX = (int)(logicWidth * 0.8f),
                sizeY = (int)(logicHeight * 0.111f);

        quickButton.setSize(sizeX, sizeY);
        quickButton.setX(logicWidth/2 - sizeX/2);
        quickButton.setY(logicHeight/2 - sizeY/2);

        storyButton.setSize(sizeX, sizeY);
        storyButton.setX(logicWidth/2  - sizeX/2);
        storyButton.setY((int) (logicHeight/2 - sizeY/2 +  logicHeight * 0.2f));

        paletteButton.setSize(sizeX, sizeY);
        paletteButton.setX(logicWidth/2  - sizeX/2);
        paletteButton.setY((int) (logicHeight/2 - sizeY/2 +  logicHeight * 0.4f));

        title = g.newFont("arcade.TTF",(int)(g.getLogicHeight() * 0.088f),true);
    }

    @Override
    public void orientationChanged(Graphics g,boolean isHorizontal) {
        int logicWidth = g.getLogicWidth();
        int logicHeight = g.getLogicHeight();

        if(isHorizontal){
            horizontalLayout(g,logicWidth, logicHeight);
        }else{
            verticalLayout(g, logicWidth, logicHeight);
        }
    }

    @Override
    public void save(Engine e,String filename, SharedPreferences mPreferences) {
    }

    @Override
    public void restore(Engine engine,BufferedReader reader, SharedPreferences mPreferences) {

    }

    private void updateBackground(Graphics g){
        float[] values = magnetometerSensor.getDeltaValues();

        g.setClearColor(Color.argb(255,
                (int)values[0]*1000%255,
                (int)values[1]*1000%255,
                (int)values[2]*1000%255));
    }
}
