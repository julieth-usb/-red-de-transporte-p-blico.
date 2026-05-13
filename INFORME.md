# Informe breve: lógica, probabilidad, grafos y programación

## 1) Grafos en la red de transporte

La red se modela como un grafo no dirigido donde:

- cada **estación** es un vértice,
- cada **conexión entre estaciones** es una arista.

El programa muestra:

1. Lista de conexiones por estación (visualización de adyacencias).
2. Conectividad entre dos estaciones (existencia de camino con BFS).
3. Rutas disponibles entre origen y destino (DFS para rutas simples).
4. Ruta más conveniente (camino con menor número de estaciones mediante BFS).

## 2) Lógica proposicional para control de acceso

Variables booleanas:

- \(T\): tarjeta activa.
- \(S\): saldo suficiente.
- \(F\): falla técnica.

Regla de acceso:

\[
Acceso = T \land S \land \neg F
\]

El programa recorre las 8 combinaciones posibles y determina si el acceso es PERMITIDO o DENEGADO.

## 3) Probabilidad de retrasos con distribución binomial

Datos del problema:

- \(n = 12\) buses diarios,
- \(p = 0.2\) probabilidad de retraso por bus,
- \(X \sim Binomial(n=12, p=0.2)\).

Fórmula:

\[
P(X=k)=\binom{n}{k}p^k(1-p)^{n-k}
\]

### a) Probabilidad de exactamente 3 buses con retraso

\[
P(X=3)=\binom{12}{3}(0.2)^3(0.8)^9
\]

\[
\binom{12}{3}=220
\]

\[
P(X=3)=220(0.008)(0.134217728)\approx 0.236223
\]

### b) Probabilidad de máximo 2 buses con retraso

\[
P(X\le 2)=P(X=0)+P(X=1)+P(X=2)
\]

\[
P(X=0)=\binom{12}{0}(0.2)^0(0.8)^{12}=0.068719
\]

\[
P(X=1)=\binom{12}{1}(0.2)^1(0.8)^{11}=0.206158
\]

\[
P(X=2)=\binom{12}{2}(0.2)^2(0.8)^{10}=0.283467
\]

\[
P(X\le2)\approx 0.068719 + 0.206158 + 0.283467 = 0.558345
\]

### c) Probabilidad de al menos un bus con retraso

\[
P(X\ge1)=1-P(X=0)=1-0.068719=0.931281
\]

## 4) Comparación teórica vs experimental

Además del cálculo analítico, el programa realiza simulaciones aleatorias (Monte Carlo) para muchos días y estima:

- \(P(X=3)\),
- \(P(X\le2)\),
- \(P(X\ge1)\).

Al aumentar la cantidad de simulaciones, los valores experimentales convergen a los resultados teóricos de la distribución binomial.

## 5) Relación entre componentes

- **Grafos** permiten modelar y analizar la movilidad en la ciudad.
- **Lógica proposicional** formaliza reglas de acceso operativas.
- **Probabilidad** cuantifica incertidumbre de eventos (retrasos).
- **Programación en Java** integra todo en una solución ejecutable única para soporte de decisiones.
