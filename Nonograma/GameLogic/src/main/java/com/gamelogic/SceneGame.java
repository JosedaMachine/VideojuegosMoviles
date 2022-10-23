package com.gamelogic;

import static com.engine.TouchEvent.ButtonID.LEFT_BUTTON;
import static com.engine.TouchEvent.ButtonID.RIGHT_BUTTON;
import static com.engine.TouchEvent.TouchEventType.TOUCH_EVENT;

import com.engine.Engine;
import com.engine.IGraphics;
import com.engine.Image;
import com.engine.SceneBase;
import com.engine.TouchEvent;

import java.awt.event.MouseEvent;

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
    public void input(TouchEvent event_) {
        if(event_.getType_() == TOUCH_EVENT ){
            if(event_.getID_() == LEFT_BUTTON){
                System.out.println("Izq " + "X: " +  event_.getX_()+  " Y: " + event_.getY_());
            }else if(event_.getID_() == RIGHT_BUTTON){
                System.out.println("Der " + "X: " +  event_.getX_()+  " Y: " + event_.getY_());
            }
        }
    }

    @Override
    public void init() {
        int x = 10, y = 10;
        checkBoard = new Board(x, y);
        checkBoard.generateBoard();

        gameBoard = new Board(x, y);
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

        System.out.println("Resources Loaded");
    }

    @Override
    public void render(IGraphics graphics) {
        //gameBoard.render(engine);
        checkBoard.render(engine);

        //Image newim = graphics.getImage("cross");

        //graphics.drawImage(newim, 7, 30,newim.getWidth()*2, newim.getHeight()*2);
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