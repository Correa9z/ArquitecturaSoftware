package com.notificaciones;

import java.util.List;

/**
 * SOLUCION ISP - EmailNotificacion implementa TODAS las interfaces
 * porque Email REALMENTE soporta todas esas capacidades.
 *
 * El compilador sabe que:
 * - EmailNotificacion es Enviable
 * - EmailNotificacion es Adjuntable
 * - EmailNotificacion es Programable
 * - EmailNotificacion soporta EnvioMasivo
 *
 * No hay contradicciones, no hay excepciones sorpresa.
 */
public class EmailNotificacion implements Enviable, Adjuntable, Programable, EnvioMasivo {

    private final String destinatario;
    private final Notificacion notificacion;

    public EmailNotificacion(String destinatario) {
        this.destinatario = destinatario;
        this.notificacion = new Notificacion("EMAIL", new PoliticaEmail());
    }

    @Override
    public void enviar(String mensaje) {
        notificacion.enviar(mensaje + " -> Para: " + destinatario);
    }

    @Override
    public void adjuntarArchivo(String rutaArchivo) {
        System.out.println("[EMAIL] Adjuntando archivo al email: " + rutaArchivo);
    }

    @Override
    public void programar(String fechaHora) {
        System.out.println("[EMAIL] Email programado para: " + fechaHora);
    }

    @Override
    public void enviarMasivo(List<String> destinatarios, String mensaje) {
        for (String dest : destinatarios) {
            System.out.println("[EMAIL] Envio masivo a: " + dest + " -> " + mensaje);
        }
    }
}
