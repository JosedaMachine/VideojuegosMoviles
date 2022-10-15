package com.gamelogic;

import com.engine.Engine;
import com.engine.IGraphics;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.awt.Button;
import java.awt.Panel;
import java.awt.TextField;

//TODO Implemtar render y update para scene Game
//TODO Dejar la carrera
public class SceneTitle implements SceneBase {

    private Engine engine;

    @Override
    public void init() {

        engine.getGraphics().newFont("st",0,true);

    }

    @Override
    public void render(IGraphics graphics) {

    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void input(TouchEvent event) {

    }


}
