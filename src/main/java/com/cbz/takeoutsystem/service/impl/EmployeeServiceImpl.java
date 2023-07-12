package com.cbz.takeoutsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbz.takeoutsystem.entity.Employee;
import com.cbz.takeoutsystem.mapper.EmployeeMapper;
import com.cbz.takeoutsystem.service.EmployeeService;
import com.cbz.takeoutsystem.utils.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cbz.takeoutsystem.utils.RedisConst.LOGIN_EMPLOYEE_TOKEN;
import static com.cbz.takeoutsystem.utils.SystemConst.SERVER_TOKEN_HEADER;

@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper jsonMapper;

    /**
     * 登录验证
     *
     * @param employee 员工信息: 用户名、密码
     * @return 处理结果集
     */
    @Override
    public Result<Object> login(Employee employee) {
        //登录验证
        //获取前端传来的用户名密码
        String username = employee.getUsername();
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username).eq(Employee::getPassword, password);
        Employee employeeData = getOne(wrapper);
        if (employeeData == null) {
            //未查询到数据
            return Result.error("用户名或密码错误！");
        }
        if (employeeData.getStatus() == 0) {
            //禁用
            return Result.error("用户状态异常！");
        }
        //成功
        //生成token
        String token = UUID.randomUUID().toString();
        try {
            //保存token到redis key= login:token: + {token}
            redisTemplate.opsForValue().set(LOGIN_EMPLOYEE_TOKEN + token, jsonMapper.writeValueAsString(employeeData), 3L, TimeUnit.DAYS);
            HashMap<String, Object> map = new HashMap<>();
            map.put("userInfo", employeeData);
            map.put("token", token);
            //返回用户数据和token
            return Result.success(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Result.error("登录失败！");
    }

    /**
     * 查询员工分页信息
     *
     * @param page     页起始索引
     * @param pageSize 页大小
     * @param name     姓名
     * @return 分页数据
     */
    @Override
    public Result<Object> queryEmployeesPage(Integer page, Integer pageSize, String name) {
        //查询分页数据
        LambdaQueryWrapper<Employee> wrapper = null;
        if (name != null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.like(Employee::getName, name);
        }
        Page<Employee> employeePage = new Page<>(page, pageSize);

        return Result.success(page(employeePage, wrapper));
    }

    /**
     * 根据id 查询员工信息
     *
     * @param id 目标员工id
     * @return 目标员工数据
     */
    @Override
    public Result<Employee> queryEmployeeById(Integer id) {
        //根据id查询员工
        Employee employee = getById(id);

        if (employee == null) {
            return Result.error("查询失败");
        }
        return Result.success(employee);
    }

    /**
     * 添加员工
     *
     * @param employee 待添加员工信息
     * @return 处理结果集
     */
    @Override
    public Result<Object> addEmployee(Employee employee) {
        if (employee == null) {
            return Result.error("添加失败");
        }
        log.info(employee.toString());
        //填充数据
        //生成默认密码，身份证后六位，
        String password = employee.getIdNumber().substring(12);
        //设置密码
        employee.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        //添加员工
        if (save(employee)) {
            return Result.success(null);
        }
        return Result.error("添加失败");
    }

    /**
     * 编辑员工信息
     *
     * @param employee 待处理员工信息
     * @return 处理结果集
     */
    @Override
    public Result<Object> editEmployee(Employee employee) {
        if (employee == null) {
            return Result.error("修改失败！");
        }
        //置空
        employee.setUpdateTime(null);
        employee.setUpdateUser(null);

        //修改
        if (updateById(employee)) {
            //成功
            return Result.success(null);
        }
        //失败
        return Result.error("修改失败！");
    }

    /**
     * 退出登录
     *
     * @param request 请求，获取请求头数据token
     * @return 结果集
     */
    @Override
    public Result<Object> logout(HttpServletRequest request) {
        //删除redis缓存
        String token = request.getHeader(SERVER_TOKEN_HEADER);
        redisTemplate.delete(LOGIN_EMPLOYEE_TOKEN + token);
        return Result.success(null);
    }
}
