package com.demo.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Swagger2的接口配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfigImpl {

    /**
     * 安协通模块API
     */
    @Bean
    public Docket axtRestApi() {
        //设置全局响应状态码
        List<ResponseMessage> responseMessageList = new ArrayList<>();
//        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").responseModel(new ModelRef("ApiError")).build());
//        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(400).message("参数错误").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(401).message("没有认证").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(403).message("没有没有访问权限").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求成功").build());
//        responseMessageList.add(new ResponseMessageBuilder().code(500).message("请求失败").build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("文档")
//                .globalResponseMessage(RequestMethod.GET, responseMessageList)
//                .globalResponseMessage(RequestMethod.POST, responseMessageList)
//                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .pathMapping("")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


    ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：api_接口文档")
                // 描述
                .description("描述：...")
                // 作者信息
                .contact(new Contact("z", null, null))
                // 版本
                .version("版本号:1.0")
                .build();
    }
}
