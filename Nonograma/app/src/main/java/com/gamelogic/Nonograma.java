package com.gamelogic;


import android.content.SharedPreferences;

import com.engineandroid.Audio;
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
import com.gamelogic.scenes.SceneGame;
import com.gamelogic.scenes.SceneStoryCategories;
import com.gamelogic.scenes.SceneTitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

/*
TODO: Sensores hagan algo con sentido llevo 3 programados por favor no sé qué hacer con ellos los tengo por castigo ayuda

TODO: poner anuncio de ganar vida en Game. Preguntad si tenéis dudas sobre algo de los anuncios putos

//Uno cargado cuabdo unicias y cuadno acabas cargas otro.

//Preguntas TODOS
/*
 * no podemos jamas almacenar Engine aunque sea mas eficiente?
 *
 * Pasar a los metodos en vez Engine la cosa especifica que le haga falta.
 *
 * Nonograsma es un SceneManager y va en el motor.
 * El fade tambien en el motor y que sea mas general como una transicion entre escenas (si tenemos tiempo)
 * contraints que sean dos archivos separados CONSTRAINT_X
 *
 * esta bien lo de las contraints y el otro layout? Si esta refino
 *
 * GameManager se puede hacer desde el lanzador en verdad.
 *===================
 * el restore data se hace en el onCreate()
 *
 * Por que decian que habia que guardarse datos cuando se cambia la orientacion de la pantalla y que eso en teoria llama al onCreate de nuevo
 *
 * para guardar el checksum del fichero de persistencia, deberiamos guardarlo en otro fichero el cual encriptemos?
 *
 * para guardar datos, por ejemplo vidas, monedas y cosas pequeñas, renta usar un fichero o hay algo mas eficiente que persista aunque la aplicacion
 * muera completamente? checksum
 *
 * cuando muere la app, si estaba jugando, deberia guardarse solo el estado de ese tablero en concreto
 * y si el jugador vuelve a iniciar la app y entra en ese tablero deberia estar como lo dejo.
 * Pero si entra en otro tablero distinto, sale y entra al que estaba guardado, deberia mostrarse su estado como lo dejo la ultima vez)
 *
 * no se guarda SOLAMENTE EL ULTIMO.
 *
 * >Boton de check automatico cuando gane
 *
 *
 * */

public class Nonograma implements IGame {
    Engine engine;
    UserInterface userInterface;
    SharedPreferences mPreferences;
    Stack sceneStack;

    private final  String SAVE_FILE_NAME = "boardState.txt";
    private final  String HASH_FILE_NAME = "hash.txt";
    public Nonograma(Engine engine, SharedPreferences pref) {
        this.engine = engine;

        mPreferences = pref;

        GameManager.init(engine);

        sceneStack = new Stack();

        userInterface = new UserInterface();
    }

    //Escena de titulo inicial
    @Override
    public void init() {
        GameManager.instance().restore(mPreferences);
        assert sceneStack.empty();
        pushScene(new SceneTitle());
    }

    @Override
    public void update(Engine engine, double elapsedTime) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.update(engine, elapsedTime);

            //currScene.update(elapsedTime);
            userInterface.update(elapsedTime);
        }
    }

    @Override
    public void render(Graphics graphics) {
        if(!sceneStack.empty()){
            userInterface.render(graphics);

            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.render(graphics);

            //        currScene.render(graphics);
        }
    }

    @Override
    public void processInput(TouchEvent event) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.input(engine, event);

            //        currScene.input(event);
            userInterface.input(event);
        }
    }

    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.loadResources(graphics, audio);

            //        currScene.loadResources(graphics);
        }
    }

    //Iniciar nueva escena
    @Override
    public void pushScene(SceneBase newScene) {
        newScene.init(engine);
        //Realmente solamente queremos guardar en fichero plano en la escena Game
        sceneStack.push(newScene);
        restoreScene(newScene instanceof SceneGame);
    }

    @Override
    public void previousScene() {
        sceneStack.pop();
        SceneBase scene = (SceneBase) sceneStack.peek();
        scene.init(engine);
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
    public void save() {
        FileOutputStream fos = null;
        //Por cada escena ir guardando cosas o si el gameManager tiene datos guardar esos datos y
        //comprobar si y solo si esta en escena Game guardar el tablero
        if(!sceneStack.empty()){
            GameManager.instance().save(mPreferences);
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.save(engine, SAVE_FILE_NAME, mPreferences);
        }

        File file = new File(engine.getContext().getFilesDir(), SAVE_FILE_NAME);
        //Use MD5 algorithm
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");

            String checksumBoard = engine.getFileChecksum(md5Digest, file);

            StringBuilder inverse = new StringBuilder();
            inverse.append(checksumBoard);
            inverse.reverse();

            fos = engine.openInternalFileWriting(HASH_FILE_NAME);
            fos.write(inverse.toString().getBytes());
            fos.close();
            //Do we need to encrypt it? we should but not necessary

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restore() {
        restoreScene(true);
    }

    public void restoreScene(boolean openFile) {
        FileInputStream fos = null;
        BufferedReader reader = null;
        //Esta comprobacion se hace entre escena y escena por si el archivo se cambia en ejecucion.
        //Si ha cambiado el checkSum del archivo de guardado del tablero no
        //restauramos nada y se pierde el contenido
        File hashFile = new File(engine.getContext().getFilesDir(), HASH_FILE_NAME);
        if(hashFile.exists()) {
            boolean ret = true;
            if(openFile)
              ret = checkHash(new String[]{SAVE_FILE_NAME});
            if(!ret) openFile = false;
        }

        try {
            if(openFile){
                File file = new File(engine.getContext().getFilesDir(), SAVE_FILE_NAME);
                if(file.exists()){
                    fos = engine.openInternalFileReading(SAVE_FILE_NAME);
                    reader = new BufferedReader(new InputStreamReader(fos));
                }
            }
            if(!sceneStack.empty()){
                SceneBase scene = (SceneBase) sceneStack.peek();
                scene.restore(reader, mPreferences);
            }
            if(fos != null)
                fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening board saving file.");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Dados varios archivos en orden concreto, comprobamos cada checksum con el almacenado en ese mismo orden.
    public boolean checkHash(String[] filename){
        //Use MD5 algorithm
        MessageDigest md5Digest = null;
        FileInputStream fos = null;
        BufferedReader reader = null;

        try{
            md5Digest = MessageDigest.getInstance("MD5");
            //Now open a new file with hashes
            fos = engine.openInternalFileReading(HASH_FILE_NAME);
            reader =  new BufferedReader(new InputStreamReader(fos));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < filename.length; i++){
            try {
                File file = new File(engine.getContext().getFilesDir(), filename[i]);
                String checksum = engine.getFileChecksum(md5Digest, file);
                //And check its checkSum with out checksum Generated so far?
                String hash = reader.readLine();
                StringBuilder inverse = new StringBuilder();
                inverse.append(hash);
                inverse.reverse();
                if(!Objects.equals(checksum, inverse.toString()))
                    return false;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void orientationChanged(boolean isHorizontal) {
        SceneBase scene = (SceneBase) sceneStack.peek();
        scene.orientationChanged(engine.getGraphics(),isHorizontal);
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
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.processMessage(engine, message);
        }
    }
}
