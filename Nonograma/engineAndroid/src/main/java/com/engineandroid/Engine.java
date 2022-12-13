package com.engineandroid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Engine implements Runnable{

    Graphics graphics;
    Input input;
    Audio audio;
    IGame currGame;
    AssetManager assetManager_;
    private Thread engineThread;

    boolean running, firstRun;

    public Engine(SurfaceView view, int logicWidth_ , int logicHeight_, Pair<Integer, Integer>adBanSize){
        currGame = null;
        graphics = new Graphics(view, logicWidth_, logicHeight_, adBanSize);
        input = new Input(view, graphics);
        assetManager_ = view.getContext().getAssets();
        audio = new Audio(assetManager_);
        firstRun = false;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Input getInput() {
        return input;
    }

    public Audio getAudio() {
        return audio;
    }

    /**
     * Asigna un juego a ejecutar. No se inicializa dicho hasta antes de iniciar el bucle principal
     * por motivos de inicializado del motor gráfico y prevención de errorres.
     * @param game juego que se ejecuta
     */
    public void setGame(IGame game) {
        currGame = game;
    }

    public BufferedReader openFile(String fileName) throws IOException {
        return new BufferedReader(
                new InputStreamReader(assetManager_.open(fileName), "UTF-8"));
    }

    public IGame getGame() {
        return currGame;
    }

    public void resume() {
        if (!this.running) {
            // Solo hacemos algo si no nos estábamos ejecutando ya
            // (programación defensiva)
            this.running = true;
            // Lanzamos la ejecución de nuestro método run() en un nuevo Thread.
            this.engineThread = new Thread(this);
            this.engineThread.start();
            if(currGame != null)
                this.currGame.onResume();
        }
    }

    public void pause() {
        if (this.running) {
            this.running = false;
            while (true) {
                try {
                    if(currGame != null)
                        this.currGame.onPause();
                    this.engineThread.join();
                    this.engineThread = null;
                    break;
                } catch (InterruptedException ie) {
                    // Esto no debería ocurrir nunca...
                }
            }
        }
    }

    public void orientationChanged(boolean isHorizontal){
        currGame.orientationChanged(isHorizontal);
    }

    public void processInput() {
        ArrayList<TouchEvent> list =input.getEventList();
        for (int i = 0; i < list.size(); i++){
            this.currGame.processInput(list.get(i));
        }
        input.flushEvents();
    }

    @Override
    public void run() {
        if (engineThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }

        // Si el Thread se pone en marcha
        // muy rápido, la vista podría todavía no estar inicializada.
        while(this.running && (graphics.getWidth() == 0 || currGame == null));

        //Lanzamos aqui el init del game ya que muchos valores de posición van en función del
        //tamaño del dispositivo.
        if(!firstRun){
            currGame.init();
            firstRun = true;
        }

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

            this.processInput();

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

    public void update(double elapsedTime) {
        currGame.update(elapsedTime);
    }

    public void render() {
        //~Mutex~
        //Prevenimos que en ningún otro sitio del código se permita pintar, bloqueando el canvas
        //para su modificación a continuación
        graphics.prepare();
        this.currGame.render(graphics);
        graphics.finish();
    }

    public Context getContext(){
        return this.graphics.getContext();
    }

    public void sendMessage(Message msg){
        if(currGame != null)
            currGame.sendMessage(msg);
    }



}
