package com.cbz.takeoutsystem.interceptor;

import com.cbz.takeoutsystem.utils.EmployeeHolder;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.UserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
public class ServerLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (EmployeeHolder.getEmployee() == null) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeHolder.removeEmployee();
        UserHolder.removeUser();
    }
}
