package com.appdesktop;

import com.gamelogic.SceneGame;
import com.enginepc.*;
import com.engine.*;
import javax.swing.JFrame;

public class AppDesktop {
    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        EnginePC engine = new EnginePC(renderView);

        SceneBase scene = new SceneGame(engine);

        engine.setScene(scene);
        engine.resume();
    }
}