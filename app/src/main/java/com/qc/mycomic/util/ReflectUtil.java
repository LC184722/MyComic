package com.qc.mycomic.util;

import java.lang.reflect.Field;

/**
 * @author LuQiChuang
 * @version 1.0
 * @description
 * @date 2020/8/12 21:37
 */
public class ReflectUtil {

    public static Object getFieldValueByName(Object o, String fieldName) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
