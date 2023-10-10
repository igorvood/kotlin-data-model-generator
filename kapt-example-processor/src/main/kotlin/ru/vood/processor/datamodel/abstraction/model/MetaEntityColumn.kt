package ru.vood.processor.datamodel.abstraction.model

import org.jetbrains.annotations.Nullable
import ru.vood.dmgen.annotation.Comment
import ru.vood.dmgen.annotation.Pk
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractField
import javax.lang.model.element.Element

class MetaEntityColumn(
    val position: Int,
    element: Element
) : AbstractField(element) {

    val comment: String? = element.annotation<Comment>().map {
        it.comment
    }.orElse(null)

    override fun isNullable(): Boolean {
//        val asType = element.asType()
//
//        val annotations1 = element.annotations<NotNull>()
//        val annotations2 = element.annotations<Nullable>()
//
//        val asTypeName = asType.asTypeName()
//        val kind = asType.kind
//
//
//        val annotations = asTypeName.annotations
//        val annotationMirrors = asType.annotationMirrors
//
//        if (annotations.isNotEmpty()) {
//             annotations.get(0).typeName
//        }
//
////        return element.asType().asTypeName().isNullable
        return element.annotations<Nullable>().isNotEmpty()
    }

    val inPk: Boolean =
        element.annotation<Pk>()
            .map { true }
            .orElse(false)


}

