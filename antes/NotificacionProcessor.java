package com.notificaciones;

/**
 * PROCESADOR CON MALAS PRACTICAS:
 *
 * Como LSP esta roto, este procesador no puede confiar en el polimorfismo.
 * Necesita preguntar QUE TIPO de notificacion es para manejar
 * los casos especiales de cada subclase.
 *
 * Esto es exactamente lo que LSP busca evitar:
 * el cliente (NotificacionProcessor) tiene que conocer
 * los detalles internos de cada subclase.
 *
 * Consecuencias:
 * - Si agregamos un nuevo canal, hay que modificar este procesador.
 * - El codigo crece con cada nuevo tipo.
 * - Es fragil: olvidar un caso especial genera bugs en produccion.
 */
public class NotificacionProcessor {

    // MAL: el procesador tiene que "saber" que tipo de notificacion es
    public void procesar(Notificacion notificacion, String mensaje) {

        // INSTANCEOF: senal clara de que LSP esta roto
        if (notificacion instanceof SmsNotificacion) {
            // Logica especial para SMS
            if (mensaje.length() > 160) {
                System.out.println("[PROCESSOR] Mensaje demasiado largo para SMS. Truncando...");
                mensaje = mensaje.substring(0, 160);
            }
        }

        // INSTANCEOF: otra senal de LSP roto
        if (notificacion instanceof PushNotificacion) {
            // Logica especial para Push: hay que configurar el token antes
            PushNotificacion push = (PushNotificacion) notificacion;
            push.configurarDispositivo("token-dispositivo-default");
            if (mensaje.length() > 256) {
                System.out.println("[PROCESSOR] Mensaje demasiado largo para Push. Truncando...");
                mensaje = mensaje.substring(0, 256);
            }
        }

        // Solo despues de todos los casos especiales, intentamos enviar
        try {
            notificacion.enviar(mensaje);
        } catch (IllegalArgumentException e) {
            System.out.println("[PROCESSOR] Error al enviar: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("[PROCESSOR] Estado invalido: " + e.getMessage());
        }
    }

    // MAL: metodo que tambien tiene que preguntar por tipo para adjuntar
    public void procesarConAdjunto(NotificacionService service, String mensaje, String archivo) {

        // Hay que verificar en runtime si soporta adjuntos
        if (service instanceof SmsService) {
            System.out.println("[PROCESSOR] SMS no soporta adjuntos. Enviando sin adjunto.");
            service.enviar(mensaje);
            return;
        }

        if (service instanceof PushService) {
            System.out.println("[PROCESSOR] Push no soporta adjuntos. Enviando sin adjunto.");
            service.enviar(mensaje);
            return;
        }

        // Solo EmailService llega hasta aqui
        service.enviar(mensaje);
        service.adjuntarArchivo(archivo);
    }
}
