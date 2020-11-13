package com.demo.api.controller;


import com.demo.api.entity.Response;
import com.demo.api.entity.User;
import com.demo.api.entity.groups.GroupsUser;
import com.demo.swagger.annotation.ApiGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 用户
 * </p>
 *
 * @since 2020-11-02
 */
@RestController
@Api(tags = "用户", description = "用户 发布", hidden = true)
@Validated
@RequestMapping("/user")
public class UserController {

    /**
     * User实体中字段有GroupsUser.Update.class才会展示
     * @param user
     * @return
     */
    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    @ApiGroup(GroupsUser.Update.class)
    public Response<User> update(@ApiGroup(GroupsUser.Update.class) @RequestBody User user) {
        return new Response<User>();
    }

    /**
     * User实体中字段有GroupsUser.Save.class才会展示
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/save")
    @ApiGroup(GroupsUser.Save.class)
    public Response<User> save(@ApiGroup(GroupsUser.Save.class) @RequestBody User user) {
        return new Response<User>();
    }


    @ApiOperation(value = "全部")
    @PostMapping("/all")
    public Response<User> all(@RequestBody User user) {
        return new Response<User>();
    }

}

