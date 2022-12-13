package com.gamelogic.scenes;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.utils.Button;
import com.gamelogic.enums.CATEGORY;
import com.gamelogic.utils.Fade;
import com.gamelogic.managers.GameManager;

import java.util.ArrayList;
import java.util.List;

public class SceneStoryLevels implements SceneBase {

    Font title, numFont;
    Engine engine;
    private Fade fade;
    List<Button> levels;
    private Button bttReturn;

    private CATEGORY category;
    private int lockIndex;

    public SceneStoryLevels(Engine engine_, int category) {
        this.engine = engine_;
        this.category = CATEGORY.values()[category];

        lockIndex = GameManager.instance().getLevelIndex(CATEGORY.values()[category]);
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
                500, 500, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        //Lista de botones con los diferentes tamanyos de tablero
        levels = new ArrayList<>();

        int size = (int)(logicWidth * 0.155f);

        int numCols = 4, numFils = GameManager.instance().getMaxLevel()/4;

        int xOffset = (int) (logicWidth * 0.05f),
                yOffset = (int) (logicWidth * 0.05f);

        int posX = (int) ((logicWidth - (size*numCols + xOffset*(numCols-1)))/2);
        int posY = logicHeight/3 -  size/2;

        int cont = 0;
        for (int i = 0; i < numFils; i++){
            for (int j = 0; j < numCols; j++){
                int newPosX = posX + (j*size) + (j*xOffset),
                        newPosY = posY + (i*size) + (i*yOffset);
                //TODO: Cada nivel diferente + imagenes diferentes si desbloqueado o no
                levels.add(createLevel("", newPosX, newPosY, size, size, 4, 4, cont));
                cont++;
            }
        }

        int offset = (int)(logicWidth * 0.16f),
                bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        //Boton Return to category
        bttReturn = new Button("back", logicWidth/2 - bttWidth/2,
                logicHeight - bttHeight*2, bttWidth, bttHeight) {
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
                }
            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(ColorWrap.BLACK);
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("empty0"));

    }

    //Boton de creacion de nivel
    private Button createLevel(String text, int x, int y, int sizeX, int sizeY, final int i, final int j, int lvlIndex){
        final Button button = new Button(text, x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        //Iniciar nivel con medidas adecuadas
                        if(lvlIndex <= lockIndex) {
                            engine.getAudio().playSound("click.wav");
                            setSelected(true);
                            if (fade.getState() != Fade.STATE_FADE.Out) {
                                fade.setState(Fade.STATE_FADE.Out);
                                fade.triggerFade();
                            }
                        }
                        else{
                            engine.getAudio().playSound("wrong.wav");
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete() && isSelected()){
                    setSelected(false);
                    fade.reset();
                    fade.setState(Fade.STATE_FADE.In);
                    fade.triggerFade();
                    engine.getGame().pushScene(new SceneGame(engine , i, j, category, lvlIndex));
                }
            }
        };

        button.setColor(ColorWrap.BLACK);
        //Nivel ya superado
        if(lvlIndex < lockIndex)
            button.setBackgroundImage(engine.getGraphics().getImage("tick"));
        //Siguente Nivel o bloqueado
        else
            button.setBackgroundImage(engine.getGraphics().getImage((lvlIndex != lockIndex)?"lock":"unlock"));


        return button;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto
        String title = "Select puzzle";
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
        graphics.newImage("lock.png", "lock");
        graphics.newImage("unlock.png", "unlock");
        graphics.newImage("tick.png","tick");

        numFont = graphics.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
        title = graphics.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}