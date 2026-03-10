package com.felfel.hogwarts_artifacts_online.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * The type Custom bearer token access denied handler.
 */
@Configuration
public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    /**
     * Instantiates a new Custom bearer token access denied handler.
     *
     * @param resolver the resolver
     */
    public CustomBearerTokenAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        resolver.resolveException(request,response,null,accessDeniedException);
    }
}
