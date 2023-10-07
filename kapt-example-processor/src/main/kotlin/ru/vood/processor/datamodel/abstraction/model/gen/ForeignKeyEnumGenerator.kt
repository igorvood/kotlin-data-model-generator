package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.processor.datamodel.abstraction.model.MetaForeignKey
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ForeignKeyEnumGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractDataDictionaryGenerator<Set<MetaForeignKey>>(messager, filer, processingEnv) {

    override val nameClass: String
        get() = "DataDictionaryForeignKeyEnum"

    override fun textGenerator(generatedClassData: Set<MetaForeignKey>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData
                    .map { metaForeign ->



                        metaForeign.name.value+"(${metaForeign.fromEntity.shortName}, ${metaForeign.toEntity.shortName}, ${metaForeign.uk.name.value})"
                    }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package $commonPackage
                        
import $commonPackage.DataDictionaryEntityEnum.*
import $commonPackage.DataDictionaryUniqueKeyEnum.*

enum class $nameClass(
    val fromEntity: DataDictionaryEntityEnum,
    val toEntity: DataDictionaryEntityEnum,
    val uk: DataDictionaryUniqueKeyEnum
) {
$entities
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }


}
