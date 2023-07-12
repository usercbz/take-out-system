package com.cbz.takeoutsystem.interceptor;

import com.cbz.takeoutsystem.utils.EmployeeHolder;
import com.cbz.takeoutsystem.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (UserHolder.getUser() != null) {
            response.setStatus(401);
            return true;
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeHolder.removeEmployee();
        UserHolder.removeUser();
    }
}
