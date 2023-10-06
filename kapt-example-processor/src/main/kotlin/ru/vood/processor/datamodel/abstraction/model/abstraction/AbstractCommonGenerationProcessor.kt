package ru.vood.processor.datamodel.abstraction.model.abstraction

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

abstract class AbstractCommonGenerationProcessor : AbstractProcessor() {


    protected lateinit var messager: Messager

    protected lateinit var typeUtils: Types

    protected lateinit var filer: Filer

    protected lateinit var elementUtils: Elements

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        typeUtils = processingEnv.typeUtils
        filer = processingEnv.filer
        elementUtils = processingEnv.elementUtils
    }

    protected fun log(kind: Diagnostic.Kind, msg: CharSequence?) {

        messager.printMessage(kind, "${this.javaClass.canonicalName}: $msg")
    }
}