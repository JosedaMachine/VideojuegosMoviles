package com.gamelogic;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {

    //region Variables y Constructora
    private final Engine engine;
    //Tablero chuleta para comprobar
    private Board checkBoard;
    //Tablero que ve el jugador
    private Board gameBoard;

    private Button bttCheckWin, bttReturn;
    //True cuando coincidan los tableros
    private boolean hasWon = false;
    //True cuando ocurra un movimiento y haya que comprobar
    private boolean checkWin = false;

    //Vidas
    private final int maxLives = 3;
    private int lives = maxLives;

    //Filas y columnas del tablero
    private int rows_;
    private int cols_;

    private int numRemaining = 0, numWrong = 0;
    //Fuentes
    private Font numFont, pixelFont;

    private Fade fade;

    private static final double maxTime = 2.5; //Segundos para texto incorrecto
    private double timer = maxTime;
    private boolean DEBUG = false;

    private String levelName = null;

    private Category category = null;
    private int lvlIndex = 0;

    String[] OceanLevels = {"fish", "anchor", "crab"};
    String[] MedievalLevels = {"cat" , "sword"};


    public SceneGame(Engine engine, int rows, int cols) {
        this.engine = engine;
        rows_ = rows;
        cols_ = cols;
    }

    public SceneGame(Engine engine, int rows, int cols, Category cat, int index) {
        this.engine = engine;
        rows_ = rows;
        cols_ = cols;
        category = cat;
        lvlIndex = index;

        if(cat == Category.CAT1)
            levelName = MedievalLevels[index];
    }

    //endregion + Construct y Con

    //region Override methods

    @Override
    public void update(double deltaTime) {
        //Comprueba la victoria
        if(checkWin) {
            hasWon = checkHasWon();
            checkWin = false;
            if(!hasWon){
                //Inicia el timer de derrota
                timer = 0;
                engine.getAudio().playSound("wrong.wav");

                //Si nivel historia y vidas == 0
                if(!subtractLife()){
                    //TODO: Enviar a escena de derrota o devolver a escena anterior
                    //engine.getGame().changeScene(new SceneVictory(engine , checkBoard));
                }
            }
        }

        //Avanza timer
        if(timer < maxTime){
            timer += deltaTime;
            if(timer >= maxTime)
                //Limpiar casillas rojas
                gameBoard.clearWrongsTiles();
        }

        //Victoria
        if(hasWon){
            engine.getAudio().playSound("correct.wav");

            //Si modo historia
            if (category != null){
                final int lastLvlIndex = GameManager.instance().getLevelIndex(category);
                //Si es el ultimo nivel desbloqueado -> desbloquea siguiente
                if(lastLvlIndex == lvlIndex)
                    GameManager.instance().setLevelIndex(category, lvlIndex + 1);

                //TODO: Guardar nuevo lastLvlIndex en archivo guardado
            }

            engine.getGame().changeScene(new SceneVictory(engine , checkBoard));
        }
        fade.update(deltaTime);
        bttReturn.update(deltaTime);
    }

    @Override
    public void input(TouchEvent event_) {
        bttCheckWin.input(event_);
        bttReturn.input(event_);

        if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
            //Input en casillas del tablero
            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(engine, event_.getX_(),event_.getY_());

            setTile(index.first, index.second, false);
            //Debug para mostrar el resultado
            if(event_.getID_() == TouchEvent.ButtonID.MIDDLE_BUTTON){
                DEBUG = !DEBUG;
            }
        }
    }

    @Override
    public void init() {
        loadResources(engine.getGraphics());

        //Fade In
        fade = new Fade(engine,
                0, 0,
                engine.getGraphics().getLogicWidth(), engine.getGraphics().getLogicHeight(),
                1000, 1000, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        int boardSize = (int)(engine.getGraphics().getLogicWidth() * 0.6f);


        if(levelName != null){
            createLevel(levelName, boardSize);
        }else createLevel(boardSize);

        //relación respecto a numero de casillas
        Pair<Float, Float> relations = gameBoard.getRelationFactorSize();

        float size = (float) (Math.floor(relations.first * 0.7)/1000.0f);
        pixelFont = engine.getGraphics().newFont("upheavtt.ttf", (int)(engine.getGraphics().getLogicHeight() * size), false);

        //Tamaño de los botones
        int offset = (int)(engine.getGraphics().getLogicWidth() * 0.16f),
            bttWidth = (int)(engine.getGraphics().getLogicWidth() * 0.25f),
            bttHeight = (int)(engine.getGraphics().getLogicWidth() * 0.0833f);

        //Boton Check Win
        bttCheckWin = new Button("Check", engine.getGraphics().getLogicWidth()/2 - bttWidth/2 + offset,
                engine.getGraphics().getLogicHeight() - bttHeight*3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        checkWin = true;
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };
        bttCheckWin.setFont(numFont);
        bttCheckWin.setColor(ColorWrap.BLACK);
        bttCheckWin.setBackgroundImage(engine.getGraphics().getImage("empty"));

        //Boton Return to menu
        bttReturn = new Button("Coward", engine.getGraphics().getLogicWidth()/2 - bttWidth/2 - offset,
                engine.getGraphics().getLogicHeight()- bttHeight*3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");

                        if(fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if(fade.getFadeOutComplete()){
                    engine.getGame().changeScene(new SceneTitle(engine));
                }
            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(ColorWrap.BLACK);
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    private void createLevel(String levelName, int boardSize) {
        BufferedReader reader_ = null;
        try {
            reader_ = engine.openFile("levels/" + levelName +  ".txt");
        }
        catch (IOException e) {
            System.out.println("Error opening file");
            e.printStackTrace();
        }

//        Tablero de solucion
        checkBoard = new Board(reader_, boardSize, boardSize);

        cols_ = checkBoard.getCols();
        rows_ = checkBoard.getRows();

        gameBoard = new Board(cols_, rows_, boardSize, boardSize);
    }

    private void createLevel(int boardSize) {
        checkBoard = new Board(cols_, rows_, boardSize, boardSize);
        checkBoard.generateBoard();
        //Tablero de juego
        gameBoard = new Board(cols_, rows_, boardSize, boardSize);
    }

    @Override
    public void loadResources(Graphics graphics){
        System.out.println("Loading Resources...");

        Image im = graphics.newImage("emptysquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "empty");

        im = graphics.newImage("crosssquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "cross");

        im = graphics.newImage("tom.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "tom");

        im = graphics.newImage("wrongsquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "wrong");

        im = graphics.newImage("fillsquare.png");
        if(!im.isLoaded())
            System.out.println("No se ha encontrado la imagen");
        graphics.loadImage(im, "fill");

        numFont = graphics.newFont("arcade.TTF", (int)(engine.getGraphics().getLogicHeight() * 0.04f), false);

        pixelFont = graphics.newFont("upheavtt.ttf", (int)(engine.getGraphics().getLogicHeight() * 0.1f), false);

        engine.getAudio().newSound("wrong.wav");
        engine.getAudio().newSound("correct.wav");

        System.out.println("Resources Loaded");
    }

    @Override
    public void render(Graphics graphics) {
        //Tablero
        graphics.setColor(ColorWrap.BLACK, 1.0f);
        checkBoard.drawInfoRects(engine, graphics.getLogicWidth()/2 - gameBoard.getWidth()/2, graphics.getLogicHeight()/2 - gameBoard.getHeight()/2, pixelFont);
        gameBoard.drawBoard(engine, checkBoard.getPosX(), checkBoard.getPosY(), false);

        //Botones
        bttCheckWin.render(graphics);
        bttReturn.render(graphics);

        if(DEBUG){
            checkBoard.drawBoard(engine, checkBoard.getPosX(), checkBoard.getPosY(), false);
        }

        //Texto indicando casillas incorrectas
        if(!hasWon && timer < maxTime){
            graphics.setFont(numFont);

            String remainingField = numRemaining + " remaining cells";
            String wrongField = numWrong + " wrong cells";

            Pair<Double, Double> dime_remaining = graphics.getStringDimensions(remainingField);
            Pair<Double, Double> dime_wrong = graphics.getStringDimensions(wrongField);

            graphics.setColor(ColorWrap.BLUE, 1.0f);
            graphics.drawText(remainingField, (int) (graphics.getLogicWidth()/2 - dime_remaining.first/2), (int) (graphics.getLogicHeight() * 0.05 + dime_remaining.second/2));
            graphics.setColor(ColorWrap.RED, 1.0f);
            graphics.drawText(wrongField, (int) (graphics.getLogicWidth()/2 - dime_wrong.first/2), (int) (graphics.getLogicHeight() * 0.09 + dime_wrong.second/2));

        }

        fade.render();
    }

    //endregion

    //region methods

    //Establece la casilla dada por [x][y] al siguiente estado
    private void setTile(int x, int y, boolean wrong) {
        if(x < 0 || y < 0) return;

        if(wrong){
            gameBoard.setTile(x, y, TILE.WRONG);
            return;
        }

        TILE tile = gameBoard.getTile(x, y);

        if(tile == TILE.EMPTY)
            gameBoard.setTile(x,y, TILE.FILL);
        else if(tile == TILE.FILL)
            gameBoard.setTile(x,y, TILE.CROSS);
        else
            gameBoard.setTile(x,y, TILE.EMPTY);
    }

    private boolean subtractLife(){
        lives--;
        return lives > 0;
    }

    private boolean checkMaxLives(){
        return lives == maxLives;
    }

    private void addLife(){
        if(lives < maxLives)
            lives++;
    }

    private boolean checkHasWon() {
        ArrayList<Pair<Integer, Integer>> wrongs = checkBoard.isBoardMatched(gameBoard);

        //NO recorremos hasta el final, el último es el nº de tiles
        for(int i = 0; i < wrongs.size() - 1; i++){
            //Establecemos a wrong las casillas
            setTile(wrongs.get(i).first, wrongs.get(i).second, true);
        }

        //Numero de incorrectas y que faltan
        numRemaining = checkBoard.getNumCorrectTiles() - wrongs.get(wrongs.size() - 1).first;
        numWrong = wrongs.size() - 1;

        return wrongs.size() == 1 && //Que no haya incorrectas
               wrongs.get(wrongs.size()-1).first == checkBoard.getNumCorrectTiles(); //Que haya todas las correctas
    }

    //endregion
}