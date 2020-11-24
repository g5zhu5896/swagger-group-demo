# swagger-group-demo
swagger api文档 可以用同一个model根据group在不同api里展示不同字段和描述
运行项目访问 http://192.168.1.233:8081/swagger-ui.html 看效果

@ApiGroupProperty(value={GroupsUser.Update.class},description="")用于给Model字段分组,value控制所属组,description可以设置不同的字段描述

@ApiGroupProperties(value = {@ApiGroupProperty({)}}）可以包含多个@ApiGroupProperty以便给不同分组的Model字段设置不同的字段描述

@ApiGroup(GroupsUser.Save.class) 给指定接口选择用哪个分组, 注解在方法参数表示参数用的分组, 注解在方法上表示返回值用的分组

当A对象包含B对象时 要让B对象按分组展示的话B对象需要继承BaseModel,如果B没继承BaseModel则B会展示全部字段
