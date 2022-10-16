package com.engine;

public interface Engine {
    IGraphics getGraphics();
    Input getInput();
    Audio getAudio();
    public void setGame(IGame game);
    public void resume();
    public void pause();
    public void update(double elapsedTime);
    public void render();
    public void loadResources();
}