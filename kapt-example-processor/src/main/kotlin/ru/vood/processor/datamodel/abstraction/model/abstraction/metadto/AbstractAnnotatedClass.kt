package ru.vood.processor.datamodel.abstraction.model.abstraction.metadto

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

import javax.lang.model.element.Element

abstract class AbstractAnnotatedClass<FIELD_META : AbstractField>(
    private val element: Element
)/*<Annotation>*/ {

    val kotlinMetaClass = when (val t = element.asType().asTypeName()) {
        is ClassName -> t
        else -> error("unsupported class $t")
    }

    val name: String by lazy { element.asType().toString().split(".").last() }

    abstract fun elementToIGeneratedField(e: Element): FIELD_META

    val fields: List<FIELD_META> by lazy {
        element.enclosedElements
            .filter { e: Element -> e.kind.isField }
            .map { element: Element -> elementToIGeneratedField(element) }
    }

    val shortName: String by lazy {
        val dotIdx = name.lastIndexOf('.')
        name.substring(dotIdx + 1)
    }

    val packageName: String by lazy {
        val dotIdx = name.lastIndexOf('.')
        name.substring(0, dotIdx)
    }

}