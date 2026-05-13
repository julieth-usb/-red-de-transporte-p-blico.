package com.transporte;

public class Route {
    private final String id;
    private final double estimatedMinutes;
    private final double congestionLevel;
    private final double reliabilityProbability;

    public Route(String id, double estimatedMinutes, double congestionLevel, double reliabilityProbability) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id de ruta inválido");
        }
        if (estimatedMinutes < 0) {
            throw new IllegalArgumentException("tiempo estimado inválido");
        }
        validateProbability(congestionLevel, "nivel de congestión");
        validateProbability(reliabilityProbability, "probabilidad de confiabilidad");
        this.id = id;
        this.estimatedMinutes = estimatedMinutes;
        this.congestionLevel = congestionLevel;
        this.reliabilityProbability = reliabilityProbability;
    }

    private static void validateProbability(double value, String fieldName) {
        if (value < 0 || value > 1) {
            throw new IllegalArgumentException(fieldName + " fuera de rango [0,1]");
        }
    }

    public String getId() {
        return id;
    }

    public double getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public double getCongestionLevel() {
        return congestionLevel;
    }

    public double getReliabilityProbability() {
        return reliabilityProbability;
    }
}
