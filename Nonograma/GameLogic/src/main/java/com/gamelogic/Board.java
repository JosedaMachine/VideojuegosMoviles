package com.gamelogic;

import com.engine.Engine;
import com.engine.Image;

import java.util.Arrays;
import java.util.Random;

public class Board {
    private TILE[][] board;
    private final int height;
    private final int width;

    Board(int x, int y) {
        board = new TILE[x][y];
        height = y;
        width = x;
        for (int i = 0; i < height; i++)
            Arrays.fill(board[i], TILE.EMPTY);
    }

    void generateBoard() {
        Random r = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int random = r.nextInt(10);

                if (random <= 3 )
                    board[i][j] = TILE.FILL;
            }
        }
    }

    public  int getWidth(){
        return width;
    }

    public  int getHeight(){
        return height;
    }

    public TILE getTile(int x, int y) {
        return board[x][y];
    }

    public void setTile(int x, int y, TILE tile) {
        board[x][y] = tile;
    }

    public TILE[][] getBoard() {
        return board;
    }

    public boolean isBoardMatched(Board other) {
        TILE[][] otherBoard = other.getBoard();

        //Assume they are the same size (width and height)
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(board[i][j] != otherBoard[i][j])
                    return false;
            }
        }
        return true;
    }

    //TODO implement render
    public void render(Engine e){
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                //e.draw(board[i][j]);?????-?????

                Image im = tileImage(e, board[i][j]);
                e.getGraphics().drawImage(im,i*50, j*50, 1f,1f);
            }
        }
    }

    private Image tileImage(Engine e, TILE t){
        switch (t){
            case FILL:
                return e.getGraphics().getImage("fill");
            case CROSS:
                return e.getGraphics().getImage("cross");
            case EMPTY:
                return e.getGraphics().getImage("empty");
            case WRONG:
                return e.getGraphics().getImage("wrong");
        }
        return null;
    }
}
