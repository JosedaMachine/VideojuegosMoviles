package com.engineandroid;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;

import java.util.HashMap;

public class GraphicsAndroid implements IGraphics {

    private final SurfaceView myView;
    private final SurfaceHolder holder;
    private Canvas canvas;
    private final Paint paint;
    private final AssetManager assetManager;

    HashMap<String, Image> imagesLoaded = new HashMap<>();

    int logicWidth, logicHeight;
    float scaleFactor;
    int translateFactorX, translateFactorY;

    GraphicsAndroid(SurfaceView view, int logicWidth_ , int logicHeight_){
        this.myView = view;
        this.logicHeight = logicHeight_;
        this.logicWidth = logicWidth_;

        this.myView.addOnLayoutChangeListener( new View.OnLayoutChangeListener()
        {
            public void onLayoutChange( View v,
                                        int left,    int top,    int right,    int bottom,
                                        int leftWas, int topWas, int rightWas, int bottomWas )
            {
                int widthWas = rightWas - leftWas; // Right exclusive, left inclusive
                int heightWas = bottomWas - topWas; // Bottom exclusive, top inclusive

                if(v.getWidth() != widthWas && v.getHeight() != heightWas){
                    recalcFactors(myView.getWidth(), myView.getHeight());
                }
            }
        });


        this.holder = this.myView.getHolder();

        this.paint = new Paint();
        this.paint.setColor(0xFFFFFFFF);

        this.assetManager = view.getContext().getAssets();
    }

    @Override
    public Image newImage(String path, String name) {
        Image i = imagesLoaded.get(path);
        if(i == null) {
            i = new ImageAndroid(assetManager, "images/" + path, name);
            if(!i.isLoaded())
                System.out.println("No se ha encontrado la imagen");
            loadImage(i, name);
        }
        return i;
    }

    @Override
    public IFont newFont(String name, int size, boolean isBold) {
        return new FontAndroid(assetManager, "fonts/" + name, size, isBold);
    }

    @Override
    public void clear(int color) {
        canvas.drawColor(color);
    }

    @Override
    public void translate(int x, int y) {
        canvas.translate(x, y);
    }

    @Override
    public void scale(double x, double y) {
        canvas.scale((float)x, (float)y);
    }

    @Override
    public void prepare(int color) {
        while (!holder.getSurface().isValid());
        canvas = holder.lockCanvas();
        clear(ColorAndroid.WHITE);

//        //Bandas horizontales - DEBUG
//        fillRect(0,0, translateFactorX, getHeight());
//        fillRect(getWidth() - translateFactorX, 0, translateFactorX, getHeight());
//
//        //Bandas verticales - DEBUG
//        fillRect(0,0, getWidth(), translateFactorY);
//        fillRect(0, getHeight() - translateFactorY, getWidth(), translateFactorY);

        translate(translateFactorX, translateFactorY);
        scale(scaleFactor, scaleFactor);
    }
    @Override
    public void finish(){
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {

    }

    @Override
    public void drawImage(Image image) {
       drawImage(image, 0, 0);
    }

    @Override
    public void drawImage(Image image, int x, int y) {
        ImageAndroid anImage = (ImageAndroid) image;
        canvas.drawBitmap(anImage.getImage(), x, y, paint);
    }

    @Override
    public void drawImage(Image image, int x, int y, float scaleX, float scaleY) {
        int width  = (int) ((image.getWidth()) * scaleX);
        int height = (int) ((image.getHeight())* scaleY);
        drawImage(image, x, y, width, height);
    }

    @Override
    public void drawImage(Image image, int x, int y, int width, int height) {
        ImageAndroid anImage = (ImageAndroid) image;
        canvas.drawBitmap(anImage.getScaledImage(width, height),x, y,paint);
    }

    @Override
    public void setColor(int color, float alpha) {
        paint.setColor(color);
        paint.setAlpha((int) (alpha * 255));
    }

    @Override
    public void setFont(IFont font) {
        FontAndroid anFont = (FontAndroid) font;
        paint.setTypeface(anFont.getFont());
        paint.setTextSize(anFont.getSize());
    }

    @Override
    public void fillRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        drawRect(x, y, w, h);
    }

    @Override
    public void drawRect(int x, int y, int size) {
        drawRect(x, y, size, size);
    }

    @Override
    public void drawRect(int x, int y, int w, int h) {
        canvas.drawRect(x, y, x + w, y + h, paint);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    @Override
    public void drawText(String text, int x, int y) {
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public Pair<Double, Double> getStringDimensions(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text,0,text.length(), bounds);
        return new Pair<>((double) bounds.width(), (double) bounds.height()*2);
    }

    @Override
    public int getWidth() {
        return myView.getWidth();
    }

    @Override
    public int getHeight() {
        return myView.getHeight();
    }

    @Override
    public int getLogicWidth() {
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

    /*
     * Dado un ancho y alto de ventana, calcula el reescalado y traslación necesaria para
     * adaptar la logica al tamaño actual de la pantalla
     * @param widthWindow ancho de pantalla
     * @param heightWindow alto de pantalla
     * */
    private void recalcFactors(int widthWindow, int heightWindow) {

        System.out.println("Altura Logica:" + logicHeight);
        System.out.println("Anchura Logica:" + logicWidth);
        int expectedHeight = (int) (( logicHeight * widthWindow)/ (float)logicWidth);
        int expectedWidth = (int) (( logicWidth * heightWindow)/ (float)logicHeight);
        System.out.println("Anchura :" + widthWindow);
        System.out.println("Anchura Esperada :" + expectedWidth);

        System.out.println("Altura :" + heightWindow);
        System.out.println("Altura Esperada :" + expectedHeight);

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
    public int getTranslateFactorX() {
        return translateFactorX;
    }

    @Override
    public int getTranslateFactorY() {
        return translateFactorY;
    }

    @Override
    public float getScaleFactor() {
        return scaleFactor;
    }

}
