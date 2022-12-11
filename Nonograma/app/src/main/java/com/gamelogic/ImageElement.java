package com.gamelogic;

import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.SceneElement;
import com.engineandroid.TouchEvent;

public abstract class ImageElement implements SceneElement {
    Image image;
    int posX, posY, sizeX, sizeY;
    protected ImageElement(Image img, int x, int y, int sizeX, int sizeY){
        setImage(img);
        setPos(x, y);
        setSize(sizeX, sizeY);
    }

    public void setPos(int x, int y){
        posX = x; posY = y;
    }

    public void setSize(int x, int y){
        sizeX = x; sizeY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(image, posX, posY, sizeX, sizeY);
    }
    @Override
    public abstract void update(double deltaTime);

    @Override
    public abstract void input(TouchEvent event_);
}
