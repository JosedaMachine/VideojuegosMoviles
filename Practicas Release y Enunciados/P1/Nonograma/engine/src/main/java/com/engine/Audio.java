package com.engine;

public interface Audio {

    /**
     * Devuelve un sonido ya cargado.
     * En caso contrario , o que no exista devuelve null.
     * @param name nombre del sonido
     * @return instancia del sonido cargado
     */
    Sound getSound(String name);

    /**
     * Crea un sonido en base al nombre del asset.
     * @param name nombre del sonido
     * @return instancia del sonido creado o null en caso de que no se encuentre
     */
    Sound newSound(String name);

    /**
     * Lanza un sonido ya cargado desde el inicio
     * @param name nombre del sonido
     */
    void playSound(String name);
}
