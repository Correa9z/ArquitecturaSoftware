package com.notificaciones;

import java.util.List;

/**
 * EmailService implementa todos los metodos de la interfaz gorda.
 * Para Email tiene sentido: puede enviar, adjuntar, programar y enviar masivo.
 *
 * Pero el problema no es EmailService, el problema es que
 * la interfaz NO fue disenada para que otros canales puedan implementarla honestamente.
 */
public class EmailService implements NotificacionService {

    private String destinatario;

    public EmailService(String destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public void enviar(String mensaje) {
        System.out.println("[EMAIL SERVICE] Enviando email a " + destinatario + ": " + mensaje);
    }

    @Override
    public void adjuntarArchivo(String rutaArchivo) {
        System.out.println("[EMAIL SERVICE] Adjuntando archivo: " + rutaArchivo);
    }

    @Override
    public void programar(String fechaHora) {
        System.out.println("[EMAIL SERVICE] Email programado para: " + fechaHora);
    }

    @Override
    public void enviarMasivo(List<String> destinatarios, String mensaje) {
        for (String dest : destinatarios) {
            System.out.println("[EMAIL SERVICE] Enviando masivo a: " + dest + " -> " + mensaje);
        }
    }
}
