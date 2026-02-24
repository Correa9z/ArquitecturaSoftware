package com.notificaciones;

import java.util.List;

/**
 * IMPLEMENTACION QUE ROMPE ISP:
 * SmsService implementa NotificacionService (interfaz gorda),
 * pero SMS solo soporta enviar(). Los demas metodos no tienen
 * sentido para este canal, por lo que lanzan UnsupportedOperationException.
 *
 * Problemas:
 * - El cliente que llama adjuntarArchivo() en un SmsService explota en runtime.
 * - El codigo miente: "implementa" metodos que en realidad no implementa.
 * - No hay manera de saberlo en tiempo de compilacion.
 */
public class SmsService implements NotificacionService {

    @Override
    public void enviar(String mensaje) {
        if (mensaje.length() > 160) {
            throw new IllegalArgumentException("SMS: maximo 160 caracteres");
        }
        System.out.println("[SMS SERVICE] Enviando SMS: " + mensaje);
    }

    @Override
    public void adjuntarArchivo(String rutaArchivo) {
        // MAL: metodo que no aplica, pero la interfaz lo obliga a estar aqui
        throw new UnsupportedOperationException("SMS no soporta adjuntar archivos");
    }

    @Override
    public void programar(String fechaHora) {
        // MAL: SMS tampoco soporta programacion de envios
        throw new UnsupportedOperationException("SMS no soporta programacion de envios");
    }

    @Override
    public void enviarMasivo(List<String> destinatarios, String mensaje) {
        // MAL: implementacion vacia y enganosa
        System.out.println("[SMS SERVICE] Envio masivo no disponible para SMS. Ignorado.");
    }
}
