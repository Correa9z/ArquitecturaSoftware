package com.notificaciones;

import java.util.Arrays;
import java.util.List;

/**
 * DEMOSTRACION DE BUENAS PRACTICAS - LSP e ISP BIEN APLICADOS
 *
 * Este Main muestra el sistema corregido:
 * - LSP: cualquier Notificacion puede sustituirse sin sorpresas.
 * - ISP: cada canal implementa solo lo que realmente soporta.
 * - Sin instanceof, sin UnsupportedOperationException.
 * - El compilador detecta errores que antes solo aparecian en runtime.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println(" DEMO: BUENAS PRACTICAS - LSP e ISP BIEN");
        System.out.println("===========================================\n");

        NotificacionProcessor processor = new NotificacionProcessor();

        // -------------------------------------------------------
        // CORRECTO 1: LSP - polimorfismo que funciona
        // -------------------------------------------------------
        System.out.println("--- CORRECTO 1: LSP RESPETADO ---");
        System.out.println("Todos los canales como Enviable, sin sorpresas...\n");

        List<Enviable> canales = Arrays.asList(
            new SmsNotificacion(),
            new EmailNotificacion("usuario@empresa.com"),
            new PushNotificacion("token-dispositivo-xyz")
        );

        String mensajeCorto = "Alerta: su sesion expira en 5 minutos.";

        for (Enviable canal : canales) {
            System.out.println("Enviando con: " + canal.getClass().getSimpleName());
            processor.procesar(canal, mensajeCorto);
        }

        // -------------------------------------------------------
        // CORRECTO 2: Politicas detectan mensajes invalidos
        // -------------------------------------------------------
        System.out.println("--- CORRECTO 2: POLITICAS DE VALIDACION ---");
        System.out.println("El sistema maneja bien mensajes invalidos...\n");

        String mensajeLargo = "Este es un mensaje extremadamente largo que supera "
            + "los ciento sesenta caracteres del protocolo SMS. "
            + "Deberia ser detectado y rechazado por la politica de validacion "
            + "antes de intentar el envio.";

        System.out.println("Intentando enviar mensaje largo por SMS:");
        processor.procesar(new SmsNotificacion(), mensajeLargo);

        System.out.println("Intentando enviar el mismo mensaje largo por Email:");
        processor.procesar(new EmailNotificacion("admin@empresa.com"), mensajeLargo);

        // -------------------------------------------------------
        // CORRECTO 3: ISP - adjuntos solo donde aplica
        // -------------------------------------------------------
        System.out.println("--- CORRECTO 3: ISP - INTERFACES HONESTAS ---");
        System.out.println("Solo Email acepta adjuntos (garantizado por el compilador)...\n");

        EmailNotificacion email = new EmailNotificacion("gerente@empresa.com");
        processor.procesarConAdjunto(email, "Reporte mensual adjunto.", "reporte.xlsx");

        // NOTA: El siguiente codigo NO COMPILA. El compilador lo impide:
        // SmsNotificacion sms = new SmsNotificacion();
        // processor.procesarConAdjunto(sms, "Hola", "archivo.pdf");
        // ERROR: SmsNotificacion no implementa Adjuntable
        System.out.println("[COMPILADOR] Intentar: procesarConAdjunto(smsNotificacion, ...) -> NO COMPILA");
        System.out.println("[COMPILADOR] Esto se detecta antes de ejecutar el programa.\n");

        // -------------------------------------------------------
        // CORRECTO 4: ISP - programacion solo donde aplica
        // -------------------------------------------------------
        System.out.println("--- CORRECTO 4: PROGRAMACION DE ENVIOS ---");
        System.out.println("Email y Push soportan programacion. SMS no.\n");

        EmailNotificacion emailProg = new EmailNotificacion("cliente@empresa.com");
        PushNotificacion pushProg = new PushNotificacion("token-notif-abc");

        processor.procesarProgramado(emailProg, "Recordatorio de reunion.", "2025-06-15 09:00");
        processor.procesarProgramado(pushProg, "Tu pedido esta en camino.", "2025-06-15 10:30");

        // SMS no implementa Programable, no se puede pasar a procesarProgramado.
        // El compilador lo rechaza sin necesidad de instanceof.

        // -------------------------------------------------------
        // CORRECTO 5: Envio masivo
        // -------------------------------------------------------
        System.out.println("--- CORRECTO 5: ENVIO MASIVO (solo Email) ---\n");

        EmailNotificacion emailMasivo = new EmailNotificacion("lista");
        List<String> destinatarios = Arrays.asList(
            "ana@empresa.com",
            "luis@empresa.com",
            "maria@empresa.com"
        );
        emailMasivo.enviarMasivo(destinatarios, "Mantenimiento programado este fin de semana.");

        System.out.println("\n===========================================");
        System.out.println(" Sistema predecible, extensible, confiable.");
        System.out.println("===========================================");
    }
}
