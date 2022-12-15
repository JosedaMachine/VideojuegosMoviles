package com.gamelogic;


import com.engineandroid.Engine;
import com.engineandroid.IGame;
import com.engineandroid.Graphics;
import com.engineandroid.MESSAGE_TYPE;
import com.engineandroid.Message;
import com.engineandroid.TouchEvent;
import com.engineandroid.SceneBase;
import com.engineandroid.UserInterface;
import com.gamelogic.enums.CATEGORY;
import com.gamelogic.managers.GameManager;
import com.gamelogic.scenes.SceneTitle;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Stack;

/*
TODO: persistencia
TODO: ver un anuncio al final del nivel (En pantalla de victoria boton para tal que salga si dinero al verlo)
TODO: sonido solo una vez cuando se presione, que no suene como una ametralladora
TODO: marcar casilla X con long press.

TODO: Mejor botón de Twitter, aunque ya funciona
En el manifest:
Uri builtURI = Uri. parse("https://twitter.com/intent/tweet" ).buildUpon()
 .appendQueryParameter( "text", "Este es mi texto a tweettear")
 .build() ; //Genera la URl https://twitter.com/intent/tweet?text=Este%20es%20mi%20texto%20a%20tweettear
Intent intent = new Intent(Intent. ACTION_VIEW, builtURI);
startActivity(intent) ; // startActivity es un método de Context

TODO: La aplicación se debe adaptar a cualquier resolución de pantalla. Y permitiremos jugar
TODO: tanto en horizontal como en vertical. En el caso del juego en horizontal adaptaremos el
TODO: layout para que el tablero sea el centro de nuestra pantalla.
TODO: Sensores hagan algo con sentido
*/

public class Nonograma implements IGame {
    Engine engine;
    SceneBase currScene;
    UserInterface userInterface;

    Stack sceneStack;

    public Nonograma(Engine engine){
        this.engine = engine;
        GameManager.init(engine);

        sceneStack = new Stack();

        userInterface = new UserInterface();
    }

    //Iniciar nueva escena
    @Override
    public void pushScene(SceneBase newScene) {
        newScene.init();
        sceneStack.push(newScene);
    }

    @Override
    public void previousScene() {
        sceneStack.pop();
    }

    @Override
    public boolean changeScene(String sceneClassName) {
        Iterator iterator = sceneStack.iterator();

        System.out.println("Searching");

        boolean hasBeenFound = false;
        int skipped = 0;
        while(iterator.hasNext() && !hasBeenFound){
            Object value = iterator.next();

            String valueName = value.getClass().getSimpleName();
            boolean val =  valueName.equals(sceneClassName);

            if(val) {
                System.out.println("Bien");
                hasBeenFound = true;
            }else {
                skipped++;
            }
        }

        int toPop = sceneStack.size() - skipped - 1;

        if(toPop == -1)
            return false;

        while(toPop > 0){
            sceneStack.pop();
            toPop--;
        }

        return true;
    }


    @Override
    public SceneBase getScene() {
        if(!sceneStack.empty())
            return (SceneBase) sceneStack.peek();
        else return null;
    }

    //Escena de titulo inicial
    @Override
    public void init() {
        if(currScene == null)
            pushScene(new SceneTitle(engine));
        //TODO: Esto leerlo de archivo
        GameManager.instance().setLevelIndex(CATEGORY.KITCHEN, 4);
        GameManager.instance().setLevelIndex(CATEGORY.MEDIEVAL, 4);
        GameManager.instance().setLevelIndex(CATEGORY.OCEAN, 4);
        GameManager.instance().setLevelIndex(CATEGORY.ICON, 0);

        GameManager.instance().setPaletteUnlocked(0, true, 0);
        GameManager.instance().setPaletteUnlocked(1, false, 40);
        GameManager.instance().setPaletteUnlocked(2, false, 40);
    }

    @Override
    public void update(double elapsedTime) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.update(elapsedTime);

            //currScene.update(elapsedTime);
            userInterface.update(elapsedTime);
        }
    }

    @Override
    public void render(Graphics graphics) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.render(graphics);

    //        currScene.render(graphics);
            userInterface.render(graphics);
        }
    }

    @Override
    public void processInput(TouchEvent event) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.input(event);

    //        currScene.input(event);
            userInterface.input(event);
        }
    }

    @Override
    public void loadImages(Graphics graphics) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.loadResources(graphics);

    //        currScene.loadResources(graphics);
        }
    }

    @Override
    public void onResume() {
        engine.getAudio().getMusic().play();
        engine.getAudio().resumeEverySound();
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.onResume();
//            currScene.onResume();
        }

    }

    @Override
    public void onPause() {
        engine.getAudio().getMusic().pause();
        engine.getAudio().pauseEverySound();
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.onPause();
//            currScene.onPause();
        }
    }

    @Override
    public void orientationChanged(boolean isHorizontal) {
        SceneBase scene = (SceneBase) sceneStack.peek();
        scene.orientationChanged(isHorizontal);
    }

    @Override
    public UserInterface getUserInterface(){
        return userInterface;
    }

    @Override
    public void sendMessage(Message message) {
        if(message.getType() == MESSAGE_TYPE.REWARD_NOTIFICATION){
            GameManager.instance().addMoney(message.reward);
        }else {
            currScene.processMessage(message);
        }
    }

    @Override
    public void save(FileOutputStream file) {
        //Por cada escena ir guardando cosas o si el gameManager tiene datos guardar esos datos y
        //comprobar si y solo si esta en escena Game guardar el tablero
    }

    @Override
    public void restore(BufferedReader reader) {

    }
}
