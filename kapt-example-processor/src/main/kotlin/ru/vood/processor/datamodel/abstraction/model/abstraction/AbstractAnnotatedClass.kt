package ru.vood.processor.datamodel.abstraction.model.abstraction

import javax.lang.model.element.Element

abstract class AbstractAnnotatedClass(val element: Element)/*<Annotation>*/ {
    abstract fun name(): String

    abstract fun fields(): List<IGeneratedField>

    fun shortName(): String {
        val dotIdx = name().lastIndexOf('.')
        return name().substring(dotIdx + 1)
    }

    fun packageName(): String {
        val dotIdx = name().lastIndexOf('.')
        return name().substring(0, dotIdx)
    }
//    inline fun <reified A: Annotation> annotatedBy()  : List<OrIsNullField> = fields().filter { f -> f.annotation<A>().isPresent }

}