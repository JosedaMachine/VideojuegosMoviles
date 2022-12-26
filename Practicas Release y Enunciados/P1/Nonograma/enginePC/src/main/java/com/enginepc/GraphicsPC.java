package com.enginepc;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;

import javax.swing.JFrame;

public class GraphicsPC implements IGraphics {

    private final int insetTop;
    private final JFrame window;
    private Graphics2D graphics2D;
    private final BufferStrategy bufferStrategy;

    int logicWidth, logicHeight;
    float scaleFactor;
    int translateFactorX, translateFactorY;

    HashMap<String, Image> imagesLoaded = new HashMap<>();

    private final String path = "appDesktop/assets/";

    GraphicsPC(JFrame view, int logicWidth_ , int logicHeight_){
        window = view;
        logicHeight = logicHeight_;
        logicWidth = logicWidth_;
        view.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                recalcFactors(window.getWidth(), window.getHeight());
            }
        });

        insetTop = view.getInsets().top;
        bufferStrategy = window.getBufferStrategy();
        graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
    }

    @Override
    public Image newImage(String name) {
        return new ImagePC(path + "images/" + name);
    }

    @Override
    public IFont newFont(String name, int size, boolean isBold) {
        return new FontPC(path + "fonts/"+ name , size, isBold);
    }

    @Override
    public void clear(int color) {
        this.graphics2D.setColor(new Color(color, true));
        this.graphics2D.fillRect(0,0, this.getWidth(), this.getHeight());
        this.graphics2D.setPaintMode();
    }

    @Override
    public void translate(int x, int y) {
        graphics2D.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        graphics2D.scale(x, y);
    }

    @Override
    public void prepare(int color){
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        this.clear(color);

//        //Bandas horizontales - DEBUG
//        fillRect(0,0, translateFactorX, window.getHeight());
//        fillRect(window.getWidth() - translateFactorX, 0, translateFactorX, window.getHeight());
//
//        //Bandas verticales - DEBUG
//        fillRect(0,0, window.getWidth(), translateFactorY);
//        fillRect(0, window.getHeight() - translateFactorY - insetTop, window.getWidth(), translateFactorY);
//
        translate(translateFactorX, translateFactorY);

        scale(scaleFactor, scaleFactor);
    }

    @Override
    public void finish(){
        graphics2D.dispose();
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
        drawImage(image, x, y, 1.0f, 1.0f);
    }

    @Override
    public void drawImage(Image image, int x, int y, float scaleX, float scaleY) {
        int width = (int) (image.getWidth() * scaleX);
        int height = (int) (image.getHeight() * scaleY);
        drawImage(image, x , y , width, height);
    }

    @Override
    public void drawImage(Image image, int x, int y, int width, int height) {
        graphics2D.setPaintMode();
        ImagePC copy = (ImagePC) image;
        graphics2D.drawImage(copy.getImage(), x, y+ (int)(insetTop/scaleFactor), width, height, null);
        graphics2D.setPaintMode();
    }

    @Override
    public void setColor(int color, float alpha) {
        Color c = new Color(color, true);

        if(alpha != 1){
            float[] components = new float[4];
            c.getColorComponents(components);
            c = new Color(components[0], components[1], components[2], alpha);
        }
        this.graphics2D.setColor(c);
    }

    @Override
    public void setFont(IFont font) {
        FontPC currFont = (FontPC) font;
        this.graphics2D.setFont(currFont.getFont());
    }

    @Override
    public void fillRect(int x, int y, int size) {
        fillRect(x, y, size, size);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        this.graphics2D.fillRect(x, y+ insetTop, x + w, y + h);
    }

    @Override
    public void drawRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        this.graphics2D.drawRect(x, (int) (y+ insetTop/scaleFactor), w, h);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics2D.drawLine(x1, (int) (y1+ insetTop/scaleFactor), x2, (int) (y2+ insetTop/scaleFactor));
    }

    @Override
    public void drawText(String text, int x, int y) {
        this.graphics2D.drawString(text, x, y + (insetTop/scaleFactor));
    }

    @Override
    public Pair<Double, Double> getStringDimensions(String text) {
        Rectangle2D r = graphics2D.getFontMetrics().getStringBounds(text, graphics2D);
        return new Pair<>(r.getWidth(), r.getHeight());
    }

    @Override
    public int getWidth() {
        return window.getWidth();
    }

    @Override
    public int getHeight() {
        return window.getHeight();
    }

    @Override
    public int getLogicWidth(){
        return logicWidth;
    }

    @Override
    public int getLogicHeight() {
        return logicHeight;
    }

    @Override
    public void loadImage(Image img, String key) {
        imagesLoaded.put(key, img);
    }

    @Override
    public Image getImage(String key) {
        return imagesLoaded.get(key);
    }

    @Override
    public void recalcFactors(int widthWindow, int heightWindow) {
        int expectedHeight = (int) (( logicHeight * widthWindow)/ (float)logicWidth);
        int expectedWidth = (int) (( logicWidth * heightWindow)/ (float)logicHeight);

        int bandWidth = 0, bandHeight = 0;

        if(heightWindow >= expectedHeight){
            bandHeight = (heightWindow - expectedHeight)/2;
            scaleFactor = (float)widthWindow / (float)logicWidth;
        }else{
            bandWidth = (widthWindow - expectedWidth)/2;
            scaleFactor = (float)heightWindow / (float)logicHeight;
        }

        translateFactorX = bandWidth;
        translateFactorY = bandHeight;
    }

    @Override
    public int getTranslateFactorX(){return translateFactorX;}

    @Override
    public int getTranslateFactorY(){return translateFactorY;}

    @Override
    public float getScaleFactor(){return scaleFactor;}

    public BufferStrategy getBufferStrategy(){return bufferStrategy;}
}
