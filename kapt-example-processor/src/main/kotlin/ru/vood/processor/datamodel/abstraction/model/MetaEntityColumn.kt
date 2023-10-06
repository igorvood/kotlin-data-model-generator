package ru.vood.processor.datamodel.abstraction.model

import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.Pk
import ru.vood.processor.datamodel.abstraction.model.abstracti.metadto.IGeneratedField

import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
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