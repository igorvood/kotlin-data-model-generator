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

    val entityFileldName = name[0].lowercaseChar() + name.substring(1)

    abstract fun elementToIGeneratedField(posicion: Int, e: Element): FIELD_META

    val fields: List<FIELD_META> by lazy {
        element.enclosedElements.withIndex()
            .filter { e -> e.value.kind.isField }
            .map { element -> elementToIGeneratedField(element.index, element.value) }
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