package com.engine;

public interface IFont {

    /**
     * Devuelve el tamaño de fuente.
     * El valor devuelvo no necesariamente es el tamaño de pixeles que ocupa en pantalla.
     */
    int getSize();

    /**
     * Devuelve si el tipo de fuente está en negrito.
     */
    boolean isBold();
}
