package com.gamelogic;

import com.engine.Engine;
import com.engine.Image;
import com.engine.SceneBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {
    private TILE[][] board;
    private final int rows;
    private final int cols;
    private int width, height, maxHorizontalFilled, maxVerticalFilled;
    private float relationX, relationY;
    private int posX, posY;

    private List<List<Integer>> adyancentsHorizontal;
    private List<List<Integer>> adyancentsVertical;


    Board(int x, int y, int sizeX_, int sizeY_) {
        board = new TILE[x][y];
        rows = y;
        cols = x;

        width = sizeX_;
        height = sizeY_;

        relationX =  sizeX_ / cols;
        relationY =  sizeY_ / rows;

        for(int i = 0; i < cols; i++)
            for (int j = 0; j < rows; j++)
                board[i][j] = TILE.EMPTY;
    }

    void generateBoard() {
        adyancentsHorizontal = new ArrayList<>();
        adyancentsVertical = new ArrayList<>();

        Random r = new Random();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int random = r.nextInt(10);

                if (random <= 3 )
                    board[i][j] = TILE.FILL;
            }
        }
        maxHorizontalFilled = 0;
        maxVerticalFilled = 0;
        for(int i = 0; i < rows; i++){
            List<Integer> adyacents = new ArrayList<>();
            int juntas = 0;
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == TILE.FILL){
                    juntas++;

                    if(j + 1 == cols)
                        adyacents.add(juntas);
                }
                else if(juntas != 0){
                    adyacents.add(juntas);
                    juntas = 0;
                }
            }

            if(adyacents.size() == 0) adyacents.add(0);
            else if(adyacents.size() > maxVerticalFilled){
                maxVerticalFilled = adyacents.size();
            }
            adyancentsVertical.add(adyacents);
        }

        for (int j = 0; j < cols; j++) {
            List<Integer> adyacents = new ArrayList<>();
            int juntas = 0;
            for(int i = 0; i < rows; i++){
                if (board[i][j] == TILE.FILL){
                    juntas++;

                    if(i + 1 == rows)
                        adyacents.add(juntas);
                }
                else if(juntas != 0){
                    adyacents.add(juntas);
                    juntas = 0;
                }
            }
            if(adyacents.size() == 0) adyacents.add(0);
            else if(adyacents.size() > maxHorizontalFilled){
                maxHorizontalFilled = adyacents.size();
            }
            adyancentsHorizontal.add(adyacents);
        }

        System.out.println("H: " + maxHorizontalFilled);
        System.out.println(Arrays.toString(adyancentsHorizontal.toArray()));
        System.out.println("V: " + maxVerticalFilled);
        System.out.println(Arrays.toString(adyancentsVertical.toArray()));
    }

    public  int getCols(){
        return cols;
    }

    public  int getRows(){
        return rows;
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
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                if(board[i][j] != otherBoard[i][j])
                    return false;
            }
        }
        return true;
    }

    public void render(Engine e, int x, int y){

        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if(x != posX) posX = x;
        if(y != posY) posY = y;

        //TODO: renderizar los números laterales
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                Image im = tileImage(e, board[i][j]);
                e.getGraphics().drawImage(im, (int)(i* relationX) + x, (int)(j*relationY) + y,
                                        relationX/im.getWidth(),relationY/im.getHeight());
            }
        }
    }

    boolean calculcateIndexMatrix(int pixelX, int pixelY,SceneBase scene){
        SceneGame sceneG = (SceneGame) scene;
        if(( pixelX > posX && pixelX <= posX + width ) && ( pixelY > posY && pixelY <= posY + height )){
            sceneG.i_index = (int) ((pixelX - posX)/(relationX));
            sceneG.j_index = (int) ((pixelY - posY)/(relationY));
//            System.out.println("Index X : " + sceneG.i_index +  " Index Y : " + sceneG.j_index);
            return true;
        }

        return false;
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

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
}
