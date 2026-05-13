package com.transporte;

import java.util.Set;

public class AccessRequest {
    private final String userId;
    private final boolean hasValidTicket;
    private final boolean restrictedUser;
    private final String requestedZone;
    private final Set<String> authorizedZones;

    public AccessRequest(
            String userId,
            boolean hasValidTicket,
            boolean restrictedUser,
            String requestedZone,
            Set<String> authorizedZones) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("usuario inválido");
        }
        if (requestedZone == null || requestedZone.isBlank()) {
            throw new IllegalArgumentException("zona solicitada inválida");
        }
        if (authorizedZones == null) {
            throw new IllegalArgumentException("zonas autorizadas requeridas");
        }
        this.userId = userId;
        this.hasValidTicket = hasValidTicket;
        this.restrictedUser = restrictedUser;
        this.requestedZone = requestedZone;
        this.authorizedZones = Set.copyOf(authorizedZones);
    }

    public String getUserId() {
        return userId;
    }

    public boolean hasValidTicket() {
        return hasValidTicket;
    }

    public boolean isRestrictedUser() {
        return restrictedUser;
    }

    public String getRequestedZone() {
        return requestedZone;
    }

    public Set<String> getAuthorizedZones() {
        return authorizedZones;
    }
}
