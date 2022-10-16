package com.engineandroid;

import com.engine.Image;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;

public class ImageAndroid implements Image {
    Bitmap image;

    ImageAndroid(AssetManager ass, String path){
        InputStream is = null;
        try {
            is = ass.open("images/tom.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = BitmapFactory.decodeStream(is);
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public boolean isLoaded() {
        return image != null;
    }
}
