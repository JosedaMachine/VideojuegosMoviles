package com.engineandroid;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Input {
    ArrayList<TouchEvent> events;
    private final Graphics graphics_;

    Input(View view, Graphics graphics){
        events = new ArrayList<>();
        graphics_ = graphics;

        //Genera un evento TOUCH_EVENT o RELEASE_EVENT en funcion de la posicion reescalada de
        //la pantalla, respetando la logica, y lo añade a la cola de eventos para ser procesado
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                int posX = (int)motionEvent.getX(), posY = (int)motionEvent.getY();
                TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[motionEvent.getActionIndex()];
                TouchEvent event;
                if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN){
                    event = new TouchEvent(TouchEvent.TouchEventType.TOUCH_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                            id);
                }
                else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
                    event = new TouchEvent(TouchEvent.TouchEventType.RELEASE_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                            id);
                else if(action == MotionEvent.ACTION_MOVE)
                    event = new TouchEvent(TouchEvent.TouchEventType.MOVE_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                            id);
                else
                    return false;

                events.add(event);
                return true;
            }
        });
    }

    public ArrayList<TouchEvent> getEventList() {
        return events;
    }

    public void flushEvents() {
        events.clear();
    }
}
