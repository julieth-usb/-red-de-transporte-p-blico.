package com.transporte;

import java.util.Comparator;
import java.util.List;

public class RouteDecisionEngine {
    private final double riskPenaltyMinutes;

    public RouteDecisionEngine(double riskPenaltyMinutes) {
        if (riskPenaltyMinutes < 0) {
            throw new IllegalArgumentException("penalización de riesgo inválida");
        }
        this.riskPenaltyMinutes = riskPenaltyMinutes;
    }

    public Route chooseBestRoute(List<Route> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException("no hay rutas candidatas");
        }
        return candidates.stream()
                .min(Comparator.comparingDouble(this::score))
                .orElseThrow();
    }

    private double score(Route route) {
        return route.getEstimatedMinutes() * (1.0 + route.getCongestionLevel())
                + (1.0 - route.getReliabilityProbability()) * riskPenaltyMinutes;
    }
}
