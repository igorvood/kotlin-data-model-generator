package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.dmgen.intf.IMetaFkEntity
import ru.vood.dmgen.intf.IMetaUkEntity
import ru.vood.processor.datamodel.abstraction.model.MetaForeignKeyTemporary
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ForeignKeyEnumGenerator(
    messager: Messager,

    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractDataDictionaryGenerator<Set<MetaForeignKeyTemporary>>(messager, processingEnv, rootPackage) {

    override val nameClass: String
        get() = foreignKeyEnumGeneratorNameClass

    override fun textGenerator(generatedClassData: Set<MetaForeignKeyTemporary>): Set<GeneratedFile> {
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
                    """package ${packageName.value}
                        
import ${packageName.value}.${EntityEnumGenerator.nameClassEntityEnumGenerator}.*
import ${packageName.value}.${UniqueKeyEnumGenerator.uniqueKeyEnumGeneratorNameClass}.*

enum class $nameClass(
    override val fromEntity: ${IMetaEntity::class.java.canonicalName},
    override val toEntity: ${IMetaEntity::class.java.canonicalName},
    override val uk: ${IMetaUkEntity::class.java.canonicalName}
): ${IMetaFkEntity::class.java.canonicalName} {
$entities
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName(nameClass), GeneratedCode(trimIndent)))
            }
        }


    }

companion object{
    val foreignKeyEnumGeneratorNameClass  = "DataDictionaryForeignKeyEnum"
}
}
