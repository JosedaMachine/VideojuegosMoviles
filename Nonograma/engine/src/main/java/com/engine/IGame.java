package com.engine;

public interface IGame {
    void changeScene();
    int getScene();
    void init();
    void update(double elapsedTime);
    void render(IGraphics graphics);
    void processInput(TouchEvent event);
    void loadImages(IGraphics graphics);
}
