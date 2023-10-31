package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.ColumnName
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.newIntf.ColumnEntityData
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.EntityDataClassesGenerator.Companion.entityDataClassesGeneratorPackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ColumnEntityMapGenerator(
    messager: Messager,
    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, processingEnv, rootPackage) {

    override val nameClass: String
        get() = columnEntityEnumGeneratorNameClass

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData
                    .flatMap { ent ->
                        ent.fields
                            .map { f ->
                                """${ColumnName::class.java.canonicalName}("${ent.shortName}_${f.name.value}") to ColumnEntityData(
                                |    ${EntityName::class.java.canonicalName}( "${ent.shortName}"),
                                |${rootPackage.value}${entityDataClassesGeneratorPackageName.value}.${ent.kotlinMetaClass.simpleName}Entity::${f.name.value},
                                |${ColumnName::class.java.canonicalName}("${f.name.value}"),
                                |"${f.comment}"
                                |)""".trimMargin()


//                            """${ent.shortName}_${f.name.value}(
//                                |${ent.shortName},
//                                |${ent.kotlinMetaClass.canonicalName}::${f.name.value},
//                                |${ColumnName::class.java.canonicalName}("${ent.shortName}"),
//                                |"${f.comment}"
//                                |)""".trimMargin()
                            }
                    }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
                        
import ${packageName.value}.${EntityEnumGenerator.nameClassEntityEnumGenerator}.*
import ${ColumnEntityData::class.java.canonicalName}
import kotlin.reflect.KProperty1


val columnEntityDataMap = mapOf(
$entities

)

"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object {
        val columnEntityEnumGeneratorNameClass = "DataDictionaryColumnEntityMap"
    }
}

