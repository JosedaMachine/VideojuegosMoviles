package com.example.desktop;

//Clase interna que representa la escena que queremos pintar
public class MyScene {

    private Circulito circulito;
    private Circulito circulote;
    private Engine engine;

    public MyScene(Engine engine){
        this.engine = engine;
        this.circulito = new Circulito(50,50,10,150,engine.getWidth());
        this.circulito.setColor("blue");

        this.circulote = new Circulito(50,50,50, 50, engine.getWidth());
        this.circulote.setColor("red");
    }

    public void update(double deltaTime){
        this.circulito.update(deltaTime);
        this.circulote.update(deltaTime);
    }

    public void render(){
        this.circulito.render(engine);
        this.circulote.render(engine);
    }
}