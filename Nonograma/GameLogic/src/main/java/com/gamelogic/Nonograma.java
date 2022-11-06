package com.gamelogic;


import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;
/*
TODO: reescalado?
TODO: por que parpadea al hacer resize?
TODO: hay que tener un limite maximo de resize?
TODO: Relacion de la primera resolucion con respecto a la nueva? Si
TODO: La logica tiene un tamaño inicial en relacion a la pantalla?
TODO: sonidos
TODO: bandas
TODO: translate en algun sitio?

Example:
Init Res= 1080x720
new Res = 1280x800

RelationX = InitX/newX -> 0.843
RelationY = InitY/newY -> 0.9

NuevaPos del obejeto = oldPos/relation

Supongo que es lo mismo con las escalas

Y si se pasa de una relacion del 0.5 (es mucho mas grande de lo original) o 1.5 (mucho mas pequeño)
se generan las bandas o no se puede hacer mas pequeño (respectivamente).

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
