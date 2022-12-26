package com.gamelogic;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGame;
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
    private Fade fade;
    public SceneLevels() {
    }

    List<Button> levels;

    @Override
    public void init(IGame game, IGraphics graphics, Audio audio) {

        loadResources(graphics, audio);

        //Fade In
        fade = new Fade(
                0, 0,
                graphics.getLogicWidth(), graphics.getLogicHeight(),
                1000, 1000, Fade.STATE_FADE.In);
        fade.setColor(IColor.BLACK);
        fade.triggerFade();

        //Lista de botones con los diferentes tamanyos de tablero
        levels = new ArrayList<>();

        int sizeX = (int)(graphics.getLogicWidth() * 0.155f),
            sizeY = (int)(graphics.getLogicHeight() * 0.055f);
//
        int posX = (int) (graphics.getLogicWidth()/3.5 - sizeX/2);
        int posY = graphics.getLogicHeight()/3 -  sizeY/2;
        levels.add(createLevel(audio, graphics, game, "4x4", posX, posY, sizeX, sizeY, 4, 4, false));

        posX = graphics.getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel(audio, graphics, game,"5x5", posX, posY, sizeX, sizeY, 5, 5, false));

        posX = (int) (graphics.getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel(audio, graphics, game,"5x10", posX, posY, sizeX, sizeY, 5, 10, true));

        posY = graphics.getLogicHeight()/2 -  sizeY/2;
        posX = (int) (graphics.getLogicWidth()/3.5 - sizeX/2);
        levels.add(createLevel(audio, graphics, game,"8x8", posX, posY, sizeX, sizeY, 8, 8, false) );

        posX = graphics.getLogicWidth()/2 - sizeX/2;
        levels.add(createLevel(audio, graphics, game,"10x10", posX, posY, sizeX, sizeY, 10, 10, true) );

        posX = (int) (graphics.getLogicWidth()/1.38 - sizeX/2);
        levels.add(createLevel(audio, graphics, game,"10x15", posX, posY, sizeX, sizeY, 10, 15, true) );
    }

    //Boton de creacion de nivel
    private Button createLevel(final Audio audio, IGraphics g, final IGame game, String text, int x, int y, int sizeX, int sizeY, final int i, final int j, boolean small){
        final Button button = new Button(text, x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        //Iniciar nivel con medidas adecuadas
                        audio.playSound("click.wav");
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
                    game.changeScene(new SceneGame(i, j));
                }
            }
        };

        button.setFont(titleLittle);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(g.getImage("empty"));

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

        fade.render(graphics);
    }

    @Override
    public void update(double deltaTime) {
        fade.update(deltaTime);
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).update(deltaTime);
        }
    }

    @Override
    public void input(IGame game, Audio audio, TouchEvent event) {
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).input(event);
        }
    }

    @Override
    public void loadResources(IGraphics graphics, Audio audio) {
        graphics.newImage("emptysquare.png", "empty");

        title = graphics.newFont("arcade.TTF",(int)(graphics.getLogicHeight() * 0.05f),true);
        titleLittle = graphics.newFont("arcade.TTF",(int)(graphics.getLogicHeight() * 0.035f),true);
    }
}
