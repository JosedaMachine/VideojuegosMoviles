package com.engine;

//BUSCAR COMO SE MANEJA INPUT

import java.io.Serializable;

/* *
* Clase que representa la información de un toque sobre
* la pantalla (o evento de ratón). Indicará el tipo (pulsación, liberación,
* desplazamiento), la posición y el identificador del “dedo” (o botón).
* */
public class TouchEvent implements Comparable<TouchEvent>{
    //Variables
    private final TouchEventType type_;
    private final int x_;
    private final int y_;

    private final ButtonID ID_;

    @Override
    public int compareTo(TouchEvent touchEvent) {
        return 0;
    }

    public enum TouchEventType {
        TOUCH_EVENT,
        RELEASE_EVENT,
        MOVE_EVENT,
    }

    public enum ButtonID {
        NO_BUTTON,
        LEFT_BUTTON,
        MIDDLE_BUTTON,
        RIGHT_BUTTON
    }

    public TouchEvent(TouchEventType type, int posX, int posY, ButtonID id ){
        type_ = type;
        x_ = posX; y_ = posY;
        ID_ = id;
    }

    public TouchEventType getType_() {
        return type_;
    }

    public int getX_() {
        return x_;
    }

    public int getY_() {
        return y_;
    }

    public ButtonID getID_() {
        return ID_;
    }
}
