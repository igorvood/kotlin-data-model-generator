package ru.vood.processor.datamodel.abstraction.model.abstraction.metadto

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

abstract class AbstractGenerator<META>(
    val messager: Messager,
    val filer: Filer
    ) {
    abstract fun textGenerator(generatedClassData: META): String


    protected fun log(kind: Diagnostic.Kind, msg: CharSequence?) {

        messager.printMessage(kind, "${this.javaClass.canonicalName}: $msg")
    }

}