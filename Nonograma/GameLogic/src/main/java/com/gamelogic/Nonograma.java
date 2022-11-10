package com.gamelogic;


import com.engine.Engine;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.TouchEvent;
import com.engine.SceneBase;

/*
TODO: 30 Nov modo historia totalmente funcional y mecánica de vida
TODO: Niveles bloqueados -> persistencia
TODO: O dificultad o temática (Dificultad va a ser la buena)

TODO: Fácil 20 niv 5x5
TODO: Intermedio 20 niv 8x8 (o 10X10)
TODO: Dificil 20 niv 10x10 (o 15X15)

TODO: Sistema de vidas -> check incorrecto pierde una vida
TODO: nivel correcto recupera una vida
TODO: ver un anuncio al final del nivel (En pantalla de victoria boton para tal que salga si ganas vida o dinero al verlo(o incluso 2 botones))
TODO: tb se puede pagar dinero de juego por vidas al final del nivel

TODO: Botón en pantalla victoria que lleve a whats o a twitter para indicar tu victoria -> interacción social

TODO: Varias paletas (5 por ejemplo) y estilos (cuadrados un poco diferentes o fuentes diferentes)
TODO: No estaria guapo un modo daltónico ni nada que pusiera formas características en cada casilla (fill=imagen de rombo, Wrong=X, etc)
TODO: paletas y estilos desbloqueables al pasar cierta cantidad de niveles de la historia O mediante dinero de juego (prob. mas nota)

TODO: Banner anuncio en la parte inferior del juego

TODO: Notificación push que se mande al móvil cada semana que no se abre la app

TODO: La aplicación se debe adaptar a cualquier resolución de pantalla. Y permitiremos jugar
TODO: tanto en horizontal como en vertical. En el caso del juego en horizontal adaptaremos el
TODO: layout para que el tablero sea el centro de nuestra pantalla.

 TODO: Sensores y AR proximamente
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
