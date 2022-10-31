package com.enginepc;

import com.engine.IFont;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.FontFormatException;


public class FontPC implements IFont {

    private int size;
    private boolean isBold;

    private Font font;

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

        font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            if(isBold_){
                font = font.deriveFont(Font.BOLD);
            }
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        font = font.deriveFont(Font.TRUETYPE_FONT, size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isBold() {
        return isBold;
    }

    public Font getFont() {return font;};
}
