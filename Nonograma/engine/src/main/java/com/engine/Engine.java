package com.engine;

public interface Engine {
    /**
     * Devuelve una instancia del motor gráfico.
     * @return motor gráfico
     */
    IGraphics getGraphics();
    /**
     * Devuelve una instancia del gestor de entrada de usuario.
     * @return instancia del gestor
     */
    IInput getInput();
    /**
     * Devuelve una instancia del motor del sonido.
     * @return motor del sonido
     */
    Audio getAudio();

    /**
     * Asigna un juego a ejecutar que hereda de la interfaz IGame
     * y se inicializa.
     * @param Referencia del juego a ejecutar
     */
    void setGame(IGame game);

    /**
     * Devuelve instancia del juego asignado
     * @param Juego que se ejecuta
     */
    IGame getGame();

    /**
     * Pone en marcha la hebra de ejecución del bucle principal
     */
    void resume();

    /**
     * Detiene la hebra de ejecución del bucle principal
     */
    void pause();

    /**
     * Actualiza el juego asignado
     * @param tiempo transcurrido entre frame y frame en valor decimal
     */
    void update(double elapsedTime);

    /**
     * Renderiza el juego asignado en una hebra independiente de renderizado
     */
    void render();

    /**
     * Procesa la entrada de usuario
     */
    void processInput();
}