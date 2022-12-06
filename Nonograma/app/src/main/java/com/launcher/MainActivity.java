package com.launcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.engineandroid.*;
import com.engineandroid.Engine;
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
    private AdView ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FullScreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //Init Notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Nonograma")
//                .setContentText("Dale a jugar WACHO")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //Init adds
        MobileAds.initialize(this, new OnInitializationCompleteListener(){

            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
//
        ad = findViewById(R.id.adView);
        if(ad != null){
            AdRequest adR = new AdRequest.Builder().build();
            ad.loadAd(adR);
        }

        //Init renderer
        SurfaceView renderView = findViewById(R.id.surfaceView);

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
}