package com.enginepc;

import com.engine.IInput;
import com.engine.TouchEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import java.awt.event.MouseListener;

public class InputPC implements IInput {

    private final ArrayList<TouchEvent> eventList;

    public InputPC(final JFrame view){
         eventList = new ArrayList<>();

         view.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
//                int posX = mouseEvent.getX() - view.getInsets().left;
//                int posY = mouseEvent.getY() - view.getInsets().top;
//                TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[mouseEvent.getButton()];
//                TouchEvent touch = new TouchEvent(TouchEvent.TouchEventType.TOUCH_EVENT,
//                                                  posX,
//                                                  posY,
//                                                  id);
//                eventList.add(touch);
//                TouchEvent release = new TouchEvent(TouchEvent.TouchEventType.RELEASE_EVENT,
//                                                    posX,
//                                                    posY,
//                                                    id);
//                eventList.add(release);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                int posX = mouseEvent.getX() - view.getInsets().left;
                int posY = mouseEvent.getY() - view.getInsets().top;
                TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[mouseEvent.getButton()];
                TouchEvent touch = new TouchEvent(TouchEvent.TouchEventType.TOUCH_EVENT,
                        posX,
                        posY,
                        id);
                eventList.add(touch);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                int posX = mouseEvent.getX() - view.getInsets().left;
                int posY = mouseEvent.getY() - view.getInsets().top;
                TouchEvent.ButtonID id = TouchEvent.ButtonID.values()[mouseEvent.getButton()];
                TouchEvent release = new TouchEvent(TouchEvent.TouchEventType.RELEASE_EVENT,
                        posX,
                        posY,
                        id);
                eventList.add(release);
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
