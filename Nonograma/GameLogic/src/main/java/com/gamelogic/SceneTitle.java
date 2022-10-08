package com.gamelogic;

import com.engine.Engine;
import com.engine.SceneBase;

import java.awt.Button;
import java.awt.Panel;
import java.awt.TextField;

//TODO Implemtar render y update para scene Game
//TODO implement Scene
public class SceneTitle implements SceneBase {

    private Engine engine;

    @Override
    public void init() {
        engine.getGraphics().newFont("st",0,true);
    }

    @Override
    public void render() {

    }

    @Override
    public void update(double deltaTime) {

    }
}
