package com.qianwang.dao.util;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglikai on 2017/4/26.
 */
public class BeanUtils {

    public static void parse(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    public static <T> T parse(Object source, Class<T> clazz) {
        Object target = org.springframework.beans.BeanUtils.instantiate(clazz);
        parse(source, target);
        return (T) target;
    }

    public static <T> List<T> parseList(Object source, Class<T> clazz) {
        if (source instanceof List<?>) {
            List<?> list = (List<?>) source;
            List<T> result = new ArrayList<>(list.size());
            for (Object o : list) {
                Object target = org.springframework.beans.BeanUtils.instantiate(clazz);
                parse(o, target);

                result.add((T) target);
            }
            return result;
        }

        Object target = org.springframework.beans.BeanUtils.instantiate(clazz);
        parse(source, target);
        List<T> result = new ArrayList<>(1);
        result.add((T) target);
        return result;
    }

    public static <T> T copyBean(Object source, Object dest) {
        BeanCopier copier = BeanCopier.create(source.getClass(), dest.getClass(), false);
        copier.copy(source, dest, null);
        return (T) dest;
    }

    public static <T> T copyBean(Object source, Object dest, Converter converter) {
        if (converter == null) {
            return copyBean(source, dest);
        }

        BeanCopier copier = BeanCopier.create(source.getClass(), dest.getClass(), true);
        copier.copy(source, dest, converter);
        return (T) dest;
    }

    public static Map<String, String> objToMapString(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception();
        }
        Map<String, String> map = new HashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            String value = getter != null ? String.valueOf(getter.invoke(obj)) : null;
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, Object> objToMap(Object obj) throws Exception {
        if (obj == null) {
            throw new Exception();
        }
        Map<String, Object> map = new HashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }
}
