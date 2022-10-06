package com.appdesktop;

import com.gamelogic.NonogramaGame;
import com.enginepc.EnginePC;
import javax.swing.JFrame;

public class AppDesktop {
    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        EnginePC engine = new EnginePC(renderView);

        No4aGame scene = new NonogramaGame(engine);

        engine.setScene(scene);
        engine.resume();
    }
}