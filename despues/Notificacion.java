package com.notificaciones;

/**
 * SOLUCION LSP - Clase Notificacion con composicion:
 *
 * El contrato de enviar() ahora es SIEMPRE el mismo:
 * "Valida con la politica interna y envia."
 *
 * No hay subclases que rompan este contrato.
 * El comportamiento cambia a traves de la PoliticaEnvio inyectada,
 * no a traves de herencia.
 *
 * Cualquier instancia de Notificacion es completamente sustituible
 * por otra, sin importar cual politica tiene internamente.
 * LSP garantizado.
 */
public class Notificacion {

    private final PoliticaEnvio politica;
    private final String canal;

    public Notificacion(String canal, PoliticaEnvio politica) {
        this.canal = canal;
        this.politica = politica;
    }

    /**
     * Contrato claro y estable:
     * 1. Valida el mensaje con la politica del canal.
     * 2. Si pasa, envia.
     * 3. Si no, lanza IllegalArgumentException (siempre el mismo tipo).
     *
     * Este contrato nunca cambia sin importar el canal.
     */
    public void enviar(String mensaje) {
        politica.validar(mensaje);
        System.out.println("[" + canal.toUpperCase() + "] Enviando: " + mensaje);
    }

    public String getCanal() {
        return canal;
    }
}
