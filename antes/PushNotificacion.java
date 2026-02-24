package com.notificaciones;

/**
 * OTRA VIOLACION DE LSP:
 * PushNotificacion requiere un deviceToken para funcionar.
 * Si no esta configurado, lanza una excepcion que el contrato
 * original de Notificacion NUNCA menciono.
 *
 * El padre dice: "solo necesitas un mensaje no-null."
 * El hijo dice: "ademas necesitas un deviceToken valido."
 *
 * Eso rompe la sustitucion: el cliente no puede asumir
 * que con un mensaje valido es suficiente.
 */
public class PushNotificacion extends Notificacion {

    private String deviceToken;

    // MAL: estado interno requerido que la clase padre no conoce
    public void configurarDispositivo(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void enviar(String mensaje) {
        // NUEVA CONDICION: el padre nunca pidio esto
        if (deviceToken == null || deviceToken.isEmpty()) {
            throw new IllegalStateException(
                "Push requiere un deviceToken configurado antes de enviar"
            );
        }
        if (mensaje.length() > 256) {
            throw new IllegalArgumentException(
                "Push no permite mensajes de mas de 256 caracteres"
            );
        }
        System.out.println("[PUSH] Enviando push a dispositivo " + deviceToken + ": " + mensaje);
    }
}
