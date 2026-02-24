package com.notificaciones;

/**
 * Politica de validacion para Email.
 * Email permite mensajes mas largos y solo valida que no sea null/vacio.
 */
public class PoliticaEmail implements PoliticaEnvio {

    @Override
    public void validar(String mensaje) {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("El cuerpo del email no puede estar vacio");
        }
    }
}
