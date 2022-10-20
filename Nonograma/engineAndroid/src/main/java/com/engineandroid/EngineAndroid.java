package com.engineandroid;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.engine.*;

public class EngineAndroid implements Engine, Runnable{

    GraphicsAndroid graphics;
    IGame currGame;
    private Thread renderThread;

    //TODO hacer getters
    private SurfaceView myView;
    private SurfaceHolder holder;

    boolean running;

    public EngineAndroid(SurfaceView view){
        myView = view;
        graphics = new GraphicsAndroid(view);
        holder = myView.getHolder();
    }

    @Override
    public IGraphics getGraphics() {
        return null;
    }

    @Override
    public IInput getInput() {
        return null;
    }

    @Override
    public Audio getAudio() {
        return null;
    }

    @Override
    public void setGame(IGame game) {
        currGame = game;
        game.init();
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

    }

    @Override
    public void render() {

    }

    @Override
    public void loadResources() {

    }

    @Override
    public void processInput() {

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
        while(this.running && this.myView.getWidth() == 0);
        // Espera activa. Sería más elegante al menos dormir un poco.

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
            this.update(elapsedTime);
            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            while (!this.holder.getSurface().isValid());
            this.canvas = this.holder.lockCanvas();
            this.render();
            this.holder.unlockCanvasAndPost(canvas);
        }
    }
}
