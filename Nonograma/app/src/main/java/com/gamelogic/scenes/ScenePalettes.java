package com.gamelogic.scenes;


import android.content.SharedPreferences;

import com.engineandroid.Audio;
import com.engineandroid.ColorWrap;
import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Message;
import com.engineandroid.CustomPair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.managers.GameManager;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ScenePalettes implements SceneBase {
    private Button button;
    List<Button> palettes;

    private Font title, buttonFont;
    private final String text = "Select a palette";

    private Fade fade;

    public ScenePalettes() {    }

    @Override
    public void init(Engine engine) {
        Graphics graphics = engine.getGraphics();
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

        palettes = new ArrayList<>();

        int sizeX = 225, sizeY = 50;

        int posX = logicWidth / 2 - sizeX / 2;
        int posY = logicHeight - (int) (sizeY * 2.5);

        //Boton vuelta al menu
        button = new Button("Menu", posX, posY, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (button.isInside(engine.getGraphics(), event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");
                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if (fade.getFadeOutComplete()) {
                    engine.getGame().previousScene();
                }
            }
        };

        int size = (int) (logicWidth * 0.30f);
        int offset = (int)(logicWidth*0.025f);

        posX = (logicWidth - (size*3 + offset*2))/2 ;
        posY = logicHeight / 2 - size/2;

        for (int i = 0; i < 3; i++) {
            palettes.add(createPalette(engine,posX + size * i + offset*i, posY, size, size, i));
        }


        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));
    }

    //Boton de seleccion de paleta
    private Button createPalette(Engine e,int x, int y, int sizeX, int sizeY, final int i) {
        final Button button = new Button("", x, y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (isInside(e.getGraphics(), event_.getX_(), event_.getY_())) {

                        GameManager gmInstance = GameManager.instance();

                        CustomPair<Boolean,Integer> p = gmInstance.getPaletteUnlocked(i);

                        //Si no esta bloqueado
                        if (p.first) {
                            //Seleccionado
                            e.getAudio().playSound("click.wav");
                            setSelected(true);
                            gmInstance.setPalette(i);
                            setBackgroundImage(e.getGraphics().getImage("spalette" + i));
                        } else if (p.second <= gmInstance.getMoney()){
                            //Sin seleccionar
                            gmInstance.addMoney(-p.second);
                            gmInstance.updateInterface();
                            gmInstance.setPaletteUnlocked(i, true, 0);
                            setBackgroundImage(e.getGraphics().getImage("palette" + i));
                        }
                        else
                            e.getAudio().playSound("wrong.wav");
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                //quitar seleccion si otra es seleccionada
                if(isSelected() && GameManager.instance().getPalette().ordinal() != i){
                    setSelected(false);
                    setBackgroundImage(e.getGraphics().getImage("palette" + i));
                }
            }
        };

        button.setColor(ColorWrap.BLACK);

        //Diferente si seleccionado, no seleccionado y bloqueado
        if(GameManager.instance().getPaletteUnlocked(i).first) {
            String prefix;
            if (i == GameManager.instance().getPalette().ordinal()) {
                prefix = "s";
                button.setSelected(true);
            } else
                prefix = "";

            button.setBackgroundImage(e.getGraphics().getImage(prefix + "palette" + i));
        }
        else
            button.setBackgroundImage(e.getGraphics().getImage("lock"));

        return button;
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        //Botones de paleta
        for (int i = 0; i < palettes.size(); i++) {
            Button b = palettes.get(i);
            b.render(graphics);

            CustomPair<Boolean, Integer> p = GameManager.instance().getPaletteUnlocked(i);

            //Coste de paletas
            if(!p.first) {
                String money = Integer.toString(p.second);
                CustomPair<Double, Double> dime = graphics.getStringDimensions(money);

                graphics.drawText(money, (int)(b.getX() +  b.getSizeX()/2 - dime.first / 2), (int)(b.getY() + b.getSizeY() + dime.second));

                Image coin = graphics.getImage("coin");
                float coinScale = 55 / (float) coin.getWidth();
                float offsetY = coin.getHeight() * coinScale * 0.2f;

                graphics.drawImage(coin, (int) (b.getX() + b.getSizeX()/2 - (coin.getWidth()*coinScale)/2),
                        (int)(b.getY() + b.getSizeY() + dime.second + offsetY), coinScale, coinScale);
            }
        }



        //Texto
        CustomPair<Double, Double> dime = graphics.getStringDimensions(text);
        graphics.drawText(text, (int) (logicWidth / 2 - dime.first / 2), (int) (logicHeight / 9 + dime.second / 2));

        //Boton de vuelta al menu
        button.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(Engine engine,double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
        for (int i = 0; i < palettes.size(); i++) {
            palettes.get(i).update(deltaTime);
        }
    }

    @Override
    public void input(Engine engine, TouchEvent event) {
        button.input(event);
        for (int i = 0; i < palettes.size(); i++) {
            palettes.get(i).input(event);
        }
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        graphics.newImage("palette0.png", "palette0");
        graphics.newImage("palette1.png", "palette1");
        graphics.newImage("palette2.png", "palette2");
        graphics.newImage("selectedpalette0.png", "spalette0");
        graphics.newImage("selectedpalette1.png", "spalette1");
        graphics.newImage("selectedpalette2.png", "spalette2");

        graphics.newImage("lock.png", "lock");

        audio.newSound("wrong.wav");

        title = graphics.newFont("arcade.TTF", 65, true);
        buttonFont = graphics.newFont("arcade.TTF", 50, true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void processMessage(Engine e, Message msg) {

    }

    @Override
    public void orientationChanged(Graphics graphics, boolean isHorizontal) {

    }

    @Override
    public void save(Engine e, String filename, SharedPreferences mPreferences) {
    }

    @Override
    public void restore(BufferedReader reader, SharedPreferences mPreferences) {
    }

    @Override
    public void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {

    }

    @Override
    public void verticalLayout(Graphics g, int logicWidth, int logicHeight) {

    }


}
