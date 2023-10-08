package ru.vood.processor.datamodel.abstraction.model

import com.squareup.kotlinpoet.asTypeName
import ru.vood.dmgen.annotation.Comment
import ru.vood.dmgen.annotation.Pk
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractField

import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import javax.lang.model.element.Element

class MetaEntityColumn(
    element: Element
) : AbstractField(element) {

    val comment: String? = element.annotation<Comment>().map {
        it.comment
    }.orElse(null)
    override fun isNullable(): Boolean {
        return element.asType().asTypeName().isNullable
    }

    val inPk: Boolean =
        element.annotation<Pk>()
            .map { true }
            .orElse(false)



}

