package com.gamelogic;

import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import com.engine.SceneBase;
import com.engine.TouchEvent;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {
    Engine engine;
    //Tablero chuleta para comprobar
    Board checkBoard;
    //Tablero que ve el jugador
    Board gameBoard;

    Button bttCheckWin;
    //True cuando coincidan los tableros
    boolean hasWon = false;
    //True cuando ocurra un movimiento y haya que comprobar
    boolean checkWin = false;

    private IFont numFont;

    public SceneGame(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(double deltaTime) {
        if(checkWin) {
            hasWon = checkHasWon();
            checkWin = false;
        }

    }

    @Override
    public void input(TouchEvent event_) {
        if(event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT){
            //TODO Boton que evalue tablero ->casillas malas en rojo durante X segundos
            //TODO Variable de fin de juego si tablero correcto
            //TODO hacer un width y height de la fuente

            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(event_.getX_(),event_.getY_());

            setTile(index.first, index.second, false); //SI pongo esto se pone a fill y recibe mas inputs y se pone a empty

        }
    }

    @Override
    public void init() {
        loadImages(engine.getGraphics());
        int rows = 5, cols = 10;

        //Tablero de solucion
        checkBoard = new Board(cols, rows, 500, 500);
        checkBoard.generateBoard();
        //Tablero de juego
        gameBoard = new Board(cols, rows, 500, 500);

        bttCheckWin = new Button("Finish", engine.getGraphics().getWidth() - 250, engine.getGraphics().getHeight() - 250, 150, 150) {
            @Override
            public void input(TouchEvent event_) {
                checkWin = true;
            }
        };
    }

    @Override
    public void loadImages(IGraphics graphics){
        System.out.println("Loading Resources...");

        //Image img = graphics.newImage("examplePCReal/assets/images/tom.png", graphics.getWidth(), graphics.getHeight());
        //graphics.loadImage(img, "Tom");

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

        numFont = graphics.newFont("RamadhanMubarak.ttf", 20, false);

        System.out.println("Resources Loaded");
    }

    @Override
    public void render(IGraphics graphics) {
        graphics.setColor(IColor.BLACK);
        checkBoard.drawInfoRects(engine, graphics.getWidth()/2 - gameBoard.getWidth()/2, graphics.getHeight()/2 - gameBoard.getHeight()/2, numFont);
        //checkBoard.drawBoard(engine, graphics.getWidth()/2 - gameBoard.getWidth()/2, graphics.getHeight()/2 - gameBoard.getHeight()/2);
        gameBoard.drawBoard(engine, graphics.getWidth()/2 - gameBoard.getWidth()/2, graphics.getHeight()/2 - gameBoard.getHeight()/2);
    }

    boolean hasWon() {
        return hasWon;
    }

    boolean setTile(int x, int y, boolean click) {
        if(x < 0 || y < 0) return false;

        TILE tile = gameBoard.getTile(x, y);

        if(tile == TILE.EMPTY)
            gameBoard.setTile(x,y, TILE.FILL);
        else
            gameBoard.setTile(x,y, TILE.EMPTY);

        return true;
    }


    boolean checkHasWon() {


        return checkBoard.isBoardMatched(gameBoard);
    }



}