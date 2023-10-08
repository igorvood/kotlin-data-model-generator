package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

abstract class AbstractDataDictionaryGenerator<META>(
    messager: Messager,

     processingEnv: ProcessingEnvironment

): AbstractGenerator<META>(messager, processingEnv) {

    override val subDir: String
        get() = "metaEnum"

    val commonPackage = "ru.vood.datamodel.meta.enums"//calculatePackage(packages)

    abstract val nameClass :String

}