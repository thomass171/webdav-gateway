package de.yard.webdavproxy.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;

/**
 *
 */
@Component
@Configuration
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @SuppressWarnings({"resource", "NullableProblems"})
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final boolean isActuator = request.getRequestURL().toString().contains("actuator");
        String method = request.getMethod();
        LocalDateTime startTime = LocalDateTime.now();

        if (!isActuator) {
            logRequest(method, request);
        }
        filterChain.doFilter(request, response);
        if (!isActuator) {
            logResponse(response, startTime);
        }
    }

    private void logRequest(String method, HttpServletRequest request) {

        Enumeration<String> headerIterator = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerIterator.hasMoreElements()) {
            String header = headerIterator.nextElement();
            String value = request.getHeader(header);
            if ("authorization".equals(StringUtils.lowerCase(header))) {
                log.debug("Suppressed info log for authorization header value '{}'", value);
                value = "...";
            }
            headers.append(header).append("=").append(value).append(",");
        }
        String querystring = StringUtils.defaultString(request.getQueryString());
        log.info("Request (method={},url={},header=[{}],querystring={})", method, request.getRequestURL(), headers, querystring);

    }

    private void logResponse(HttpServletResponse response, LocalDateTime startTime) {
        log.info("Response (status={}, duration={} ms)", response.getStatus(), ChronoUnit.MILLIS.between(startTime, LocalDateTime.now()));
    }
}

