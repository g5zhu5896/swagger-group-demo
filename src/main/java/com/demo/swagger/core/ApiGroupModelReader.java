package com.demo.swagger.core;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.demo.swagger.annotation.ApiGroup;
import com.demo.swagger.annotation.ApiGroupProperties;
import com.demo.swagger.annotation.ApiGroupProperty;
import com.demo.swagger.bean.BaseModel;
import com.demo.swagger.util.ReflectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.schema.ModelProvider;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.lang.reflect.Field;
import java.util.Map;

import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;

/**
 * 获取api model 生成Model(包含swagger展示的相关字段,ModelRef引用的就是model)并缓存,增加关于组的Model放入缓存
 *
 */
@Component
@Primary
public class ApiGroupModelReader extends ApiModelReader {

    public static Map<String, Model> CACHE = Maps.newHashMap();

    @Autowired
    public ApiGroupModelReader(@Qualifier("cachedModels") ModelProvider modelProvider, TypeResolver typeResolver, DocumentationPluginsManager pluginsManager) {
        super(modelProvider, typeResolver, pluginsManager);
    }

    @Override
    public Map<String, Model> read(RequestMappingContext context) {
        Map<String, Model> modelMap = super.read(context);
        Map<String, Model> currentModel = Maps.newHashMap();
        /*遍历默认生成的model 此处都是作为参数的model*/
        modelMap.entrySet().forEach(entry -> {
            Model item = entry.getValue();
            String key = entry.getKey();
            /*生成非return的model*/
            if (!CACHE.containsKey(key)) {
                Class<?> clazz = item.getType().getErasedType();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    ApiGroupProperties apiGroupProperties = field.getAnnotation(ApiGroupProperties.class);
                    ApiGroupProperty[] apiGroupPropertyArray = null;
                    if (apiGroupProperties != null) {
                        apiGroupPropertyArray = apiGroupProperties.value();
                    } else {
                        apiGroupPropertyArray = new ApiGroupProperty[]{field.getAnnotation(ApiGroupProperty.class)};
                    }
                    for (ApiGroupProperty apiGroupProperty : apiGroupPropertyArray) {
                        /*如果model里的字段包含ApiGroupProperty注解,则生成关于注解包含的Group的Model(此Model值包含和Group有关的属性)*/
                        if (apiGroupProperty != null) {
                            /*获取当前字段的Model*/
                            String propertyKey = field.getName();
                            ModelProperty modelProperty = item.getProperties().get(propertyKey);

                            for (Class<?> groupClazz : apiGroupProperty.value()) {
                                String groupName = org.apache.commons.lang.StringUtils.substringAfterLast(groupClazz.getName(), ".");
                                String id = item.getId() + groupName;

                                Model model = currentModel.get(id);
                                //当前组Model的所有字段属性
                                Map<String, ModelProperty> properties = null;
                                if (model == null) {
                                    /*如果不存在当前组的model则新建一个*/
                                    properties = Maps.newHashMap();
                                    model = new Model(id, item.getName(), item.getType(), item.getQualifiedType(), properties, item.getDescription(),
                                            item.getBaseModel(), item.getDiscriminator(), item.getSubTypes(), item.getExample(), item.getXml());
                                    currentModel.put(id, model);
                                    CACHE.put(id, model);
                                } else {
                                    properties = model.getProperties();
                                }

                                ModelProperty currentModelProperty = generateModelProperty(modelProperty, groupName, apiGroupProperty);
                                properties.put(propertyKey, currentModelProperty);
                            }
                        }
                    }
                }
                CACHE.put(key, item);
            }

            /*生成return的model*/
            if (context.getReturnType().equals(item.getType())) {
                Optional<ApiGroup> annotation = context.findAnnotation(ApiGroup.class);
                if (annotation.isPresent()) {
                    Class<?> groupClazz = annotation.get().value();
                    String groupName = org.apache.commons.lang.StringUtils.substringAfterLast(groupClazz.getName(), ".");

                    String id = item.getId() + groupName;

                    if (CACHE.containsKey(id)) {
                        return;
                    }

                    Map<String, ModelProperty> properties = Maps.newHashMap();
                    Model model = new Model(id, item.getName(), item.getType(), item.getQualifiedType(), properties, item.getDescription(),
                            item.getBaseModel(), item.getDiscriminator(), item.getSubTypes(), item.getExample(), item.getXml());
                    currentModel.put(id, model);
                    CACHE.put(id, model);

                    item.getProperties().forEach((s, modelProperty) -> {
                        properties.put(s, generateModelProperty(modelProperty, groupName));
                    });
                }
            }
        });
        modelMap.putAll(currentModel);
        return modelMap;
    }


    /**
     * 判断当前类型和BaseModel是否有关系,有的话则重新生成ModelProperty,否则继续使用原来的BaseModel
     *
     * @param modelProperty
     * @param groupName
     * @return
     */
    private ModelProperty generateModelProperty(ModelProperty modelProperty, String groupName, ApiGroupProperty apiGroupProperty) {
        ModelProperty currentModelProperty = generateModelProperty(modelProperty, groupName);

        /*如果当前ApiGroupProperty有配置字段描述则替换掉原来的字段描述*/
        String description = apiGroupProperty.description();
        if (StringUtils.isNotBlank(apiGroupProperty.description())) {
            ResolvedType type = modelProperty.getType();
            currentModelProperty = new ModelProperty(modelProperty.getName(), type,
                    modelProperty.getQualifiedType(), modelProperty.getPosition(), modelProperty.isRequired(),
                    modelProperty.isHidden(), modelProperty.isReadOnly(), modelProperty.isAllowEmptyValue(), description,
                    modelProperty.getAllowableValues(), modelProperty.getExample(), modelProperty.getPattern(),
                    modelProperty.getDefaultValue(), modelProperty.getXml(), modelProperty.getVendorExtensions());
            ReflectUtils.setPrivateField(currentModelProperty, "modelRef", modelProperty.getModelRef());
        }
        return currentModelProperty;
    }

    /**
     * 判断当前类型和BaseModel是否有关系,有的话则重新生成ModelProperty,否则继续使用原来的BaseModel
     *
     * @param modelProperty
     * @param groupName
     * @return
     */
    private ModelProperty generateModelProperty(ModelProperty modelProperty, String groupName) {
        ResolvedType type = modelProperty.getType();
        if (isContainerType(type)) {
            ResolvedType collectionElementType = collectionElementType(type);
            /*如果泛型是否继承BaseModel*/
            if (!BaseModel.class.isAssignableFrom(collectionElementType.getErasedType())) {
                return modelProperty;
            }
        } else if (!BaseModel.class.isAssignableFrom(type.getErasedType())) {
            /*如果当前字段类型是否继承BaseModel*/
            return modelProperty;
        }

        /*如果当前字段类型或者泛型继承BaseModel 则需要把ModelRef指向组相关的Model*/
        ModelProperty currentModelProperty = new ModelProperty(modelProperty.getName(), type,
                modelProperty.getQualifiedType(), modelProperty.getPosition(), modelProperty.isRequired(),
                modelProperty.isHidden(), modelProperty.isReadOnly(), modelProperty.isAllowEmptyValue(), modelProperty.getDescription(),
                modelProperty.getAllowableValues(), modelProperty.getExample(), modelProperty.getPattern(),
                modelProperty.getDefaultValue(), modelProperty.getXml(), modelProperty.getVendorExtensions()).
                updateModelRef(new ModelGroupReferenceProvider(modelProperty.getModelRef(), groupName));
        return currentModelProperty;
    }
}
