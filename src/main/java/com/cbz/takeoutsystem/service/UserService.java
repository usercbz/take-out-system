package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.dto.LoginForm;
import com.cbz.takeoutsystem.entity.User;
import com.cbz.takeoutsystem.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface UserService extends IService<User> {

    Result<HashMap<String, String>> login(LoginForm loginForm);

    Result<String> sendCode(String phone);

    Result<Object> logout(HttpServletRequest request);
}
