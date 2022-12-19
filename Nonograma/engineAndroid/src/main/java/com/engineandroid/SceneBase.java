package com.engineandroid;

import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.FileOutputStream;

// Base genérica de las escenas para manejo en Game
public interface SceneBase {
    void init();

    void render(Graphics graphics);

    void update(double deltaTime);

    void input(TouchEvent event);

    void loadResources(Graphics graphics);

    void onResume();

    void onPause();

    void processMessage(Message msg);

    void orientationChanged(boolean isHorizontal);

    void save(String filename, SharedPreferences mPreferences);

    void restore(BufferedReader reader, SharedPreferences mPreferences);

    void horizontalLayout(Graphics g, int logicWidth, int logicHeight);

    void verticalLayout(Graphics g, int logicWidth, int logicHeight);
}
