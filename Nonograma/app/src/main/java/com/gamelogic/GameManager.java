package com.gamelogic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.engineandroid.Engine;
import com.launcher.MainActivity;

import java.util.HashMap;

public class GameManager {
    private static GameManager instance_;
    private final int maxLevels = 4;
    private final int maxMoney = 10000;
    private Engine engine;

    public GameManager(Engine engine){
        this.engine = engine;
    }

    private HashMap<Category, Integer> categoryLevelIndexes = new HashMap<>();
    private int money = 0;

    public static GameManager instance(){
        return  instance_;
    }

    static boolean init(Engine engine){
        instance_ = new GameManager(engine);
        return true;
    }

    static boolean release(){
        return true;
    }

    public int getMaxLevel(){
        return maxLevels;
    }
    public int getLevelIndex(Category category){
        return categoryLevelIndexes.get(category);
    }
    public void setLevelIndex(Category category, int index){
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

    public int setMoney(int m){
        return money = m;
    }

    public void addMoney(int m) {
        Log.d("MONEY", ""+m);
        money += m;
        money = Math.min(maxMoney, money);
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
            shareIntent.setClassName("com.twitter.android",
                    "com.twitter.android.PostActivity");
            shareIntent.setType("text/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
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
}
