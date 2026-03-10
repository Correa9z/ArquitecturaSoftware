# Reto de Diseño – Sistema de Impresoras

## Contexto

En el sistema actual todas las impresoras implementan la misma interfaz:

```java
public interface ImpresoraService {

    void imprimir(String documento);

    void escanear(String documento);

    void enviarFax(String numero);

}

**Código actual del sistema**

**Impresora Multifuncional**

package impresoras;

public class ImpresoraMultifuncional implements ImpresoraService {

    @Override
    public void imprimir(String documento) {
        System.out.println("Imprimiendo: " + documento);
    }

    @Override
    public void escanear(String documento) {
        System.out.println("Escaneando: " + documento);
    }

    @Override
    public void enviarFax(String numero) {
        System.out.println("Enviando fax a: " + numero);
    }
}
