package com.cbz.takeoutsystem.utils;

/**
 * redis常量
 */
public class RedisConst {

    public final static String LOGIN_EMPLOYEE_TOKEN = "login:token:employee:";
    public final static Long LOGIN_EMPLOYEE_TOKEN_TTL = 3L;

    public final static String LOGIN_USER_TOKEN = "login:token:user:";
    public final static Long LOGIN_USER_TOKEN_TTL = 3L;

    public final static String LOGIN_CODE_PRE = "login:code:";
    public final static Long LOGIN_CODE_TTL = 10L;
}
