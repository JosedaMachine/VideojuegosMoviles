package com.engine;

import com.engine.TouchEvent;

import java.util.ArrayList;

/*
* Proporciona las funcionalidades de entrada básicas. El juego no requiere una
* interfaz compleja, por lo que se utiliza únicamente la pulsación sobre la pantalla
* (o click con el ratón)
* */

public interface IInput {
    // Devuelve la lista de eventos recibidos desde la última invocación
    ArrayList<TouchEvent> getEventList();
    // Vacía la lista de eventos
    void flushEvents();
}
