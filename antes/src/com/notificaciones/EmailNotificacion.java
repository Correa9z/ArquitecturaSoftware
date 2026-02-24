package com.notificaciones;

/**
 * EmailNotificacion respeta el contrato de Notificacion:
 * cualquier mensaje no-null se envia sin restricciones adicionales.
 *
 * Esto funciona bien con el polimorfismo, pero el problema
 * esta en SmsNotificacion y PushNotificacion.
 */
public class EmailNotificacion extends Notificacion {

    private String destinatario;

    public EmailNotificacion(String destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public void enviar(String mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("Mensaje obligatorio");
        }
        System.out.println("[EMAIL] Enviando email a " + destinatario + ": " + mensaje);
    }
}
