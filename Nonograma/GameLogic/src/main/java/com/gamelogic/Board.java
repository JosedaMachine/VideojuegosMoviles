package com.gamelogic;

public class Board {
    TILE[][] board;
    TILE[][] solutionBoard;

    Board(int x, int y) {
        board = new TILE[x][y];
    }

    void GenerateBoard() {
    }

    public TILE GetTile(int x, int y) {
        return null;
    }

    public void SetTile(int x, int y, TILE tile) {

    }

    public TILE[][] GetTiles() {
        return null;
    }

    boolean CheckWin() { return false; }


}
