package com.cbz.takeoutsystem.utils;

import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class BeanUtils {

    public static <T> T toDTO(Class<T> DTOClass, Object obj) {
        //反射构造
        T dto = null;
        try {
            dto = DTOClass.getConstructor().newInstance();

            Class<? super T> superclass = DTOClass.getSuperclass();

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field f :
                    fields) {
                f.setAccessible(true);
                String name = f.getName();
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                Field field = superclass.getDeclaredField(name);
                field.setAccessible(true);
                field.set(dto, f.get(obj));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return dto;
    }

    public static <T> T toBean(Class<T> BeanClass, Object dto) {
        //反射构造
        T bean = null;
        try {
            bean = BeanClass.getConstructor().newInstance();

            Class<?> superclass = dto.getClass().getSuperclass();

            for (Field field : superclass.getDeclaredFields()) {
                field.setAccessible(true);
                String name = field.getName();
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                Field field1 = BeanClass.getDeclaredField(name);
                field1.setAccessible(true);
                Object o = field.get(dto);
                if (o == null) {
                    continue;
                }

                field1.set(bean, o);
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /**
     * 复制属性
     *
     * @param des    目标属性
     * @param source 源数据
     * @param <T>    类型
     * @return 复制后的内容
     */
    public static <T> T copyProperty(@NonNull T des, @NonNull Object source) {
        Class<?> desClass = des.getClass();
        Class<?> sourceClass = source.getClass();

        Field[] desFields = desClass.getDeclaredFields();

        for (Field desField : desFields) {
            //暴力访问
            desField.setAccessible(true);
            String name = desField.getName();
            if (name.equals("serialVersionUID")) {
                continue;
            }
            try {
                Field sourceField = sourceClass.getDeclaredField(name);
                //暴力访问
                sourceField.setAccessible(true);
                try {
                    desField.set(des, sourceField.get(source));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
//                e.printStackTrace();

            }

        }

        return des;
    }
}
