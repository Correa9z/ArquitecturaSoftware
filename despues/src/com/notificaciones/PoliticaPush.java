package com.notificaciones;

/**
 * Politica de validacion para notificaciones Push.
 * Push tiene su propio limite de caracteres (256) y
 * necesita que el mensaje no este vacio.
 */
public class PoliticaPush implements PoliticaEnvio {

    private static final int LIMITE_CARACTERES = 256;

    @Override
    public void validar(String mensaje) {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("El mensaje Push no puede estar vacio");
        }
        if (mensaje.length() > LIMITE_CARACTERES) {
            throw new IllegalArgumentException(
                "Push no permite mensajes de mas de " + LIMITE_CARACTERES + " caracteres"
            );
        }
    }
}
