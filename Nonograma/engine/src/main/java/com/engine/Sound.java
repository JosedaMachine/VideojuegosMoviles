package com.engine;

public interface Sound {

    public abstract void play();

    public abstract void pause();

    public boolean isLoaded();

    public void setLoop(boolean l);
}
