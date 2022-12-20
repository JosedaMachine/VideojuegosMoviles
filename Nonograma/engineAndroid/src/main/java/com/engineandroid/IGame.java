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
    void update(Engine engine,double elapsedTime);
    void render(Graphics graphics);
    void processInput(TouchEvent event);
    void loadResources(Graphics graphics, Audio audio);
    void onResume();
    void onPause();
    void orientationChanged(boolean isHorizontal);
    void sendMessage(Message message);
    void save();
    void restore();
    UserInterface getUserInterface();
}
