package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.TouchEvent;

public abstract class Button {
    private Image backgroundImage = null;
    private IFont font = null;
    private String text;
    private int color;

    private int posX;
    private int posY;
    private int sizeX;
    private int sizeY;

    public Button(String text_, int posX_, int posY_, int sizeX_, int sizeY_){
        text = text_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
    }

    public void render(IGraphics graphics){
        if(backgroundImage != null)
            graphics.drawImage(backgroundImage, posX - sizeX/2, posY - sizeY/2, sizeX, sizeY);

        if(font != null){
            graphics.setColor(color);
            graphics.setFont(font);
            Pair<Double, Double> dime = graphics.getStringDimensions(text);
            graphics.drawText(text, (int) (posX - dime.first/2), (int) (posY + dime.second/2));
        }


    }

    public void setFont(IFont font) {
        this.font = font;
    }

    public abstract void input(TouchEvent event_);

    public boolean isInside(int pixelX, int pixelY){
        return (pixelX > posX && pixelX <= posX + sizeX ) && ( pixelY > posY && pixelY <= posY + sizeY);
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
