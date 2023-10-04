package ru.vood.processor.datamodel.abstraction.model.abstraction

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.asTypeName
import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

abstract class IGeneratedField(val element: Element) {

    val kotlinMetaClass = fold({ it }, { it })
    protected inline fun <reified T> fold(
        classNameF: (ClassName) -> T,
        parameterizedTypeNameF: (ParameterizedTypeName) -> T,
    ): T {
        return when (val t = element.asType().asTypeName()) {
            is ClassName -> classNameF(t)
            is ParameterizedTypeName -> parameterizedTypeNameF(t)
            else -> error("unsupported class $t")
        }
    }

    val name: String = element.simpleName.toString()

    abstract fun isNullable(): Boolean

    val type: String = element.asType().toString()

    val typeCollection by lazy {
        val fold = fold(
            { null },
            {
                when (val canonicalName = it.rawType.canonicalName) {
                    "java.util.Set" -> SupportedGenericType.SET
                    "java.util.List" -> SupportedGenericType.LIST
                    else -> error("unsupported generic type $canonicalName")
                }
            }
        )
        fold
    }

    val typeField = fold(
        { it },
        {
            it.typeArguments[0]

        }
    )

//    fun betterClass(): AbstractAnnotatedClass?
//    fun isUpdateble(): Boolean
}

inline fun <reified ANNO : Annotation> IGeneratedField.annotation(): Optional<ANNO> =
    element.annotation()


inline fun <reified ANNO : Annotation> Element.annotation(): Optional<ANNO> {
    val annotation = this.getAnnotation(ANNO::class.java)
    return Optional.ofNullable(annotation)
}
inline fun <reified ANNO : Annotation> Element.necessaryAnnotation(): ANNO = annotation<ANNO>().orElseGet { error("Y $this не найдена обязательная аннотация ${ANNO::class.java.canonicalName}") }


inline fun <reified ANNO : Annotation> Element.annotations(): Array<ANNO> {
//    val actionType = processingEnv.getElementUtils().getTypeElement(ANNO::class.java.name).asType()
//    val filter = this.annotationMirrors
//        .filter { it.annotationType.equals(actionType) }
//        .flatMap { it.elementValues.entries  }
//    val first = filter.first().value.value
//    val element = this
    return this.getAnnotationsByType(ANNO::class.java)

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
