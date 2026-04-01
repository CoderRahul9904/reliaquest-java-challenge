package com.challenge.api.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 * Servlet filter that enforces API key authentication on all incoming requests.
 * Consumers must supply a valid X-API-Key header to access employee endpoints.
 */
@Component
public class ApiSecurityFilter implements Filter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String VALID_API_KEY = "rq-secret-key-2024";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String apiKey = httpRequest.getHeader(API_KEY_HEADER);
        if (!VALID_API_KEY.equals(apiKey)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Unauthorized: invalid or missing API key\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
