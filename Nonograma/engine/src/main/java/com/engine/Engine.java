package com.engine;

import com.gamelogic.NonogramaGame;

public interface Engine {
    Graphics getGraphics();
    Input getInput();
    Audio getAudio();
    public void setScene(NonogramaGame scene);
}