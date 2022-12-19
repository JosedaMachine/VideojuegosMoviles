package com.gamelogic.utils;

import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
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

    private int posX, posY, sizeX, sizeY;
    private int offsetX, offsetY;

    private boolean selected;

    private ConstraintX constrX;
    private ConstraintY constrY;

    //Necesitamos una referencia al grafics para obtener valores cuando se usan constraints.
    Graphics g;

    boolean usingConstraints;

    public Button(String text_, int posX_, int posY_, int sizeX_, int sizeY_){
        text = text_;
        posX = posX_;
        posY = posY_;
        sizeX = sizeX_;
        sizeY = sizeY_;
        selected = false;
        usingConstraints = false;
    }

    public void render(Graphics graphics){
        //Imagen de fondo
        if(backgroundImage != null){
            if(usingConstraints)
                graphics.drawImage(backgroundImage, constrX, constrY , offsetX,offsetY,sizeX, sizeY);
            else graphics.drawImage(backgroundImage, posX, posY, sizeX, sizeY);
        }

        //Texto interior
        if(font != null){
            graphics.setColor(color, 1.0f);
            graphics.setFont(font);
            Pair<Double, Double> dime = graphics.getStringDimensions(text);

            if(usingConstraints)
                graphics.drawText(text, constrX, constrY , (int) (offsetX +  sizeX/2 - dime.first/2) , (int)(offsetY + sizeY/2 + dime.second/3));
            else graphics.drawText(text, (int) (posX +  sizeX/2 - dime.first/2) , (int)(posY + sizeY/2 + dime.second/3));
        }
    }

    public void setConstraints(Graphics g_, ConstraintX constrX_,int offsetX_, ConstraintY constrY_, int offsetY_){
        g = g_;
        constrX = constrX_;
        constrY = constrY_;
        offsetX = offsetX_;
        offsetY = offsetY_;
    }

    public void setUsingConstraints(boolean using){
        usingConstraints = using;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public abstract void input(TouchEvent event_);

    public abstract void update(double deltaTime);

    //Comprobacion para gestionar input
    public boolean isInside(Graphics g, int pixelX, int pixelY){
        if(usingConstraints){
            int posX_ = g.getConstraintXValue(constrX);
            int posY_ = g.getConstraintYValue(constrY);
            return (pixelX > (posX_ + offsetX) && pixelX <= (posX_ + offsetX) + sizeX) && ( pixelY > (posY_ + offsetY) && pixelY <= (posY_ + offsetY) + sizeY);
        }
        else return (pixelX > posX && pixelX <= posX + sizeX) && ( pixelY > posY && pixelY <= posY + sizeY);
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

    public int getX(){
        if(usingConstraints){
            int posX_ = g.getConstraintXValue(constrX);
            return (posX_ + offsetX);
        }
        else return posX;
    }
    public int getY(){
        if(usingConstraints){
            int posY_ = g.getConstraintYValue(constrY);
            return (posY_ + offsetY);
        }
        return posY;
    }
    public void setX(int x_){posX = x_;}
    public void setY(int y_){posY = y_;}

    public void setSize(int x, int y) {
        sizeX = x;
        sizeY = y;
    }
    public void setSizeX(int x_){sizeX = x_;}
    public void setSizeY(int y_){sizeY = y_;}

    public int getSizeX(){return sizeX;}
    public int getSizeY(){return sizeY;}
}
