import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SistemaTransporteCucuta {

    public static void main(String[] args) {
        GrafoEstaciones red = construirRed();

        System.out.println("=== RED DE TRANSPORTE (GRAFO) ===");
        red.imprimirConexiones();

        String origen = "Centro";
        String destino = "Aeropuerto";

        System.out.println("\nRutas disponibles entre " + origen + " y " + destino + ":");
        List<List<String>> rutas = red.encontrarRutas(origen, destino);
        for (int i = 0; i < rutas.size(); i++) {
            System.out.println((i + 1) + ". " + String.join(" -> ", rutas.get(i)));
        }

        boolean hayConectividad = red.hayCamino(origen, destino);
        System.out.println("\n¿Existe conectividad entre " + origen + " y " + destino + "?: " + hayConectividad);

        List<String> rutaMasConveniente = red.rutaMasConveniente(origen, destino);
        System.out.println("Ruta más conveniente (menor número de estaciones): "
                + String.join(" -> ", rutaMasConveniente));

        System.out.println("\n=== CONTROL DE ACCESO (LÓGICA PROPOSICIONAL) ===");
        simularAccesos();

        System.out.println("\n=== ANÁLISIS DE RETRASOS (DISTRIBUCIÓN BINOMIAL) ===");
        int n = 12;
        double p = 0.2;

        double probExactamente3 = probabilidadBinomial(n, 3, p);
        double probMaximo2 = probabilidadAcumulada(n, 2, p);
        double probAlMenos1 = 1 - probabilidadBinomial(n, 0, p);

        System.out.printf("P(X = 3) = %.6f%n", probExactamente3);
        System.out.printf("P(X <= 2) = %.6f%n", probMaximo2);
        System.out.printf("P(X >= 1) = %.6f%n", probAlMenos1);

        int simulaciones = 200000;
        ResultadoExperimental experimental = simularRetrasosExperimental(n, p, simulaciones, 42L);

        System.out.println("\nComparación con resultados experimentales (" + simulaciones + " simulaciones):");
        System.out.printf("Experimental P(X = 3) ≈ %.6f%n", experimental.exactamente3);
        System.out.printf("Experimental P(X <= 2) ≈ %.6f%n", experimental.maximo2);
        System.out.printf("Experimental P(X >= 1) ≈ %.6f%n", experimental.alMenos1);
    }

    private static GrafoEstaciones construirRed() {
        GrafoEstaciones grafo = new GrafoEstaciones();
        grafo.conectar("Centro", "Terminal");
        grafo.conectar("Centro", "La Libertad");
        grafo.conectar("Terminal", "Clínica Norte");
        grafo.conectar("La Libertad", "Universidad");
        grafo.conectar("Universidad", "Aeropuerto");
        grafo.conectar("Clínica Norte", "Aeropuerto");
        grafo.conectar("Terminal", "Universidad");
        return grafo;
    }

    private static void simularAccesos() {
        System.out.println("TarjetaActiva | SaldoSuficiente | FallaTecnica | Acceso");
        for (boolean tarjetaActiva : Arrays.asList(false, true)) {
            for (boolean saldoSuficiente : Arrays.asList(false, true)) {
                for (boolean fallaTecnica : Arrays.asList(false, true)) {
                    boolean acceso = permitirAcceso(tarjetaActiva, saldoSuficiente, fallaTecnica);
                    System.out.printf("%-13s | %-15s | %-12s | %s%n",
                            tarjetaActiva,
                            saldoSuficiente,
                            fallaTecnica,
                            acceso ? "PERMITIDO" : "DENEGADO");
                }
            }
        }
    }

    private static boolean permitirAcceso(boolean tarjetaActiva, boolean saldoSuficiente, boolean fallaTecnica) {
        return tarjetaActiva && saldoSuficiente && !fallaTecnica;
    }

    private static double probabilidadBinomial(int n, int k, double p) {
        return combinacion(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
    }

    private static double probabilidadAcumulada(int n, int kMax, double p) {
        double suma = 0.0;
        for (int k = 0; k <= kMax; k++) {
            suma += probabilidadBinomial(n, k, p);
        }
        return suma;
    }

    private static double combinacion(int n, int k) {
        if (k < 0 || k > n) {
            return 0.0;
        }
        int ajustado = Math.min(k, n - k);
        double resultado = 1.0;
        for (int i = 1; i <= ajustado; i++) {
            resultado *= (double) (n - ajustado + i) / i;
        }
        return resultado;
    }

    private static ResultadoExperimental simularRetrasosExperimental(int busesPorDia,
                                                                      double probRetraso,
                                                                      int simulaciones,
                                                                      long semilla) {
        Random random = new Random(semilla);
        int conteoExactamente3 = 0;
        int conteoMaximo2 = 0;
        int conteoAlMenos1 = 0;

        for (int dia = 0; dia < simulaciones; dia++) {
            int retrasados = 0;
            for (int bus = 0; bus < busesPorDia; bus++) {
                if (random.nextDouble() < probRetraso) {
                    retrasados++;
                }
            }

            if (retrasados == 3) {
                conteoExactamente3++;
            }
            if (retrasados <= 2) {
                conteoMaximo2++;
            }
            if (retrasados >= 1) {
                conteoAlMenos1++;
            }
        }

        return new ResultadoExperimental(
                conteoExactamente3 / (double) simulaciones,
                conteoMaximo2 / (double) simulaciones,
                conteoAlMenos1 / (double) simulaciones
        );
    }

    private static final class ResultadoExperimental {
        private final double exactamente3;
        private final double maximo2;
        private final double alMenos1;

        private ResultadoExperimental(double exactamente3, double maximo2, double alMenos1) {
            this.exactamente3 = exactamente3;
            this.maximo2 = maximo2;
            this.alMenos1 = alMenos1;
        }
    }

    private static final class GrafoEstaciones {
        private final Map<String, Set<String>> adyacencias = new HashMap<>();

        private void conectar(String a, String b) {
            adyacencias.computeIfAbsent(a, key -> new HashSet<>()).add(b);
            adyacencias.computeIfAbsent(b, key -> new HashSet<>()).add(a);
        }

        private void imprimirConexiones() {
            for (Map.Entry<String, Set<String>> entry : adyacencias.entrySet()) {
                System.out.println(entry.getKey() + " -> " + String.join(", ", entry.getValue()));
            }
        }

        private boolean hayCamino(String origen, String destino) {
            if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
                return false;
            }

            Set<String> visitados = new HashSet<>();
            Deque<String> cola = new ArrayDeque<>();
            cola.add(origen);
            visitados.add(origen);

            while (!cola.isEmpty()) {
                String actual = cola.poll();
                if (actual.equals(destino)) {
                    return true;
                }
                for (String vecino : adyacencias.getOrDefault(actual, Set.of())) {
                    if (visitados.add(vecino)) {
                        cola.add(vecino);
                    }
                }
            }
            return false;
        }

        private List<String> rutaMasConveniente(String origen, String destino) {
            if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
                return List.of();
            }

            Map<String, String> previo = new HashMap<>();
            Set<String> visitados = new HashSet<>();
            Deque<String> cola = new ArrayDeque<>();
            cola.add(origen);
            visitados.add(origen);

            while (!cola.isEmpty()) {
                String actual = cola.poll();
                if (actual.equals(destino)) {
                    break;
                }
                for (String vecino : adyacencias.getOrDefault(actual, Set.of())) {
                    if (visitados.add(vecino)) {
                        previo.put(vecino, actual);
                        cola.add(vecino);
                    }
                }
            }

            if (!visitados.contains(destino)) {
                return List.of();
            }

            List<String> ruta = new ArrayList<>();
            String actual = destino;
            while (actual != null) {
                ruta.add(0, actual);
                actual = previo.get(actual);
            }
            return ruta;
        }

        private List<List<String>> encontrarRutas(String origen, String destino) {
            List<List<String>> rutas = new ArrayList<>();
            if (!adyacencias.containsKey(origen) || !adyacencias.containsKey(destino)) {
                return rutas;
            }
            buscarRutasDFS(origen, destino, new HashSet<>(), new ArrayList<>(), rutas);
            return rutas;
        }

        private void buscarRutasDFS(String actual,
                                    String destino,
                                    Set<String> visitados,
                                    List<String> camino,
                                    List<List<String>> rutas) {
            visitados.add(actual);
            camino.add(actual);

            if (actual.equals(destino)) {
                rutas.add(new ArrayList<>(camino));
            } else {
                for (String vecino : adyacencias.getOrDefault(actual, Set.of())) {
                    if (!visitados.contains(vecino)) {
                        buscarRutasDFS(vecino, destino, visitados, camino, rutas);
                    }
                }
            }

            camino.remove(camino.size() - 1);
            visitados.remove(actual);
        }
    }
}
