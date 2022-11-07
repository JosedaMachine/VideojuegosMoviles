package com.gamelogic;


import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;
/*
TODO: tamaño assets en funcion del tamaño de la logica

 */
public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;
    public Nonograma(Engine engine){
        this.engine = engine;
    }

    //Iniciar nueva escena
    @Override
    public void changeScene(SceneBase newScene) {
        currScene = newScene;
        currScene.init();
    }

    @Override
    public SceneBase getScene() {
        return currScene;
    }

    //Escena de titulo inicial
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

    @Override
    public void loadImages(IGraphics graphics) {
        currScene.loadResources(graphics);
    }
}
