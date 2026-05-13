package com.transporte;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MobilityDecisionSystemTest {
    public static void main(String[] args) {
        shouldChooseBestRouteBalancingTimeAndRisk();
        shouldDenyAccessWhenTicketIsInvalid();
        shouldAllowAccessWhenWildcardZoneExists();
        shouldComputeCombinedEventProbability();
        shouldClassifyRiskByThresholds();
        System.out.println("All tests passed.");
    }

    private static void shouldChooseBestRouteBalancingTimeAndRisk() {
        MobilityDecisionService service = new MobilityDecisionService(30.0);
        Route best = service.decideRoute(List.of(
                new Route("R1", 24, 0.2, 0.95),
                new Route("R2", 20, 0.8, 0.60),
                new Route("R3", 27, 0.1, 0.90)));

        assertEquals("R1", best.getId(), "Debe elegir la ruta con mejor puntaje total");
    }

    private static void shouldDenyAccessWhenTicketIsInvalid() {
        MobilityDecisionService service = new MobilityDecisionService(30.0);
        boolean allowed = service.authorizeAccess(new AccessRequest(
                "u-1",
                false,
                false,
                "CENTRO",
                Set.of("CENTRO", "NORTE")));

        assertFalse(allowed, "Sin ticket válido debe negar acceso");
    }

    private static void shouldAllowAccessWhenWildcardZoneExists() {
        MobilityDecisionService service = new MobilityDecisionService(30.0);
        boolean allowed = service.authorizeAccess(new AccessRequest(
                "u-2",
                true,
                false,
                "SUR",
                Set.of("*")));

        assertTrue(allowed, "Con zona '*' debe permitir acceso");
    }

    private static void shouldComputeCombinedEventProbability() {
        ProbabilisticEventAnalyzer analyzer = new ProbabilisticEventAnalyzer();
        double probability = analyzer.combinedProbability(Map.of(
                "lluvia", 0.20,
                "accidente", 0.10));

        assertApproxEquals(0.28, probability, 0.0001, "Probabilidad combinada incorrecta");
    }

    private static void shouldClassifyRiskByThresholds() {
        ProbabilisticEventAnalyzer analyzer = new ProbabilisticEventAnalyzer();
        assertEquals(ProbabilisticEventAnalyzer.RiskLevel.LOW, analyzer.classifyRisk(0.10), "Riesgo bajo incorrecto");
        assertEquals(
                ProbabilisticEventAnalyzer.RiskLevel.MEDIUM,
                analyzer.classifyRisk(0.55),
                "Riesgo medio incorrecto");
        assertEquals(ProbabilisticEventAnalyzer.RiskLevel.HIGH, analyzer.classifyRisk(0.90), "Riesgo alto incorrecto");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " | esperado=" + expected + ", actual=" + actual);
        }
    }

    private static void assertApproxEquals(double expected, double actual, double tolerance, String message) {
        if (Math.abs(expected - actual) > tolerance) {
            throw new AssertionError(message + " | esperado=" + expected + ", actual=" + actual);
        }
    }
}
