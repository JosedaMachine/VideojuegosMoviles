package com.gamelogic.scenes;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.util.ArrayList;
import java.util.List;

public class SceneQuickLevels implements SceneBase {

    Font title, numFont;
    Font titleLittle;
    Engine engine;
    private Fade fade;
    public SceneQuickLevels(Engine engine_) {
        this.engine = engine_;
    }

    private Button bttReturn;

    List<Button> levels;

    @Override
    public void init() {

        loadResources(engine.getGraphics());

        int logicWidth = engine.getGraphics().getLogicWidth();
        int logicHeight = engine.getGraphics().getLogicHeight();
        //Fade In
        fade = new Fade(engine,
                0, 0,
                    logicWidth, logicHeight,
                500, 500, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        //Lista de botones con los diferentes tamanyos de tablero
        levels = new ArrayList<>();

        int sizeX = (int)(logicWidth * 0.155f),
            sizeY = (int)(logicHeight * 0.055f);
//
        int posX = (int) (logicWidth/3.5 - sizeX/2);
        int posY = logicHeight/3 -  sizeY/2;
        levels.add(createLevel("4x4", posX, posY, sizeX, sizeY, 4, 4, false));

        posX = logicWidth/2 - sizeX/2;
        levels.add(createLevel("5x5", posX, posY, sizeX, sizeY, 5, 5, false));

        posX = (int) (logicWidth/1.38 - sizeX/2);
        levels.add(createLevel("5x10", posX, posY, sizeX, sizeY, 5, 10, true));

        posY = logicHeight/2 -  sizeY/2;
        posX = (int) (logicWidth/3.5 - sizeX/2);
        levels.add(createLevel("8x8", posX, posY, sizeX, sizeY, 8, 8, false) );

        posX = logicWidth/2 - sizeX/2;
        levels.add(createLevel("10x10", posX, posY, sizeX, sizeY, 10, 10, true) );

        posX = (int) (logicWidth/1.38 - sizeX/2);
        levels.add(createLevel("10x15", posX, posY, sizeX, sizeY, 10, 15, true) );

        int offset = (int)(logicWidth * 0.16f),
                bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        //Boton Return to menu
        bttReturn = new Button("menu", logicWidth/2 - bttWidth/2,
                logicHeight- bttHeight*3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        setSelected(true);
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete() && isSelected()){
                    setSelected(false);
                    engine.getGame().previousScene();
//                    engine.getGame().changeScene("SceneLevels");
//                    engine.getGame().pushScene(new SceneTitle(engine));
                }
            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(ColorWrap.BLACK);
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));
    }

    //Boton de creacion de nivel
    private Button createLevel(String text, int x, int y, int sizeX, int sizeY, final int i, final int j, boolean small){
        final Button button = new Button(text, x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        //Iniciar nivel con medidas adecuadas
                        engine.getAudio().playSound("click.wav");
                        setSelected(true);
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete() && isSelected()){
                    setSelected(false);
                    engine.getGame().pushScene(new SceneGame(engine , i, j));
                    fade.reset();
                    fade.setState(Fade.STATE_FADE.In);
                    fade.triggerFade();
                }
            }
        };

        button.setFont(titleLittle);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        return button;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto
        String title = "Select puzzle size";
        Pair<Double, Double> dime = graphics.getStringDimensions(title);
        graphics.drawText(title, (int) (graphics.getLogicWidth()/2 - dime.first/2), (int) (graphics.getLogicHeight()*0.15 + dime.second/2));

        //Botones
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).render(graphics);
        }
        bttReturn.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        bttReturn.update(deltaTime);

        for(int i = 0; i < levels.size(); i++){
            levels.get(i).update(deltaTime);
        }
    }

    @Override
    public void input(TouchEvent event) {
        bttReturn.input(event);
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).input(event);
        }
    }

    @Override
    public void loadResources(Graphics graphics) {
        int logicHeight = engine.getGraphics().getLogicHeight();

        title = graphics.newFont("arcade.TTF",(int)(logicHeight * 0.05f),true);
        numFont = graphics.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
        titleLittle = graphics.newFont("arcade.TTF",(int)(logicHeight * 0.035f),true);
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
}
