package com.demo.api.entity;

import com.demo.api.entity.groups.GroupsUser;
import com.demo.swagger.annotation.ApiGroupProperties;
import com.demo.swagger.annotation.ApiGroupProperty;
import com.demo.swagger.bean.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @date 2020/11/12 14:40
 */
@Data
@ApiModel("部门实体")
public class Department extends BaseModel {

    @ApiGroupProperty({GroupsUser.Save.class})
    @ApiModelProperty("Id")
    private Integer id;

    @ApiGroupProperties({
            @ApiGroupProperty(value = {GroupsUser.Save.class}, description = "部门名称(描述变了)"),
            @ApiGroupProperty(value = {GroupsUser.Update.class})
    })
    @ApiModelProperty("部门名称")
    private String name;

    @ApiGroupProperty({GroupsUser.Save.class, GroupsUser.Update.class})
    @ApiModelProperty("部门备注")
    private String remarks;
}
