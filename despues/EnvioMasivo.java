package com.notificaciones;

import java.util.List;

/**
 * SOLUCION ISP - Interfaz segregada: EnvioMasivo
 *
 * Solo los canales que soportan envio masivo implementan esta interfaz.
 * Cada canal que la implementa puede definir su propia semantica
 * para "masivo" (lista de emails, lista de tokens, etc.).
 */
public interface EnvioMasivo {

    void enviarMasivo(List<String> destinatarios, String mensaje);
}
