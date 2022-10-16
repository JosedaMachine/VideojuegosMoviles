package com.enginepc;

import com.engine.IInput;
import com.engine.TouchEvent;

import java.util.ArrayList;
import java.util.List;

public class InputPC implements IInput {

    private ArrayList<TouchEvent> eventList = new ArrayList<TouchEvent>();

    @Override
    public ArrayList<TouchEvent> getEventList() {
        return eventList;
    }
}
