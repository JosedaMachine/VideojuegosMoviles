package com.engine;

public interface Engine {
    IGraphics getGraphics();
    Input getInput();
    Audio getAudio();
    public void setScene(SceneBase scene);
    public void resume();
    public void pause();
    public void update(double elapsedTime);
}