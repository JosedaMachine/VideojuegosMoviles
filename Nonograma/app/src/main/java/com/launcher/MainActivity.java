package com.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.engineandroid.*;
import com.engineandroid.Engine;
import com.engineandroid.AdManager;
import com.gamelogic.Nonograma;
import com.google.android.gms.ads.AdView;
import com.nonograma.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    final int width = 600;
    final int height = 900;
    private Engine engine;
    private final String sharedPrefFile = "com.nonogramPreferences";

    //Game installed at
    // /data/data/com.nonograma
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createWindow();
        //Init Notification
        createNotificationChannel();

        //Init adds
        AdManager.init(this);
        AdManager.instance().initializeAds();
        AdManager.instance().buildBannerAd(findViewById(R.id.adView));
        Pair<Integer, Integer> dimAd = AdManager.instance().getBannerSize();

        //Init Engine & renderer
        SurfaceView renderView = findViewById(R.id.surfaceView);
        this.engine = new Engine(renderView, width, height, dimAd);

        //Reference to Admanager
        AdManager.instance().setEngine(engine);

        //Init Game
        SharedPreferences  mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        IGame game = new Nonograma(engine, mPreferences);

        //Set Game and play
        engine.resume();
        engine.setGame(game);

        detectIntent(getIntent());
    }

    private void createWindow(){
        //FullScreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public void finish() {
        super.finish();

        System.out.println("Stop");
    }

    //Reanudar
    @Override
    protected void onResume() {
        super.onResume();
        this.engine.resume();
    }

    //Pausa
    @Override
    protected void onPause() {
        super.onPause();
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        //preferencesEditor.putInt("count", mCount);
        //preferencesEditor.putBoolean("playing", True);
        //preferencesEditor.apply(); //también podemos usar .commit()
        this.engine.pause();

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("Saving...");
        engine.save();
    }


    //Somehow this is not called
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
        System.out.println("Restoring...");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stop");
    }

    @Override
    protected void onDestroy() {
        System.out.println("Destroy");
        createAlarm();
        super.onDestroy();
    }

    //Voltear movil
    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                System.out.println("Horizontal");
                this.engine.orientationChanged(true);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                System.out.println("Vertical");
                this.engine.orientationChanged(false);
                break;
            default:
        }
    }

    private void detectIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null ) {
            if(extras.containsKey("RewardNotification")){
                Message msg = new Message(MESSAGE_TYPE.REWARD_NOTIFICATION);
                msg.reward = extras.getInt("RewardNotification");
                engine.sendMessage(msg);
            }
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ExampleID", "ExampleID", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void createAlarm(){
        //Crea la alarma que posteriormente creará la notificación
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Alarm", "any");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        //Elimina una alarma previamente puesta
        alarmManager.cancel(pendingIntent);

        //El tiempo debería ser 1 semana en milisegundos (ahora está a 30000 que son 30 seg, para mostrar su funcionamiento)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+30000, pendingIntent);
    }
}