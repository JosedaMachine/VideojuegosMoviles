package com.examplejavareal;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

//Clase interna encargada de obtener el SurfaceHolder y pintar con el canvas
public class MyRenderClass implements Runnable{

    private JFrame myView;
    private BufferStrategy bufferStrategy;
    private Graphics2D graphics2D;

    private Thread renderThread;

    private boolean running;

    private MyScene scene;

    private ArrayList<MouseEvent> eventList;

    public MyRenderClass(JFrame myView){
        this.myView = myView;
        this.myView.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                //Component c = (Component)evt.getSource();
                System.out.println("componentResized: "+evt.getSource());
                graphics2D.dispose();

                bufferStrategy.show();
                graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
            }
        });

        this.myView.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                eventList.add(mouseEvent);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                eventList.add(mouseEvent);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                eventList.add(mouseEvent);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                eventList.add(mouseEvent);
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                eventList.add(mouseEvent);
            }
        });

        this.bufferStrategy = this.myView.getBufferStrategy();
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        this.eventList = new ArrayList<>();
    }

    public int getWidth(){
        return this.myView.getWidth();
    }

    public int getHeight(){
        return this.myView.getHeight();
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

            this.proccessInput();

            this.update(elapsedTime);

            if (currentTime - informePrevio > 1000000000l) {
                long fps = frames * 1000000000l / (currentTime - informePrevio);
                System.out.println("" + fps + " fps");
                frames = 0;
                informePrevio = currentTime;
            }
            ++frames;

            // Pintamos el frame
            do {
                do {
                    Graphics graphics = this.bufferStrategy.getDrawGraphics();
                    try {
                        this.render();
                    }
                    finally {
                        graphics.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
                    }
                } while(this.bufferStrategy.contentsRestored());
                this.bufferStrategy.show();
            } while(this.bufferStrategy.contentsLost());

            //
            /*
            // Posibilidad: cedemos algo de tiempo. Es una medida conflictiva...
            try { Thread.sleep(1); } catch(Exception e) {}
            */
        }
    }

    protected void update(double deltaTime) {
        this.scene.update(deltaTime);
    }

    public void setScene(MyScene scene) {
        this.scene = scene;
    }

    protected void renderText(float x, float y, String fontPath, String text, Color color, int size){
        //path a ruta del asset a partir de la raiz del proyecto
        InputStream is = null;
        try {
            is = new FileInputStream(fontPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Font awtFont = null;
        try {
            awtFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        awtFont = awtFont.deriveFont(Font.TRUETYPE_FONT, size);
//        Graphics graphics = bufferStrategy.getDrawGraphics();
//        graphics.setFont(awtFont);
        this.graphics2D.setColor(color);
        this.graphics2D.setFont(awtFont);
        this.graphics2D.drawString(text, x, y);
    }

    protected void renderImages(float x, float y, int width, int height, String path) {
        Image img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.drawImage(img, (int)x, (int)y, width, height,null);
    }

    protected void renderCircle(float x, float y, float r){
        this.graphics2D.setColor(Color.white);
        this.graphics2D.fillOval((int)x, (int)y, (int)r*2, (int)r*2);
        this.graphics2D.setPaintMode();
    }

    protected void render() {
        // "Borramos" el fondo.
        this.graphics2D.setColor(Color.BLACK);
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        // Pintamos la escena
        this.scene.render();
    }

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

    void proccessInput(){
        for(int i = 0; i < eventList.size(); i++)
            this.scene.input(eventList.get(i));
        eventList.clear();
    }

}

