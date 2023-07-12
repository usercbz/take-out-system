package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.dto.LoginForm;
import com.cbz.takeoutsystem.service.impl.UserServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("login")
    public Result<HashMap<String,String>> login(@RequestBody LoginForm loginForm) {
        return userService.login(loginForm);
    }

    @GetMapping("code")
    public Result<String> sendCode(@RequestParam String phone) {
        return userService.sendCode(phone);
    }

    @PostMapping("logout")
    public Result<Object> logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
