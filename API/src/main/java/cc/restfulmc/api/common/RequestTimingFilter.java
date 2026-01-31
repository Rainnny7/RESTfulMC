package cc.restfulmc.api.common;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @author Braydon
 */
public final class RequestTimingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request.setAttribute(Constants.REQUEST_START_TIME_ATTRIBUTE, System.currentTimeMillis());
        }
        chain.doFilter(request, response);
    }
}