package com.engine;

public interface IGraphics {

    Image newImage(String name);

    IFont newFont(String name, int size, boolean isBold);

    void clear(int color);

    void translate(int x, int y);

    void scale(double x, double y);

    void save();

    void restore();

    void drawImage(Image image);

    void drawImage(Image image, int x, int y);

    void drawImage(Image image, int x, int y, float scaleX, float scaleY);

    void drawImage(Image image, int x, int y, int width, int height);

    void setColor(int color);

    void setFont(IFont font);

    void fillRect(int x, int y, int size);

    void fillRect(int x, int y, int w, int h);

    void drawRect(int x, int y, int size);

    void drawRect(int x, int y, int w, int h);

    void drawLine(int x1, int y1, int x2, int y2);

    void drawText(String text, int x, int y);

    Pair<Double, Double> getStringDimensions(String text);

    int getWidth();

    int getHeight();

    // Para cambio de resoluución
    int getLogicWidth(int x);
    int getLogicHeight(int y);

    int parseRealLogic();


    void loadImage(Image img, String key);

    Image getImage(String key);
}
