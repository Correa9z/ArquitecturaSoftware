package com.notificaciones;

import java.util.List;

/**
 * VIOLACION DE ISP - Interfaz "gorda":
 * Esta interfaz obliga a TODOS los canales a implementar
 * adjuntarArchivo(), programar() y enviarMasivo(),
 * aunque muchos canales (SMS, Push) no soporten esas operaciones.
 *
 * Consecuencia: las implementaciones que no soportan un metodo
 * se ven forzadas a lanzar UnsupportedOperationException
 * o dejar el cuerpo vacio, lo cual es enganoso y peligroso.
 */
public interface NotificacionService {

    void enviar(String mensaje);

    // SMS no soporta adjuntar archivos
    void adjuntarArchivo(String rutaArchivo);

    // SMS no soporta programacion de envios
    void programar(String fechaHora);

    // Push no soporta envio masivo de la misma forma
    void enviarMasivo(List<String> destinatarios, String mensaje);
}
