package com.boot.example.annotation;

import java.lang.annotation.*;

/**
 * @author TuoZhou
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
}
