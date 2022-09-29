package com.examplejavareal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

//Clase interna que representa la escena que queremos pintar
public class MyScene {
    private float x;
    private float y;
    private int radius;
    private int speed;

    private MyRenderClass renderClass;

    public MyScene(){
        this.x=50;
        this.y=50;
        this.radius = 50;
        this.speed = 150;
    }

    public void init(MyRenderClass renderClass){
        this.renderClass = renderClass;
    }

    public void update(double deltaTime){
        int maxX = this.renderClass.getWidth()-this.radius;

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

    public void render(){
        renderClass.renderCircle(this.x, this.y, this.radius);
        renderClass.renderText(this.x, this.y, "app/src/main/assets/fonts");
        renderClass.renderImages(this.x, this.y, this.radius);
    }
}