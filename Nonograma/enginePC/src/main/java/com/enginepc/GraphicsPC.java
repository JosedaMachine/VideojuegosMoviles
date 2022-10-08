package com.enginepc;

import com.engine.Font;
import com.engine.Graphics;
import com.engine.Image;

public class GraphicsPC implements Graphics {
    @Override
    public Image newImage(String name, int width, int height) {
        return null;
    }

    @Override
    public Font newFont(String name, int size, boolean isBold) {
        return null;
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
