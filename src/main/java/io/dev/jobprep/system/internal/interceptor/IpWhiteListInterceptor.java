package io.dev.jobprep.system.internal.interceptor;

import io.dev.jobprep.system.internal.whitelist.application.WhiteListManager;
import io.dev.jobprep.util.IpAddressHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpWhiteListInterceptor implements HandlerInterceptor {

    private static final String LOCAL = "127.0.0.1";

    private final WhiteListManager whiteListManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String clientIp = IpAddressHelper.getClientIp(request);
        log.info("Call internal API fron client with accessIp {}", clientIp);

        if (clientIp.equals(LOCAL)) {
            return true;
        }

        if (!whiteListManager.isBelongWhiteList(clientIp)) {
            log.warn("Detect Non-allowed accessIp {} for URI {} access!", clientIp, request.getRequestURI());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non-allowed Ip-Address!");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
