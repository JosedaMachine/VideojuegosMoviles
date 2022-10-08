package com.enginepc;

import com.engine.IFont;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.FontFormatException;


public class FontPC implements IFont {

    int size;
    boolean isBold;

    Font currFont;

    FontPC(String fontName, int size_, boolean isBold_){
        size = size_;
        isBold = isBold_;

        //path a ruta del asset a partir de la raiz del proyecto
        InputStream is = null;
        try {
            is = new FileInputStream(fontName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        currFont = null;
        try {
            currFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currFont = currFont.deriveFont(Font.TRUETYPE_FONT, size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isBold() {
        return isBold;
    }
}
