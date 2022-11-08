package com.enginepc;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.IColor;
import com.engine.IGraphics;
import com.engine.IGame;
import com.engine.IInput;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
public class EnginePC implements Engine, Runnable{

    JFrame view;
    GraphicsPC graphics;
    IGame currGame;
    InputPC input;
    AudioPC audio;

    boolean running;
    private Thread renderThread;

    public EnginePC(JFrame renderView, int logicWidth, int logicHeight){
        view = renderView;

        //ESTO EL TICHER LO HACE EN GRAPHICSPC
        int intentos = 100;
        while(intentos-- > 0) {
            try {
                view.createBufferStrategy(2);
                break;
            }
            catch(Exception e) {
            }
        } // while pidiendo la creación de la buffeStrategy
        if (intentos == 0) {
            System.err.println("No pude crear la BufferStrategy");
            return;
        }

        graphics = new GraphicsPC(renderView, logicWidth, logicHeight);

        input = new InputPC(renderView, graphics);
        audio = new AudioPC();
    }

    @Override
    public IGraphics getGraphics() {
        return graphics;
    }

    @Override
    public IInput getInput() {
        return input;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setGame(IGame game) {
        currGame = game;
        game.init();
    }

    @Override
    public IGame getGame() {
        return currGame;
    }

    @Override
    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.renderThread = new Thread(this);
            this.renderThread.start();
        }
    }

    @Override
    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    this.renderThread.join();
                    this.renderThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }

    @Override
    public void update(double elapsedTime) {
        currGame.update(elapsedTime);
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        //Espera a crear ventana
        while(this.running && view.getWidth() == 0);

        long lastFrameTime = System.nanoTime();
        long informePrevio = lastFrameTime; // Informes de FPS


        // Bucle de juego principal.
        while(running && currGame != null) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Actualizamos
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;
            this.processInput();
            this.update(elapsedTime);
            this.render();
        }
    }

    @Override
    public void render() {
        do {
            do {
                try {
                    graphics.prepare(IColor.WHITE);
                    this.currGame.render(graphics);
                }
                finally{
                    graphics.finish();
                }
            } while(this.graphics.getBufferStrategy().contentsRestored());

            graphics.getBufferStrategy().show();
        } while(this.graphics.getBufferStrategy().contentsLost());
    }

    @Override
    public void processInput() {
        ArrayList<TouchEvent> list =input.getEventList();
        for (int i = 0; i < list.size(); i++){
            this.currGame.processInput(list.get(i));
        }
        input.getEventList().clear();
    }

}