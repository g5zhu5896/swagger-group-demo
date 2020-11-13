package com.demo.swagger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Repeatable(ApiGroupProperties.class)
public @interface ApiGroupProperty {
    Class<?>[] value();

    /**
     * 字段名称,用于替换当前组在接口文档的字段描述
     * 替换ApiModelProperty.value
     *
     * @return
     */
    String description() default "";
}
