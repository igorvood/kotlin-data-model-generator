package ru.vood.processor.datamodel.abstraction.model.abstraction

import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror

inline fun <reified ANNO : Annotation> Element.annotation(): Optional<ANNO> {
    val annotation = this.getAnnotation(ANNO::class.java)
    return Optional.ofNullable(annotation)
}
inline fun <reified ANNO : Annotation> Element.necessaryAnnotation(): ANNO =
    annotation<ANNO>().orElseGet { error("Y $this не найдена обязательная аннотация ${ANNO::class.java.canonicalName}") }


inline fun <reified ANNO : Annotation> Element.annotations(): Array<ANNO> {
    return this.getAnnotationsByType(ANNO::class.java)
}


// нужны ли ф-ции ниже не ясно

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
fun <T> Element.getInterface(cl: Class<T>): TypeElement? {
    val allInterfaces = this.getAllInterfaces()


    val firstOrNull =
        allInterfaces
            .filterIsInstance<DeclaredType>()
            .map { it.asElement() }
            .filterIsInstance<TypeElement>()
            .firstOrNull { it.qualifiedName.toString() == cl.canonicalName }

    return firstOrNull
}

