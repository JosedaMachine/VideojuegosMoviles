package com.appdesktop;

import com.gamelogic.Nonograma;
import com.enginepc.*;
import com.engine.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AppDesktop {

    private static JFrame renderView;

    public static void main(String[] args) {
        renderView = new JFrame("Mi aplicación");

        //Tamanyo ventana
        int width = 600;
        int height = 900;

        ColorPC.Init();

        //Params renderView iniciales
        renderView.setSize(width, height);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);
        renderView.setVisible(true);

        //Inicio engine
        EnginePC engine = new EnginePC(renderView, width, height);

        //Inicio game
        IGame game = new Nonograma(engine);

        engine.setGame(game);
        engine.resume();



        renderView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(renderView,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });

    }
}