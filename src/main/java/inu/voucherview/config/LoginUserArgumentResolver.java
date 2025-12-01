package inu.voucherview.config;

import inu.voucherview.annotation.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginUser 어노테이션이 있고, Long 타입인 파라미터만 지원
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // JwtAuthenticationFilter에서 설정한 userId 가져오기
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new IllegalArgumentException("로그인이 필요합니다");
        }

        return userId;
    }
}
