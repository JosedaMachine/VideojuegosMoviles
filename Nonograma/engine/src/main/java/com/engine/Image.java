package com.engine;

// Envuelve una imagen de mapa de bits para ser utilizada a modo de sprite
public interface Image {
    // Devuelve el ancho de la imagen
    int getWidth();
    // Devuelve el alto de la imagen
    int getHeight();
    // Comprueba que la imagen se haya cargado correctamente
    boolean isLoaded();
}
