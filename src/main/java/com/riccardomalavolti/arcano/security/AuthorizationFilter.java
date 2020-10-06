package com.riccardomalavolti.arcano.security;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.riccardomalavolti.arcano.exceptions.AccessDeniedException;

@Provider
@Dependent
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        // @DenyAll > @RolesAllowed > @PermitAll
        if (method.isAnnotationPresent(DenyAll.class)) {
            throw new AccessDeniedException("You don't have permissions to perform this action.");
        }

        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
            return;
        }

        if (method.isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }


        rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
        }

        if (resourceInfo.getResourceClass().isAnnotationPresent(PermitAll.class)) {
            // Do nothing
            return;
        }

        // Authentication is required for non-annotated methods
        if (!isAuthenticated(requestContext)) {
            throw new AccessDeniedException("Authentication is required to perform this action.");
        }
    }

    /**
     * Perform authorization based on roles.
     *
     * @param rolesAllowed
     * @param requestContext
     */
    private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) {

        if (rolesAllowed.length > 0 && !isAuthenticated(requestContext)) {
            throw new AccessDeniedException("Authentication is required to perform this action.");
        }

        for (final String role : rolesAllowed) {
            if (requestContext.getSecurityContext().isUserInRole(role)) {
                return;
            }
        }

        throw new AccessDeniedException("You don't have permissions to perform this action.");
    }

    /**
     * Check if the user is authenticated.
     *
     * @param requestContext
     * @return
     */
    private boolean isAuthenticated(final ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }
}