package com.bluebik.userurlapi.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WebFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init LoggingFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        String clientIP = getClientIpAddress(servletRequest);
        logger.info("Client IP "+clientIP+" has accessed to resource");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("Destroy LoggingFilter");
    }

    private static String getClientIpAddress(ServletRequest servletReuest) {
        HttpServletRequest clientRequest = (HttpServletRequest) servletReuest;
        String xForwardedForHeader = clientRequest.getHeader("X-Forwarded-For");
        String ipAddress = "";
        if (xForwardedForHeader == null) {
            ipAddress =  clientRequest.getRemoteAddr();
        }
        return ipAddress;
    }
}
