package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class SceneLevels implements SceneBase {

    IFont title;
    IFont titleLittle;
    Engine engine;
    private Fade fade;
    public SceneLevels(Engine engine_) {
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
        fade.setColor(IColor.BLACK);
        fade.triggerFade();

        //Lista de botones con los diferentes tamanyos de tablero
        levels = new ArrayList<>();

        int sizeX = (int)(engine.getGraphics().getLogicWidth() * 0.155f),
            sizeY = (int)(engine.getGraphics().getLogicHeight() * 0.055f);
//
        int posX = (int) (engine.getGraphics().getLogicWidth()/3.5 - sizeX/2);
        int posY = engine.getGraphics().getLogicHeight()/3 -  sizeY/2;
        levels.add(createLevel("4x4", posX, posY, sizeX, sizeY, 4, 4, false));

        posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel("5x5", posX, posY, sizeX, sizeY, 5, 5, false));

        posX = (int) (engine.getGraphics().getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel("5x10", posX, posY, sizeX, sizeY, 5, 10, true));

        posY = engine.getGraphics().getLogicHeight()/2 -  sizeY/2;
        posX = (int) (engine.getGraphics().getLogicWidth()/3.5 - sizeX/2);
        levels.add(createLevel("8x8", posX, posY, sizeX, sizeY, 8, 8, false) );

        posX = engine.getGraphics().getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel("10x10", posX, posY, sizeX, sizeY, 10, 10, true) );

        posX = (int) (engine.getGraphics().getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel("10x15", posX, posY, sizeX, sizeY, 10, 15, true) );
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
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));

        return button;
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK, 1.0f);

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
    public void loadResources(IGraphics graphics) {
        Image im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        title = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
        titleLittle = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.035f),true);
    }
}
