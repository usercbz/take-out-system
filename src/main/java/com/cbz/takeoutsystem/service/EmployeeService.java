package com.cbz.takeoutsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbz.takeoutsystem.entity.Employee;
import com.cbz.takeoutsystem.utils.Result;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Result<Object> login(Employee employee);

    Result<Object> queryEmployeesPage(Integer page, Integer pageSize,String name);

    Result<Employee> queryEmployeeById(Integer id);

    Result<Object> addEmployee(Employee employee);

    Result<Object> editEmployee(Employee employee);

    Result<Object> logout(HttpServletRequest request);
}
