package com.demo.swagger.core;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Function;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;

import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.ResolvedTypes.allowableValues;


/**
 * ModelReference 关于组的应用(生成model引用对象)
 *
 */
public class ModelGroupReferenceProvider implements Function<ResolvedType, ModelReference> {

    private final ModelReference modelRef;
    private final String groupName;

    public ModelGroupReferenceProvider(ModelReference modelRef, String groupName) {
        this.modelRef = modelRef;
        this.groupName = groupName;
    }

    @Override
    public ModelReference apply(ResolvedType resolvedType) {
        return modelReference(resolvedType, modelRef.getType(), modelRef);
    }


    private ModelReference modelReference(ResolvedType type, String typeName, ModelReference currentModelRef) {


        if (isContainerType(type)) {
            /*如果是集合(会有个泛型),则递归给泛型的ModelRef.type拼接上组名称*/
            ResolvedType collectionElementType = collectionElementType(type);

            return new ModelRef(
                    typeName,
                    modelReference(collectionElementType, currentModelRef.getItemType(), currentModelRef.itemModel().get()),
                    allowableValues(collectionElementType));
        }
        if (Void.class.equals(type.getErasedType()) || Void.TYPE.equals(type.getErasedType())) {
            return new ModelRef("void");
        }
        if (MultipartFile.class.isAssignableFrom(type.getErasedType())) {
            return new ModelRef("__file");
        }
        /*不是集合则给当前ModelRef.type拼接上组名称*/
        return new ModelRef(typeName + groupName, allowableValues(type));
    }
}
