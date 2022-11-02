package com.engineandroid;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.engine.*;

import java.util.ArrayList;

public class EngineAndroid implements Engine, Runnable{

    GraphicsAndroid graphics;
    InputAndroid input;
    AudioAndroid audio;
    IGame currGame;
    private Thread renderThread;

    boolean running;

    public EngineAndroid(SurfaceView view){
        currGame = null;
        graphics = new GraphicsAndroid(view);
        input = new InputAndroid(view);
        audio = new AudioAndroid(view.getContext().getAssets());
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
    public void loadResources() {
        this.currGame.loadImages(graphics);
    }

    @Override
    public void processInput() {
        ArrayList<TouchEvent> list =input.getEventList();
        for (int i = 0; i < list.size(); i++){
            this.currGame.processInput(list.get(i));
        }
        input.getEventList().clear();
    }

    @Override
    public void run() {
        if (renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && (graphics.getWidth() == 0 || currGame == null));
        // Espera activa. Sería más elegante al menos dormir un poco.

        currGame.init();

        long lastFrameTime = System.nanoTime();

        long informePrevio = lastFrameTime; // Informes de FPS
        int frames = 0;

        // Bucle de juego principal.
        while(running) {
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;

            // Informe de FPS
            double elapsedTime = (double) nanoElapsedTime / 1.0E9;

            //this.processInput();

            this.update(elapsedTime);

            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                //System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            render();
        }
    }

    @Override
    public void update(double elapsedTime) {
        currGame.update(elapsedTime);
    }

    @Override
    public void render() {
        graphics.lockCanvas();
        this.currGame.render(graphics);
        graphics.unlockCanvas();
    }
}
