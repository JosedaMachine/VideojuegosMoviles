package com.gamelogic;

import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.TouchEvent;

public abstract class Button {
    private Image backgroundImage = null;
    private Font font = null;
    private final String text;
    private int color;

    private final int posX, posY, sizeX, sizeY;
    private boolean selected;
    public Button(String text_, int posX_, int posY_, int sizeX_, int sizeY_){
        text = text_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        selected = false;
    }

    public void render(Graphics graphics){
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

    public void setFont(Font font) {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getX(){return posX;}
    public int getY(){return posY;}
    public int getSizeX(){return sizeX;}
    public int getSizeY(){return sizeY;}
}
