package com.engine;

// Base genérica de las escenas para manejo en Game
public interface SceneBase {
    void init(IGame game, IGraphics graphics, Audio audio);

    void render(IGraphics graphics);

    void update(double deltaTime);

    void input(IGame game, Audio audio, TouchEvent event_);

    void loadResources(IGraphics graphics, Audio audio);
}
