package com.example.androidconceptapp;

public class Circulito {

    private float x;
    private float y;
    private int radius;
    private int speed;

    private String color;

    private int maxX;
    public Circulito(float x, float y, int r, int speed, int maxX){
        this.x=x;
        this.y=y;
        this.radius = r;
        this.speed = speed;
        this.maxX = maxX;
    }

    public void update(double deltaTime, Engine engine){
        System.out.println(this.maxX);
        int maxX = engine.getWidth()-this.radius;
        if(this.maxX <= 0) //si se diera caso que aún no tenemos tamaño de ventana
            maxX = 1000;

        this.x += this.speed * deltaTime;
        this.y += 2*deltaTime;
        while(this.x < 0 || this.x > maxX-this.radius) {
            // Vamos a pintar fuera de la pantalla. Rectificamos.
            if (this.x < 0) {
                // Nos salimos por la izquierda. Rebotamos.
                this.x = -this.x;
                this.speed *= -1;
            } else if (this.x > maxX-this.radius) {
                // Nos salimos por la derecha. Rebotamos
                this.x = 2 * (maxX-this.radius) - this.x;
                this.speed *= -1;
            }
        }
    }
    public void render(Engine engine) {
        engine.pintarCirculo(this.x, this.y, this.radius, this.color);
    }

    public void setColor(String color) {
        this.color=color;
    }
}
