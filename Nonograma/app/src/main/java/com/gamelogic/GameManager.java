package com.gamelogic;

public class GameManager {
    private static GameManager instance_;

    public GameManager(){

    }

    public static GameManager Instance(){
        return  instance_;
    }

    static boolean Init(){
        return  true;
    }

    static boolean Release(){
        return true;
    }

}
