package com.engineandroid;

import com.engine.Image;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;

public class ImageAndroid implements Image {
    Bitmap image;
    String name;

    ImageAndroid(AssetManager ass, String path, String name){
        InputStream is = null;
        try {
            is = ass.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = BitmapFactory.decodeStream(is);
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return  image.getHeight();
    }

    @Override
    public boolean isLoaded() {
        return image != null;
    }

    public Bitmap getScaledImage(int width, int heigth){
        return Bitmap.createScaledBitmap(image, width, heigth, false);
    }
}
