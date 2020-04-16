package com.sificomlib.data.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ColumType {
    EColumType[] columType() default {EColumType.TEXT};
    Class objColumClass() default String.class;
}
