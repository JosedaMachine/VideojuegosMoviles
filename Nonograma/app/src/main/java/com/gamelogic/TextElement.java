package com.gamelogic;

import android.graphics.Color;

import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Pair;
import com.engineandroid.SceneElement;
import com.engineandroid.TouchEvent;

public abstract class TextElement implements SceneElement {
    Font textFont;
    int posX, posY, sizeX, sizeY;
    String text;
    int color;

    protected TextElement(Font f, int x, int y, int sizeX, int sizeY, String text){
        setPos(x, y);
        setSize(sizeX, sizeY);
        setTextFont(f);
        setText(text);
        setColor(Color.BLACK);
    }

    public void setColor(int c){
        color = c;
    }
    public void setSize(int x, int y){
        sizeX = x; sizeY = y;
    }

    public void setText(String txt){
        text = txt;
    }

    public String getText() {
        return text;
    }

    public void setTextFont(Font f){
        textFont = f;
    }

    public void setPos(int x, int y){
        posX = x; posY = y;
    }

    public Font getTextFont() {
        return textFont;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    @Override
    public void render(Graphics graphics){
        graphics.setColor(color, 1.0f);
        graphics.setFont(getTextFont());
        Pair<Double, Double> dime = graphics.getStringDimensions(getText());

        graphics.drawText(text, (int) (posX +  sizeX/2 - dime.first/2) ,
                (int)(posY + sizeY/2 + dime.second/3));
    };

    @Override
    public abstract void update(double deltaTime);

    @Override
    public abstract void input(TouchEvent event_);
}
