package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.*
import ru.vood.dmgen.intf.newIntf.UKEntityData
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.UkName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.ContextDataClassesGenerator.Companion.contextDataClassesGeneratorPackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.EntityDataClassesGenerator
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class UniqueKeyMapGenerator(
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
                                    ukDto.cols.map { columnName -> """${ColumnName::class.java.canonicalName}("${metaEnt.name}_${columnName.value}")""" }
                                        .sorted()
                                        .joinToString(",")
                                val constructorParams = ukDto.cols.map { columnName -> """data.${columnName.value}""" }
                                    .sorted()
                                    .joinToString(",")
//
                                        """${UkName::class.java.canonicalName}("${ukDto.name.value}") to UKEntityData(
                                    |setOf($ukCols),
                                    |${EntityName::class.java.canonicalName}("${metaEnt.shortName}"),
                                    |{data: ${metaEnt.shortName}Entity -> ${metaEnt.shortName}Context${ukDto.name.value}($constructorParams) }
                                    |//${rootPackage.value}${contextDataClassesGeneratorPackageName.value}.${metaEnt.shortName}Context${ukDto.name.value}::class,

                                    |)""".trimMargin()

//                                """${ukDto.name.value}(
//                                    |setOf($ukCols),
//                                    |${packageName.value}.${nameClassEntityEnumGenerator}.${metaEnt.shortName},
//                                    |${rootPackage.value}${contextDataClassesGeneratorPackageName.value}.${metaEnt.shortName}Context${ukDto.name.value}::class,
//                                    |)""".trimMargin()

                            }
                    }

                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
                        
import ${rootPackage.value}${EntityDataClassesGenerator.entityDataClassesGeneratorPackageName.value}.*
import ${rootPackage.value}${contextDataClassesGeneratorPackageName.value}.*
import ${UKEntityData::class.java.canonicalName}
import kotlin.reflect.KClass


val uniqueKeyMap = mapOf(
$entities

)


"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object{
        val uniqueKeyEnumGeneratorNameClass = "DataDictionaryUniqueKeyMap"
    }

}

