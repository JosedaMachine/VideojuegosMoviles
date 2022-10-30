package com.engine;

public interface Engine {
    IGraphics getGraphics();
    IInput getInput();
    Audio getAudio();
    public void setGame(IGame game);
    // TODO (no es todo pero pa tener en cuenta):
    // En clase IGame tiene un getter (get IState)
    public IGame getGame();
    public void resume();
    public void pause();
    public void update(double elapsedTime);
    public void render();
    public void loadResources();
    public void processInput();
}