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
    }

    val inPk: Boolean by lazy {
        element.annotation<Pk>()
            .map { true }
            .orElse(false)
    }

}