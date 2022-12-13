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

public class SceneStoryCategories implements SceneBase {
    Font title, levelLabel, numFont;
    Engine engine;
    private Fade fade;

    public SceneStoryCategories(Engine engine_) {
        this.engine = engine_;
    }

    List<Button> levels;
    Button bttReturn;
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

        int size = (int)(logicWidth * 0.40f);

        int numCols = 2, numFils = 2;

        int xOffset = (int) (logicWidth * 0.05f),
                yOffset = (int) (logicWidth * 0.05f);

        int posX = (logicWidth - (size*numCols + xOffset*(numCols-1)))/2;
        int posY = (int)(logicHeight/2.5) - size/2;

        int cont = 0;
        for (int i = 0; i < numFils; i++){

            for (int j = 0; j < numCols; j++){
                int newPosX = posX + (j*size) + (j*xOffset),
                        newPosY = posY + (i*size) + (i*yOffset);

                levels.add(createCategory(newPosX, newPosY, size, size, cont));
                cont++;
            }
        }

        int offset = (int)(logicWidth * 0.16f),
                bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        //Boton Return to menu
        bttReturn = new Button("menu", logicWidth/2 - bttWidth/2,
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
    private Button createCategory(int x, int y, int sizeX, int sizeY, final int i){
        final Button button = new Button("", x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){

                        //Si no esta bloqueado
                        if(i == 0 || GameManager.instance().getLevelIndex(CATEGORY.values()[i-1]) == GameManager.instance().getMaxLevel()){
                            engine.getAudio().playSound("click.wav");
                            setSelected(true);
                            if(fade.getState() != Fade.STATE_FADE.Out) {
                                fade.setState(Fade.STATE_FADE.Out);
                                fade.triggerFade();
                            }
                        }
                        else
                            engine.getAudio().playSound("wrong.wav");
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
                    engine.getGame().pushScene(new SceneStoryLevels(engine, i));
                }
            }
        };

        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage(
                    (i > 0 && GameManager.instance().getLevelIndex(CATEGORY.values()[i-1]) < GameManager.instance().getMaxLevel())?"lock":"category" + i));

        return button;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Texto
        String title = "Select A Category";
        Pair<Double, Double> dime = graphics.getStringDimensions(title);
        graphics.drawText(title, (int) (graphics.getLogicWidth()/2 - dime.first/2),
                (int) (graphics.getLogicHeight()*0.15 + dime.second/2));

        graphics.setFont(levelLabel);

        //Botones
        for(int i = 0; i < levels.size(); i++){
            Button b = levels.get(i);

            b.render(graphics);
            int size = b.getSizeX();
            graphics.drawImage(graphics.getImage("buttonbox"), (int)(b.getX() + size*0.25f),
                    (int) (b.getY() - size * 0.075f), size/2, (int)(size * 0.15f));

            graphics.drawText(GameManager.instance().getLevelIndex(CATEGORY.values()[i]) + "/"
                    + GameManager.instance().getMaxLevel(), (int) (b.getX() + dime.first*0.22f), (int) (b.getY() + dime.second*0.3f));
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

        for(int i = 0; i<4;i++)
            graphics.newImage("category" + i + ".png", "category" + i );

        engine.getAudio().newSound("wrong.wav");

        numFont = graphics.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
        title = graphics.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
        levelLabel = graphics.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.04f),true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
