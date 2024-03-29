package com.gamelogic;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGame;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.util.ArrayList;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {
//    private final Engine engine;
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

    private Fade fade;

    private static final double maxTime = 2.5; //Segundos para texto incorrecto
    private double timer = maxTime;
    private boolean DEBUG = false;

    public SceneGame(int rows, int cols) {
//        this.engine = engine;
        rows_ = rows;
        cols_ = cols;
    }

    @Override
    public void update(double deltaTime) {
        //Avanza timer
        if(timer < maxTime){
            timer += deltaTime;
            if(timer >= maxTime)
                //Limpiar casillas rojas
                gameBoard.clearWrongsTiles();
        }

        fade.update(deltaTime);
        bttReturn.update(deltaTime);
    }

    @Override
    public void input(IGame game, Audio audio, TouchEvent event_) {
        bttCheckWin.input(event_);
        bttReturn.input(event_);

        //Comprueba la victoria
        if(checkWin) {
            hasWon = checkHasWon();
            checkWin = false;
            if(!hasWon){
                //Inicia el timer de derrota
                timer = 0;
                audio.playSound("wrong.wav");
            }
        }

        //Victoria
        if(hasWon){
            audio.playSound("correct.wav");
            game.changeScene(new SceneVictory(checkBoard));
        }

        if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
            //Input en casillas del tablero
            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(audio, event_.getX_(),event_.getY_());

            setTile(index.first, index.second, false);
            //Debug para mostrar el resultado
            if(event_.getID_() == TouchEvent.ButtonID.MIDDLE_BUTTON){
                DEBUG = !DEBUG;
            }
        }
    }

    @Override
    public void init(final IGame game, IGraphics graphics, final Audio audio) {
        loadResources(graphics, audio);

        //Fade In
        fade = new Fade(
                0, 0,
                graphics.getLogicWidth(), graphics.getLogicHeight(),
                1000, 1000, Fade.STATE_FADE.In);
        fade.setColor(IColor.BLACK);
        fade.triggerFade();

        int boardSize = (int)(graphics.getLogicWidth() * 0.6f);

        //Tablero de solucion
        checkBoard = new Board(cols_, rows_, boardSize, boardSize);
        checkBoard.generateBoard();
        //Tablero de juego
        gameBoard = new Board(cols_, rows_, boardSize, boardSize);

        //relación respecto a numero de casillas
        Pair<Float, Float> relations = gameBoard.getRelationFactorSize();

        float size = (float) (Math.floor(relations.first * 0.7)/1000.0f);
        pixelFont = graphics.newFont("upheavtt.ttf", (int)(graphics.getLogicHeight() * size), false);

        //Tamaño de los botones
        int offset = (int)(graphics.getLogicWidth() * 0.16f),
            bttWidth = (int)(graphics.getLogicWidth() * 0.25f),
            bttHeight = (int)(graphics.getLogicWidth() * 0.0833f);

        //Boton Check Win
        bttCheckWin = new Button("Check", graphics.getLogicWidth()/2 - bttWidth/2 + offset,
                graphics.getLogicHeight() - bttHeight*3, bttWidth, bttHeight) {
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
        bttCheckWin.setBackgroundImage(graphics.getImage("empty"));

        //Boton Return to menu
        bttReturn = new Button("Coward", graphics.getLogicWidth()/2 - bttWidth/2 - offset,
                graphics.getLogicHeight()- bttHeight*3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
                    if(isInside(event_.getX_(),event_.getY_())){
                        audio.playSound("click.wav");

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
                    game.changeScene(new SceneTitle());
                }
            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(IColor.BLACK);
        bttReturn.setBackgroundImage(graphics.getImage("empty"));
    }

    @Override
    public void loadResources(IGraphics graphics, Audio audio){
        System.out.println("Loading Resources...");

       graphics.newImage("emptysquare.png","empty");

       graphics.newImage("crosssquare.png", "cross");

        graphics.newImage("tom.png", "tom");

        graphics.newImage("wrongsquare.png", "wrong");

        graphics.newImage("fillsquare.png", "fill");

        numFont = graphics.newFont("arcade.TTF", (int)(graphics.getLogicHeight() * 0.04f), false);

        pixelFont = graphics.newFont("upheavtt.ttf", (int)(graphics.getLogicHeight() * 0.1f), false);

        audio.newSound("wrong.wav");
        audio.newSound("correct.wav");

        System.out.println("Resources Loaded");
    }

    @Override
    public void render(IGraphics graphics) {
        //Tablero
        graphics.setColor(IColor.BLACK, 1.0f);
        checkBoard.drawInfoRects(graphics, graphics.getLogicWidth()/2 - gameBoard.getWidth()/2, graphics.getLogicHeight()/2 - gameBoard.getHeight()/2, pixelFont);
        gameBoard.drawBoard(graphics, checkBoard.getPosX(), checkBoard.getPosY(), false);

        //Botones
        bttCheckWin.render(graphics);
        bttReturn.render(graphics);

        if(DEBUG){
            checkBoard.drawBoard(graphics, checkBoard.getPosX(), checkBoard.getPosY(), false);
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

        fade.render(graphics);
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