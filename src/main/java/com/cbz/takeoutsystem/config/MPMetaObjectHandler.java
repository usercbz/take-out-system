package com.cbz.takeoutsystem.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cbz.takeoutsystem.utils.EmployeeHolder;
import com.cbz.takeoutsystem.utils.UserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MybatisPlus 自动填充
 */
@Component
public class MPMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("createUser", EmployeeHolder.getEmployee() != null ? EmployeeHolder.getEmployee().getId() : UserHolder.getUser().getId(), metaObject);

        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateUser", EmployeeHolder.getEmployee() != null ? EmployeeHolder.getEmployee().getId() : UserHolder.getUser().getId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateUser", EmployeeHolder.getEmployee() != null ? EmployeeHolder.getEmployee().getId() : UserHolder.getUser().getId(), metaObject);
    }
}
