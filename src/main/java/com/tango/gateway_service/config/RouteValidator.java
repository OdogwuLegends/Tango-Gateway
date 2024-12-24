package com.tango.gateway_service.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> permittedEndpoints = List.of(
            "/api/v1/auth/sign-up",
            "/api/v1/auth/login",
            "/api/v1/auth/initiate-password-reset",
            "/api/v1/auth/complete-password-reset"
    );


    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String requestPath = request.getURI().getPath();
                boolean shouldBypass = permittedEndpoints
                        .stream()
                        .anyMatch(requestPath::contains);
                return !shouldBypass;
            };

}
