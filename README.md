# -red-de-transporte-p-blico.

Solución Java mínima para apoyar decisiones inteligentes en un sistema de movilidad:

- **Rutas inteligentes:** selección de la mejor ruta según tiempo estimado, congestión y confiabilidad.
- **Control de acceso:** validación de acceso por ticket, estado del usuario y zonas autorizadas.
- **Eventos probabilísticos:** cálculo del riesgo combinado de eventos y clasificación del nivel de riesgo.

## Estructura

- `src/main/java/com/transporte/...`: implementación principal.
- `src/test/java/com/transporte/MobilityDecisionSystemTest.java`: pruebas básicas ejecutables con Java estándar.

## Compilar y ejecutar pruebas

```bash
mkdir -p out
javac -d out $(find src/main/java src/test/java -name "*.java")
java -cp out com.transporte.MobilityDecisionSystemTest
```
