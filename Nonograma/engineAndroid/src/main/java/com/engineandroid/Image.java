package com.engineandroid;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.io.IOException;
import java.io.InputStream;

public class Image {
    Bitmap image;

    Image(AssetManager ass, String path){
        InputStream is = null;
        try {
            is = ass.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public boolean isLoaded() {
        return image != null;
    }

    public Bitmap getScaledImage(int width, int heigth){
        return Bitmap.createScaledBitmap(image, width, heigth, true);
    }
}
