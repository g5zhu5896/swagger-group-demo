package com.demo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @date 2020/11/12 17:31
 */
@Data
@ApiModel(description = "返回结果实体")
public class Response<T> {
    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private int code;

    /**
     * 返回内容
     */
    @ApiModelProperty("返回内容")
    private String message;

    /**
     * 数据对象
     */
    @ApiModelProperty("数据对象")
    private T data;
}
