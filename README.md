# Sistema de Notificaciones — LSP e ISP

Proyecto de exposición sobre los principios **LSP** (Liskov Substitution Principle)
e **ISP** (Interface Segregation Principle) aplicados a un sistema de notificaciones en Java.

El proyecto está dividido en dos carpetas que muestran el contraste entre un sistema
mal diseñado y uno corregido.

---

## Estructura del proyecto

```
Arquitectura/
├── antes/          ← Sistema con LSP e ISP rotos
└── despues/        ← Sistema refactorizado con LSP e ISP bien aplicados
```

---

## Conceptos clave

### LSP — Principio de Sustitución de Liskov

> Si una clase `B` extiende de `A`, el sistema debería poder usar `B`
> en cualquier lugar donde espera una `A`, sin comportamientos inesperados.

En otras palabras: una subclase **no puede agregar nuevas restricciones** ni lanzar
excepciones que la clase base no contempla. Si lo hace, rompe el contrato
y el polimorfismo deja de ser confiable.

**Señales de LSP roto:**
- La subclase lanza excepciones nuevas que el padre no lanza.
- La subclase agrega condiciones extra que el padre no tiene.
- El procesador necesita `instanceof` para manejar casos especiales.
- Hay validaciones condicionales por tipo (`if (obj instanceof X)`).

### ISP — Principio de Segregación de Interfaces

> Ninguna clase debería verse obligada a implementar métodos que no usa.

Una interfaz "gorda" con muchos métodos obliga a sus implementaciones a mentir:
ya sea dejando métodos vacíos o lanzando `UnsupportedOperationException`.

**Señales de ISP roto:**
- Métodos con cuerpo vacío en una implementación.
- `UnsupportedOperationException` en métodos de una interfaz.
- Comentarios como "// No aplica para este canal".
- Documentación que dice "ignorar este método si eres X".

---

## Carpeta `antes/` — Malas prácticas

### Problema 1: LSP roto por herencia con restricciones nuevas

La clase base `Notificacion` define un contrato:

```
"Si el mensaje no es null, la notificación se envía."
```

Pero las subclases agregan condiciones que ese contrato nunca prometió:

| Subclase | Restricción nueva (no prevista en el padre) |
|---|---|
| `SmsNotificacion` | Mensaje no puede superar 160 caracteres |
| `PushNotificacion` | Requiere un `deviceToken` configurado previamente |

**Consecuencia directa:** el procesador no puede confiar en el polimorfismo.
Necesita preguntar qué tipo es cada objeto para manejar sus casos especiales:

```java
// NotificacionProcessor.java — ANTES
if (notificacion instanceof SmsNotificacion) {
    // truncar mensaje...
}
if (notificacion instanceof PushNotificacion) {
    PushNotificacion push = (PushNotificacion) notificacion;
    push.configurarDispositivo("token-default");
}
```

Cada nuevo canal que se agregue **obliga a modificar el procesador**.
El sistema es frágil y difícil de extender.

### Problema 2: ISP roto por interfaz gorda

`NotificacionService` declara cuatro métodos:

```java
interface NotificacionService {
    void enviar(String mensaje);
    void adjuntarArchivo(String rutaArchivo);   // SMS no lo soporta
    void programar(String fechaHora);            // SMS no lo soporta
    void enviarMasivo(List<String> dest, String msg); // Push lo malinterpreta
}
```

`SmsService` y `PushService` se ven forzados a implementar métodos que no tienen sentido
para su canal. El resultado son métodos que mienten o explotan:

```java
// SmsService.java — ANTES
@Override
public void adjuntarArchivo(String rutaArchivo) {
    throw new UnsupportedOperationException("SMS no soporta adjuntar archivos");
}
```

El código **compila**, pero **explota en tiempo de ejecución**. No hay forma de saberlo
antes de ejecutar el programa.

### Archivos en `antes/`

```
antes/src/com/notificaciones/
├── Notificacion.java           ← Clase base (contrato que las subclases rompen)
├── SmsNotificacion.java        ← LSP roto: agrega límite de 160 chars
├── PushNotificacion.java       ← LSP roto: requiere deviceToken oculto
├── EmailNotificacion.java      ← Funciona bien (referencia de contraste)
├── NotificacionService.java    ← ISP roto: interfaz gorda con 4 métodos
├── SmsService.java             ← ISP roto: lanza UnsupportedOperationException
├── PushService.java            ← ISP roto: lanza UnsupportedOperationException
├── EmailService.java           ← Implementa todo (es el único canal que puede)
├── NotificacionProcessor.java  ← Consecuencia: lleno de instanceof
└── Main.java                   ← Demuestra las explosiones en runtime
```

---

## Carpeta `despues/` — Buenas prácticas

