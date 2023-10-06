package ru.vood.processor.datamodel.abstraction.model

import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.Pk
import ru.vood.processor.datamodel.abstraction.model.abstracti.metadto.AbstractField

import ru.vood.processor.datamodel.abstraction.model.abstracti.annotation
import javax.lang.model.element.Element

class MetaEntityColumn(
    element: Element
) : AbstractField(element) {
    override fun isNullable(): Boolean {
        return element.asType().asTypeName().nullable
    }

    val inPk: Boolean by lazy {
        element.annotation<Pk>()
            .map { true }
            .orElse(false)
    }


}

