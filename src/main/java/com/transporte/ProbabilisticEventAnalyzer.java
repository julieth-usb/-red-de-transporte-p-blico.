package com.transporte;

import java.util.Map;

public class ProbabilisticEventAnalyzer {
    public double combinedProbability(Map<String, Double> eventProbabilities) {
        if (eventProbabilities == null || eventProbabilities.isEmpty()) {
            throw new IllegalArgumentException("se requieren eventos probabilísticos");
        }

        double probabilityNoEvent = 1.0;
        for (double probability : eventProbabilities.values()) {
            if (probability < 0 || probability > 1) {
                throw new IllegalArgumentException("probabilidad fuera de rango [0,1]");
            }
            probabilityNoEvent *= (1.0 - probability);
        }
        return 1.0 - probabilityNoEvent;
    }

    public RiskLevel classifyRisk(double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("probabilidad fuera de rango [0,1]");
        }
        if (probability < 0.30) {
            return RiskLevel.LOW;
        }
        if (probability < 0.70) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH
    }
}
