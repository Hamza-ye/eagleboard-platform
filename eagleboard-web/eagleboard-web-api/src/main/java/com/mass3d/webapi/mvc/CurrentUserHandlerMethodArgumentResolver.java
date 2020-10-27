package com.mass3d.webapi.mvc;

import com.mass3d.user.CurrentUserService;
import com.mass3d.user.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver
{
    private final CurrentUserService currentUserService;

    public CurrentUserHandlerMethodArgumentResolver( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }

    @Override
    public boolean supportsParameter( MethodParameter parameter )
    {
        return "currentUser".equals( parameter.getParameterName() )
            && User.class.isAssignableFrom( parameter.getParameterType() );
    }

    @Override
    public Object resolveArgument( MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory ) throws Exception
    {
        return currentUserService.getCurrentUser();
    }
}
