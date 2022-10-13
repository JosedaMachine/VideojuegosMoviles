package com.enginepc;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GraphicsPC implements IGraphics {

    private JFrame window;
    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;

    GraphicsPC(JFrame view){
        window = view;
        bufferStrategy = window.getBufferStrategy();
        graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
    }

    @Override
    public Image newImage(String name, int width, int height) {
        return new ImagePC(name, width, height);
    }

    @Override
    public IFont newFont(String name, int size, boolean isBold) {
        return new FontPC(name , size, isBold);
    }

    @Override
    public void clear(int color) {
        this.graphics2D.setColor(new Color(color, true));
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        //Se pierde el color previo
        //Meter una variable del color para no perderlo?
    }

    @Override
    public void translate(int x, int y) {
        //Sí, es así de fácil
        graphics2D.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        //Este también es así de fácil
        graphics2D.scale(x, y);
    }

    @Override
    public void render(SceneBase scene) {
        do {
            do {
                try {
                    clear(0);
                    scene.render(this);
                }
                finally {
                    graphics2D.dispose(); //Elimina el contexto gráfico y libera recursos del sistema realacionado
                }
            } while(bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while(bufferStrategy.contentsLost());
    }

    @Override
    public void save() {
        //Guardar juego
    }

    @Override
    public void restore() {
        //Restaurar, entiendo
    }

    @Override
    public void drawImage(Image image) {
        drawImage(image, 0, 0);
    }

    @Override
    public void drawImage(Image image, int x, int y) {
        drawImage(image, x, y, 1, 1);
    }

    @Override
    public void drawImage(Image image, int x, int y, float scaleX, float scaleY) {
        ImagePC copy = (ImagePC) image;
        int width = (int) (copy.getWidth() * scaleX);
        int height = (int) (copy.getHeight() * scaleY);

        graphics2D.drawImage(copy.getJavaImage(), x - width / 2, y - height / 2, width, height, null);
    }

    @Override
    public void setColor(int color) {
        this.graphics2D.setColor(new Color(color, true));
    }

    @Override
    public void setFont(IFont font) {
        FontPC pcFont = (FontPC) font;
        this.graphics2D.setFont(pcFont.currFont);
    }

    @Override
    public void fillSquare(int x, int y, int size) {
        this.graphics2D.fillRect(x, y, x + size, y + size);
    }

    @Override
    public void drawSquare(int x, int y, int size) {
        this.graphics2D.drawRect(x, y, size, size);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics2D.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawText(String text, int x, int y) {
        this.graphics2D.drawString(text, x, y);
    }

    @Override
    public int getWidth() {
        return window.getWidth();
    }

    @Override
    public int getHeight() {
        return window.getHeight();
    }

    public BufferStrategy getBufferStrategy(){return bufferStrategy;}
}
