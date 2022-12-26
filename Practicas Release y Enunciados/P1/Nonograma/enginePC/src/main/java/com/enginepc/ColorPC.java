package com.enginepc;

import com.engine.IColor;
import java.awt.Color;

public class ColorPC extends IColor {
    /**
     * Inicializar los valores de los colores
     */
    public static void  Init(){
        IColor.BLACK = Color.black.getRGB();
        IColor.RED = Color.red.getRGB();
        IColor.BLUE = Color.blue.getRGB();
        IColor.WHITE = Color.white.getRGB();
        IColor.GRAY = Color.gray.getRGB();
    }

}
