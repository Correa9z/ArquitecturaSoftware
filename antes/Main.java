package com.notificaciones;

import java.util.Arrays;

/**
 * DEMOSTRACION DE MALAS PRACTICAS
 *
 * Este Main muestra los problemas que ocurren cuando se rompen LSP e ISP:
 * - Excepciones inesperadas en tiempo de ejecucion.
 * - Necesidad de instanceof para manejar casos especiales.
 * - Metodos que lanzan UnsupportedOperationException.
 * - Codigo fragil que explota con mensajes validos.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println(" DEMO: MALAS PRACTICAS - LSP e ISP ROTOS");
        System.out.println("===========================================\n");

        // -------------------------------------------------------
        // PROBLEMA 1: LSP ROTO - polimorfismo que no funciona
        // -------------------------------------------------------
        System.out.println("--- PROBLEMA 1: LSP ROTO ---");
        System.out.println("El sistema cree que puede tratar a todos igual...\n");

        Notificacion[] canales = {
            new EmailNotificacion("usuario@empresa.com"),
            new SmsNotificacion(),
            new PushNotificacion()   // deviceToken nunca configurado
        };

        String mensajeLargo = "Este es un mensaje de prueba que supera los ciento sesenta "
            + "caracteres permitidos por el protocolo SMS y que deberia enviarse sin problemas "
            + "segun el contrato de la clase base Notificacion que solo valida null.";

        for (Notificacion canal : canales) {
            System.out.println("Intentando enviar con: " + canal.getClass().getSimpleName());
            try {
                canal.enviar(mensajeLargo);  // Solo Email lo soporta
            } catch (IllegalArgumentException e) {
                System.out.println("  [FALLA] " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("  [FALLA] " + e.getMessage());
            }
            System.out.println();
        }

        // -------------------------------------------------------
        // PROBLEMA 2: instanceof en el procesador
        // -------------------------------------------------------
        System.out.println("--- PROBLEMA 2: PROCESSOR USA instanceof ---");
        System.out.println("El procesador tiene que conocer cada tipo internamente...\n");

        NotificacionProcessor processor = new NotificacionProcessor();
        processor.procesar(new SmsNotificacion(), mensajeLargo);
        processor.procesar(new PushNotificacion(), "Notificacion de alerta");
        System.out.println();

        // -------------------------------------------------------
        // PROBLEMA 3: ISP ROTO - UnsupportedOperationException
        // -------------------------------------------------------
        System.out.println("--- PROBLEMA 3: ISP ROTO - Metodos no soportados ---");
        System.out.println("El sistema llama metodos de la interfaz sin saber que explotan...\n");

        NotificacionService sms = new SmsService();
        NotificacionService push = new PushService("device-abc-123");

        // Esto compila perfectamente... pero explota en runtime
        System.out.println("Llamando adjuntarArchivo() en SmsService:");
        try {
            sms.adjuntarArchivo("/documentos/contrato.pdf");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [EXPLOSION] " + e.getMessage());
        }

        System.out.println("\nLlamando adjuntarArchivo() en PushService:");
        try {
            push.adjuntarArchivo("/imagenes/banner.png");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [EXPLOSION] " + e.getMessage());
        }

        System.out.println("\nLlamando programar() en SmsService:");
        try {
            sms.programar("2025-06-15 09:00");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [EXPLOSION] " + e.getMessage());
        }

        // -------------------------------------------------------
        // PROBLEMA 4: procesarConAdjunto lleno de instanceof
        // -------------------------------------------------------
        System.out.println("\n--- PROBLEMA 4: procesarConAdjunto con logica condicional ---");
        processor.procesarConAdjunto(sms, "Hola", "archivo.pdf");
        processor.procesarConAdjunto(push, "Alerta critica", "imagen.png");
        processor.procesarConAdjunto(
            new EmailService("admin@empresa.com"),
            "Reporte mensual",
            "reporte.xlsx"
        );

        System.out.println("\n===========================================");
        System.out.println(" El sistema compila... pero esta fracturado");
        System.out.println("===========================================");
    }
}
