package com.enginepc;

import com.engine.IFont;
import com.engine.Graphics;
import com.engine.Image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GraphicsPC implements Graphics {

    private Graphics2D graphics2D;
    private BufferStrategy bufferStrategy;

    GraphicsPC(JFrame view){
        bufferStrategy = view.getBufferStrategy();
        graphics2D = (Graphics2D) bufferStrategy.getDrawGraphics();
    }

    @Override
    public Image newImage(String name, int width, int height) {
        return null;
    }

    @Override
    public IFont newFont(String name, int size, boolean isBold) {
        return new FontPC(name , size, isBold);
    }

    @Override
    public void clear(int color) {

    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public void scale(int x, int y) {

    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {

    }

    @Override
    public void drawImage(Image image) {

    }

    @Override
    public void drawImage(Image image, int x, int y) {

    }

    @Override
    public void setColor(int color) {
        Color c = Color.BLACK;
        this.graphics2D.setColor(c);
    }

    @Override
    public void setFont(IFont font) {
        FontPC pcFont = (FontPC) font;
        this.graphics2D.setFont(pcFont.currFont);
    }

    @Override
    public void fillSquare(int x, int y, int size) {

    }

    @Override
    public void drawSquare(int x, int y, int size) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawText(String text, int x, int y) {
        this.graphics2D.drawString(text, x, y);
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
