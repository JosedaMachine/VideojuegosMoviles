package com.engineandroid;

import com.engineandroid.Graphics;
import com.engineandroid.TouchEvent;

public interface SceneElement {
    void render(Graphics graphics);
    void update(double deltaTime);
    void input(TouchEvent event_);
}
