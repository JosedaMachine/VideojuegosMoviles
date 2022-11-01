package com.nonograma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import com.engineandroid.*;
import com.engine.*;
import com.gamelogic.Nonograma;

public class MainActivity extends AppCompatActivity {

    private Engine engine;

    private SurfaceView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init renderer
        this.renderView = new SurfaceView(this);
        setContentView(this.renderView);

        ColorAndroid.Init();
        //Init Engine
        this.engine = new EngineAndroid(this.renderView);
        //Init Game
        IGame game = new Nonograma(engine);

        //Set Game and play
        engine.setGame(game);
        //Tambien se podrian cargar los recursos dentro del setscene
        engine.loadResources();
        engine.resume();
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