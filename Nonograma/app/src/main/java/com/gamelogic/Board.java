package com.gamelogic;

import androidx.core.util.Pair;

import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;

import com.engineandroid.CustomPair;

import com.gamelogic.enums.TILE;

import java.io.FileOutputStream;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Board {



    private TILE[][] board;
    private Integer[][] colors;
    private int rows = 0;
    private int cols = 0;
    private int width, height;
    private int maxHorizontalFilled, maxVerticalFilled;
    private float relationX, relationY;
    private int posX, posY;
    private int alphaX, alphaY;

    private int tileSize;
    private boolean hasChanged_ = false;
    private int numCorrectTiles;
    private List<List<Pair<Integer,Integer>>> adyancentsHorizontal;
    private List<List<Pair<Integer,Integer>>> adyancentsVertical;


    public Board(int cols, int rows, int sizeX_, int sizeY_, int tileSize) {
        board = new TILE[cols][rows];
        colors = new Integer[cols][rows];
        this.rows = rows;
        this.cols = cols;

        //Relacion para cuando hay menos filas que columnas
        setSize(sizeX_, sizeY_);
        //Tablero incialmente vacio
        for (int i = 0; i < this.cols; i++)
            for (int j = 0; j < this.rows; j++){
                board[i][j] = TILE.EMPTY;
                colors[i][j] = -1;
            }

        numCorrectTiles = 0;

        setTileSize(tileSize);
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
            int k = 0;
            while (k < rows && (mLine = reader.readLine()) != null) {
                for(int j = 0; j < mLine.length(); j++){
                    if(mLine.charAt(j) == '.'){
                        board[j][i] = TILE.FILL;
                        numCorrectTiles++;
                    }
                    else board[j][i] = TILE.EMPTY;
                }
                i++;
                k++;
            }
        } catch (IOException e) {
            System.out.println("Error creating board by plane text");
        }
//        numCorrectTiles = 0;
        //Relacion para cuando hay menos filas que columnas
        setSize(sizeX_, sizeY_);

        assignColors();

        calcAdjyacents();

        setTileSize(tileSize);
    }

    private void assignColors(){
        ArrayList<Pair<Integer, Integer>> directions = new ArrayList<Pair<Integer, Integer>>();
        directions.add(new Pair<Integer, Integer>(1, 0));
        directions.add(new Pair<Integer, Integer>(-1, 0));
        directions.add(new Pair<Integer, Integer>(0, 1));
        directions.add(new Pair<Integer, Integer>(0, -1));

        Random r = new Random();
        //Seleccionamos entre 4 colores
        Integer[] colors;
        colors = new Integer[]{ColorWrap.BLACK,ColorWrap.BLUE,ColorWrap.RED, ColorWrap.GRAY};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int indexColor = r.nextInt(colors.length);
                assignColorsAux(i, j, directions, colors[indexColor]);
            }
        }

    }

    private void assignColorsAux(int rows , int cols, ArrayList<Pair<Integer, Integer>> dirs, int color){
        if(rows < 0 || cols < 0 || rows >= this.rows || cols >= this.cols)
            return;

        //Seleccionamos entre 4 colores
        // Preguntamos si es sol y si no se le ha asignado ningun color
        if (board[cols][rows] == TILE.FILL && colors[cols][rows] == -1) {
            colors[cols][rows] = color;
            for(int i = 0; i< dirs.size(); i++){
                assignColorsAux(rows +  dirs.get(i).first, cols +  dirs.get(i).second, dirs, color);
            }
        }
    }

    public void saveBoardState(FileOutputStream file){
        try {
            file.write((Integer.toString(this.cols) + "\n").getBytes());
            file.write((Integer.toString(this.rows)+ "\n").getBytes());
            //Las columnas son filas y filas columnas
            for (int j = 0; j < this.rows; j++){
                String line = "";
                for (int i = 0; i < this.cols; i++){
                    if( board[i][j] == TILE.FILL || board[i][j] == TILE.WRONG){
                        line =  line + '.';
                    }else if (board[i][j] == TILE.CROSS){
                        line =  line + 'x';
                    }else{
                        line =  line + 'p';
                    }
                }
                file.write((line + "\n").getBytes());
            }
//            file.write((" \n").getBytes());
        } catch (IOException e) {
            System.out.println("Error saving board state");
        }
    }

    public void updateBoardState(BufferedReader reader){
        try {
            String mLine;
            //Leemos filas y columnas
            mLine = reader.readLine();
            int cols_ = Integer.parseInt(mLine);
            mLine = reader.readLine();
            int rows_ = Integer.parseInt(mLine);

            assert cols_ == cols;
            assert rows_ == rows;

            //Por cada character de la fila String, asignamos filas de la columna
            int i = 0;
            int k = 0;
            while (k < rows_ && (mLine = reader.readLine()) != null) {
                for(int j = 0; j < mLine.length(); j++){
                    if(mLine.charAt(j) == '.'){
                        board[j][i] = TILE.FILL;
                        numCorrectTiles++;
                    }
                    else if(mLine.charAt(j) == 'x'){
                        board[j][i] = TILE.CROSS;
                    }
                    else board[j][i] = TILE.EMPTY;
                }
                i++;
                k++;
            }
        } catch (IOException e) {
            System.out.println("Error updating Board cells");
        }
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

        assignColors();

        calcAdjyacents();
    }

    void calcAdjyacents(){
        adyancentsHorizontal = new ArrayList<>();
        adyancentsVertical = new ArrayList<>();
        //Calculamos los numeros adyacentes horizontal
        maxHorizontalFilled = 0;
        maxVerticalFilled = 0;
        for (int i = 0; i < rows; i++) {
            List<Pair<Integer,Integer>> adyacents = new ArrayList<>();
            int juntas = 0;
            for (int j = 0; j < cols; j++) {
                if (board[j][i] == TILE.FILL) {
                    juntas++;

                    if (j + 1 == cols)
                        adyacents.add(new Pair<Integer, Integer>(juntas, this.colors[j][i]));
                } else if (juntas != 0) {
                    adyacents.add(new Pair<Integer, Integer>(juntas, this.colors[j][i]));
                    juntas = 0;
                }
            }

            if (adyacents.size() == 0) adyacents.add(new Pair<Integer, Integer>(0,-1));
            else if (adyacents.size() > maxHorizontalFilled) {
                maxHorizontalFilled = adyacents.size();
            }
            adyancentsHorizontal.add(adyacents);
        }

        //vertical
        for (int j = 0; j < cols; j++) {
            List<Pair<Integer,Integer>> adyacents = new ArrayList<>();
            int juntas = 0;
            for (int i = 0; i < rows; i++) {
                if (board[j][i] == TILE.FILL) {
                    juntas++;

                    if (i + 1 == rows)
                        adyacents.add(new Pair<Integer, Integer>(juntas, this.colors[j][i]));
                } else if (juntas != 0) {
                    adyacents.add(new Pair<Integer, Integer>(juntas, this.colors[j][i]));
                    juntas = 0;
                }
            }
            if (adyacents.size() == 0) adyacents.add(new Pair<Integer, Integer>(0,-1));
            else if (adyacents.size() > maxVerticalFilled) {
                maxVerticalFilled = adyacents.size();
            }
            adyancentsVertical.add(adyacents);
        }
    }

    public void setTileSize(int tileSize_){
        this.tileSize = tileSize_;
    }

    public void setSize(int sizeX_, int sizeY_){
        float relationRowCol = this.rows /(float) this.cols;

        width = sizeX_;
        height = (int)(sizeY_*relationRowCol);

        relationX = sizeX_ /(float) this.cols;
        relationY = (sizeY_ /(float) this.rows) * relationRowCol;
    }

    public CustomPair<Float, Float> getRelationFactorSize(){
        return new CustomPair<>(relationX, relationY);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXInfoRect() {return posX - alphaX; }
    public int getYInfoRect() {return posY - alphaY; }

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

    public void setTile(int x, int y, TILE tile, Integer color) {
        board[x][y] = tile;
        colors[x][y] = color;
    }

    public TILE[][] getBoard() {
        return board;
    }

    public Integer[][] getBoardColors() {
        return colors;
    }

    // Comprueba si los tableros coinciden
    public ArrayList<CustomPair<Integer, Integer>> isBoardMatched(Board other) {
        TILE[][] otherBoard = other.getBoard();
        Integer[][] otherBoardColors = other.getBoardColors();
        ArrayList<CustomPair<Integer, Integer>> diff = new ArrayList<>();
        int tilesMatched = 0;
        //Assume they are the same size (width and height)
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if(otherBoard[i][j] == TILE.CROSS || otherBoard[i][j] == TILE.EMPTY)
                    continue;
                if (board[i][j] != otherBoard[i][j] && colors[i][j] != otherBoardColors[i][j] )
                    diff.add(new CustomPair<>(i, j));
                else
                    tilesMatched++;

            }
        }

        //Metemos al final el nº de tiles que coinciden
        diff.add(new CustomPair<>(tilesMatched, -1));

        return diff;
    }

    private void drawNumRect(Graphics g) {

        // dibujar cuadro lateral
        g.drawLine(alphaX, posY + (int)relationY*rows, posX, posY + (int)relationY*rows);
        g.drawLine(alphaX, posY, posX, posY);
        g.drawLine(alphaX, posY, alphaX, posY + (int)relationY*rows);
        //Dibujar cuadro superior
        g.drawLine(posX, alphaY, posX, posY);
        g.drawLine(posX + (int)relationX*cols, alphaY, posX + (int)relationX*cols, posY);
        g.drawLine(posX, alphaY, posX + (int)relationX*cols, alphaY);
    }

    private void drawNums(Graphics g, int fontSize) {
        int i, j;
        //Dibujar numero de correctos Horizontal
        for (i = 0; i < adyancentsHorizontal.size(); i++) {
            int size = adyancentsHorizontal.get(i).size();
            for (j = size - 1; j >= 0; j--) {

                Pair<Integer, Integer> cell = adyancentsHorizontal.get(i).get(j);
                Integer num = cell.first;
                if (num != 0){

                    g.setColor(cell.second, 1.0f);
                    g.drawText(num.toString(),
                            posX - ((size-1 - j) * (fontSize+fontSize/2)) - fontSize,
                            (int) (i * (int)relationY) + posY + (int)((int)relationY/1.3));
                }
            }
        }

        //Vertical
        for (i = 0; i < adyancentsVertical.size(); i++) {
            int size = adyancentsVertical.get(i).size();
            for (j = size - 1; j >= 0; j--) {
                Pair<Integer, Integer> cell = adyancentsVertical.get(i).get(j);
                Integer num = cell.first;
                if (num != 0){

                    g.setColor(cell.second, 1.0f);
                    g.drawText(num.toString(),
                            (int) (i * (int)relationX) + posX + ((int)relationX/2),
                            // se suma fontsize/2 porque el punto inicial del texto es la esquina inferior izquierda
                            posY - ((size-1-j) * (fontSize+fontSize/2)) - fontSize/2);
                }
            }
        }
    }

    public void drawBoard(Graphics g, int x, int y, boolean win, int pal) {
        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if (x != posX) posX = x;
        if (y != posY) posY = y;

        //Dibujar cada tile teniendo en cuenta el tamanyo total del tablero
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Image im = tileImage(g, board[i][j], colors[i][j], pal);
                assert im != null;

                if(board[i][j] == TILE.FILL && !hasChanged_)
                    hasChanged_ = true;

                if(!win || board[i][j] == TILE.FILL)
                    g.drawImage(im, i * (int)(relationX) + x, j * (int)(relationY) + y ,
                        relationX / tileSize, relationY / tileSize);
            }
        }
    }

    public void drawInfoRects(Graphics g, int x, int y, Font font) {
        g.setFont(font);
        g.setColor(ColorWrap.BLACK, 1.0f);
        int fontSize = font.getSize();

        //Recalculamos X para que el tablero junto al cuadro numerico esten centrados en X
        int boardMiddle = (width - x)/2;
        alphaX = x - maxHorizontalFilled * (fontSize+fontSize/2);
        int newMiddle = (width - alphaX)/2;
        x += newMiddle- boardMiddle;

        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if (x != posX) posX = x;
        if (y != posY) posY = y;

        //Posicion inicial de cuadro numerico
        alphaX = x - maxHorizontalFilled * (fontSize+fontSize/2);
        alphaY = y - maxVerticalFilled * (fontSize+fontSize/2);

        drawNumRect(g);

        drawNums(g, fontSize);
    }

    public CustomPair<Integer,Integer> calculcateIndexMatrix(int pixelX, int pixelY) {
        // Comprueba si esta dentro del tablero
        if ((pixelX > posX && pixelX <= posX + width) && (pixelY > posY && pixelY <= posY + height)) {
            // Localiza posicion y selecciona el tile
            int i_index = (int) ((pixelX - posX) / (relationX));
            int j_index = (int) ((pixelY - posY) / (relationY));

            return new CustomPair<>(i_index, j_index);
        }

        return new CustomPair<>(-1, -1);
    }


    //Coge image dependiendo del tile
    private Image tileImage(Graphics g, TILE t, Integer Color, int pal) {
        switch (t) {
            case FILL:
                if(Objects.equals(Color, ColorWrap.BLACK))
                    return g.getImage("fillBLACK");
                else if(Objects.equals(Color, ColorWrap.RED))
                    return g.getImage("fillRED");
                else if(Objects.equals(Color, ColorWrap.BLUE))
                    return g.getImage("fillBLUE");
                else if(Objects.equals(Color, ColorWrap.GRAY))
                    return g.getImage("fillGRAY");

//                return g.getImage("fill" + pal);
            case CROSS:
                return g.getImage("cross"+ pal);
            case EMPTY:
                return g.getImage("empty"+ pal);
            case WRONG:
                return g.getImage("wrong"+ pal);
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


    public boolean hasChanged() {
        return  hasChanged_;
    }
}
