package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.dto.LoginForm;
import com.cbz.takeoutsystem.entity.User;
import com.cbz.takeoutsystem.mapper.UserMapper;
import com.cbz.takeoutsystem.service.UserService;
import com.cbz.takeoutsystem.utils.RedisConst;
import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.ValidateCodeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cbz.takeoutsystem.utils.RedisConst.*;
import static com.cbz.takeoutsystem.utils.SystemConst.CLIENT_TOKEN_HEADER;
import static com.cbz.takeoutsystem.utils.SystemConst.DEFAULT_CODE_LEN;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 登录
     *
     * @param loginForm 登录数据
     * @return 处理结果集
     */
    @Override
    public Result<HashMap<String, String>> login(LoginForm loginForm) {
        //数据
        String phone = loginForm.getPhone();
        String formCode = loginForm.getCode();
        //验证码校验

        if (phone == null || formCode == null) {
            return Result.error("登录失败！");
        }

        //查询redis，获取验证码
        String cacheCode = redisTemplate.opsForValue().get(LOGIN_CODE_PRE + phone);
        //判断
        if (cacheCode == null || !cacheCode.equals(formCode)) {
            //验证码错误
            return Result.error("验证码有误！");
        }
        //验证码正确
        //根据phone查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = getOne(wrapper);

        //查询成功
        if (user == null) {
            //查询失败
            //新建用户
            user = new User();
            user.setPhone(phone);
            save(user);
        }
        String token = UUID.randomUUID().toString();
        try {
            //存入redis
            redisTemplate.opsForValue().set(LOGIN_USER_TOKEN + token, jsonMapper.writeValueAsString(user), LOGIN_USER_TOKEN_TTL, TimeUnit.DAYS);
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("username", user.getName());
            return Result.success(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.error("登录失败！");
    }

    @Override
    public Result<String> sendCode(String phone) {
        //发送验证码
        //生成验证码
        String code = ValidateCodeUtils.generateValidateCode(DEFAULT_CODE_LEN);
        //保存验证码
        redisTemplate.opsForValue().set(RedisConst.LOGIN_CODE_PRE + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.info(code);//打印到控制台
        //回调
        return Result.success("发生验证码成功");
    }

    @Override
    public Result<Object> logout(HttpServletRequest request) {
        //退出功能
        //redis 删除缓存
        String token = request.getHeader(CLIENT_TOKEN_HEADER);
        redisTemplate.delete(LOGIN_USER_TOKEN + token);
        return Result.success(null);
    }
}
