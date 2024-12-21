package com.tango.gateway_service.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> permittedEndpoints = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/initiate-password-reset",
            "/api/v1/auth/complete-password-reset"
    );

//    public Predicate<ServerHttpRequest> isSecured =
//            request -> permittedEndpoints
//                    .stream()
//                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

//    public Predicate<ServerHttpRequest> isSecured =
//            request -> {
//                String requestPath = request.getURI().getPath();
//                boolean shouldBypass = permittedEndpoints
//                        .stream()
//                        .noneMatch(requestPath::contains);
//
//                if (shouldBypass) {
//                    System.err.println("Bypassing authentication for: " + requestPath);
//                }
//
//                return shouldBypass;
//            };

    // Predicate to determine if the request should be secured or not
    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String requestPath = request.getURI().getPath();
                // Check if the request path is in the permitted list
                boolean shouldBypass = permittedEndpoints
                        .stream()
                        .anyMatch(requestPath::contains); // Return true if it's in the list, meaning it should bypass the check

                if (shouldBypass) {
                    System.err.println("Bypassing authentication for: " + requestPath);
                }

                return !shouldBypass; // If it's in the permitted list, return false (no JWT needed)
            };

}
