package com.engine;

public interface SceneBase {

    void init();

    void render(IGraphics graphics);

    void update(double deltaTime);

    void input(TouchEvent event);

    void loadImages(IGraphics graphics);
}
