package io.dev.jobprep.common.auth.resolver;

import io.dev.jobprep.common.auth.JwtToken;
import io.dev.jobprep.domain.security.jwt.exception.TokenException;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

@Component
public class JwtTokenAnnotationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(JwtToken.class) != null;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory
    ) throws Exception {

        UsernamePasswordAuthenticationToken authentication = getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            if (principalDetails == null || principalDetails.getUsername().isEmpty()) {
                throw new TokenException(AUTH_MISSING_CREDENTIALS);
            }
            return (Long) Long.parseLong(principalDetails.getUsername());
        }
        throw new TokenException(AUTH_MISSING_CREDENTIALS);
    }

    private UsernamePasswordAuthenticationToken getAuthentication() {
        return (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }
}
