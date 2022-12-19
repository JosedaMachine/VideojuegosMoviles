package com.gamelogic.scenes;

import android.content.SharedPreferences;

import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.utils.Button;
import com.gamelogic.enums.CATEGORY;
import com.gamelogic.utils.Fade;
import com.gamelogic.managers.GameManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
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
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
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

        int bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        //Boton Return to menu
        bttReturn = new Button("menu", logicWidth/2 - bttWidth/2,
                logicHeight - bttHeight*2, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(engine.getGraphics(), event_.getX_(),event_.getY_())){
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

        if(engine.getGraphics().orientationHorizontal()){
            horizontalLayout(engine.getGraphics(), logicWidth, logicHeight);
        }
    }

    //Boton de creacion de nivel
    private Button createCategory(int x, int y, int sizeX, int sizeY, final int i){
        final Button button = new Button("", x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(engine.getGraphics(), event_.getX_(),event_.getY_())){

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
//            int size = b.getSizeX();
//            graphics.drawImage(graphics.getImage("buttonbox"), (int)(b.getX() + size*0.25f),
//                    (int) (b.getY() - size * 0.075f), size/2, (int)(size * 0.15f));
//
//            graphics.drawText(GameManager.instance().getLevelIndex(CATEGORY.values()[i]) + "/"
//                    + GameManager.instance().getMaxLevel(), (int) (b.getX() + dime.first*0.22f), (int) (b.getY() + dime.second*0.3f));
        }

        bttReturn.render(graphics);


        fade.render(graphics);
    }

    private void verticalLayout(Graphics g, int logicWidth, int logicHeight) {
//        int sizeX = (int)(logicWidth * 0.155f * 3),
//                sizeY = (int)(logicHeight * 0.055f) * 3;
//
//        int[] xOffsets = {sizeX*2, -sizeX/2 , (int) (-sizeX*1.5f * 2)};
//        int[] yOffsets = {-sizeY, sizeY/2};
//
//        int jW = 0;
//        int jH = 0;
//
//        ConstraintX a = ConstraintX.values()[0];
//
//        numFont = g.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
//        title = g.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
//        levelLabel = g.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.04f),true);
//
//        for (int i = 0; i < levels.size(); i++){
//            if(jW >= 3) {
//                jW = 0;
//                jH = 1;
//            }
//            Button b = levels.get(i);
//            b.setFont(titleLittle);
//            b.setSize(sizeX, sizeY);
//            b.setUsingConstraints(true);
//            b.setConstraints(ConstraintX.values()[jW], xOffsets[jW], ConstraintY.CENTER, yOffsets[jH]);
//            jW++;
//        }
//
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
                Button b = levels.get(cont);
                b.setSize(size, size);
                b.setX(newPosX);
                b.setY(newPosY);
                b.setUsingConstraints(false);
                cont++;
            }
        }

        int bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        bttReturn.setUsingConstraints(false);
        bttReturn.setFont(numFont);
        bttReturn.setX(logicWidth/2 - bttWidth/2);
        bttReturn.setY(logicHeight - bttHeight*2);
        bttReturn.setSize(bttWidth, bttHeight);

    }

    private void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {
//        int sizeX = (int)(logicWidth * 0.155f),
//                sizeY = (int)(logicHeight * 0.055f);
//
//        float[] widthFactors = {3.5f, 2, 1.38f};
//        float[] heightFactors = {3, 2};
//        int jW = 0;
//        int jH = 0;
//
//        numFont = g.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
//        title = g.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
//        levelLabel = g.newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.04f),true);
//        for (int i = 0; i < levels.size(); i++){
//            if(jW >= 3) {
//                jW = 0;
//                jH = 1;
//            }
//            Button b = levels.get(i);
//            b.setFont(titleLittle);
//            b.setSize(sizeX, sizeY);
//            b.setX((int) (logicWidth/widthFactors[jW] - sizeX/2));
//            b.setY((int) (logicHeight/heightFactors[jH] -  sizeY/2));
//            b.setUsingConstraints(false);
//            jW++;
//        }
        int size = (int)(logicWidth * 0.7f);
        int numCols = 2, numFils = 2;

        int xOffset = (int) (logicWidth * 0.05f),
                yOffset = (int) (logicWidth * 0.05f);

        int posX = (logicWidth - (size*numCols + xOffset*(numCols-1)))/2;
        int posY = (int)(logicHeight/2.5) - size/2;

        ConstraintX[] constrX = {ConstraintX.LEFT, ConstraintX.CENTER, ConstraintX.CENTER, ConstraintX.RIGHT};
        int[] xOffsets = {(int) (size*0.2f), (int) (-size*1.1f), (int) (size*0.005f), (int) (-size*1.3)};
        for (int i = 0; i < levels.size(); i++){
//            for (int j = 0; j < numCols; j++){
//                int newPosX = posX + (j*size) + (j*xOffset),
//                        newPosY = posY + (i*size) + (i*yOffset);
            Button b = levels.get(i);
            b.setSize(size, size);
            b.setUsingConstraints(true);
            b.setConstraints(constrX[i], xOffsets[i], ConstraintY.CENTER, -size/2);
//            }
        }

        int bttWidth = (int)(logicWidth * 0.25f *2),
                bttHeight = (int)(logicWidth * 0.0833f * 2);

        bttReturn.setFont(numFont);
        bttReturn.setSize(bttWidth, bttHeight);
        bttReturn.setUsingConstraints(true);
        bttReturn.setConstraints(ConstraintX.CENTER, -bttWidth/2, ConstraintY.BOTTOM, (int) -(bttHeight * 1.25f));
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

    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void orientationChanged(boolean isHorizontal) {
        int logicWidth = engine.getGraphics().getLogicWidth();
        int logicHeight = engine.getGraphics().getLogicHeight();

        if(isHorizontal){
            horizontalLayout(engine.getGraphics(), logicWidth, logicHeight);
        }else{
            verticalLayout(engine.getGraphics(), logicWidth, logicHeight);
        }
    }

    @Override
    public void save(FileOutputStream file, SharedPreferences mPreferences) {
    }

    @Override
    public void restore(BufferedReader reader, SharedPreferences mPreferences) {
    }
}
