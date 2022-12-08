package com.gamelogic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.engineandroid.Engine;
import com.launcher.MainActivity;

import java.util.HashMap;

public class GameManager {
    private static GameManager instance_;
    private final int maxLevels = 4;
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

    public int setMoney(int m){
        return money = m;
    }

    // Genera el intent para Twitter.
    // Primero comprueba si tienes Twitter instalado
    // Luego abre la aplicación o te abre el navegador con el tuit preparado
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

    public void buildNotification(String titleText, String contentText){
        Intent intent = new Intent(engine.getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(engine.getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(engine.getContext(), "ExampleID")
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle(titleText)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(engine.getContext());
        managerCompat.notify(1, builder.build());
    }
}
