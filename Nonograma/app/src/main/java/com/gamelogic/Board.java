package com.gamelogic;

import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Image;
import com.engineandroid.Pair;
import com.gamelogic.enums.TILE;
import java.lang.Math;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private TILE[][] board;
    private int rows = 0;
    private int cols = 0;
    private final int width, height;
    private int maxHorizontalFilled, maxVerticalFilled;
    private final float relationX, relationY;
    private int posX, posY;

    private int tileSize;

    private int numCorrectTiles;
    private List<List<Integer>> adyancentsHorizontal;
    private List<List<Integer>> adyancentsVertical;


    public Board(int cols, int rows, int sizeX_, int sizeY_, int tileSize) {
        board = new TILE[cols][rows];
        this.rows = rows;
        this.cols = cols;

        //Relacion para cuando hay menos filas que columnas
        float relationRowCol = this.rows /(float) this.cols;

        width = sizeX_;
        height = (int)(sizeY_*relationRowCol);

        relationX = sizeX_ /(float) this.cols;
        relationY = (sizeY_ /(float) this.rows) * relationRowCol;

        //Tablero incialmente vacio
        for (int i = 0; i < this.cols; i++)
            for (int j = 0; j < this.rows; j++)
                board[i][j] = TILE.EMPTY;

        numCorrectTiles = 0;

        this.tileSize = tileSize;
    }

    public Board(BufferedReader reader, int sizeX_, int sizeY_, int tileSize){
        //Relacion para cuando hay menos filas que columnas
        try {
            String mLine;
            mLine = reader.readLine();
            this.cols = Integer.parseInt(mLine);
            mLine = reader.readLine();
            this.rows = Integer.parseInt(mLine);

            board = new TILE[this.cols][this.rows];

            //Por cada character de la fila String, asignamos filas de la columna
            int i = 0;
            while ((mLine = reader.readLine()) != null) {
                for(int j = 0; j < mLine.length(); j++){
                    if(mLine.charAt(j) == '.'){
                        board[j][i] = TILE.FILL;
                        numCorrectTiles++;
                    }
                    else board[j][i] = TILE.EMPTY;
                }
                i++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
//        numCorrectTiles = 0;
        //Relacion para cuando hay menos filas que columnas
        float relationRowCol = this.rows /(float) this.cols;

        width = sizeX_;
        height = (int)(sizeY_*relationRowCol);

        relationX = sizeX_ /(float) this.cols;
        relationY = (sizeY_ /(float) this.rows) * relationRowCol;

        calcAdjyacents();
        this.tileSize = tileSize;
    }

    public void generateBoard() {
        //generamos el tablero solucion
        Random r = new Random();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int random = r.nextInt(10);

                if (random <= 3){
                    board[i][j] = TILE.FILL;
                    numCorrectTiles++;
                }
            }
        }

        calcAdjyacents();
    }

    void calcAdjyacents(){
        adyancentsHorizontal = new ArrayList<>();
        adyancentsVertical = new ArrayList<>();
        //Calculamos los numeros adyacentes horizontal
        maxHorizontalFilled = 0;
        maxVerticalFilled = 0;
        for (int i = 0; i < rows; i++) {
            List<Integer> adyacents = new ArrayList<>();
            int juntas = 0;
            for (int j = 0; j < cols; j++) {
                if (board[j][i] == TILE.FILL) {
                    juntas++;

                    if (j + 1 == cols)
                        adyacents.add(juntas);
                } else if (juntas != 0) {
                    adyacents.add(juntas);
                    juntas = 0;
                }
            }

            if (adyacents.size() == 0) adyacents.add(0);
            else if (adyacents.size() > maxHorizontalFilled) {
                maxHorizontalFilled = adyacents.size();
            }
            adyancentsHorizontal.add(adyacents);
        }

        //vertical
        for (int j = 0; j < cols; j++) {
            List<Integer> adyacents = new ArrayList<>();
            int juntas = 0;
            for (int i = 0; i < rows; i++) {
                if (board[j][i] == TILE.FILL) {
                    juntas++;

                    if (i + 1 == rows)
                        adyacents.add(juntas);
                } else if (juntas != 0) {
                    adyacents.add(juntas);
                    juntas = 0;
                }
            }
            if (adyacents.size() == 0) adyacents.add(0);
            else if (adyacents.size() > maxVerticalFilled) {
                maxVerticalFilled = adyacents.size();
            }
            adyancentsVertical.add(adyacents);
        }
    }


    public Pair<Float, Float> getRelationFactorSize(){
        return new Pair<>(relationX, relationY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getNumCorrectTiles(){
        return numCorrectTiles;
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

    // Comprueba si los tableros coinciden
    public ArrayList<Pair<Integer, Integer>> isBoardMatched(Board other) {
        TILE[][] otherBoard = other.getBoard();
        ArrayList<Pair<Integer, Integer>> diff = new ArrayList<>();
        int tilesMatched = 0;
        //Assume they are the same size (width and height)
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if(otherBoard[i][j] == TILE.CROSS || otherBoard[i][j] == TILE.EMPTY)
                    continue;
                if (board[i][j] != otherBoard[i][j])
                    diff.add(new Pair<>(i, j));
                else
                    tilesMatched++;

            }
        }

        //Metemos al final el nº de tiles que coinciden
        diff.add(new Pair<>(tilesMatched, -1));

        return diff;
    }

    private void drawNumRect(Engine e, int x, int y, int alphaX, int alphaY) {

        // dibujar cuadro lateral
        e.getGraphics().drawLine(alphaX, y + (int)relationY*rows, x, y + (int)relationY*rows);
        e.getGraphics().drawLine(alphaX, y, x, y);
        e.getGraphics().drawLine(alphaX, y, alphaX, y + (int)relationY*rows);
        //Dibujar cuadro superior
        e.getGraphics().drawLine(x, alphaY, x, y);
        e.getGraphics().drawLine(x + (int)relationX*cols, alphaY, x + (int)relationX*cols, y);
        e.getGraphics().drawLine(x, alphaY, x + (int)relationX*cols, alphaY);
    }

    private void drawNums(Engine e, int x, int y, int fontSize) {
        int i, j;
        //Dibujar numero de correctos Horizontal
        for (i = 0; i < adyancentsHorizontal.size(); i++) {
            int size = adyancentsHorizontal.get(i).size();
            for (j = size - 1; j >= 0; j--) {
                Integer num = adyancentsHorizontal.get(i).get(j);
                if (num != 0)
                    e.getGraphics().drawText(num.toString(),
                            x - ((size-1 - j) * (fontSize+fontSize/2)) - fontSize,
                            (int) (i * (int)relationY) + y + (int)((int)relationY/1.3));
            }
        }

        //Vertical
        for (i = 0; i < adyancentsVertical.size(); i++) {
            int size = adyancentsVertical.get(i).size();
            for (j = size - 1; j >= 0; j--) {
                Integer num = adyancentsVertical.get(i).get(j);
                if (num != 0)
                    e.getGraphics().drawText(num.toString(),
                            (int) (i * (int)relationX) + x + ((int)relationX/2),
                            // se suma fontsize/2 porque el punto inicial del texto es la esquina inferior izquierda
                            y - ((size-1-j) * (fontSize+fontSize/2)) - fontSize/2);
            }
        }
    }

    public void drawBoard(Engine e, int x, int y, boolean win, int pal) {
        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if (x != posX) posX = x;
        if (y != posY) posY = y;

        //Dibujar cada tile teniendo en cuenta el tamanyo total del tablero
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Image im = tileImage(e, board[i][j], pal);
                assert im != null;
                if(!win || board[i][j] == TILE.FILL)
                    e.getGraphics().drawImage(im, i * (int)(relationX) + x, j * (int)(relationY) + y ,
                        relationX / tileSize, relationY / tileSize);
            }
        }
    }

    public void drawInfoRects(Engine e, int x, int y, Font font) {
        e.getGraphics().setFont(font);
        e.getGraphics().setColor(ColorWrap.BLACK, 1.0f);
        int fontSize = font.getSize();

        //Recalculamos X para que el tablero junto al cuadro numerico esten centrados en X
        int boardMiddle = (width - x)/2;
        int alphaX = x - maxHorizontalFilled * (fontSize+fontSize/2);
        int newMiddle = (width - alphaX)/2;
        x += newMiddle- boardMiddle;

        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if (x != posX) posX = x;
        if (y != posY) posY = y;

        //Posicion inicial de cuadro numerico
        alphaX = x - maxHorizontalFilled * (fontSize+fontSize/2);
        int alphaY = y - maxVerticalFilled * (fontSize+fontSize/2);

        drawNumRect(e, x, y, alphaX, alphaY);

        drawNums(e, x, y, fontSize);
    }

    public Pair<Integer,Integer> calculcateIndexMatrix(int pixelX, int pixelY) {
        // Comprueba si esta dentro del tablero
        if ((pixelX > posX && pixelX <= posX + width) && (pixelY > posY && pixelY <= posY + height)) {
            // Localiza posicion y selecciona el tile
            int i_index = (int) ((pixelX - posX) / (relationX));
            int j_index = (int) ((pixelY - posY) / (relationY));

            return new Pair<>(i_index, j_index);
        }

        return new Pair<>(-1, -1);
    }

    //Coge image dependiendo del tile
    private Image tileImage(Engine e, TILE t, int pal) {
        switch (t) {
            case FILL:
                return e.getGraphics().getImage("fill" + pal);
            case CROSS:
                return e.getGraphics().getImage("cross"+ pal);
            case EMPTY:
                return e.getGraphics().getImage("empty"+ pal);
            case WRONG:
                return e.getGraphics().getImage("wrong"+ pal);
        }
        return null;
    }

    //Transforma la tiles rojas en llenas (Llamado tras X segundos de mostrar los fallos)
    //Pasan a llenas porque es como estaban antes de empezar. SI el jugador hace clic en una casilla
    //roja, esta pasa a empty
    public void clearWrongsTiles(){
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j] == TILE.WRONG)
                    board[i][j] = TILE.FILL;
            }
        }
    }

    public int getPosX(){return posX;};
    public int getPosY(){return posY;};


    public boolean toFile(File file, Scanner reader){



        return true;
    }

    public boolean readFile(File file, Scanner reader){

        

        return true;
    }
}
