package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.processor.datamodel.abstraction.model.MetaForeignKey
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

class ForeignKeyEnumGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractGenerator<Set<MetaForeignKey>>(messager, filer, processingEnv) {

    override val subDir: String
        get() = "metaEnum"

    override fun textGenerator(generatedClassData: Set<MetaForeignKey>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
//                val commonPackage: String = "ru.vood.datamodel.meta.enums"//calculatePackage(packages)
//
//                val entities = generatedClassData
//                    .map { it.shortName }
//                    .sorted()
//                    .joinToString(",\n")
//                val trimIndent =
//"""package $commonPackage
//
//enum class MetaEntityEnum {
//$entities
//}
//"""
                setOf(GeneratedFile(FileName("MetaEntityEnum"), GeneratedCode("trimIndent")))
            }
        }


    }


}
