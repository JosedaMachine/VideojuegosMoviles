package com.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Font {
    int size;
    boolean isBold;
    Typeface currFont;

    public Font(AssetManager ass, String path, int size, boolean bold){
        this.isBold = bold;
        this.size = size;
        currFont = Typeface.createFromAsset(ass, path);
    }

    public int getSize() {
        return size;
    }

    public boolean isBold() {
        return isBold;
    }

    public Typeface getFont(){
        return currFont;
    }
}
