package com.gamelogic;

import com.engine.Audio;
import com.engine.Engine;
import com.engine.IColor;
import com.engine.IFont;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private TILE[][] board;
    private final int rows;
    private final int cols;
    private final int width, height;
    private int maxHorizontalFilled, maxVerticalFilled;
    private final float relationX, relationY;
    private int posX, posY;

    private int numCorrectTiles;
    private List<List<Integer>> adyancentsHorizontal;
    private List<List<Integer>> adyancentsVertical;


    Board(int x, int y, int sizeX_, int sizeY_) {
        board = new TILE[x][y];
        rows = y;
        cols = x;

        //Relacion para cuando hay menos filas que columnas
        float relationRowCol = rows/(float)cols;

        width = sizeX_;
        height = (int)(sizeY_*relationRowCol);

        relationX = sizeX_ /(float) cols;
        relationY = (sizeY_ /(float) rows) * relationRowCol;

        //Tablero incialmente vacio
        for (int i = 0; i < cols; i++)
            for (int j = 0; j < rows; j++)
                board[i][j] = TILE.EMPTY;

        numCorrectTiles = 0;
    }

    void generateBoard() {
        adyancentsHorizontal = new ArrayList<>();
        adyancentsVertical = new ArrayList<>();

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

    private void drawNumRect(IGraphics g, int x, int y, int alphaX, int alphaY) {

        // dibujar cuadro lateral
        g.drawLine(alphaX, y + height - 1, x, y + height - 1);
        g.drawLine(alphaX, y, x, y);
        g.drawLine(alphaX, y, alphaX, y + height - 1);
        //Dibujar cuadro superior
        g.drawLine(x, alphaY, x, y);
        g.drawLine(x + width - 1, alphaY, x + width - 1, y);
        g.drawLine(x, alphaY, x + width - 1, alphaY);
    }

    private void drawNums(IGraphics g, int x, int y, int fontSize) {
        int i, j;
        //Dibujar numero de correctos Horizontal
        for (i = 0; i < adyancentsHorizontal.size(); i++) {
            int size = adyancentsHorizontal.get(i).size();
            for (j = size - 1; j >= 0; j--) {
                Integer num = adyancentsHorizontal.get(i).get(j);
                if (num != 0)
                    g.drawText(num.toString(),
                            x - ((size-1 - j) * (fontSize+fontSize/2)) - fontSize,
                            (int) (i * relationY) + y + (int)(relationY/1.3));
            }
        }

        //Vertical
        for (i = 0; i < adyancentsVertical.size(); i++) {
            int size = adyancentsVertical.get(i).size();
            for (j = size - 1; j >= 0; j--) {
                Integer num = adyancentsVertical.get(i).get(j);
                if (num != 0)
                    g.drawText(num.toString(),
                            (int) (i * relationX) + x + (int)(relationX/2),
                            // se suma fontsize/2 porque el punto inicial del texto es la esquina inferior izquierda
                            y - ((size-1-j) * (fontSize+fontSize/2)) - fontSize/2);
            }
        }
    }

    public void drawBoard(IGraphics g, int x, int y, boolean win) {
        //En caso de que se mueva el tablero o reescalado o algo por el estilo
        if (x != posX) posX = x;
        if (y != posY) posY = y;


        //Dibujar cada tile teniendo en cuenta el tamanyo total del tablero
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Image im = tileImage(e, board[i][j]);

                if(im == null) continue;

                if(!win || board[i][j] == TILE.FILL)
                    g.drawImage(im, (int) (i * relationX) + x, (int) (j * relationY) + y,
                        relationX / im.getWidth(), (relationY) / im.getHeight());
            }
        }
    }

    public void drawInfoRects(IGraphics g, int x, int y, IFont font) {
        g.setFont(font);
        g.setColor(IColor.BLACK, 1.0f);
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

        drawNumRect(g, x, y, alphaX, alphaY);

        drawNums(g, x, y, fontSize);
    }

    Pair<Integer,Integer> calculcateIndexMatrix(Audio audio, int pixelX, int pixelY) {
        // Comprueba si esta dentro del tablero
        if ((pixelX > posX && pixelX <= posX + width) && (pixelY > posY && pixelY <= posY + height)) {
            // Localiza posicion y selecciona el tile
            int i_index = (int) ((pixelX - posX) / (relationX));
            int j_index = (int) ((pixelY - posY) / (relationY));

            audio.playSound("click.wav");
            return new Pair<>(i_index, j_index);
        }

        return new Pair<>(-1, -1);
    }

    //Coge image dependiendo del tile
    private Image tileImage(IGraphics g, TILE t) {
        switch (t) {
            case FILL:
                return g.getImage("fill");
            case CROSS:
                return g.getImage("cross");
            case EMPTY:
                return g.getImage("empty");
            case WRONG:
                return g.getImage("wrong");
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
}
