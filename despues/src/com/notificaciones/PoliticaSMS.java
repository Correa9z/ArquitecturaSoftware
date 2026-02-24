package com.notificaciones;

/**
 * Politica de validacion especifica para SMS.
 * La restriccion de 160 caracteres ahora vive AQUI,
 * no en una subclase de Notificacion.
 *
 * El contrato de Notificacion nunca se rompe.
 */
public class PoliticaSMS implements PoliticaEnvio {

    private static final int LIMITE_CARACTERES = 160;

    @Override
    public void validar(String mensaje) {
        if (mensaje == null || mensaje.isEmpty()) {
            throw new IllegalArgumentException("El mensaje SMS no puede estar vacio");
        }
        if (mensaje.length() > LIMITE_CARACTERES) {
            throw new IllegalArgumentException(
                "El SMS supera el limite de " + LIMITE_CARACTERES + " caracteres. "
                + "Longitud actual: " + mensaje.length()
            );
        }
    }
}
