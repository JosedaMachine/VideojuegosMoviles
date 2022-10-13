package com.example.androidconceptapp;

public class MyScene {

    private Circulito circulito;
    private Circulito circulote;
    private Engine engine;

    public MyScene(Engine engine){
        this.engine = engine;
        this.circulito = new Circulito(0,500,50,150,engine.getWidth());
        this.circulito.setColor("blue");

        this.circulote = new Circulito(0,500,50, 50, engine.getWidth());
        this.circulote.setColor("red");
    }

    public void update(double deltaTime){
        this.circulito.update(deltaTime, this.engine);
        this.circulote.update(deltaTime, this.engine);
    }

    public void render(){
        this.circulito.render(engine);
        this.circulote.render(engine);
    }
}
