package com.engine;

public interface Engine {
    IGraphics getGraphics();
    IInput getInput();
    Audio getAudio();
    public void setGame(IGame game);
    public IGame getGame();
    public void resume();
    public void pause();
    public void update(double elapsedTime);
    public void render();
    public void processInput();
}