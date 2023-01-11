package com.engineandroid;

import android.graphics.Color;

public class ColorWrap {

    /**
     * Valor entero que representa rgba
     */
    public static Integer RED = null;
    /**
     * Valor entero que representa rgba
     */
    public static Integer BLUE = null;
    /**
     * Valor entero que representa rgba
     */
    public static Integer GRAY = null;
    /**
     * Valor entero que representa rgba
     */
    public static Integer WHITE = null;
    /**
     * Valor entero que representa rgba
     */
    public static Integer BLACK = null;    /**
     * Valor entero que representa rgba
     */
    public static Integer GREEN = null;



    /**
     * Inicializar los valores de los colores
     */
    public static void  Init(){
        BLACK = android.graphics.Color.BLACK;
        RED = android.graphics.Color.RED;
        BLUE = android.graphics.Color.BLUE;
        WHITE = android.graphics.Color.WHITE;
        GRAY = android.graphics.Color.GRAY;
        GREEN = Color.GREEN;
    }
}
