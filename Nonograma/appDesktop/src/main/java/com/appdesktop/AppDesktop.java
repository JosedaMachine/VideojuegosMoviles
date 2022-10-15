package com.appdesktop;

import com.gamelogic.SceneGame;
import com.enginepc.*;
import com.engine.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class AppDesktop {
    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.7;
        double height = screenSize.getHeight() * 0.7;

        ColorPC.Init();
        Color black = (Color) ColorPC.BLACK;

        renderView.setSize((int) width, (int) height);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        EnginePC engine = new EnginePC(renderView);

        SceneBase scene = new SceneGame(engine);

        engine.setScene(scene);
        //Tambien se podrian cargar los recursos dentro del setscene
        engine.loadResources();
        engine.resume();
    }
}