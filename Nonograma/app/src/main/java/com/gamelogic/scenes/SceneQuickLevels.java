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
import com.gamelogic.enums.PALETTE;
import com.gamelogic.managers.GameManager;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SceneQuickLevels implements SceneBase {

    Font title, numFont;
    Font titleLittle;
    Engine engine;
    private Fade fade;

    ConstraintX constrX;
    ConstraintY constrY;


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
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
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
        levels.add(createLevel("4x4", posX, posY, sizeX, sizeY, 4, 4, false, 10));

        posX = logicWidth/2 - sizeX/2;
        levels.add(createLevel("5x5", posX, posY, sizeX, sizeY, 5, 5, false, 12));

        posX = (int) (logicWidth/1.38 - sizeX/2);
        levels.add(createLevel("5x10", posX, posY, sizeX, sizeY, 5, 10, true, 15));

        posY = logicHeight/2 -  sizeY/2;
        posX = (int) (logicWidth/3.5 - sizeX/2);
        levels.add(createLevel("8x8", posX, posY, sizeX, sizeY, 8, 8, false, 15) );

        posX = logicWidth/2 - sizeX/2;
        levels.add(createLevel("10x10", posX, posY, sizeX, sizeY, 10, 10, true, 20) );

        posX = (int) (logicWidth/1.38 - sizeX/2);
        levels.add(createLevel("10x15", posX, posY, sizeX, sizeY, 10, 15, true, 25) );

        int bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        //Boton Return to menu
        bttReturn = new Button("menu", logicWidth/2 - bttWidth/2,
                logicHeight- bttHeight*2, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(engine.getGraphics(),event_.getX_(),event_.getY_())){
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
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        if(engine.getGraphics().orientationHorizontal()){
            horizontalLayout(engine.getGraphics(), logicWidth, logicHeight);
        }
    }

    //Boton de creacion de nivel
    private Button createLevel(String text, int x, int y, int sizeX, int sizeY, final int i, final int j, boolean small, int reward){
        final Button button = new Button(text, x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(engine.getGraphics(), event_.getX_(),event_.getY_())){
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
                    engine.getGame().pushScene(new SceneGame(engine , i, j, reward));
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
    public void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {
        int sizeX = (int)(logicWidth * 0.155f * 3),
                sizeY = (int)(logicHeight * 0.055f) * 3;

        int[] xOffsets = {sizeX*2, -sizeX/2 , (int) (-sizeX*1.5f * 2)};
        int[] yOffsets = {-sizeY, sizeY/2};

        int jW = 0;
        int jH = 0;


        ConstraintX a = ConstraintX.values()[0];

        titleLittle = g.newFont("arcade.TTF",(int)(logicHeight * 0.035f * 2),true);
        numFont = g.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f * 2), false);
        title = g.newFont("arcade.TTF",(int)(logicHeight * 0.05f) * 2,true);
        for (int i = 0; i < levels.size(); i++){
            if(jW >= 3) {
                jW = 0;
                jH = 1;
            }
            Button b = levels.get(i);
            b.setFont(titleLittle);
            b.setSize(sizeX, sizeY);
            b.setUsingConstraints(true);
            b.setConstraints(g, ConstraintX.values()[jW], xOffsets[jW], ConstraintY.CENTER, yOffsets[jH]);
            jW++;
        }

        int bttWidth = (int)(logicWidth * 0.25f *2),
                bttHeight = (int)(logicWidth * 0.0833f * 2);

        bttReturn.setFont(numFont);
        bttReturn.setSize(bttWidth, bttHeight);
        bttReturn.setUsingConstraints(true);
        bttReturn.setConstraints(g, ConstraintX.CENTER, -bttWidth/2, ConstraintY.BOTTOM, (int) -(bttHeight * 1.25f));

    }

    @Override
    public void verticalLayout(Graphics g, int logicWidth, int logicHeight) {
        int sizeX = (int)(logicWidth * 0.155f),
                sizeY = (int)(logicHeight * 0.055f);

        float[] widthFactors = {3.5f, 2, 1.38f};
        float[] heightFactors = {3, 2};
        int jW = 0;
        int jH = 0;
        titleLittle = g.newFont("arcade.TTF",(int)(logicHeight * 0.035f),true);
        numFont = g.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);
        title = g.newFont("arcade.TTF",(int)(logicHeight * 0.05f),true);
        for (int i = 0; i < levels.size(); i++){
            if(jW >= 3) {
                jW = 0;
                jH = 1;
            }
            Button b = levels.get(i);
            b.setFont(titleLittle);
            b.setSize(sizeX, sizeY);
            b.setX((int) (logicWidth/widthFactors[jW] - sizeX/2));
            b.setY((int) (logicHeight/heightFactors[jH] -  sizeY/2));
            b.setUsingConstraints(false);
            jW++;
        }
        int bttWidth = (int)(logicWidth * 0.25f),
                bttHeight = (int)(logicWidth * 0.0833f);

        bttReturn.setUsingConstraints(false);
        bttReturn.setFont(numFont);
        bttReturn.setX(logicWidth/2 - bttWidth/2);
        bttReturn.setY(logicHeight- bttHeight*2);
        bttReturn.setSize(bttWidth, bttHeight);
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
