package com.gamelogic.scenes;

import android.content.SharedPreferences;

import com.engineandroid.ConstraintX;
import com.engineandroid.ConstraintY;
import com.engineandroid.Engine;
import com.engineandroid.ColorWrap;
import com.engineandroid.Font;
import com.engineandroid.Graphics;
import com.engineandroid.Image;
import com.engineandroid.Message;
import com.engineandroid.Pair;
import com.engineandroid.SceneBase;
import com.engineandroid.TouchEvent;
import com.gamelogic.Board;
import com.gamelogic.utils.Button;
import com.gamelogic.enums.CATEGORY;
import com.gamelogic.utils.Fade;
import com.gamelogic.managers.GameManager;
import com.gamelogic.enums.TILE;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

//////////////////////////////// SCENE GAME //////////////////////////////////
public class SceneGame implements SceneBase {

    public class TileTouched {
        public int x, y;
        public boolean touched;

        public TileTouched() {
            x = -1;
            y = -1;
            touched = false;
        }
    }

    //region Variables y Constructora
    private final Engine engine;
    //Tablero chuleta para comprobar
    private Board checkBoard;
    //Tablero que ve el jugador
    private Board gameBoard;

    private Button bttCheckWin, bttReturn;
    //True cuando coincidan los tableros
    private boolean hasWon = false;
    //True cuando ocurra un movimiento y haya que comprobar
    private boolean checkWin = false;
    private boolean isHoldingPress = false;
    //Vidas
    private final int maxLives = 3;
    private int lives = maxLives;


    SharedPreferences sharedPreferences;
    //Filas y columnas del tablero
    private int rows_;
    private int cols_;

    private int tileSize, boardSize;

    private int numRemaining = 0, numWrong = 0;
    //Fuentes
    private Font numFont, pixelFont;

    private Fade fade;

    private static final double maxTime = 2.5; //Segundos para texto incorrecto
    private double timer = maxTime;
    private boolean DEBUG = false;

    private String levelName = null;

    private CATEGORY category = null;
    private int lvlIndex = 0;
    private int reward;
    private boolean boardHasChanged;

    TileTouched tileTouchedInfo_ = null;

    String[] KitchenLevels = {"fork", "spoon", "knife", "plate",
            "pan", "pot", "oven", "table",
            "salt", "napkin", "pizza", "sandwich",
            "chair", "chefhat", "chilli", "glass",
            "bottle", "candle", "chopsticks", "spatula"};

    String[] MedievalLevels = {"bow", "sword", "cat", "crossbow",
            "helmet", "arrow", "cross", "castle",
            "banner", "crossingswords", "crown", "beer",
            "dragon", "law", "flail", "gallows",
            "chestplate", "flag", "shield", "wineglass"};

    String[] OceanLevels = {"fish", "horizon", "fishnet", "hook",
            "crab", "fishingrod", "helm", "figurehead",
            "sailboat", "eyepatch", "algae", "barrel",
            "chest", "coin", "skull", "jellyfish",
            "shark", "rope", "anchor", "shell"};

    String[] AnimalLevels = {"bunny", "duck", "giraffe", "elephant",
            "bear", "mouse", "snail", "flamingo",
            "snake", "deer", "dolphin", "duck2",
            "giraffe2", "mouse2", "pelican", "dinosaur",
            "parrot", "squid", "owl", "deer2"};

    public SceneGame(Engine engine, int rows, int cols, int reward) {
        this.engine = engine;
        rows_ = rows;
        cols_ = cols;
        this.reward = reward;
    }

    public SceneGame(Engine engine, int rows, int cols, CATEGORY cat, int index) {
        this.engine = engine;

        //Story mode reward fijo
        this.reward = 10;
        rows_ = rows;
        cols_ = cols;
        category = cat;
        lvlIndex = index;

        if (cat == CATEGORY.KITCHEN)
            levelName = "kitchen/" + KitchenLevels[index];
        else if (cat == CATEGORY.MEDIEVAL)
            levelName = "medieval/" + MedievalLevels[index];
        else if (cat == CATEGORY.OCEAN)
            levelName = "ocean/" + OceanLevels[index];
        else levelName = "animal/" + AnimalLevels[index];
    }

