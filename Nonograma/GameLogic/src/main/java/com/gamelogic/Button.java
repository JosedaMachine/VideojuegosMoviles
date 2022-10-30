package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.TouchEvent;

public class Button {
    private Image backgroundImage = null;
    private IFont font = null;
    private String text;
    private IGame game;
    private int color;

    private int posX;
    private int posY;
    private int sizeX;
    private int sizeY;

    private Engine engine;

    public Button(String text_, int posX_, int posY_, int sizeX_, int sizeY_, IGame game_, Engine engine_){
        text = text_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        engine = engine_;
        game = game_;
    }

    public void render(IGraphics graphics){

        if(font != null){
            graphics.setColor(color);
            graphics.setFont(font);
            graphics.drawText(text, posX, posY);
        }

        if(backgroundImage != null)
            graphics.drawImage(backgroundImage, posX, posY, sizeX, sizeY);
    }

    public void setFont(IFont font) {
        this.font = font;
    }

    public void input(TouchEvent event_){
        if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
//            if(event_.getID_() == LEFT_BUTTON){
//                System.out.println("Izq " + "X: " +  event_.getX_()+  " Y: " + event_.getY_());
//            }else if(event_.getID_() == RIGHT_BUTTON){
//                System.out.println("Der " + "X: " +  event_.getX_()+  " Y: " + event_.getY_());
//            }
            if(isInside(event_.getX_(),event_.getY_())){
                game.changeScene(new SceneGame(engine));
            }
        }
    }

    private boolean isInside(int pixelX, int pixelY){
        return (pixelX > posX && pixelX <= posX + sizeX ) && ( pixelY > posY && pixelY <= posY + sizeY);
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
