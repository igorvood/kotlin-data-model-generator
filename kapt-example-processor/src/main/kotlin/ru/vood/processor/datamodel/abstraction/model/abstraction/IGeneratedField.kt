package ru.vood.processor.datamodel.abstraction.model.abstraction

import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

interface IGeneratedField {

    val element: Element
    fun name(): String = element.simpleName.toString()

    fun isNullable(): Boolean
    fun type(): String = element.asType().toString()
//    fun betterClass(): AbstractAnnotatedClass?
//    fun isUpdateble(): Boolean
}

inline fun <reified ANNO : Annotation> IGeneratedField.annotation(): Optional<ANNO> =
    element.annotation()


inline fun <reified ANNO : Annotation> Element.annotation(): Optional<ANNO> {
//    val actionType = processingEnv.getElementUtils().getTypeElement(ANNO::class.java.name).asType()
//    val filter = this.annotationMirrors
//        .filter { it.annotationType.equals(actionType) }
//        .flatMap { it.elementValues.entries  }
//    val first = filter.first().value.value
//    val element = this
    val annotation = this.getAnnotation(ANNO::class.java)
    return Optional.ofNullable(annotation)
}

inline fun <reified ANNO : Annotation> Element.annotationValue(
    processingEnv: ProcessingEnvironment,
    valueName: String
): Any {
    val actionType = processingEnv.getElementUtils().getTypeElement(ANNO::class.java.name).asType()
    val filter = this.annotationMirrors
        .filter { it.annotationType.equals(actionType) }
        .flatMap { it.elementValues.entries }
    return filter
        .filter { it.key.simpleName.toString() == valueName }
        .map { it.value.value }
        .first()


}
