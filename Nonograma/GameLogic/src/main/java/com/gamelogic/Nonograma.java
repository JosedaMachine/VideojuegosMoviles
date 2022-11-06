package com.gamelogic;


import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;
/*
TODO: por que parpadea al hacer resize?
TODO: fuente en funcion del tamaño casilla o mas peque
TODO: todo lo de movil
TODO: que el tablero se coloque con respecto a su tamaño + la caja de numeros

 */
public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;
    public Nonograma(Engine engine){
        this.engine = engine;
    }

    @Override
    public void changeScene(SceneBase newScene) {
        currScene = newScene;
        currScene.init();
    }

    @Override
    public SceneBase getScene() {
        return currScene;
    }

    @Override
    public void init() {
        changeScene(new SceneTitle(engine));
    }

    @Override
    public void update(double elapsedTime) {
        currScene.update(elapsedTime);
    }

    @Override
    public void render(IGraphics graphics) {
        currScene.render(graphics);
    }

    @Override
    public void processInput(TouchEvent event) {
        currScene.input(event);
    }

    // Se podría hacer esto en el init de cada escena pero
    // de momento lo hace el motor en cada app
    @Override
    public void loadImages(IGraphics graphics) {
        currScene.loadResources(graphics);
    }
}
