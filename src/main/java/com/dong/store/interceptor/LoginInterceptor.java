package com.dong.store.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检测拦截器类
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //自定义拦截器规则，如果uid等于null则重定向回登录页
        if (request.getSession().getAttribute("uid") == null){
            //重定向到login.html
            response.sendRedirect("/web/login.html");
            return false;
        }
        return true;
    }
}
