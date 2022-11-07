package com.appdesktop;

import com.gamelogic.Nonograma;
import com.enginepc.*;
import com.engine.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class AppDesktop {
    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");

        int width = 600;
        int height = 900;

        ColorPC.Init();

        renderView.setSize(width, height);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        EnginePC engine = new EnginePC(renderView, width, height);

        IGame game = new Nonograma(engine);

        engine.setGame(game);
        //Tambien se podrian cargar los recursos dentro del setscene
//        engine.loadResources();
        engine.resume();
    }
}