package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.entity.Employee;
import com.cbz.takeoutsystem.service.impl.EmployeeServiceImpl;
import com.cbz.takeoutsystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @PostMapping("login")
    public Result<Object> login(@RequestBody Employee employee) {
        return employeeService.login(employee);
    }

    @GetMapping("page")
    public Result<Object> getEmployeesPage(@RequestParam Integer page, @RequestParam Integer pageSize,@RequestParam(required = false) String name) {
        return employeeService.queryEmployeesPage(page, pageSize,name);
    }

    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable Integer id) {
        return employeeService.queryEmployeeById(id);
    }

    @PostMapping
    public Result<Object> addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping
    public Result<Object> editEmployee(@RequestBody Employee employee) {
        return employeeService.editEmployee(employee);
    }

    @PostMapping("logout")
    public Result<Object> logout(HttpServletRequest request) {
        return employeeService.logout(request);
    }
}
