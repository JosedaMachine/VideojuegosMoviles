package com.engineandroid;

// Base genérica de las escenas para manejo en Game
public interface SceneBase {
    void init();

    void render(Graphics graphics);

    void update(double deltaTime);

    void input(TouchEvent event);

    void loadResources(Graphics graphics);

    void onResume();

    void onPause();
}
