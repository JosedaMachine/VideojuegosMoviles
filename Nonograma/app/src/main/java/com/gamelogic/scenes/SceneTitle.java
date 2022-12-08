package com.gamelogic.scenes;

import android.os.Debug;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.Sound;
import com.engineandroid.TouchEvent;
import com.gamelogic.AccelerometerSensor;
import com.gamelogic.Button;
import com.gamelogic.Fade;

public class SceneTitle implements SceneBase {
    private final Engine engine;
    private Fade fade;
    private Button quickButton, storyButton;
    private Font title;
    private String titleText = "Nonogram";

    private AccelerometerSensor accelerometerSensor;

    public SceneTitle(Engine engine_) {

        this.engine = engine_;
    }

    @Override
    public void init() {
        loadResources(engine.getGraphics());

        //Fade In
        fade = new Fade(engine,
                        0, 0,
                             engine.getGraphics().getLogicWidth(), engine.getGraphics().getLogicHeight(),
                       500, 500, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        //Posicion y tamanyo de boton (290x100)
        int sizeX = (int)(engine.getGraphics().getLogicWidth() * 0.8f),
                sizeY = (int)(engine.getGraphics().getLogicHeight() * 0.111f);

        int posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2,
                posY = engine.getGraphics().getLogicHeight()/2 - sizeY/2;

        //Boton de play
        quickButton = new Button("Quick Game", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(quickButton.isInside(event_.getX_(),event_.getY_())){
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
                    engine.getGame().changeScene(new SceneLevels(engine));
                }
            }
        };

        posY += engine.getGraphics().getLogicHeight() * 0.2f;

        //Boton de play
        storyButton = new Button("Story Mode", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(storyButton.isInside(event_.getX_(),event_.getY_())){
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
                    engine.getGame().changeScene(new SceneCategory(engine));
                }
            }
        };

        quickButton.setFont(title);
        quickButton.setColor(ColorWrap.BLACK);
        quickButton.setBackgroundImage(engine.getGraphics().getImage("empty"));

        storyButton.setFont(title);
        storyButton.setColor(ColorWrap.BLACK);
        storyButton.setBackgroundImage(engine.getGraphics().getImage("empty"));

        //Musica en loop
        Sound music =  engine.getAudio().getSound("music.wav");

        if(!music.alreadyPlaying()) {
            engine.getAudio().playSound("music.wav");
            music.setLoop(true);
            music.setVolume(-15);
        }

        //Acceleremoter
        accelerometerSensor = new AccelerometerSensor(engine.getGraphics().getContext());
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

        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        //Vacio
        fade.update(deltaTime);
        quickButton.update(deltaTime);
        storyButton.update(deltaTime);
        updateBackground();
    }

    @Override
    public void input(TouchEvent event) {
        quickButton.input(event);
        storyButton.input(event);
    }

    @Override
    public void loadResources(Graphics graphics) {
        Image im = graphics.newImage("crosssquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "cross");

        im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        engine.getAudio().newSound("music.wav");
        engine.getAudio().newSound("click.wav");
        //0.88 es el porcentaje que ocupa la fuente arcade en alto de pantalla lógica, es decir un 8%
        title = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.088f),true);
    }

    @Override
    public void onResume() {
        accelerometerSensor.onResume();
    }

    @Override
    public void onPause() {
        accelerometerSensor.onPause();
    }

    private void updateBackground(){
        //TODO funciona pero lo que hace es una tontería
        float[] deltas = accelerometerSensor.getDeltaValues();

        int i = 0;
        while(i < 3 && deltas[i] < 10){
            i++;
        }

        //Si detecta movimiento
        if(i < 3){
            engine.getGraphics().setClearColor(ColorWrap.BLACK);
            System.out.println("Changed to Black");
        }
//        else{
//           //engine.getGraphics().setClearColor(ColorWrap.WHITE);
//            //System.out.println("Es blanco");
//        }
    }
}
