package com.notificaciones;

/**
 * SOLUCION ISP - Interfaz segregada: Enviable
 *
 * Todos los canales de notificacion PUEDEN enviar mensajes.
 * Esta es la capacidad minima y universal.
 *
 * Al separar esta interfaz de las demas,
 * los canales implementan SOLO lo que realmente soportan.
 */
public interface Enviable {

    void enviar(String mensaje);
}
