package ru.vood.processor.datamodel.abstraction.model.abstraction

import java.util.*
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

inline fun <reified ANNO : Annotation, reified TYPE_VAL> Element.proxyAnnotationValue(
    processingEnv: ProcessingEnvironment,
    valueName: String
): Optional<TYPE_VAL> {
    val map = this.annotation<ANNO>()
        .map { idAnnoTat ->
            val annotationValue = this.annotationValue<ANNO>(processingEnv, valueName)
            return@map annotationValue as TYPE_VAL
        }
    return map
}

fun String.mapKotlinType(): String =
    if (this == "java.lang.String") "String"
//    else if (this == "java.math.BigInteger") "Int"
    else if (this == "java.lang.Integer") "Int"
    else if (this == "boolean") "Boolean"
    else if (this == "int") "Int"

    else this


fun <T> Element.getDirectlyImplementsInterface(cl: Class<T>): Set<TypeMirror> {
    val interfaces: MutableList<out TypeMirror> = (this as TypeElement).interfaces
    return interfaces
        .filterIsInstance<DeclaredType>()
        .filter { (it.asElement() as TypeElement).qualifiedName.toString() == cl.canonicalName }
        .toSet()
}


fun Element.getAllInterfaces(): Set<TypeMirror> {
    val interfaces: MutableList<out TypeMirror> = (this as TypeElement).interfaces
    return getParentInterfaces(interfaces.toSet())
}

private fun getParentInterfaces(interfaces: Set<TypeMirror>): Set<TypeMirror> {
    val typeMirrors = if (interfaces.isEmpty()) interfaces
    else {
        val newInterfaces = interfaces
            .filterIsInstance<DeclaredType>()
            .flatMap { (it.asElement() as TypeElement).interfaces }

        if (newInterfaces.isEmpty())
            interfaces
        else {
            val parentInterfaces = getParentInterfaces(newInterfaces.toSet())
            interfaces
                .plus(newInterfaces)
                .plus(parentInterfaces)
        }
    }
    return typeMirrors
}

@Deprecated("Требует отладки")
fun  <T> Element.getInterface(cl: Class<T>): TypeElement? {
    val allInterfaces = this.getAllInterfaces()


    val firstOrNull =
        allInterfaces
            .filterIsInstance<DeclaredType>()
            .map { it.asElement() }
            .filterIsInstance<TypeElement>()
            .firstOrNull { it.qualifiedName.toString() ==  cl.canonicalName}

    return firstOrNull
}

