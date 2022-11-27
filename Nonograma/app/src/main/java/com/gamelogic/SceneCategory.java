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

public class SceneCategory implements SceneBase {

    Font title, levelLabel;
    Engine engine;
    private Fade fade;
    private final int maxLevels = 20;

    public SceneCategory(Engine engine_) {
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

        int size = (int)(engine.getGraphics().getLogicWidth() * 0.40f);

        int numCols = 2, numFils = 2;

        int xOffset = (int) (engine.getGraphics().getLogicWidth() * 0.05f),
                yOffset = (int) (engine.getGraphics().getLogicWidth() * 0.05f);

        int posX = (engine.getGraphics().getLogicWidth() - (size*numCols + xOffset*(numCols-1)))/2;
        int posY = engine.getGraphics().getLogicHeight()/2 - size/2;

        int cont = 0;
        for (int i = 0; i < numFils; i++){

            for (int j = 0; j < numCols; j++){
                int newPosX = posX + (j*size) + (j*xOffset),
                        newPosY = posY + (i*size) + (i*yOffset);

                levels.add(createLevel(newPosX, newPosY, size, size, cont));
                cont++;
            }
        }
    }

    //Boton de creacion de nivel
    private Button createLevel(int x, int y, int sizeX, int sizeY, final int i){
        final Button button = new Button("", x ,y, sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){

                        //Si no esta bloqueado
                        if(i == 0 || GameManager.instance().getLevelIndex(Category.values()[i-1]) == maxLevels){
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
                    engine.getGame().changeScene(new SceneStory(engine, i));
                }
            }
        };

        button.setColor(ColorWrap.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage(
                    (i > 0 && GameManager.instance().getLevelIndex(Category.values()[i-1]) < maxLevels)?"lock":"category" + i));

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
            graphics.drawImage(graphics.getImage("empty"), (int)(b.getX() + size*0.25f),
                    (int) (b.getY() - size * 0.075f), size/2, (int)(size * 0.15f));

            graphics.drawText(GameManager.instance().getLevelIndex(Category.values()[i]) + "/"
                    + maxLevels, (int) (b.getX() + dime.first*0.22f), (int) (b.getY() + dime.second*0.3f));
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

        for(int i = 0; i<4;i++) {
            im = graphics.newImage("category" + i + ".png");
            if (!im.isLoaded())
                System.out.println("No se ha encontrado la imagen");
            graphics.loadImage(im, "category" + i );
        }

        engine.getAudio().newSound("wrong.wav");

        title = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.05f),true);
        levelLabel = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.04f),true);
    }
}
