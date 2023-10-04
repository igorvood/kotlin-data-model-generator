package ru.vood.processor.datamodel.abstraction.model

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.Pk
import ru.vood.dmgen.annotation.Uk
import ru.vood.processor.datamodel.abstraction.model.abstraction.IGeneratedField
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import javax.lang.model.element.Element

class MetaEntityColumn(
    element: Element
) : IGeneratedField(element) {
    override fun isNullable(): Boolean {
        return element.asType().asTypeName().nullable
        /*return when (val asType = element.asType().asTypeName()) {
            is ParameterizedTypeName -> asType.nullable
            is ClassName -> asType.nullable
            else -> error("$element dot'n compatible")
        }*/
    }

    val inPk: Boolean by lazy {
        element.annotation<Pk>()
            .map { true }
            .orElse(false)
    }

    val uks by lazy { element.annotations<Uk>() }


}