package com.gamelogic;

import com.engineandroid.Engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Lives implements Serializable{
    private int lives_;



    public Lives(){
        lives_ = 0;
    }

    public Lives(int live){
        lives_ = live;
    }

    public void  subtract(int i){
        lives_ -= i;
    }

    public void add(int i){
        lives_ += i;
    }

    public int getLives(){
        return lives_;
    }
}
