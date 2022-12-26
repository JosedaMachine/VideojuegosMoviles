package com.enginepc;

import com.engine.IInput;
import com.engine.TouchEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class InputPC implements IInput {

    private final ArrayList<TouchEvent> eventList;
    private final GraphicsPC graphics_;

    public InputPC(final JFrame view, GraphicsPC graphics){
         eventList = new ArrayList<>();
         graphics_ = graphics;

         //Genera un evento TOUCH_EVENT o RELEASE_EVENT en funcion de la posicion reescalada de
         //la pantalla, respetando la logica, y lo añade a la cola de eventos para ser procesado
         view.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int posX = mouseEvent.getX();
                int posY = mouseEvent.getY() - view.getInsets().top;

                if(posX - graphics_.getTranslateFactorX()>0 && posY - graphics_.getTranslateFactorY()>0){
                    TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[mouseEvent.getButton()];
                    TouchEvent touch = new TouchEvent(TouchEvent.TouchEventType.TOUCH_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                            id);
                    eventList.add(touch);
                }
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                int posX = mouseEvent.getX();
                int posY = mouseEvent.getY() - view.getInsets().top;
                if(posX - graphics_.getTranslateFactorX()>0 && posY - graphics_.getTranslateFactorY()>0){
                    TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[mouseEvent.getButton()];
                    TouchEvent release = new TouchEvent(TouchEvent.TouchEventType.RELEASE_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                             id);
                    eventList.add(release);
                }
            }
        });

    }

    @Override
    public ArrayList<TouchEvent> getEventList() {
        return eventList;
    }

    @Override
    public void flushEvents() {
        eventList.clear();
    }


}
