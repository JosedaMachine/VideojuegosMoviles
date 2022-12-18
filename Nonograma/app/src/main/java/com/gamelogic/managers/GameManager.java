package com.gamelogic.managers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.engineandroid.Engine;
import com.engineandroid.Font;
import com.engineandroid.Pair;
import com.engineandroid.TouchEvent;
import com.engineandroid.UserInterface;
import com.gamelogic.enums.CATEGORY;
import com.gamelogic.enums.PALETTE;
import com.gamelogic.utils.ImageElement;
import com.gamelogic.utils.TextElement;

import java.io.FileOutputStream;
import java.util.HashMap;

public class GameManager {
    private static GameManager instance_;
    private final int maxLevels = 20;
    private final int maxMoney = 10000;
    private Engine engine;

    public GameManager(Engine engine){
        this.engine = engine;
    }

    //Indices de ultimo nivel bloqueado por categoria
    private HashMap<CATEGORY, Integer> categoryLevelIndexes = new HashMap<>();
    private int money = 0;

    private PALETTE currentPalette = PALETTE.DEFAULT;
    //Paletas desbloqueadas
    private HashMap<PALETTE, Pair<Boolean, Integer>>  unlockedPalettes = new HashMap<>();

    public static GameManager instance(){
        return  instance_;
    }

    public static boolean init(Engine engine){
        instance_ = new GameManager(engine);
        return true;
    }

    static boolean release(){
        return true;
    }

    public int getMaxLevel(){
        return maxLevels;
    }
    public int getLevelIndex(CATEGORY category){
        return categoryLevelIndexes.get(category);
    }
    public void setLevelIndex(CATEGORY category, int index){
        categoryLevelIndexes.put(category, index);
    }

    public int getMoney(){
        return money;
    }

    public String getTextMoney() {
        if(money < 10)
            return "000" + money;
        if(money < 100)
            return "00" + money;
        if(money < 1000)
            return "0" + money;

        return "" + money;
    }

    public void addMoney(int m) {
        Log.d("MONEY", ""+m);
        money += m;
        money = Math.min(maxMoney, money);
    }

    public void updateInterface(){
        UserInterface uinterface = engine.getGame().getUserInterface();
        if(uinterface.getElement(0) != null){
            TextElement ui = (TextElement) engine.getGame().getUserInterface().getElement(0);
            ui.setText(getTextMoney());
        }else{
            Font title = engine.getGraphics().newFont("arcade.TTF",(int)(engine.getGraphics().getLogicHeight() * 0.088f),true);
            int logicWidth = engine.getGraphics().getLogicWidth();
            uinterface.clearElements();
            uinterface.addElement(new TextElement(title, logicWidth - 170, 50, 5, 5, GameManager.instance().getTextMoney()) {
                @Override
                public void update(double deltaTime) {}
                @Override
                public void input(TouchEvent event_) {}
            });

            uinterface.addElement(new ImageElement(engine.getGraphics().getImage("coin"), logicWidth - 65, 25, 55, 55) {
                @Override
                public void update(double deltaTime) {}
                @Override
                public void input(TouchEvent event_) {}
            });
        }
    }
    // Genera el intent para Twitter.
    // Primero comprueba si tienes Twitter instalado
    // Luego abre la aplicación o te abre el navegador con el tuit preparado
    //TODO si vemos que tal mover a SceneVictory o yo qué sé
    public Intent getTwitterIntent(String shareText)
    {
        Intent shareIntent;

        if(doesPackageExist("com.twitter.android"))
        {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        else
        {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + shareText;
            Uri uri = Uri.parse(tweetUrl);
            shareIntent = new Intent(Intent.ACTION_VIEW, uri);
        }
        return shareIntent;
    }

    private boolean doesPackageExist(String targetPackage){
        PackageManager pm= engine.getContext().getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public PALETTE getPalette(){
        return currentPalette;
    }
    public void setPalette(int pal){
        currentPalette = PALETTE.values()[pal];
    }

    public void setPaletteUnlocked(int pal, boolean lock, int price){
        unlockedPalettes.put(PALETTE.values()[pal], new Pair<>(lock, price));
    }
    public Pair<Boolean, Integer> getPaletteUnlocked(int pal){
        return unlockedPalettes.get(PALETTE.values()[pal]);
    }

    public void save(FileOutputStream file, SharedPreferences mPreferences){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        //Money
        preferencesEditor.putInt("money", money);

        //Levels Unlocked
        preferencesEditor.putInt("kitchen", getLevelIndex(CATEGORY.KITCHEN));
        preferencesEditor.putInt("medieval", getLevelIndex(CATEGORY.MEDIEVAL));
        preferencesEditor.putInt("ocean", getLevelIndex(CATEGORY.OCEAN));
        preferencesEditor.putInt("animal", getLevelIndex(CATEGORY.ANIMAL));

        //Palettes Unlocked and Selected
        preferencesEditor.putInt("paletteSelected", currentPalette.ordinal());
        String palettesUnlocked = "";
        for (int i = 0; i <3; i++)
            palettesUnlocked =  palettesUnlocked + (getPaletteUnlocked(i).first ? '1' : '0' );
        preferencesEditor.putString("palettesUnlocked", palettesUnlocked);

//        preferencesEditor.putBoolean("savingBoard", true);
        preferencesEditor.apply(); //también podemos usar .commit()
    }

    public void restore(SharedPreferences mPreferences){
        //Money
        int coins = mPreferences.getInt("money", 0);
        addMoney(coins);

        //Levels Unlocked
        int kitchenIndex = mPreferences.getInt("kitchen", 20);
        int medievalIndex =mPreferences.getInt("medieval", 20);
        int oceanIndex = mPreferences.getInt("ocean", 20);
        int animalIndex = mPreferences.getInt("animal", 0);

        GameManager.instance().setLevelIndex(CATEGORY.KITCHEN, kitchenIndex);
        GameManager.instance().setLevelIndex(CATEGORY.MEDIEVAL, medievalIndex);
        GameManager.instance().setLevelIndex(CATEGORY.OCEAN, oceanIndex);
        GameManager.instance().setLevelIndex(CATEGORY.ANIMAL, animalIndex);

        //Palettes Unlocked and Selected
        int palette = mPreferences.getInt("paletteSelected", 0);
        setPalette(palette);
        String palettesUnlocked = mPreferences.getString("palettesUnlocked", "100");
        GameManager.instance().setPaletteUnlocked(0, palettesUnlocked.charAt(0) == '1', 0);
        GameManager.instance().setPaletteUnlocked(1, palettesUnlocked.charAt(1) == '1', 40);
        GameManager.instance().setPaletteUnlocked(2, palettesUnlocked.charAt(2) == '1', 40);
    }
}
