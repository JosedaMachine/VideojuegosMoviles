package com.gamelogic;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.TouchEvent;

public abstract class Button {
    private Image backgroundImage = null;
    private IFont font = null;
    private final String text;
    private int color;

    private final int posX, posY, sizeX, sizeY;

    public Button(String text_, int posX_, int posY_, int sizeX_, int sizeY_){
        text = text_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
    }

    public void render(IGraphics graphics){
        //Imagen de fondo
        if(backgroundImage != null)
            graphics.drawImage(backgroundImage, posX, posY, sizeX, sizeY);

        //Texto interior
        if(font != null){
            graphics.setColor(color, 1.0f);
            graphics.setFont(font);
            Pair<Double, Double> dime = graphics.getStringDimensions(text);

            graphics.drawText(text, (int) (posX +  sizeX/2 - dime.first/2) , (int)(posY + sizeY/2 + dime.second/3));
        }
    }

    public void setFont(IFont font) {
        this.font = font;
    }

    public abstract void input(TouchEvent event_);

    public abstract void update(double deltaTime);

    //Comprobacion para gestionar input
    public boolean isInside(int pixelX, int pixelY){
        return (pixelX > posX && pixelX <= posX + sizeX) && ( pixelY > posY && pixelY <= posY + sizeY);
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
