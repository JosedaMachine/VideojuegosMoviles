package com.nonograma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.engineandroid.*;
import com.engine.*;
import com.gamelogic.Nonograma;

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

        //Init renderer
        SurfaceView renderView = new SurfaceView(this);
        setContentView(renderView);

        ColorAndroid.Init();
        //Init Engine
        this.engine = new EngineAndroid(renderView);
        //Init Game
        IGame game = new Nonograma(engine);

        //Set Game and play
        engine.resume();
        engine.setGame(game);
        //Tambien se podrian cargar los recursos dentro del setscene


    }

    @Override
    protected void onResume() {
        super.onResume();
        this.engine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.engine.pause();
    }
}