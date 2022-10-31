package com.enginepc;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.HashMap;

import javax.swing.JFrame;

public class GraphicsPC implements IGraphics {

    private int insetTop, insetLeft;
    private final JFrame window;
    private Graphics2D graphics2D;
    private final BufferStrategy bufferStrategy;

    HashMap<String, Image> imagesLoaded = new HashMap<>();

    private String path = "appDesktop/assets/";

    GraphicsPC(JFrame view){
        window = view;

        insetTop = view.getInsets().top;
        insetLeft = view.getInsets().left;
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
        //Sí, es así de fácil
        graphics2D.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        //Este también es así de fácil
        graphics2D.scale(x, y);
    }

    public void prepare(int color){
        this.graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
        //TODO: Escalado resolucion
        //this.graphics2D.scale(1,1);
        //this.graphics2D.translate(1,1);
        this.clear(color);
    }

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
        graphics2D.drawImage(copy.getImage(), x+ insetLeft, y+ insetTop, width, height, null);
        graphics2D.setPaintMode();
    }

    @Override
    public void setColor(int color) {
        this.graphics2D.setColor(new Color(color, true));
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
        this.graphics2D.fillRect(x+ insetLeft, y+ insetTop, x + w, y + h);
    }

    @Override
    public void drawRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        this.graphics2D.drawRect(x+ insetLeft, y+ insetTop, w, h);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.graphics2D.drawLine(x1+ insetLeft, y1+ insetTop, x2+ insetLeft, y2+ insetTop);
    }

    @Override
    public void drawText(String text, int x, int y) {
        this.graphics2D.drawString(text, x+ insetLeft, y + insetTop);
    }

    @Override
    public Pair<Double, Double> getStringDimensions(String text) {
        Rectangle2D r = graphics2D.getFontMetrics().getStringBounds(text, graphics2D);
        System.out.println();
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
    public int getLogicWidth(int x) {
        return 0;
    }

    @Override
    public int getLogicHeight(int y) {
        return 0;
    }

    @Override
    public int parseRealLogic() {
        return 0;
    }

    @Override
    public void loadImage(Image img, String key) {
        imagesLoaded.put(key, img);
    }

    @Override
    public Image getImage(String key) {
        return imagesLoaded.get(key);
    }

    public BufferStrategy getBufferStrategy(){return bufferStrategy;}

    public Graphics getGraphics(){return this.graphics2D;}
}
