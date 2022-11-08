package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.util.ArrayList;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {
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

    //Filas y columnas del tablero
    private final int rows_, cols_;

    private int numRemaining = 0, numWrong = 0;
    //Fuentes
    private IFont numFont, pixelFont;

    private static final double maxTime = 2.5; //Segundos para texto incorrecto
    private double timer = maxTime;
    private boolean DEBUG = false;

    public SceneGame(Engine engine, int rows, int cols) {
        this.engine = engine;
        rows_ = rows;
        cols_ = cols;
    }

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
            engine.getGame().changeScene(new SceneVictory(engine , checkBoard));
        }
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

        int boardSize = 360;

        //Tablero de solucion
        checkBoard = new Board(cols_, rows_, boardSize, boardSize);
        checkBoard.generateBoard();
        //Tablero de juego
        gameBoard = new Board(cols_, rows_, boardSize, boardSize);

        //relación respecto a numero de casillas
        Pair<Float, Float> relations = gameBoard.getRelationFactorSize();

        int size = (int) Math.round(relations.first * 0.7);
        pixelFont = engine.getGraphics().newFont("upheavtt.ttf", size, false);

        //Tamaño de los botones
        int offset = 100, bttWidth = 150, bttHeight = 50;

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
        bttCheckWin.setColor(IColor.BLACK);
        bttCheckWin.setBackgroundImage(engine.getGraphics().getImage("empty"));

        //Boton Return to menu
        bttReturn = new Button("Coward", engine.getGraphics().getLogicWidth()/2 - bttWidth/2 - offset,
                engine.getGraphics().getLogicHeight()- bttHeight*3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        engine.getAudio().playSound("click.wav");
                        engine.getGame().changeScene(new SceneTitle(engine));
                    }
                }
            }

            @Override
            public void update(double deltaTime) {

            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(IColor.BLACK);
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("empty"));
    }

    @Override
    public void loadResources(IGraphics graphics){
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

        numFont = graphics.newFont("arcade.TTF", 40, false);

        pixelFont = graphics.newFont("upheavtt.ttf", 10, false);

        engine.getAudio().newSound("wrong.wav");
        engine.getAudio().newSound("correct.wav");

        System.out.println("Resources Loaded");
    }

    @Override
    public void render(IGraphics graphics) {
        //Tablero
        graphics.setColor(IColor.BLACK, 1.0f);
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

            graphics.setColor(IColor.BLUE, 1.0f);
            graphics.drawText(remainingField, (int) (graphics.getLogicWidth()/2 - dime_remaining.first/2), (int) (graphics.getLogicHeight() * 0.05 + dime_remaining.second/2));
            graphics.setColor(IColor.RED, 1.0f);
            graphics.drawText(wrongField, (int) (graphics.getLogicWidth()/2 - dime_wrong.first/2), (int) (graphics.getLogicHeight() * 0.09 + dime_wrong.second/2));

        }
    }

    //Establece la casilla dada por [x][y] al siguiente estado
    void setTile(int x, int y, boolean wrong) {
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

    boolean checkHasWon() {
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
}