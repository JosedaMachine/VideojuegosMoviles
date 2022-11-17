package com.engineandroid;

import android.graphics.Color;

public class ColorAndroid extends IColor {
    /**
     * Inicializar los valores de los colores
     */
    public static void  Init(){
        IColor.BLACK = Color.BLACK;
        IColor.RED = Color.RED;
        IColor.BLUE = Color.BLUE;
        IColor.WHITE = Color.WHITE;
        IColor.GRAY = Color.GRAY;
    }
}
