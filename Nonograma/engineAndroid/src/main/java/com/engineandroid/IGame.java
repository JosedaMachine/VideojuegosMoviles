package com.engineandroid;


import android.os.Bundle;

public interface IGame {
    void pushScene(SceneBase newScene);
    void previousScene();
    boolean changeScene(String sceneClassName);
    SceneBase getScene();
    void init();
    void update(double elapsedTime);
    void render(Graphics graphics);
    void processInput(TouchEvent event);
    void loadImages(Graphics graphics);
    void onResume();
    void onPause();
    void sendMessage(Bundle message);
    UserInterface getUserInterface();
}
