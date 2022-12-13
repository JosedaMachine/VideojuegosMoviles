package com.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.engineandroid.*;
import com.engineandroid.Engine;
import com.gamelogic.AdManager;
import com.gamelogic.Nonograma;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.nonograma.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Engine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FullScreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Init Notification
        createNotificationChannel();
        //TODO llamar esto al cerrar la app
        createAlarm();

        //Init adds
        AdManager.init(this);
        AdManager.instance().initializeAds();
        AdManager.instance().buildBannerAd();

        //Init renderer
        SurfaceView renderView = findViewById(R.id.surfaceView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = 600;
        int height = 900;

        ColorWrap.Init();
        //Init Engine
        this.engine = new Engine(renderView, width, height);
        //Init Game
        IGame game = new Nonograma(engine);

        //Set Game and play
        engine.resume();
        engine.setGame(game);

        detectIntent(getIntent());
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
        this.engine.pause();
    }

    //Voltear movil
    @Override
    public void onConfigurationChanged(Configuration myConfig) {
        super.onConfigurationChanged(myConfig);
        int orient = getResources().getConfiguration().orientation;
        switch(orient) {
            case Configuration.ORIENTATION_LANDSCAPE:
                System.out.println("Vertical");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                System.out.println("Horizontal");
                break;
            default:
        }
    }

    private void detectIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null ) {
            engine.sendMessage(extras);
        }else
            Log.d("MONEY", "EXTRAS NULL");
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

        //TODO cambiar el tiempo a 1 semana en milisegundos (ahora está a 30000 que son 30 seg)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+30000, pendingIntent);
    }

}