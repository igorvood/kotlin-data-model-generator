package ru.vood.processor.datamodel.abstraction.model.abstraction

abstract class AbstractGeneratedClass<out AnnotatedClass : AbstractAnnotatedClass>(val annotatedClass: AnnotatedClass) {

    abstract fun generatedClassName(): String

    abstract fun generatedPackageName(): String

    fun fullGeneratedName(): String = "${generatedPackageName()}.${generatedClassName()}"

}
