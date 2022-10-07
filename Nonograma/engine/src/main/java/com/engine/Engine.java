package com.engine;

public interface Engine {
    Graphics getGraphics();
    Input getInput();
    Audio getAudio();
    public void setScene(SceneBase scene);
}