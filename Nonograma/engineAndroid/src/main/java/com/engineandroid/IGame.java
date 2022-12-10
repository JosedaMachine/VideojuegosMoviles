package com.engineandroid;


import android.os.Bundle;

public interface IGame {
    void changeScene(SceneBase newScene);
    SceneBase getScene();
    void init();
    void update(double elapsedTime);
    void render(Graphics graphics);
    void processInput(TouchEvent event);
    void loadImages(Graphics graphics);
    void onResume();
    void onPause();
    void sendMessage(Bundle message);
}
