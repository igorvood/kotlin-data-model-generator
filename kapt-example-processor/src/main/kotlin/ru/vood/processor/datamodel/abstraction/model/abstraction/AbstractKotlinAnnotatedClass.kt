package ru.vood.processor.datamodel.abstraction.model.abstraction

import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element

abstract class AbstractKotlinAnnotatedClass<FIELD_META : IGeneratedField>(
    private val element: Element
)/*<Annotation>*/ {
    val name: String by lazy { element.asType().toString() }

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
//    inline fun <reified A: Annotation> annotatedBy()  : List<OrIsNullField> = fields().filter { f -> f.annotation<A>().isPresent }

}