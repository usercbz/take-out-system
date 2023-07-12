package com.cbz.takeoutsystem.interceptor;

import com.cbz.takeoutsystem.entity.Employee;
import com.cbz.takeoutsystem.entity.User;
import com.cbz.takeoutsystem.utils.EmployeeHolder;
import com.cbz.takeoutsystem.utils.UserHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static com.cbz.takeoutsystem.utils.RedisConst.*;
import static com.cbz.takeoutsystem.utils.SystemConst.CLIENT_TOKEN_HEADER;
import static com.cbz.takeoutsystem.utils.SystemConst.SERVER_TOKEN_HEADER;

/**
 * 系统拦截器
 */
public class SystemInterceptor implements HandlerInterceptor {

    private StringRedisTemplate redisTemplate;
    private ObjectMapper jsonMapper;

    public SystemInterceptor(StringRedisTemplate redisTemplate, ObjectMapper jsonMapper) {
        this.redisTemplate = redisTemplate;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取根据token 查询redis获取用户
        String serverToken = request.getHeader(SERVER_TOKEN_HEADER);
        String clientToken = request.getHeader(CLIENT_TOKEN_HEADER);

        boolean flag = false;

        if ((serverToken == null || serverToken.equals("")) && (clientToken == null || clientToken.equals(""))) {
            //未登录
            response.setStatus(401);
            return false;
        }

        if (serverToken != null && !serverToken.equals("")) {
            //server端
            //查询redis
            String key = LOGIN_EMPLOYEE_TOKEN + serverToken;
            String jsonStr = redisTemplate.opsForValue().get(key);
            if (jsonStr != null && !jsonStr.equals("")) {
                Employee employee = jsonMapper.readValue(jsonStr, Employee.class);

                if (employee != null) {
                    EmployeeHolder.saveEmployee(employee);
                    //刷新有效期
                    redisTemplate.expire(key, LOGIN_EMPLOYEE_TOKEN_TTL, TimeUnit.DAYS);
                    flag = true;
                }
            }
        }

        if (clientToken != null && !clientToken.equals("")) {
            //client端
            //查询redis
            String key = LOGIN_USER_TOKEN + clientToken;
            String jsonStr = redisTemplate.opsForValue().get(key);
            if (jsonStr != null && !jsonStr.equals("")) {
                User user = jsonMapper.readValue(jsonStr, User.class);

                if (user != null) {
                    UserHolder.saveUser(user);
                    //刷新有效期
                    redisTemplate.expire(key, LOGIN_USER_TOKEN_TTL, TimeUnit.DAYS);
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        EmployeeHolder.removeEmployee();
        UserHolder.removeUser();
    }
}
