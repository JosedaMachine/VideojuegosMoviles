package com.engineandroid;

import java.util.ArrayList;

public class UserInterface {
    ArrayList<SceneElement> elements;
    public UserInterface(){
        elements = new ArrayList<>();
    }
    public void input(TouchEvent event){
        for(int i = 0; i < elements.size(); i++){
            elements.get(i).input(event);
        }
    }

    public void render(Graphics graphics){
        for(int i = 0; i < elements.size(); i++){
            elements.get(i).render(graphics);
        }
    }

    public void update(double delta){
        for(int i = 0; i < elements.size(); i++){
            elements.get(i).update(delta);
        }
    }

    public void addElement(SceneElement element){
        elements.add(element);
    }

    public SceneElement getElement(int index){
        if(index >= elements.size())
            return null;
        return elements.get(index);
    }

    public void removeElement(SceneElement element){
        elements.remove(element);
    }

    public void clearElements(){
        elements.clear();
    }
}
