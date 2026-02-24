package com.notificaciones;

import java.util.List;

/**
 * OTRA IMPLEMENTACION QUE ROMPE ISP:
 * PushService tampoco puede adjuntar archivos, y el envio masivo
 * funciona diferente (necesita tokens de dispositivos, no emails).
 *
 * La interfaz lo obliga a implementar cosas que no le corresponden.
 */
public class PushService implements NotificacionService {

    private String deviceToken;

    public PushService(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[PUSH SERVICE] Enviando push a " + deviceToken + ": " + mensaje);
    }

    @Override
    public void adjuntarArchivo(String rutaArchivo) {
        // MAL: Push no soporta adjuntos, pero la interfaz lo exige
        throw new UnsupportedOperationException("Push no soporta adjuntar archivos");
    }

    @Override
    public void programar(String fechaHora) {
        // Push si puede programar, pero solo bajo ciertas condiciones
        System.out.println("[PUSH SERVICE] Push programado para: " + fechaHora);
    }

    @Override
    public void enviarMasivo(List<String> destinatarios, String mensaje) {
        // MAL: destinatarios aqui son tokens, no emails. La firma es enganosa.
        for (String token : destinatarios) {
            System.out.println("[PUSH SERVICE] Push masivo a token " + token + ": " + mensaje);
        }
    }
}
