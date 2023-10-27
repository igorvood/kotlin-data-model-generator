package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.IMetaColumnEntity
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.dmgen.intf.IMetaUkEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.EntityEnumGenerator.Companion.nameClassEntityEnumGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.ContextDataClassesGenerator.Companion.contextDataClassesGeneratorPackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class UniqueKeyEnumGenerator(
    messager: Messager,

    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

    ) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, processingEnv, rootPackage) {

    override val nameClass: String
        get() = uniqueKeyEnumGeneratorNameClass

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData
                    .flatMap { metaEnt ->
                        metaEnt.uniqueKeysFields.keys
                            .map { ukDto ->
                                val ukCols =
                                    ukDto.cols.map { columnName -> metaEnt.name + "_" + columnName.value }
                                        .sorted()
                                        .joinToString(",")

                                """${ukDto.name.value}(
                                    |setOf($ukCols),
                                    |${packageName.value}.${nameClassEntityEnumGenerator}.${metaEnt.shortName},
                                    |${rootPackage.value}${contextDataClassesGeneratorPackageName.value}.${metaEnt.shortName}Context${ukDto.name.value}::class,
                                    |)""".trimMargin()

                            }
                    }

                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
                        
import ${packageName.value}.${ColumnEntityEnumGenerator.columnEntityEnumGeneratorNameClass}.*
import kotlin.reflect.KClass

enum class $nameClass(
    override val columns: Set<${IMetaColumnEntity::class.java.canonicalName}>,
    override val entity : ${IMetaEntity::class.java.canonicalName},
    override val contextOfClass: KClass<*>
): ${IMetaUkEntity::class.java.canonicalName}{
$entities
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object{
        val uniqueKeyEnumGeneratorNameClass = "DataDictionaryUniqueKeyEnum"
    }

}

