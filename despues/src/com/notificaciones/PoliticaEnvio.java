package com.notificaciones;

/**
 * SOLUCION LSP - Interfaz de Politica de Envio:
 *
 * En lugar de que las subclases cambien el contrato de enviar(),
 * extraemos las REGLAS de validacion a un objeto separado.
 *
 * Asi Notificacion puede tener una sola politica de envio
 * sin necesidad de herencia que rompa el contrato original.
 *
 * Patron: Composicion sobre herencia.
 */
public interface PoliticaEnvio {

    /**
     * Valida si el mensaje cumple las reglas del canal.
     * Lanza IllegalArgumentException si no cumple.
     */
    void validar(String mensaje);
}