### Solución 1: LSP con composición sobre herencia

En lugar de que las subclases alteren el contrato de `Notificacion`,
las reglas de validación se extraen a objetos separados (`PoliticaEnvio`):

```
Notificacion
└── PoliticaEnvio (interfaz)
    ├── PoliticaSMS   → valida límite de 160 chars
    ├── PoliticaEmail → valida que no sea null
    └── PoliticaPush  → valida límite de 256 chars + token
```

El contrato de `Notificacion.enviar()` nunca cambia:

```java
public void enviar(String mensaje) {
    politica.validar(mensaje);  // cada canal tiene su política
    System.out.println("[" + canal + "] Enviando: " + mensaje);
}
```

**El comportamiento cambia mediante composición, no mediante herencia.**
Cualquier `Notificacion` puede sustituirse por otra sin sorpresas. LSP garantizado.

El procesador ya no necesita `instanceof`:

```java
// NotificacionProcessor.java — DESPUÉS
public void procesar(Enviable canal, String mensaje) {
    canal.enviar(mensaje);  // funciona para cualquier canal, sin condiciones
}
```

### Solución 2: ISP con interfaces segregadas

La interfaz gorda se divide en cuatro interfaces pequeñas y honestas:

```
Enviable       → void enviar(String mensaje)
Adjuntable     → void adjuntarArchivo(String archivo)
Programable    → void programar(String fechaHora)
EnvioMasivo    → void enviarMasivo(List<String> dest, String mensaje)
```

Cada canal implementa **solo lo que realmente soporta**:

| Canal | Enviable | Adjuntable | Programable | EnvioMasivo |
|---|:---:|:---:|:---:|:---:|
| `SmsNotificacion` | ✓ | — | — | — |
| `PushNotificacion` | ✓ | — | ✓ | — |
| `EmailNotificacion` | ✓ | ✓ | ✓ | ✓ |

Si alguien intenta llamar `adjuntarArchivo()` en un SMS, **el compilador lo rechaza**
antes de ejecutar el programa. No hay `UnsupportedOperationException` posible.

El procesador usa generics para garantizarlo en tiempo de compilación:

```java
// Solo acepta canales que REALMENTE soportan adjuntos
public <T extends Enviable & Adjuntable> void procesarConAdjunto(T canal, ...) {
    canal.enviar(mensaje);
    canal.adjuntarArchivo(archivo);
}
```

### Archivos en `despues/`

```
despues/src/com/notificaciones/
├── PoliticaEnvio.java          ← Interfaz de política (composición para LSP)
├── PoliticaSMS.java            ← Regla: máximo 160 caracteres
├── PoliticaEmail.java          ← Regla: mensaje no vacío
├── PoliticaPush.java           ← Regla: máximo 256 caracteres
├── Notificacion.java           ← Contrato estable, usa composición
├── Enviable.java               ← Interfaz: solo enviar()
├── Adjuntable.java             ← Interfaz: solo adjuntarArchivo()
├── Programable.java            ← Interfaz: solo programar()
├── EnvioMasivo.java            ← Interfaz: solo enviarMasivo()
├── SmsNotificacion.java        ← Implementa: Enviable
├── PushNotificacion.java       ← Implementa: Enviable + Programable
├── EmailNotificacion.java      ← Implementa: Enviable + Adjuntable + Programable + EnvioMasivo
├── NotificacionProcessor.java  ← Sin instanceof, usa generics
└── Main.java                   ← Demuestra el sistema limpio y predecible
```

---

## Comparación directa

| Aspecto | `antes/` | `despues/` |
|---|---|---|
| Polimorfismo | Roto, no confiable | Garantizado por LSP |
| Contrato de `enviar()` | Cambia según subclase | Siempre igual |
| Detección de errores | En tiempo de ejecución | En tiempo de compilación |
| `instanceof` en processor | Sí, obligatorio | No, innecesario |
| `UnsupportedOperationException` | Sí, frecuente | Imposible |
| Agregar nuevo canal | Modifica el procesador | No toca el procesador |
| Mecanismo de variación | Herencia | Composición + interfaces |

---

## Trade-offs

Aplicar LSP e ISP correctamente tiene costos reales que hay que considerar:

**Ventajas**
- APIs confiables y predecibles.
- Errores detectados por el compilador, no en producción.
- Agregar nuevos canales no rompe el código existente.
- El procesador no necesita conocer los detalles de cada canal.

**Desventajas**
- Más clases y más interfaces (puede parecer sobreingeniería en sistemas pequeños).
- Requiere diseñar bien el contrato desde el inicio.
- Si el dominio cambia mucho, el contrato puede quedarse corto.
- Puede entrar en conflicto con KISS y YAGNI en proyectos simples.
