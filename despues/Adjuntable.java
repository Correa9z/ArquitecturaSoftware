package com.notificaciones;

/**
 * SOLUCION ISP - Interfaz segregada: Adjuntable
 *
 * Solo los canales que REALMENTE soportan adjuntos implementan esta interfaz.
 * SMS y Push no la implementan, y eso es totalmente correcto.
 *
 * Si el codigo necesita adjuntar un archivo, usa esta interfaz
 * y el compilador garantiza que el canal lo soporta.
 */
public interface Adjuntable {

    void adjuntarArchivo(String rutaArchivo);
}
