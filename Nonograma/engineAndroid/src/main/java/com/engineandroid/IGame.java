package com.engineandroid;


import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileOutputStream;

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
    void orientationChanged(boolean isHorizontal);
    void sendMessage(Message message);
    void save(FileOutputStream file);
    void restore(BufferedReader reader);
    UserInterface getUserInterface();
}
