package com.engineandroid;

import com.engine.IFont;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FontAndroid implements IFont {
    int size;
    boolean isBold;
    Typeface currFont;

    public FontAndroid(AssetManager ass, String path, int size, boolean bold){
        this.isBold = bold;
        this.size = size;
        currFont = Typeface.createFromAsset(ass, path);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isBold() {
        return isBold;
    }

    public Typeface getFont(){
        return currFont;
    }
}
