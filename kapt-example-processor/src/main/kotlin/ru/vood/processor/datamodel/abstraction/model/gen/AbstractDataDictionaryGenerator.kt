package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

abstract class AbstractDataDictionaryGenerator<META>(
    messager: Messager,
    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractGenerator<META>(messager, processingEnv, rootPackage) {

    override val subDir: String
        get() = "metaEnum"

    val commonPackage: String = "ru.vood.datamodel.meta.enums"//calculatePackage(packages)

    abstract val nameClass: String

}