package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.ColumnName
import ru.vood.dmgen.intf.IMetaColumnEntity
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ColumnEntityEnumGenerator(
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
                    .flatMap { ent -> ent.fields
                        .map { f->
                            """${ent.shortName}_${f.name}(
                                |${ent.shortName},
                                |${ent.kotlinMetaClass.canonicalName}::${f.name},
                                |${ColumnName::class.java.canonicalName}("${ent.shortName}"),
                                |"${f.comment}"
                                |)""".trimMargin()
                        }
                    }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
                        
import ${packageName.value}.${EntityEnumGenerator.nameClassEntityEnumGenerator}.*                        
import kotlin.reflect.KProperty1

enum class $nameClass(
    override val entity: ${IMetaEntity::class.java.canonicalName},
    override val kProperty1: KProperty1<*, *>,
    override val columnName: ${ColumnName::class.java.canonicalName},
    override val comment: String,
): ${IMetaColumnEntity::class.java.canonicalName} {
$entities
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object{
        val columnEntityEnumGeneratorNameClass = "DataDictionaryColumnEntityEnum"
    }
}

