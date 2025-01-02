package io.dev.jobprep.common.logging.mdc;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCRequestLoggingFilter implements Filter {

    // TODO: Filter vs OncePerRequestFilter, 어떤 부분에 대한 로깅을 적용할지

    private static final String REQUEST_HEADER = "X-RequestID";
    private static final String ID = "request_id";
    private static final String DASH = "-";
    private static final String EMPTY = "";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        var requestId = ((HttpServletRequest) request).getHeader(REQUEST_HEADER);
        MDC.put(ID, requestId == null || requestId.isBlank() ?
            UUID.randomUUID().toString().replace(DASH, EMPTY) : requestId);

        chain.doFilter(request, response);
        MDC.clear();

    }

}
