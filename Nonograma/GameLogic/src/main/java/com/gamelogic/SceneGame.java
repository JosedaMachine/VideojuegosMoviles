package com.gamelogic;

import com.engine.Engine;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;
import com.engine.TouchEvent;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {
    Engine engine;
    //Tablero chuleta para comprobar
    Board checkBoard;
    //Tablero que ve el jugador
    Board gameBoard;

    //True cuando coincidan los tableros
    boolean hasWon = false;
    //True cuando ocurra un movimiento y haya que comprobar
    boolean checkWin = false;

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
    public void input(TouchEvent event) {

    }

    @Override
    public void init() {
        int x = 10, y = 10;
        checkBoard = new Board(x, y);
        checkBoard.generateBoard();
    }

    @Override
    public void loadImages(IGraphics graphics){
        System.out.println("Loading Resources...");
        Image img = graphics.newImage("examplePCReal/assets/images/tom.png", graphics.getWidth(), graphics.getHeight());
        graphics.loadImage(img, "Tom");
    }

    @Override
    public void render(IGraphics graphics) {
        //gameBoard.render(engine);
        Image im = graphics.newImage("examplePCReal/assets/images/tom.png", graphics.getWidth(), graphics.getHeight());
//        im = graphics.getImage("Tom");
        if(im == null)
            System.out.println("No se ha encontrado la imagen");
        else
            graphics.drawImage(im, graphics.getWidth()/2, graphics.getHeight()/2,graphics.getWidth(), graphics.getHeight());
    }

    void init(int x, int y){
        checkBoard = new Board(x, y);
        checkBoard.generateBoard();

        gameBoard = new Board(x, y);
    }

    boolean hasWon() {
        return hasWon;
    }

    boolean setTile(int x, int y, boolean click) {
        TILE tile = gameBoard.getTile(x, y);

        if(tile == TILE.FILL)
            gameBoard.setTile(x,y, TILE.FILL);
        else
            gameBoard.setTile(x,y, TILE.EMPTY);

        checkWin = true;
        return true;
    }


    boolean checkHasWon() {
        return checkBoard.isBoardMatched(gameBoard);
    }



}