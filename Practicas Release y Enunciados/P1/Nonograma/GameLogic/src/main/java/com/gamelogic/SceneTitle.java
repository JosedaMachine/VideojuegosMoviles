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
import com.engine.Sound;
import com.engine.TouchEvent;

public class SceneTitle implements SceneBase {

    private Fade fade;
    private Button button;
    private IFont title;
    private String titleText = "Nonogram";
    public SceneTitle() {
    }

    @Override
    public void init(final IGame game, IGraphics graphics, final Audio audio) {
        loadResources(graphics, audio);


        //Fade In
        fade = new Fade(
                        0, 0,
                             graphics.getLogicWidth(), graphics.getLogicHeight(),
                       1000, 1000, Fade.STATE_FADE.In);
        fade.setColor(IColor.BLACK);
        fade.triggerFade();

        //Posicion y tamanyo de boton (290x100)
        int sizeX = (int)(graphics.getLogicWidth() * 0.483f),
                sizeY = (int)(graphics.getLogicHeight() * 0.111f);

        int posX = graphics.getLogicWidth()/2 - sizeX/2;
        int posY = graphics.getLogicHeight()/2 - sizeY/2;

        //Boton de play
        button = new Button("Play", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        audio.playSound("click.wav");

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
                    game.changeScene(new SceneLevels());
                }
            }
        };

        button.setFont(title);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(graphics.getImage("empty"));


        audio.startMusic();

    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK, 1.0f);

        Pair<Double, Double> dime = graphics.getStringDimensions(titleText);
        //Texto del titulo
        graphics.drawText(titleText, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()*0.25 + dime.second/2));

        //Boton
        button.render(graphics);

        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        //Vacio
        fade.update(deltaTime);
        button.update(deltaTime);
    }

    @Override
    public void input(IGame game, Audio audio, TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadResources(IGraphics graphics, Audio audio) {
        graphics.newImage("crosssquare.png", "cross");

        graphics.newImage("emptysquare.png", "empty");

        audio.setMusic("music.wav");
        audio.newSound("click.wav");
        //0.88 es el porcentaje que ocupa la fuente arcade en alto de pantalla lógica, es decir un 8%
        title = graphics.newFont("arcade.TTF",(int)(graphics.getLogicHeight() * 0.088f),true);
    }
}
