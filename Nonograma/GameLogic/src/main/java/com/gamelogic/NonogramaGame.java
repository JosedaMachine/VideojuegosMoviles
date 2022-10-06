package com.gamelogic;

import com.engine.Engine;

import java.awt.Graphics;

public class NonogramaGame {
    Engine engine;

    Board checkBoard;
    Board gameBoard;

    boolean hasWon = false;

    boolean checkWin = false;

    NonogramaGame(Engine engine) {
        this.engine = engine;
    }

    void init(int x, int y){
        checkBoard = new Board(x, y);
        checkBoard.GenerateBoard();

        gameBoard = new Board(x, y);
    }

    boolean hasWon() {
        return hasWon;
    }

    // TODO comprobar setTile
    boolean setTile(int x, int y, boolean click) {
        gameBoard.GetTile(x, y);
        //Comprobar cosa?
        gameBoard.SetTile(x,y, TILE.FILL);
        checkWin = true;
        return true;
    }

    void update(double deltaTime) {
        if(checkWin) {
            //CheckWin();
            checkWin = false;
        }
    }

    void render() {
        gameBoard.render(engine);

    }

}