package com.gamelogic.scenes;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.Button;
import com.gamelogic.Fade;

public class SceneDefeat implements SceneBase {

    private final Engine engine;

    private Button button;
    private Font title, buttonFont;
    private final String victoryText = "DEFEAT!";

    private Fade fade;

    public SceneDefeat(Engine engine_) {
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
        button = new Button("To Menu", posX, posY,sizeX, sizeY) {
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
                    engine.getGame().changeScene(new SceneTitle(engine));
                }
            }
        };

        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
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
        //TODO: igual no se debería multiplicar por la escala la pos x,y desde aqui y hacerlo desde el propio draw image
        graphics.drawImage(im, logicWidth/2 - (int)((im.getWidth()*scale)/2),
                logicHeight/2 - (int)((im.getHeight()*scale)/2), scale, scale);

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
    public void input(TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadResources(Graphics graphics) {
        graphics.newImage("crosssquare.png", "cross");
        graphics.newImage("emptysquare.png", "empty");
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
}
