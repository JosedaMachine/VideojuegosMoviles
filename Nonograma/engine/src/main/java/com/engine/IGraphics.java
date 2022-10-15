package com.engine;

public interface IGraphics {

    Image newImage(String name, int width, int height);

    IFont newFont(String name, int size, boolean isBold);

    void clear(int color);

    void translate(int x, int y);

    void scale(double x, double y);

    void render(SceneBase scene);

    void save();

    void restore();

    void drawImage(Image image);

    void drawImage(Image image, int x, int y);

    void drawImage(Image image, int x, int y, float scaleX, float scaleY);

    void drawImage(Image image, int x, int y, int width, int height);

    void setColor(int color);

    void setFont(IFont font);

    void fillSquare(int x, int y, int size);

    void drawSquare(int x, int y, int size);

    void drawLine(int x1, int y1, int x2, int y2);

    void drawText(String text, int x, int y);

    int getWidth();

    int getHeight();

    void loadImage(Image img, String key);

    Image getImage(String key);
}
