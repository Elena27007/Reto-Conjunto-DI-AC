package org.example;

import org.example.views.InicioSesion;

/**
 * Clase principal que ejecuta la aplicación
 */
public class Main {
    /**
     * Método que instancia un objeto InicioSesion y lo hace visible para que se muestre
     * @param args
     */
    public static void main(String[] args) {
        var ventana = new InicioSesion();
        ventana.setVisible(true);
    }
}