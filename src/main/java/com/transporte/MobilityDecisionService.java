package com.transporte;

import java.util.List;
import java.util.Map;

public class MobilityDecisionService {
    private final RouteDecisionEngine routeDecisionEngine;
    private final AccessControlEngine accessControlEngine;
    private final ProbabilisticEventAnalyzer probabilisticEventAnalyzer;

    public MobilityDecisionService(double riskPenaltyMinutes) {
        this.routeDecisionEngine = new RouteDecisionEngine(riskPenaltyMinutes);
        this.accessControlEngine = new AccessControlEngine();
        this.probabilisticEventAnalyzer = new ProbabilisticEventAnalyzer();
    }

    public Route decideRoute(List<Route> routes) {
        return routeDecisionEngine.chooseBestRoute(routes);
    }

    public boolean authorizeAccess(AccessRequest request) {
        return accessControlEngine.canAccess(request);
    }

    public ProbabilisticEventAnalyzer.RiskLevel analyzeRisk(Map<String, Double> eventProbabilities) {
        double probability = probabilisticEventAnalyzer.combinedProbability(eventProbabilities);
        return probabilisticEventAnalyzer.classifyRisk(probability);
    }
}
