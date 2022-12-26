package com.engine;

// Base genérica de las escenas para manejo en Game
public interface SceneBase {
    void init();

    void render(IGraphics graphics);

    void update(double deltaTime);

    void input(TouchEvent event);

    void loadResources(IGraphics graphics);
}
