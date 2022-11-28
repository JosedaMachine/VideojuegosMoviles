package com.gamelogic;

import java.util.HashMap;

public class GameManager {
    private static GameManager instance_;
    private final int maxLevels = 4;

    public GameManager(){

    }

    private HashMap<Category, Integer> categoryLevelIndexes = new HashMap<>();
    private int money = 0;

    public static GameManager instance(){
        return  instance_;
    }

    static boolean init(){
        instance_ = new GameManager();
        return  true;
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
}
