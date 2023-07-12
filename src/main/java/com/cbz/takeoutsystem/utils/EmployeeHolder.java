package com.cbz.takeoutsystem.utils;

import com.cbz.takeoutsystem.entity.Employee;

public class EmployeeHolder {
    private final static ThreadLocal<Employee> tl = new ThreadLocal<>();

    public static void saveEmployee(Employee employee) {
        tl.set(employee);
    }
    public static Employee getEmployee() {
        return tl.get();
    }
    public static void removeEmployee() {
        tl.remove();
    }
}