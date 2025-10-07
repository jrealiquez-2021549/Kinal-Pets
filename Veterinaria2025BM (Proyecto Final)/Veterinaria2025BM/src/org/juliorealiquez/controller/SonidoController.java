
package org.juliorealiquez.controller;

public class SonidoController {
    private static double volumenEfectos = 1.0; // valor por defecto (de 0.0 a 1.0)

    public static double getVolumenEfectos() {
        return volumenEfectos;
    }

    public static void setVolumenEfectos(double volumen) {
        volumenEfectos = volumen;
    }
}
