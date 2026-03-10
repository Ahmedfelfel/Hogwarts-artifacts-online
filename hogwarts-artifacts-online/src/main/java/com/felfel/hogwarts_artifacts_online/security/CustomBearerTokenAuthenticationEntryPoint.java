package com.felfel.hogwarts_artifacts_online.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * The type Custom bearer token authentication entry point.
 */
@Component
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    /**
     * Instantiates a new Custom bearer token authentication entry point.
     *
     * @param resolver the resolver
     */
    public CustomBearerTokenAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate","Basic realm=\"Hogwarts Artifacts Online\"");
        resolver.resolveException(request,response,null,authException);
    }
}
