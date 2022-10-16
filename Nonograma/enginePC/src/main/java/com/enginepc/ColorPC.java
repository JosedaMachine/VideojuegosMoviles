package com.enginepc;

import com.engine.IColor;

import java.awt.Color;

public class ColorPC extends IColor {

    public static void  Init(){
        IColor.BLACK = Color.black;
        IColor.RED = Color.red;
        IColor.BLUE = Color.blue;
        IColor.WHITE = Color.white;
        IColor.GRAY = Color.gray;
    }

}
