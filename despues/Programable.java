package com.notificaciones;

/**
 * SOLUCION ISP - Interfaz segregada: Programable
 *
 * Solo los canales que soportan envio programado implementan esta interfaz.
 * SMS no puede programar envios, por lo que no la implementa.
 */
public interface Programable {

    void programar(String fechaHora);
}
