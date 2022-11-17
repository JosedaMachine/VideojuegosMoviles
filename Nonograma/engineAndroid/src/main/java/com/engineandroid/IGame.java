package com.engineandroid;


public interface IGame {
    void changeScene(SceneBase newScene);
    SceneBase getScene();
    void init();
    void update(double elapsedTime);
    void render(Graphics graphics);
    void processInput(TouchEvent event);
    void loadImages(Graphics graphics);
}
