package com.engine;


// Envuelve los sonidos
public interface Sound {
    // Reproduce el sonido
    void play();

    // Cambia el sonido a loop en función del parámetro
    void setLoop(boolean l);

    // Cambia el volumen del sonido en función de vol
    void setVolume(int vol);

}
