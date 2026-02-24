package com.notificaciones;

/**
 * VIOLACION DE LSP:
 * El contrato de Notificacion dice: "Si el mensaje no es null, se envia."
 * SmsNotificacion agrega una restriccion NUEVA que el contrato no contempla:
 * el mensaje no puede superar 160 caracteres.
 *
 * Consecuencia: el sistema no puede usar SmsNotificacion donde espera
 * una Notificacion sin riesgo de excepciones inesperadas.
 */
public class SmsNotificacion extends Notificacion {

    @Override
    public void enviar(String mensaje) {
        // NUEVA RESTRICCION no contemplada en el contrato del padre
        if (mensaje.length() > 160) {
            throw new IllegalArgumentException(
                "El SMS supera el limite de 160 caracteres. Longitud actual: " + mensaje.length()
            );
        }
        System.out.println("[SMS] Enviando SMS: " + mensaje);
    }
}
