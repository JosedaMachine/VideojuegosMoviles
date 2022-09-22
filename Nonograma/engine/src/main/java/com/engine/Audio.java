package com.engine;

import com.engine.Sound;

public interface Audio {

    Sound getSound(String name);

    Sound newSound(String name);

    Sound playSound(String name);
}