    //endregion + Construct y Con

    //region Override methods

    @Override
    public void update(double deltaTime) {
        //Comprueba la victoria
        if (checkWin) {
            hasWon = checkHasWon();
            checkWin = false;
            if (!hasWon) {
                //Inicia el timer de derrota
                timer = 0;
                engine.getAudio().playSound("wrong.wav");

                //Si nivel historia y vidas == 0
                if (!subtractLife()) {
                    engine.getGame().pushScene(new SceneDefeat(engine));
                }
            }
        }

        //Avanza timer
        if (timer < maxTime) {
            timer += deltaTime;
            if (timer >= maxTime)
                //Limpiar casillas rojas
                gameBoard.clearWrongsTiles();
        }

        //Victoria
        if (hasWon) {
            engine.getAudio().playSound("correct.wav");

            //Si modo historia
            if (category != null) {
                final int lastLvlIndex = GameManager.instance().getLevelIndex(category);
                //Si es el ultimo nivel desbloqueado -> desbloquea siguiente
                if (lastLvlIndex == lvlIndex)
                    GameManager.instance().setLevelIndex(category, lvlIndex + 1);
            }

            engine.getGame().pushScene(new SceneVictory(engine, checkBoard, reward));
        }
        fade.update(deltaTime);
        bttReturn.update(deltaTime);
    }

