package cc.restfulmc.api.log;

import cc.restfulmc.api.common.Constants;
import cc.restfulmc.api.common.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Braydon
 */
@ControllerAdvice
@Log4j2(topic = "Request Logger")
public class RequestLogger implements ResponseBodyAdvice<Object> {
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest rawRequest,
                                  @NonNull ServerHttpResponse rawResponse) {
        HttpServletRequest request = ((ServletServerHttpRequest) rawRequest).getServletRequest();
        HttpServletResponse response = ((ServletServerHttpResponse) rawResponse).getServletResponse();

        // Calculate processing time
        Long startTime = (Long) request.getAttribute(Constants.REQUEST_START_TIME_ATTRIBUTE);
        long processingTime = startTime != null ? System.currentTimeMillis() - startTime : -1;

        log.info("[{}] {} | {} | '{}' | {}ms",
                response.getStatus(),
                request.getMethod(),
                IPUtils.getRealIp(request),
                request.getRequestURI(),
                processingTime
        );
        return body;
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}