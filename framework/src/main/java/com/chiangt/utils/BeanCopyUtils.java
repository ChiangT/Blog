package com.chiangt.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils(){

    }

    public static <V> V copyBean(Object source, Class<V> clazz){
        V result = null;
        try {
            //创建目标对象
            result = clazz.newInstance();
            //拷贝属性
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <V> List<V> copyBeanList(List<?> source, Class<V> clazz){
        return source.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