    @Override
    public void input(TouchEvent event_) {
        bttCheckWin.input(event_);
        bttReturn.input(event_);

        if (event_.getType_() == TouchEvent.TouchEventType.TOUCH_EVENT) {
            isHoldingPress = false;
            tileTouchedInfo_.touched = false;
        } else if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
            isHoldingPress = true;
            //Input en casillas del tablero
            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(event_.getX_(), event_.getY_());

            if (index.first != -1 && index.second != -1) {
                setTile(index.first, index.second, false, false);
                engine.getAudio().playSound("click.wav");
            }
            //Debug para mostrar el resultado
            if (event_.getID_() == TouchEvent.ButtonID.MIDDLE_BUTTON) {
                DEBUG = !DEBUG;
            }
            isHoldingPress = false;
            tileTouchedInfo_.touched = false;
        } else if (event_.getType_() == TouchEvent.TouchEventType.LONG_EVENT) {
            //Input en casillas del tablero
            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(event_.getX_(), event_.getY_());

            if (index.first != -1 && index.second != -1) {
                setTile(index.first, index.second, false, true);
                engine.getAudio().playSound("click.wav");
            }
            isHoldingPress = false;
            tileTouchedInfo_.touched = false;
        } else if (isHoldingPress && event_.getType_() == TouchEvent.TouchEventType.MOVE_EVENT) {
            //Input en casillas del tablero
            Pair<Integer, Integer> index = gameBoard.calculcateIndexMatrix(event_.getX_(), event_.getY_());

            if (index.first != -1 && index.second != -1)
                setTile(index.first, index.second, false, false);
        }
    }

    @Override
    public void init() {
        tileTouchedInfo_ = new TileTouched();
        loadResources(engine.getGraphics());
        int logicWidth = engine.getGraphics().getLogicWidth();
        int logicHeight = engine.getGraphics().getLogicHeight();

        //Fade In
        fade = new Fade(engine,
                ConstraintX.LEFT, ConstraintY.TOP,
                ConstraintX.RIGHT, ConstraintY.BOTTOM,
                500, 500, Fade.STATE_FADE.In);
        fade.setColor(ColorWrap.BLACK);
        fade.triggerFade();

        boardSize = (int) (logicWidth * 0.6f);

        if (levelName != null) {
            createLevel(levelName, boardSize);
        } else createLevel(boardSize);

        boardHasChanged = gameBoard.hasChanged();

        //relación respecto a numero de casillas
        Pair<Float, Float> relations = gameBoard.getRelationFactorSize();

        float size = (float) (Math.floor(relations.first * 0.7) / 1000.0f);
        pixelFont = engine.getGraphics().newFont("upheavtt.ttf", (int) (logicHeight * size), false);

        //Tamaño de los botones
        int offset = (int) (logicWidth * 0.16f),
                bttWidth = (int) (logicWidth * 0.25f),
                bttHeight = (int) (logicWidth * 0.0833f);

        //Boton Check Win
        bttCheckWin = new Button("Check", logicWidth / 2 - bttWidth / 2 + offset,
                logicHeight - bttHeight * 3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (isInside(engine.getGraphics(), event_.getX_(), event_.getY_())) {
                        checkWin = true;
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
            }
        };
        bttCheckWin.setFont(numFont);
        bttCheckWin.setColor(ColorWrap.BLACK);
        bttCheckWin.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));

        //Boton Return to menu
        bttReturn = new Button("Coward", logicWidth / 2 - bttWidth / 2 - offset,
                logicHeight - bttHeight * 3, bttWidth, bttHeight) {
            @Override
            public void input(TouchEvent event_) {
                if (event_.getType_() == TouchEvent.TouchEventType.RELEASE_EVENT) {
                    if (isInside(engine.getGraphics(),event_.getX_(), event_.getY_())) {
                        engine.getAudio().playSound("click.wav");

                        if (fade.getState() != Fade.STATE_FADE.Out) {
                            fade.setState(Fade.STATE_FADE.Out);
                            fade.triggerFade();
                        }
                    }
                }
            }

            @Override
            public void update(double deltaTime) {
                if (fade.getFadeOutComplete()) {
                    engine.getGame().previousScene();
//                    engine.getGame().changeScene("SceneLevels");
//                    engine.getGame().pushScene(new SceneTitle(engine));
                }
            }
        };
        bttReturn.setFont(numFont);
        bttReturn.setColor(ColorWrap.BLACK);
        bttReturn.setBackgroundImage(engine.getGraphics().getImage("buttonbox"));
    }

    @Override
    public void loadResources(Graphics graphics) {
        System.out.println("Loading Resources...");

        int palette = GameManager.instance().getPalette().ordinal();
        //Carga de cuadrados con paletas/disenos
        graphics.newImage("emptysquare" + palette + ".png", "empty" + palette);
        graphics.newImage("crosssquare" + palette + ".png", "cross" + palette);
        graphics.newImage("wrongsquare" + palette + ".png", "wrong" + palette);
        graphics.newImage("fillsquare" + palette + ".png", "fill" + palette);
        tileSize = graphics.getImage("empty" + palette).getWidth();

        graphics.newImage("heart.png", "heart");
        graphics.newImage("emptyheart.png", "emptyheart");

        numFont = graphics.newFont("arcade.TTF", (int) (engine.getGraphics().getLogicHeight() * 0.04f), false);
        pixelFont = graphics.newFont("upheavtt.ttf", (int) (engine.getGraphics().getLogicHeight() * 0.1f), false);

        engine.getAudio().newSound("wrong.wav");
        engine.getAudio().newSound("correct.wav");

        System.out.println("Resources Loaded");
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void processMessage(Message msg) {

    }

    @Override
    public void orientationChanged(boolean isHorizontal) {
        //TODO acuerdate joseda que en el game falta el botón de ver un vídeo para recuperar una vida
    }

    @Override
    public void render(Graphics graphics) {
        int logicWidth = graphics.getLogicWidth();
        int logicHeight = graphics.getLogicHeight();
        int palette = GameManager.instance().getPalette().ordinal();

        //Tablero
        graphics.setColor(ColorWrap.BLACK, 1.0f);
        checkBoard.drawInfoRects(engine, logicWidth / 2 - gameBoard.getWidth() / 2, logicHeight / 2 - gameBoard.getHeight() / 2, pixelFont);
        gameBoard.drawBoard(engine, checkBoard.getPosX(), checkBoard.getPosY(), false, palette);

        //Botones
        bttCheckWin.render(graphics);
        bttReturn.render(graphics);

        if (DEBUG) {
            checkBoard.drawBoard(engine, checkBoard.getPosX(), checkBoard.getPosY(), false, palette);
        }

        //Corazones
        Image heart = graphics.getImage("heart"),
                emHeart = graphics.getImage("emptyheart");

        float heartScale = 0.1f;
        float xOffset = logicWidth * 0.005f + heart.getWidth() * heartScale,
                yOffset = logicHeight * 0.005f;

        for (int i = 0; i < maxLives; i++) {
            //TODO: igual no se debería multiplicar por la escala la pos x,y desde aqui y hacerlo desde el propio draw image
            graphics.drawImage((i < lives) ? heart : emHeart,
                    (int) (checkBoard.getPosX() + xOffset * i),
                    (int) (checkBoard.getPosY() + checkBoard.getHeight() + yOffset), heartScale, heartScale);
        }


        //Texto indicando casillas incorrectas
        if (!hasWon && timer < maxTime) {
            graphics.setFont(numFont);

            String remainingField = numRemaining + " remaining cells";
            String wrongField = numWrong + " wrong cells";

            Pair<Double, Double> dime_remaining = graphics.getStringDimensions(remainingField);
            Pair<Double, Double> dime_wrong = graphics.getStringDimensions(wrongField);

            graphics.setColor(ColorWrap.BLUE, 1.0f);
            graphics.drawText(remainingField, (int) (logicWidth / 2 - dime_remaining.first / 2), (int) (logicHeight * 0.05 + dime_remaining.second / 2));
            graphics.setColor(ColorWrap.RED, 1.0f);
            graphics.drawText(wrongField, (int) (logicWidth / 2 - dime_wrong.first / 2), (int) (logicHeight * 0.09 + dime_wrong.second / 2));

        }

        fade.render(graphics);
    }

    //endregion

    //region methods

    private void createLevel(String levelName, int boardSize) {
        BufferedReader reader_ = null;
        try {
            reader_ = engine.openAssetFile("levels/" + levelName + ".txt");
            checkBoard = new Board(reader_, boardSize, boardSize, tileSize);
        } catch (IOException e) {
            System.out.println("Error opening file");
            e.printStackTrace();
        } finally {
            if (reader_ != null) {
                try {
                    reader_.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        cols_ = checkBoard.getCols();
        rows_ = checkBoard.getRows();

        gameBoard = new Board(cols_, rows_, boardSize, boardSize, tileSize);
    }

    private void createLevel(int boardSize) {
        checkBoard = new Board(cols_, rows_, boardSize, boardSize, tileSize);
        checkBoard.generateBoard();
        //Tablero de juego
        gameBoard = new Board(cols_, rows_, boardSize, boardSize, tileSize);
    }

    //Establece la casilla dada por [x][y] al siguiente estado
    private void setTile(int x, int y, boolean wrong, boolean lTouch) {
        if (x < 0 || y < 0) return;

        //Si ha entrado a este metodo quiere decir que el jugador
        //ha interactuado con el tablero. Perdemos los datos guardados de cualquier tablero
        //que se haya guardado anteriormente
        if (!boardHasChanged) {
            boardHasChanged = true;
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putBoolean("savingBoard", false);
            preferencesEditor.apply();
        }


        if (wrong) {
            gameBoard.setTile(x, y, TILE.WRONG);
            return;
        }

        if (tileTouchedInfo_.x == x && tileTouchedInfo_.y == y && tileTouchedInfo_.touched)
            return;

        TILE tile = gameBoard.getTile(x, y);

        if (lTouch && tile == TILE.EMPTY)
            gameBoard.setTile(x, y, TILE.CROSS);
        else if (tile == TILE.EMPTY)
            gameBoard.setTile(x, y, TILE.FILL);
        else
            gameBoard.setTile(x, y, TILE.EMPTY);

        tileTouchedInfo_.x = x;
        tileTouchedInfo_.y = y;
        tileTouchedInfo_.touched = true;
    }

    private boolean subtractLife() {
        lives--;
        return lives > 0;
    }

    private boolean checkMaxLives() {
        return lives == maxLives;
    }

    //Anyadir al ver anuncio
    private void addLife() {
        if (lives < maxLives)
            lives++;
    }

    private boolean checkHasWon() {
        ArrayList<Pair<Integer, Integer>> wrongs = checkBoard.isBoardMatched(gameBoard);

        //NO recorremos hasta el final, el último es el nº de tiles
        for (int i = 0; i < wrongs.size() - 1; i++) {
            //Establecemos a wrong las casillas
            setTile(wrongs.get(i).first, wrongs.get(i).second, true, false);
        }

        //Numero de incorrectas y que faltan
        numRemaining = checkBoard.getNumCorrectTiles() - wrongs.get(wrongs.size() - 1).first;
        numWrong = wrongs.size() - 1;

        return wrongs.size() == 1 && //Que no haya incorrectas
                wrongs.get(wrongs.size() - 1).first == checkBoard.getNumCorrectTiles(); //Que haya todas las correctas
    }


    @Override
    public void save(FileOutputStream file, SharedPreferences mPreferences) {
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        //Detectar si hay cambios o si ha perdido vidas, de lo contrario no guardamos nada
        if (lives == 3 && !gameBoard.hasChanged()) {
            preferencesEditor.putBoolean("savingBoard", false);
            return;
        }

        preferencesEditor.putBoolean("savingBoard", true);
        //Vidas
        preferencesEditor.putInt("lives", lives);
        //Nivel en cuestion, si es Historia o partida rapida
        if (levelName != null) {
            int catN = category.ordinal();
            preferencesEditor.putString("levelCat", Integer.toString(catN) + Integer.toString(lvlIndex));
            preferencesEditor.putString("levelQuickSize", "-");
        }else{
            preferencesEditor.putString("levelCat", "-");
            String cols_ = Integer.toString(gameBoard.getCols());
            String rows_ = Integer.toString(gameBoard.getRows());
            preferencesEditor.putString("levelQuickSize", cols_ + "x" + rows_);
            checkBoard.saveBoardState(file);
        }

        gameBoard.saveBoardState(file);

        preferencesEditor.apply(); //también podemos usar .commit()
    }

    @Override
    public void restore(BufferedReader reader, SharedPreferences mPreferences) {
        sharedPreferences = mPreferences;
        //En caso de que se juegue por primera vez o no se haya guardado, no hacemos nada.
        boolean boardSaved = mPreferences.getBoolean("savingBoard", false);

        if(!boardSaved || reader == null)
            return;

        //De lo contrario recuperamos valores

        String levelCat = mPreferences.getString("levelCat", "-");
        String levelQuick = mPreferences.getString("levelQuickSize", "-");

        if(!Objects.equals(levelCat, "-") && levelName != null){

            int catN = Integer.parseInt(String.valueOf(levelCat.charAt(0)));
            int indexLvl = Integer.parseInt(levelCat.substring(1));
            //Comprobamos si estamos en el ultimo nivel que se guardó
            if ((catN == category.ordinal()) && indexLvl == lvlIndex) {
                lives = mPreferences.getInt("lives", 3);
                gameBoard.updateBoardState(reader);
            }
        } else if (!Objects.equals(levelQuick, "-")) {
            //buscarHasta que haya una x

            String[] size = levelQuick.split("x");

            int cols_ = Integer.parseInt(size[0]);
            int rows_ = Integer.parseInt(size[1]);

            if (gameBoard.getCols() == cols_ && gameBoard.getRows() == rows_) {
                lives = mPreferences.getInt("lives", 3);
                //TODO reward en quick???
                checkBoard = new Board(reader, boardSize, boardSize, tileSize);
                gameBoard.updateBoardState(reader);
            }
        }
    }

    @Override
    public void horizontalLayout(Graphics g, int logicWidth, int logicHeight) {

    }

    @Override
    public void verticalLayout(Graphics g, int logicWidth, int logicHeight) {

    }
    //endregion
}