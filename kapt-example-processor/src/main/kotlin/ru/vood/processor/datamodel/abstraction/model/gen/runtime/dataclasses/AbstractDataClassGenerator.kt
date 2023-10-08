package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

abstract class AbstractDataClassGenerator<META>(
    messager: Messager,
     filer: Filer,
     processingEnv: ProcessingEnvironment

): AbstractGenerator<META>(messager, processingEnv) {

    override val subDir: String
        get() = "metaEnum"

    val commonPackage = "ru.vood.datamodel.meta.enums"//calculatePackage(packages)

//    abstract val nameClass :String

}