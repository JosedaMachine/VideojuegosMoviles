package com.engine;

import com.engine.TouchEvent;
import java.util.List;

public interface Input {
    List<TouchEvent> getTouchEvents();
    void processInput();
}
