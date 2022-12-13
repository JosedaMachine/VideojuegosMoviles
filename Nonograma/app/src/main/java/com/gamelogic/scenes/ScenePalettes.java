package com.gamelogic.scenes;


import com.engineandroid.ColorWrap;
import com.engineandroid.Engine;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.managers.GameManager;
import com.gamelogic.utils.Button;
import com.gamelogic.utils.Fade;

import java.util.ArrayList;
import java.util.List;

public class ScenePalettes implements SceneBase {

    private Engine engine;

    private Button button;
    List<Button> palettes;
    int selectedPalette;

    private Font title, buttonFont;
    private final String text = "Select a palette";

    private Fade fade;

    public ScenePalettes(Engine engine_) {
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
                    if (button.isInside(event_.getX_(), event_.getY_())) {
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
            palettes.add(createPalette(posX + size * i + offset*i, posY, size, size, i));
        }


        button.setFont(buttonFont);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty0"));
    }

    //Boton de seleccion de paleta
    private Button createPalette(int x, int y, int sizeX, int sizeY, final int i) {
        final Button button = new Button("", x, y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (isInside(event_.getX_(), event_.getY_())) {

                        GameManager gmInstance = GameManager.instance();

                        Pair<Boolean,Integer> p = gmInstance.getPaletteUnlocked(i);

                        //Si no esta bloqueado
                        if (p.first) {
                            engine.getAudio().playSound("click.wav");
                            setSelected(true);
                            gmInstance.setPalette(i);
                            setBackgroundImage(engine.getGraphics().getImage("spalette" + i));
                        } else if (p.second <= gmInstance.getMoney()){
                            gmInstance.addMoney(-p.second);
                            gmInstance.setPaletteUnlocked(i, true, 0);
                            setBackgroundImage(engine.getGraphics().getImage("palette" + i));
                        }
                        else
                            engine.getAudio().playSound("wrong.wav");
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                //quitar seleccion si otra es seleccionada
                if(isSelected() && GameManager.instance().getPalette().ordinal() != i){
                    setSelected(false);
                    setBackgroundImage(engine.getGraphics().getImage("palette" + i));
                }
            }
        };

        button.setColor(ColorWrap.BLACK);

        if(GameManager.instance().getPaletteUnlocked(i).first) {
            String prefix;
            if (i == GameManager.instance().getPalette().ordinal()) {
                prefix = "s";
                button.setSelected(true);
            } else
                prefix = "";

            button.setBackgroundImage(engine.getGraphics().getImage(prefix + "palette" + i));
        }
        else
            button.setBackgroundImage(engine.getGraphics().getImage("lock"));

        return button;
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();

        graphics.setFont(title);
        graphics.setColor(ColorWrap.BLACK, 1.0f);

        for (int i = 0; i < palettes.size(); i++) {
            Button b = palettes.get(i);
            b.render(graphics);

            Pair<Boolean, Integer> p = GameManager.instance().getPaletteUnlocked(i);

            if(!p.first) {

                String money = Integer.toString(p.second);
                Pair<Double, Double> dime = graphics.getStringDimensions(money);

                graphics.drawText(money, (int)(b.getX() +  b.getSizeX()/2 - dime.first / 2), (int)(b.getY() + b.getSizeY() + dime.second));

                Image coin = graphics.getImage("coin");
                float coinScale = 55 / (float) coin.getWidth();
                float offsetY = coin.getHeight() * coinScale * 0.2f;

                graphics.drawImage(coin, (int) (b.getX() + b.getSizeX()/2 - (coin.getWidth()*coinScale)/2),
                        (int)(b.getY() + b.getSizeY() + dime.second + offsetY), coinScale, coinScale);
            }
        }



        //Texto
        Pair<Double, Double> dime = graphics.getStringDimensions(text);
        graphics.drawText(text, (int) (logicWidth / 2 - dime.first / 2), (int) (logicHeight / 9 + dime.second / 2));

        //Boton de vuelta al menu
        button.render(graphics);
        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        button.update(deltaTime);
        for (int i = 0; i < palettes.size(); i++) {
            palettes.get(i).update(deltaTime);
        }
    }

    @Override
    public void input(TouchEvent event) {
        button.input(event);
        for (int i = 0; i < palettes.size(); i++) {
            palettes.get(i).input(event);
        }
    }

    @Override
    public void loadResources(Graphics graphics) {
        graphics.newImage("palette0.png", "palette0");
        graphics.newImage("palette1.png", "palette1");
        graphics.newImage("palette2.png", "palette2");
        graphics.newImage("selectedpalette0.png", "spalette0");
        graphics.newImage("selectedpalette1.png", "spalette1");
        graphics.newImage("selectedpalette2.png", "spalette2");

        graphics.newImage("lock.png", "lock");

        engine.getAudio().newSound("wrong.wav");

        title = graphics.newFont("arcade.TTF", 65, true);
        buttonFont = graphics.newFont("arcade.TTF", 50, true);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


}
