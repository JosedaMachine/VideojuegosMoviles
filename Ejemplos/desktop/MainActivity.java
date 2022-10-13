package com.example.desktop;

import javax.swing.JFrame;

public class MainActivity {

    public static void main(String[] args) {
        JFrame renderView = new JFrame("Mi aplicación");

        renderView.setSize(600, 400);
        renderView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renderView.setIgnoreRepaint(true);

        renderView.setVisible(true);
        Engine engine = new Engine(renderView);

        MyScene scene = new MyScene(engine);

        engine.setScene(scene);
        engine.resume();
    }


}