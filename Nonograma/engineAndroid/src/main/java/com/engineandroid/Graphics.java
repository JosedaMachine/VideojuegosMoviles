package com.engineandroid;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.HashMap;


public class Graphics {

    public enum ConstraintX{
        LEFT, CENTER, RIGHT
    }

    public enum ConstraintY{
        TOP, CENTER, BOTTOM
    }

    private final SurfaceView myView;
    private final SurfaceHolder holder;
    private Canvas canvas;
    private final Paint paint;
    private final AssetManager assetManager;

    HashMap<String, Image> imagesLoaded = new HashMap<>();

    int logicWidth, logicHeight;
    float scaleFactor;
    int translateFactorX, translateFactorY;

    private int clearColor = ColorWrap.WHITE;
    //TODO: preguntar si esta bien que grafics tenga datos de AdView para que tenga en cuenta su tamaño para pintar.
    private Pair<Integer, Integer> adViewDimensions;




    Graphics(SurfaceView view, int logicWidth_ , int logicHeight_, Pair<Integer, Integer> adViewDimensions_){
        this.myView = view;
        this.logicHeight = logicHeight_;
        this.logicWidth = logicWidth_;
        adViewDimensions = adViewDimensions_;

        this.myView.addOnLayoutChangeListener( new View.OnLayoutChangeListener()
        {
            public void onLayoutChange( View v,
                                        int left,    int top,    int right,    int bottom,
                                        int leftWas, int topWas, int rightWas, int bottomWas )
            {
                int widthWas = rightWas - leftWas; // Right exclusive, left inclusive
                int heightWas = bottomWas - topWas; // Bottom exclusive, top inclusive

                if(v.getWidth() != widthWas && v.getHeight() != heightWas){
                    recalcFactors(myView.getWidth(), myView.getHeight() - adViewDimensions.second);
                }
            }
        });


        this.holder = this.myView.getHolder();

        this.paint = new Paint();
        this.paint.setColor(0xFFFFFFFF);

        this.assetManager = view.getContext().getAssets();
    }

    public Image newImage(String path, String name) {
        Image i = imagesLoaded.get(path);
        if(i == null) {
            i = new Image(assetManager, "images/" + path);
            if(!i.isLoaded())
                System.out.println("No se ha encontrado la imagen");
            loadImage(i, name);
        }
        return i;
    }

    public Font newFont(String name, int size, boolean isBold) {
        return new Font(assetManager, "fonts/" + name, size, isBold);
    }

    public void clear(int color) {
        canvas.drawColor(color);
    }

    public void translate(int x, int y) {
        canvas.translate(x, y);
    }

    public void scale(double x, double y) {
        canvas.scale((float)x, (float)y);
    }

    public void prepare() {
        while (!holder.getSurface().isValid());
        canvas = holder.lockCanvas();
        clear(clearColor);

//        //Bandas horizontales - DEBUG
//        fillRect(0,0, translateFactorX, getHeight());
//        fillRect(getWidth() - translateFactorX, 0, translateFactorX, getHeight());
//
//        //Bandas verticales - DEBUG
//        fillRect(0,0, getWidth(), translateFactorY);
//        fillRect(0, getHeight() - translateFactorY, getWidth(), translateFactorY);

        translate(translateFactorX, 0);
        scale(scaleFactor, scaleFactor);
    }

    public void finish(){
        holder.unlockCanvasAndPost(canvas);
    }

    public void save() {

    }

    public void restore() {

    }

    public void drawImageWithConstraints(Image image, ConstraintX constrX,ConstraintY constrY, int offsetX, int offsetY){
        drawImage(image, 0, 0);
    }

    public void drawImage(Image image) {
        drawImage(image, 0, 0);
    }

    public void drawImage(Image image, int x, int y) {
        drawImage(image, x, y, 1.0f, 1.0f);
    }

    public void drawImage(Image image, int x, int y, float scaleX, float scaleY) {
        int width  = (int) ((image.getWidth()) * scaleX);
        int height = (int) ((image.getHeight())* scaleY);
        drawImage(image, x, y, width, height);
    }

    public void drawImage(Image image, int x, int y, int width, int height) {
        canvas.drawBitmap(image.getScaledImage(width, height),x, y,paint);
    }

    public void setColor(int color, float alpha) {
        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
    }

    public void setFont(Font anFont) {
        paint.setTypeface(anFont.getFont());
        paint.setTextSize(anFont.getSize());
    }

    public void fillRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    public void fillRect(int x, int y, int w, int h) {
        drawRect(x, y, w, h);
    }

    public void drawRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    public void drawRect(int x, int y, int w, int h) {
        canvas.drawRect(x, y, x + w, y + h, paint);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    public void drawText(String text, int x, int y) {
        canvas.drawText(text, x, y, paint);
    }

    public Pair<Double, Double> getStringDimensions(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text,0,text.length(), bounds);
        return new Pair<>((double) bounds.width(), (double) bounds.height()*2);
    }

    public int getWidth() {
        return myView.getWidth();
    }

    public int getHeight() {
        return myView.getHeight();
    }

    public int getLogicWidth() {
        return logicWidth;
    }

    public int getLogicHeight() {
        return logicHeight;
    }

    public void loadImage(Image img, String key) {
        imagesLoaded.put(key, img);
    }

    public Image getImage(String key) {
        return imagesLoaded.get(key);
    }

    public void recalcFactors(int widthWindow, int heightWindow) {
//        System.out.println("Altura Logica:" + logicHeight);
//        System.out.println("Anchura Logica:" + logicWidth);
        int expectedHeight = (int) (( logicHeight * widthWindow)/ (float)logicWidth);
        int expectedWidth = (int) (( logicWidth * heightWindow)/ (float)logicHeight);
//        System.out.println("Anchura :" + widthWindow);
//        System.out.println("Anchura Esperada :" + expectedWidth);

//        System.out.println("Altura :" + heightWindow);
//        System.out.println("Altura Esperada :" + expectedHeight);

        int bandWidth = 0, bandHeight = 0;
        if(heightWindow >= expectedHeight){
            bandHeight = (heightWindow - expectedHeight)/2;
            scaleFactor = (float)widthWindow / (float)logicWidth;
        }else{
            bandWidth = (widthWindow - expectedWidth)/2;
            scaleFactor = (float)heightWindow / (float)logicHeight;
        }

        //Generally we dont want to move in Y
        translateFactorX = bandWidth;
        translateFactorY = 0;
    }

    public int getTranslateFactorX() {
        return translateFactorX;
    }

    public int getTranslateFactorY() {
        return translateFactorY;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public Context getContext() {return myView.getContext();}

    public void setClearColor(int color) {clearColor = color;}

    public int getClearColor() {return clearColor;}
}
