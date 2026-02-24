package com.notificaciones;

/**
 * SOLUCION ISP - PushNotificacion implementa Enviable y Programable.
 *
 * Push puede enviar y programar, pero NO puede adjuntar archivos.
 * Al no implementar Adjuntable, es imposible en tiempo de compilacion
 * intentar adjuntar un archivo a un canal Push.
 *
 * El deviceToken ahora es parte del constructor: es un requisito
 * explicito, no un estado oculto que puede fallar en runtime.
 */
public class PushNotificacion implements Enviable, Programable {

    private final String deviceToken;
    private final Notificacion notificacion;

    public PushNotificacion(String deviceToken) {
        if (deviceToken == null || deviceToken.isEmpty()) {
            throw new IllegalArgumentException("El deviceToken es obligatorio para Push");
        }
        this.deviceToken = deviceToken;
        this.notificacion = new Notificacion("PUSH", new PoliticaPush());
    }

    @Override
    public void enviar(String mensaje) {
        notificacion.enviar(mensaje + " -> Dispositivo: " + deviceToken);
    }

    @Override
    public void programar(String fechaHora) {
        System.out.println("[PUSH] Notificacion push programada para: " + fechaHora
            + " en dispositivo: " + deviceToken);
    }
}
