package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.annotation.FlowEntityType
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IEntity
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.dmgen.intf.newIntf.EntityData
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.EntityDataClassesGenerator.Companion.entityDataClassesGeneratorPackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityMapGenerator(
    messager: Messager,

    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractDataDictionaryGenerator<Set<MetaEntity>>(messager, processingEnv, rootPackage) {

    override val nameClass: String
        get() = nameClassEntityEnumGenerator

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        return when (generatedClassData.isEmpty()) {
            true -> setOf()
            false -> {
                val entities = generatedClassData

                    .map {

                        """${EntityName::class.java.canonicalName}("${it.shortName}") to EntityData(
                            |${it.kotlinMetaClass.canonicalName}::class, 
                            |${rootPackage.value}${entityDataClassesGeneratorPackageName.value}.${it.kotlinMetaClass.simpleName}Entity::class,
                            |${rootPackage.value}${entityDataClassesGeneratorPackageName.value}.${it.kotlinMetaClass.simpleName}Entity.serializer(),
                            |${EntityName::class.java.canonicalName}("${it.shortName}"), 
                            |"${it.comment}",
                            |${it.flowEntityType}
                            |)""".trimMargin()
                    }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import ${FlowEntityType::class.java.canonicalName}.*
import ${FlowEntityType::class.java.canonicalName}
import ${EntityData::class.java.canonicalName}

val entityDataMap = mapOf(
$entities

)




"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object {
        val nameClassEntityEnumGenerator = "DataDictionaryEntityMap"
    }

}

