package com.tango.gateway_service.config;

import com.tango.gateway_service.exceptions.MissingAuthorizationHeaderException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final RouteValidator routeValidator;
    private final JwtService jwtService;

    public JwtAuthFilter(RouteValidator routeValidator, JwtService jwtService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MissingAuthorizationHeaderException("Missing Authorization header");
                }

//                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwt = "";
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwt = authHeader.substring(7);
                }

                try {
                    boolean isValid = jwtService.isValidToken(jwt);
                    if (!isValid) {
                        throw new RuntimeException("Invalid token");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}