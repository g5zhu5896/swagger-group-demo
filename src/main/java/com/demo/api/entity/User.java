package com.demo.api.entity;

import com.demo.api.entity.groups.GroupsUser;
import com.demo.swagger.annotation.ApiGroupProperties;
import com.demo.swagger.annotation.ApiGroupProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @date 2020/11/12 14:40
 */
@Data
@ApiModel("用户实体")
public class User {
    @ApiGroupProperty({GroupsUser.Save.class})
    @ApiModelProperty("Id")
    private Integer id;

    @ApiGroupProperties({
            @ApiGroupProperty(value = {GroupsUser.Save.class}, description = "姓名(描述变了)"),
            @ApiGroupProperty(value = {GroupsUser.Update.class})
    })
    @ApiModelProperty("姓名")
    private String name;

    @ApiGroupProperty({GroupsUser.Save.class, GroupsUser.Update.class})
    @ApiModelProperty("电话")
    private String tel;

    @ApiGroupProperty({GroupsUser.Save.class, GroupsUser.Update.class})
    @ApiModelProperty("部门")
    private Department department;

    @ApiGroupProperty({GroupsUser.Save.class, GroupsUser.Update.class})
    @ApiModelProperty("职位")
    private Position position;

}
