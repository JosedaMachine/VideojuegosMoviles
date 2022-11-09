package com.engine;

public interface Audio {

    Sound getSound(String name);

    Sound newSound(String name);

    void playSound(String name);
}
