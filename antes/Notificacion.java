package com.notificaciones;

/**
 * CLASE BASE - Contrato original:
 * "Si el mensaje no es null, la notificacion se envia."
 *
 * MAL: Esta clase define un contrato que las subclases van a romper
 * al agregar nuevas restricciones no previstas aqui.
 */
public class Notificacion {

    public void enviar(String mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("Mensaje obligatorio");
        }
        System.out.println("[BASE] Enviando notificacion: " + mensaje);
    }
}
