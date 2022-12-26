package com.engine;


// Envuelve los sonidos
public interface Sound {
    // Reproduce el sonido
    void play();

    // Pausa el sonido
    void pause();

    // Comprueba que el sonido se haya cargado correctamente
    boolean isLoaded();

    // Cambia el sonido a loop en función del parámetro
    void setLoop(boolean l);

    // Cambia el volumen del sonido en función de vol
    void setVolume(int vol);

    // Comprueba si el sonido ya está sonando
    boolean alreadyPlaying();
}
