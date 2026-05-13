package com.transporte;

public class AccessControlEngine {
    public boolean canAccess(AccessRequest request) {
        if (!request.hasValidTicket()) {
            return false;
        }
        if (request.isRestrictedUser()) {
            return false;
        }
        return request.getAuthorizedZones().contains("*")
                || request.getAuthorizedZones().contains(request.getRequestedZone());
    }
}
