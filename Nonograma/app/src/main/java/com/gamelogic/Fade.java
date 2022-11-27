package com.gamelogic;


import com.engineandroid.Engine;
import com.engineandroid.Graphics;
import com.engineandroid.ColorWrap;

public class Fade{

    public enum STATE_FADE{In, Out};

    private int timeIn_, timeOut_;
    private boolean play;
    private double percentageIn, percentageOut;
    private float alpha;
    private int colorFade;
    private final Engine e_;
    private Graphics g;

    private STATE_FADE state_;

    private final int posX_;
    private final int posY_;
    private final int sizeX_;
    private final int sizeY_;

    private boolean fadeOutComplete = false, fadeInComplete = false;

    public Fade(Engine e, int posX, int posY, int width, int height, int timeIn, int timeOut, STATE_FADE state){
        e_ = e;
        if(state == STATE_FADE.Out){
            alpha = 0.0f;
        }else alpha = 1.0f;

        colorFade = ColorWrap.BLACK;
        //Posicion y tamanyo
        posX_ = posX;
        posY_ = posY;
        sizeX_ = width;
        sizeY_ = height;

        //Tiempos de fade
        timeIn_ = timeIn  /** App::FPS*/;
        timeOut_ = timeOut /** App::FPS*/;
        state_ = state;
        play = false;

        percentageIn = (1.0f / (float)timeIn_);
        percentageOut = (1.0f/ (float)timeOut_);
    }

    public void render(){
        e_.getGraphics().setColor(colorFade, alpha);
        e_.getGraphics().fillRect(posX_, posY_, sizeX_ , sizeY_);
    }

    public void update(double deltaTime){
        if (play){
            //Resta o suma alpha dependiendo de si es Fade In o Fade Out
            if (state_ == STATE_FADE.In && !fadeInComplete){
//              lerpPrecise(alpha, 0, percentageIn)
                alpha -= deltaTime;
                if (alpha <= 0){
                    fadeInComplete = true;
                    play = false;
                }

            }
            else if(state_ == STATE_FADE.Out && !fadeOutComplete){
//                lerpPrecise(alpha, 1.0f, percentageOut)
                alpha += deltaTime;
                if (alpha >= 1.0f){
                    fadeOutComplete = true;
                    play = false;
                }
            }
        }
    }

    public void setState(STATE_FADE state){
        if(state_ != state)
        state_ = state;
    }

    public void setColor(int color){
        colorFade = color;
    }

    public boolean getFadeInComplete() {
        return fadeInComplete;
    }

    public boolean getFadeOutComplete(){
        return fadeOutComplete;
    }

    /// <summary>
    /// Inicia el Fade según el estado en que estE
    /// </summary>
    public void triggerFade(){
        if (!play) play = true;
    }

    /// <summary>
    /// Asigna el tiempo de Fade Out en milisegundos
    /// </summary>
    /// <param name="time"> en milisegundos</param>
    public void setTimeIn(int time){
        timeIn_ = time;
        percentageIn = ((float)1.0f / (float)timeIn_);
    }

    /// <summary>
    /// Asigna el tiempo de Fade Out en milisegundos
    /// </summary>
    /// <param name="time"> en milisegundos</param>
    public void setTimeOut(int time){
        timeOut_ = time;
        percentageOut = ((float)1.0f / (float)timeOut_);
    }

    public boolean isPlaying(){
        return play;
    }

    private float lerpPrecise(float a, float b, double t) {
        return (float) (a * (1.0f - t) + b * t);
    }

    public STATE_FADE getState(){
        return state_;
    }
};
