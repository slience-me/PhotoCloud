package xyz.slienceme.tuyun.utils;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author slience_me
 * @Time : 2021/7/16  9:41
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Delay {
    int time() default 1;//TODO 时间以 秒 为单位
}
