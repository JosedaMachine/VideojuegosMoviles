package com.engineandroid;

import android.view.MotionEvent;
import android.view.View;

import com.engine.IInput;
import com.engine.TouchEvent;

import java.util.ArrayList;

public class InputAndroid implements IInput {
    ArrayList<TouchEvent> events;
    private GraphicsAndroid graphics_;

    InputAndroid(View view, GraphicsAndroid graphics){
        events = new ArrayList<>();
        graphics_ = graphics;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();
                int posX = (int)motionEvent.getX(), posY = (int)motionEvent.getY();
                TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[motionEvent.getActionIndex()];
                TouchEvent event;
                if(action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)
                    event = new TouchEvent(TouchEvent.TouchEventType.TOUCH_EVENT,
                            (int)((posX - graphics_.getTranslateFactorX())/ graphics_.getScaleFactor()),
                            (int)((posY - graphics_.getTranslateFactorY())/ graphics_.getScaleFactor()),
                            id);
                else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP)
                    event = new TouchEvent(TouchEvent.TouchEventType.RELEASE_EVENT,
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

    @Override
    public ArrayList<TouchEvent> getEventList() {
        return events;
    }

    @Override
    public void flushEvents() {
        events.clear();
    }


}
