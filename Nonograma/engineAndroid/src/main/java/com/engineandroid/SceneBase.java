package com.engineandroid;

import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.FileOutputStream;

// Base genérica de las escenas para manejo en Game
public interface SceneBase {
    void init(Engine engine);

    void render(Graphics graphics);

    void update(Engine engine, double deltaTime);

    void input(Engine e, TouchEvent event);

    void loadResources(Graphics graphics, Audio audio);

    void onResume();

    void onPause();

    void processMessage(Engine engine,Message msg);

    void orientationChanged(Graphics graphics, boolean isHorizontal);

    void save(Engine engine, String filename, SharedPreferences mPreferences);

    void restore(Engine engine, BufferedReader reader, SharedPreferences mPreferences);

    void horizontalLayout(Graphics g, int logicWidth, int logicHeight);

    void verticalLayout(Graphics g, int logicWidth, int logicHeight);
}
