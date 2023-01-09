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
import com.gamelogic.managers.GameManager;
import com.gamelogic.scenes.SceneGame;
import com.gamelogic.scenes.SceneQuickLevels;
import com.gamelogic.scenes.SceneStoryCategories;
import com.gamelogic.scenes.SceneStoryLevels;
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
import com.gamelogic.enums.CATEGORY;

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

    /**
     * Inicializa el juego con la escena de titulo
     */
    @Override
    public void init() {
        GameManager.instance().restore(mPreferences);
        assert sceneStack.empty();

        boolean boardSaved = mPreferences.getBoolean("savingBoard", false);


        pushScene(new SceneTitle());

        if(boardSaved){
            String levelCat = mPreferences.getString("levelCat", "-");
            String levelQuick = mPreferences.getString("levelQuickSize", "-");

            if(!Objects.equals(levelCat, "-")){

                int catN = Integer.parseInt(String.valueOf(levelCat.charAt(0)));
                int indexLvl = Integer.parseInt(levelCat.substring(1));

                String[] size = levelQuick.split("x");

                int cols_ = Integer.parseInt(size[0]);
                int rows_ = Integer.parseInt(size[1]);

                CATEGORY cat = CATEGORY.values()[catN];
                pushScene(new SceneStoryCategories());
                pushScene(new SceneStoryLevels(cat.ordinal()));
                pushScene(new SceneGame(rows_, cols_, cat, indexLvl));

            } else if (!Objects.equals(levelQuick, "-")) {
                //buscarHasta que haya una x

                String[] size = levelQuick.split("x");

                int cols_ = Integer.parseInt(size[0]);
                int rows_ = Integer.parseInt(size[1]);
                pushScene(new SceneQuickLevels());
                pushScene(new SceneGame(rows_, cols_, 0));
            }
        }
    }

    /**
     * Actualiza la escena que se encuentra en la cima de la pila
     */
    @Override
    public void update(Engine engine, double elapsedTime) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.update(engine, elapsedTime);

            //currScene.update(elapsedTime);
            userInterface.update(elapsedTime);
        }
    }

    /**
     * Renderiza la escena que se encuentra en la cima de la pila
     */
    @Override
    public void render(Graphics graphics) {
        if(!sceneStack.empty()){
            userInterface.render(graphics);

            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.render(graphics);
        }
    }

    /**
     * Procesa el input la escena que se encuentra en la cima de la pila
     */
    @Override
    public void processInput(TouchEvent event) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.input(engine, event);
            userInterface.input(event);
        }
    }

    /**
     * Carga los recursos de la escena que se encuentra en la cima de la pila
     */
    @Override
    public void loadResources(Graphics graphics, Audio audio) {
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.loadResources(graphics, audio);
        }
    }

    /**
     * Apila una nueva escena y la restaura en caso de haber valores guardados
     */
    @Override
    public void pushScene(SceneBase newScene) {
        newScene.init(engine);
        sceneStack.push(newScene);
        restoreScene(newScene instanceof SceneGame);
    }

    /**
     * Desapila una escena
     */
    @Override
    public void previousScene() {
        sceneStack.pop();
        SceneBase scene = (SceneBase) sceneStack.peek();
        scene.init(engine);
    }

    /**
     * Cambia a una escena que se encuentra en la pila dado el nombre de su clase.
     * En caso de no estar, se devuelve false. De lo contrario se realizan varios desapilamientos
     * hasta llegar a la encontrada y se devuelve true.
     */
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

    /**
     * Devuelve la escena que se encuentra en la cima de la pila
     */
    @Override
    public SceneBase getScene() {
        if(!sceneStack.empty())
            return (SceneBase) sceneStack.peek();
        else return null;
    }

    /**
     * Reanuda la escena que se encuentra en la cima de la pila
     */
    @Override
    public void onResume() {
        engine.getAudio().getMusic().play();
        engine.getAudio().resumeEverySound();
        if(!sceneStack.empty()){
            SceneBase scene = (SceneBase) sceneStack.peek();
            scene.onResume();
        }

    }

    /**
     * Pausa la escena que se encuentra en la cima de la pila
     */
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

    /**
     * Guarda los datos generales de la partida y si se necesita se guarda el contenido
     * de la escena que se encuentra en la cima de la pila en un fichero de texto.
     * Para este se genera un hash para la prevención de manipulacion externa.
     */
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

    /**
     * Restaura la escena que se encuentra en la cima de la pila
     */
    @Override
    public void restore() {
        restoreScene(true);
    }

    /**
     * Reanuda la escena que se encuentra en la cima de la pila. Si se trata de la escena SceneGame
     * se restaura el estado del tablero mediante un fichero de texto, cuyo hash se ha comprobado
     * anteriormente para la prevención de manipulación externa.
     */
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

    /*Dados varios archivos en orden concreto, comprobamos cada checksum con el almacenado en ese mismo orden.
    */
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

    /**
     * Informa a la escena que se encuentra en la cima de la pila de que ha habido un giro de
     * pantalla.
     */
    @Override
    public void orientationChanged(boolean isHorizontal) {
        SceneBase scene = (SceneBase) sceneStack.peek();
        scene.orientationChanged(engine.getGraphics(),isHorizontal);
    }

    @Override
    public UserInterface getUserInterface(){
        return userInterface;
    }

    /**
     * Manda un mensaje a la escena que se encuentra en la cima de la pila o
     * si se trata de una notificación con recompensa se manda al GameManager.
     */
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
