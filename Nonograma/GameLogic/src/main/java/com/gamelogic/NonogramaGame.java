package com.gamelogic;

import com.engine.Engine;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class NonogramaGame {
    Engine engine;

    //Tablero chuleta para comprobar
    Board checkBoard;
    //Tablero que ve el jugador
    Board gameBoard;

    //True cuando coincidan los tableros
    boolean hasWon = false;
    //True cuando ocurra un movimiento y haya que comprobar
    boolean checkWin = false;

    NonogramaGame(Engine engine) {
        this.engine = engine;
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

    void update(double deltaTime) {
        if(checkWin) {
            hasWon = checkHasWon();
            checkWin = false;
        }
    }

    void render() {
        gameBoard.render(engine);
    }

    boolean checkHasWon() {
        return checkBoard.isBoardMatched(gameBoard);
    }

}