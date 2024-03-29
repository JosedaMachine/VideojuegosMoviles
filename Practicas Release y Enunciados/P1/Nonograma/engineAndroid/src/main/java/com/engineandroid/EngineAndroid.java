package com.engineandroid;

import android.view.SurfaceView;

import com.engine.*;

import java.util.ArrayList;

public class EngineAndroid implements Engine, Runnable{

    GraphicsAndroid graphics;
    InputAndroid input;
    AudioAndroid audio;
    IGame currGame;
    private Thread engineThread;

    boolean running;

    public EngineAndroid(SurfaceView view, int logicWidth_ , int logicHeight_){
        currGame = null;
        graphics = new GraphicsAndroid(view, logicWidth_, logicHeight_);
        input = new InputAndroid(view, graphics);
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

    /**
     * Asigna un juego a ejecutar. No se inicializa dicho hasta antes de iniciar el bucle principal
     * por motivos de inicializado del motor gráfico y prevención de errorres.
     * @param game juego que se ejecuta
     */
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
            this.engineThread = new Thread(this);
            this.engineThread.start();
            if(currGame != null)
                this.currGame.onResume();
        }
    }

    @Override
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

    @Override
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
        // Espera activa. Sería más elegante al menos dormir un poco.

        //Lanzamos aqui el init del game ya que muchos valores de posición van en función del
        //tamaño del dispositivo.
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

    @Override
    public void update(double elapsedTime) {
        currGame.update(elapsedTime);
    }

    @Override
    public void render() {
        //~Mutex~
        //Prevenimos que en ningún otro sitio del código se permita pintar, bloqueando el canvas
        //para su modificación a continuación
        graphics.prepare(0);
        this.currGame.render(graphics);
        graphics.finish();
    }
}
