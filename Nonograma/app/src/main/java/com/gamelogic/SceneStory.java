package com.gamelogic;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class SceneStory implements SceneBase {

    Font title;
    Font titleLittle;
    Engine engine;
    private Fade fade;
    public SceneStory(Engine engine_) {
        this.engine = engine_;
    }

    List<Button> levels;

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

        //Lista de botones con los diferentes tamanyos de tablero
        levels = new ArrayList<>();

        int sizeX = (int)(engine.getGraphics().getLogicWidth() * 0.155f),
                sizeY = (int)(engine.getGraphics().getLogicHeight() * 0.055f);
//
        int posX = (int) (engine.getGraphics().getLogicWidth()/3.5 - sizeX/2);
        int posY = engine.getGraphics().getLogicHeight()/3 -  sizeY/2;

        //TODO: anyadir niveles en for
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 4, 4, false));

        posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 5, 5, false));

        posX = (int) (engine.getGraphics().getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 5, 10, true));

        posY = engine.getGraphics().getLogicHeight()/2 -  sizeY/2;
        posX = (int) (engine.getGraphics().getLogicWidth()/3.5 - sizeX/2);
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 8, 8, false) );

        posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 10, 10, true) );

        posX = (int) (engine.getGraphics().getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel("", posX, posY, sizeX, sizeX, 10, 15, true) );
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
                    engine.getGame().changeScene(new SceneGame(engine , i, j));
                }
            }
        };

        button.setFont(titleLittle);
        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("lock"));

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

        fade.render();
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).update(deltaTime);
        }
    }

    @Override
    public void input(TouchEvent event) {
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).input(event);
        }
    }

    @Override
    public void loadResources(Graphics graphics) {
        Image im = graphics.newImage("lock.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "lock");

        title = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
        titleLittle = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.035f),true);
    }
}
