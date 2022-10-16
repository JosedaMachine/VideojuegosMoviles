package com.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;

public class GraphicsAndroid implements IGraphics {

    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;
    private AssetManager assetManager;


    GraphicsAndroid(SurfaceView view){
        this.myView = view;

        this.holder = this.myView.getHolder();

        this.paint = new Paint();
        this.paint.setColor(0xFFFFFFFF);

        this.assetManager = view.getContext().getAssets();
    }

    @Override
    public Image newImage(String name) {
        return new ImageAndroid(assetManager, name);
    }

    @Override
    public IFont newFont(String name, int size, boolean isBold) {
        return new FontAndroid(assetManager, name, size, isBold);
    }

    @Override
    public void clear(int color) {

    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public void scale(double x, double y) {

    }

    @Override
    public void render(SceneBase scene) {

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
    public void drawImage(Image image, int x, int y, float scaleX, float scaleY) {

    }

    @Override
    public void drawImage(Image image, int x, int y, int width, int height) {

    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public void setFont(IFont font) {

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

    @Override
    public void loadImage(Image img, String key) {

    }

    @Override
    public Image getImage(String key) {
        return null;
    }
}
