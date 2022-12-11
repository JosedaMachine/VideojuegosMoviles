package com.gamelogic;


import android.os.Bundle;
import android.util.Log;

import com.engineandroid.Engine;
import com.engineandroid.IGame;
import com.engineandroid.Graphics;
import com.engineandroid.TouchEvent;
import com.engineandroid.SceneBase;
import com.engineandroid.UserInterface;
import com.gamelogic.scenes.SceneTitle;

/*
TODO: persistencia
TODO: Botones de volver a escena anterior en vez de solo a title

TODO: Pedir en el manifest todas las pantallas menos las pequeñas

TODO: ver un anuncio al final del nivel (En pantalla de victoria boton para tal que salga si dinero al verlo)

TODO: Mejor botón de Twitter, aunque ya funciona
En el manifest:
Uri builtURI = Uri. parse("https://twitter.com/intent/tweet" ).buildUpon()
 .appendQueryParameter( "text", "Este es mi texto a tweettear")
 .build() ; //Genera la URl https://twitter.com/intent/tweet?text=Este%20es%20mi%20texto%20a%20tweettear
Intent intent = new Intent(Intent. ACTION_VIEW, builtURI);
startActivity(intent) ; // startActivity es un método de Context


TODO: Varias paletas (2 por ejemplo) y estilos (cuadrados un poco diferentes o fuentes diferentes)
TODO: paletas y estilos desbloqueables mediante dinero de juego

TODO: La aplicación se debe adaptar a cualquier resolución de pantalla. Y permitiremos jugar
TODO: tanto en horizontal como en vertical. En el caso del juego en horizontal adaptaremos el
TODO: layout para que el tablero sea el centro de nuestra pantalla.

TODO: Sensores hagan algo con sentido
*/

public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;
    UserInterface userInterface;


    public Nonograma(Engine engine){
        this.engine = engine;
        GameManager.init(engine);
        //TODO: Esto leerlo de archivo
        GameManager.instance().setLevelIndex(Category.CAT0, 4);
        GameManager.instance().setLevelIndex(Category.CAT1, 4);
        GameManager.instance().setLevelIndex(Category.CAT2, 4);
        GameManager.instance().setLevelIndex(Category.CAT3, 0);

        userInterface = new UserInterface();
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
        if(currScene == null)
            changeScene(new SceneTitle(engine));
    }

    @Override
    public void update(double elapsedTime) {
        currScene.update(elapsedTime);
        userInterface.update(elapsedTime);
    }

    @Override
    public void render(Graphics graphics) {
        currScene.render(graphics);
        userInterface.render(graphics);
    }

    @Override
    public void processInput(TouchEvent event) {
        currScene.input(event);
        userInterface.input(event);
    }

    @Override
    public void loadImages(Graphics graphics) {
        currScene.loadResources(graphics);
    }

    @Override
    public void onResume() {
        if(currScene != null)
            currScene.onResume();
        engine.getAudio().getMusic().play();
        engine.getAudio().resumeEverySound();
    }

    @Override
    public void onPause() {
        if(currScene != null)
            currScene.onPause();
        engine.getAudio().getMusic().pause();
        engine.getAudio().pauseEverySound();
    }

    @Override
    public UserInterface getUserInterface(){
        return userInterface;
    }

    @Override
    public void sendMessage(Bundle message) {
        if(message.containsKey("RewardNotification")){
            GameManager.instance().addMoney(message.getInt("RewardNotification"));
        }
        Log.d("MONEY", "MENSAJE " + message.getInt("RewardNotification"));
    }
}
