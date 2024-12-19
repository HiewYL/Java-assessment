package com.profile.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        //Incoming request details
        logRequestDetails(wrappedRequest);

        //Process request
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        //Response details
        logResponseDetails(wrappedResponse);

        //write back response to client
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Params: {}", request.getParameterMap());
        logger.info("Request Body: {}", new String(request.getContentAsByteArray()));
    }

    private void logResponseDetails(ContentCachingResponseWrapper response) {
        logger.info("Response Status: {}", response.getStatus());
        try {
            logger.info("Response Body: {}", new String(response.getContentAsByteArray()));
        } catch (Exception e) {
            logger.error("Error while logging response body", e);
        }
    }
}
