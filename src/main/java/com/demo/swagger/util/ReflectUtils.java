package com.demo.swagger.util;

import java.lang.reflect.Field;

/**
 * 反射工具类
 *
 * @date 2020/9/9 11:17
 */
public class ReflectUtils {

    /**
     * <p>
     * 反射对象修改私有字段
     * </p>
     *
     * @param obj   对象
     * @param field 字段名称
     * @param value 字段值
     */
    public static void setPrivateField(final Object obj, final String field, Object value) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("反射私有变量赋值失败", e);
        }
    }
}
