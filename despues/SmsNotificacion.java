package com.notificaciones;

/**
 * SOLUCION ISP - SmsNotificacion implementa SOLO Enviable:
 *
 * SMS solo puede enviar. No puede adjuntar, no puede programar,
 * no tiene envio masivo en el mismo sentido.
 *
 * El compilador garantiza que nadie puede llamar
 * adjuntarArchivo() ni programar() en un SmsNotificacion
 * porque simplemente no existen en esta clase.
 *
 * No hay UnsupportedOperationException. No hay mentiras.
 */
public class SmsNotificacion implements Enviable {

    private final Notificacion notificacion;

    public SmsNotificacion() {
        this.notificacion = new Notificacion("SMS", new PoliticaSMS());
    }

    @Override
    public void enviar(String mensaje) {
        notificacion.enviar(mensaje);
    }
}
