package com.enginepc;

import com.engine.Image;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagePC implements Image {
    java.awt.Image image;
    String name;

    ImagePC(String name){
        try {
            image = ImageIO.read(new File(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWidth(){
        return image.getWidth(null);
    }

    @Override
    public int getHeight(){
        return image.getHeight(null);
    }

    @Override
    public boolean isLoaded() {
        return image != null;
    }

    public java.awt.Image getImage(){
        return image;
    }

}
