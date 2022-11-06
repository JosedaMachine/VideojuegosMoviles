package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

public class SceneVictory implements SceneBase {

    private final Engine engine;

    private Button button;
    private IFont title, buttonFont;
    private String victoryText = "VICTORY!";

    private final Board checkBoard;
    public SceneVictory(Engine engine_, Board checkboard) {
        this.checkBoard = checkboard;
        this.engine = engine_;
    }

    @Override
    public void init() {

        //NOTA
        //Al parecer el renderer no se ha inicializado, por lo que cuando ponemos las medidas con respecto a la pantalla
        // nos da 0 puesto que si width = 0 pues width/2 = 0

        loadResources(engine.getGraphics());

        int sizeX = 225, sizeY = 50;

        int posX = engine.getGraphics().getWidth()/2 - sizeX/2;
        int posY = engine.getGraphics().getHeight()- (int)(sizeY*2.5);

        button = new Button("To Menu", posX, posY,sizeX, sizeY) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(button.isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        engine.getGame().changeScene(new SceneTitle(engine));
                    }
                }
            }
        };

        button.setFont(buttonFont);
        button.setColor(IColor.BLACK);
        button.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setFont(title);
        graphics.setColor(IColor.BLACK);

        Pair<Double, Double> dime = graphics.getStringDimensions(victoryText);
        graphics.drawText(victoryText, (int) (graphics.getWidth()/2 - dime.first/2), (int) (graphics.getHeight()/8 + dime.second/2));

        checkBoard.drawBoard(engine, graphics.getWidth()/2 - checkBoard.getWidth()/2, graphics.getHeight()/2 - checkBoard.getHeight()/2, true);

        button.render(graphics);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void input(TouchEvent event) {
        button.input(event);
    }

    @Override
    public void loadResources(IGraphics graphics) {
        Image im = graphics.newImage("crosssquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "cross");

        im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        title = engine.getGraphics().newFont("arcade.TTF",75,true);

        buttonFont = engine.getGraphics().newFont("arcade.TTF",50,true);
    }
}
