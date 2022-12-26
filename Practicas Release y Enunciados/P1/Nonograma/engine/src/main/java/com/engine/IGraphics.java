package com.engine;

public interface IGraphics {
    // Limpia, dibuja las bandas de los bordes y
    // recoloca la lógica dada una traslación y una escala
    void prepare(int color);

    // Posrenderizado de graphics
    void finish();

    // Carga una imagen almacenada en el
    // contenedor de recursos de la aplicación a partir de su nombre
    Image newImage(String path, String name);

    // Crea una nueva fuente del tamaño especificado a partir de un
    // fichero .ttf. Se indica si se desea o no fuente en negrita
    IFont newFont(String name, int size, boolean isBold);

    // Borra el contenido completo de la ventana,
    // rellenándolo con un color recibido como parámetro
    void clear(int color);

    // Operación de traslación de la pantalla
    void translate(int x, int y);

    // Operación de escalado de la pantalla
    void scale(double x, double y);

    void save();

    void restore();

    // Recibe una imagen y la muestra en la pantalla
    void drawImage(Image image);

    // Recibe una imagen y la muestra en la pantalla en las coordenadas (x,y)
    void drawImage(Image image, int x, int y);

    // Recibe una imagen y la muestra en la pantalla en las coordenadas (x,y), reescalada
    // por scaleX, scaleY
    void drawImage(Image image, int x, int y, float scaleX, float scaleY);

    // Recibe una imagen y la muestra en la pantalla en las coordenadas (x,y),
    // con un ancho y alto especificos
    void drawImage(Image image, int x, int y, int width, int height);

    // Establece el color a utilizar en las operaciones
    // de dibujado posteriores
    void setColor(int color, float alpha);

    // Establece la fuente a utilizar en las operaciones
    // de dibujado posteriores
    void setFont(IFont font);

    // Dibuja un cuadrado relleno del color activo
    void fillRect(int x, int y, int size);

    // Dibuja un rectángulo relleno del color activo
    void fillRect(int x, int y, int w, int h);

    // Dibuja un cuadrado sin relleno del color activo
    void drawRect(int x, int y, int size);

    // Dibuja un rectángulo sin relleno del color activo
    void drawRect(int x, int y, int w, int h);

    // Dibuja una línea recta del color activo
    void drawLine(int x1, int y1, int x2, int y2);

    // Escribe el texto con la fuente y color activos
    void drawText(String text, int x, int y);

    /*
    * Dado un String, devuelve el tamaño que tendría en pixeles en pantalla
    * para la fuente actual
    * @param text string a comprobar
    * @return pair con ancho y alto del tamaño
    * */
    Pair<Double, Double> getStringDimensions(String text);

    // Devuelve el ancho actual de la pantalla
    int getWidth();

    // Devuelve el alto actual de la pantalla
    int getHeight();

    // Devuelve el ancho lógico de la pantalla
    int getLogicWidth();

    // Devuelve el alto lógico de la pantalla
    int getLogicHeight();

    // Carga una imagen en la pool de imágenes
    void loadImage(Image img, String key);

    // Obtiene una imagen de la pool de imágenes
    Image getImage(String key);

    // Obtiene el factor de traslación en X
    int getTranslateFactorX();

    // Obtiene el factor de traslación en Y
    int getTranslateFactorY();

    // Obtiene el factor de escalado
    float getScaleFactor();
}
