package com.notificaciones;

/**
 * PROCESADOR LIMPIO - Sin instanceof, sin condicionales por tipo:
 *
 * Gracias a LSP e ISP correctamente aplicados:
 * - procesar() recibe cualquier Enviable y simplemente llama enviar().
 *   No necesita saber que tipo especifico es.
 * - procesarConAdjunto() solo acepta objetos que son TANTO Enviable
 *   como Adjuntable. El compilador garantiza que el canal lo soporta.
 *
 * Si en el futuro agregamos un nuevo canal (WhatsApp, Telegram, etc.),
 * este procesador NO necesita modificarse.
 * Eso es OCP + LSP + ISP trabajando juntos.
 */
public class NotificacionProcessor {

    /**
     * Procesa el envio de cualquier canal.
     * No necesita instanceof. No necesita casos especiales.
     * El polimorfismo funciona correctamente porque LSP se respeta.
     */
    public void procesar(Enviable canal, String mensaje) {
        try {
            canal.enviar(mensaje);
            System.out.println("[PROCESSOR] Envio exitoso.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("[PROCESSOR] Mensaje invalido para este canal: " + e.getMessage() + "\n");
        }
    }

    /**
     * Procesa envio con adjunto.
     * El parametro <T extends Enviable & Adjuntable> garantiza en tiempo
     * de COMPILACION que el canal soporta ambas operaciones.
     * No hay UnsupportedOperationException posible.
     */
    public <T extends Enviable & Adjuntable> void procesarConAdjunto(
            T canal, String mensaje, String archivo) {
        canal.enviar(mensaje);
        canal.adjuntarArchivo(archivo);
        System.out.println("[PROCESSOR] Envio con adjunto exitoso.\n");
    }

    /**
     * Programa el envio de un canal que lo soporte.
     * Solo los canales Programable pueden llegar aqui.
     */
    public void procesarProgramado(Programable canal, String mensaje, String fechaHora) {
        canal.programar(fechaHora);
        System.out.println("[PROCESSOR] Envio programado para: " + fechaHora + "\n");
    }
}
