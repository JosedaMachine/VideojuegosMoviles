package com.gamelogic;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.Sound;
import com.engineandroid.TouchEvent;

public class SceneTitle implements SceneBase {

    private final Engine engine;
    private Fade fade;
    private Button button;
    private Font title;
    private String titleText = "Nonogram";
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
                       1000, 1000, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        //Posicion y tamanyo de boton (290x100)
        int sizeX = (int)(engine.getGraphics().getLogicWidth() * 0.483f),
                sizeY = (int)(engine.getGraphics().getLogicHeight() * 0.111f);

        int posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        int posY = engine.getGraphics().getLogicHeight()/2 - sizeY/2;

        //Boton de play
        button = new Button("Play", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");

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
                if(fade.getFadeOutComplete()){
                    engine.getGame().changeScene(new SceneLevels(engine));
                }
            }
        };

        button.setFont(title);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));

        //Musica en loop
        Sound music =  engine.getAudio().getSound("music.wav");

        if(!music.alreadyPlaying()) {
            engine.getAudio().playSound("music.wav");
            music.setLoop(true);
            music.setVolume(-15);
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        Pair<Double, Double> dime = graphics.getStringDimensions(titleText);
        //Texto del titulo
        graphics.drawText(titleText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()*0.25 + dime.second/2));

        //Boton
        button.render(graphics);

        fade.render();
    }

    @Override
    public void update(double deltaTime) {
        //Vacio
        fade.update(deltaTime);
        button.update(deltaTime);
    }

    @Override
    public void input(TouchEvent event) {
        button.input(event);
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
}