package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IEntity
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses.EntityDataClassesGenerator.Companion.entityDataClassesGeneratorPackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityEnumGenerator(
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
                    .map { """${it.shortName}(${it.kotlinMetaClass.canonicalName}::class, 
                        |${rootPackage.value}${entityDataClassesGeneratorPackageName.value}.${it.kotlinMetaClass.simpleName}Entity::class,
                        |${rootPackage.value}${entityDataClassesGeneratorPackageName.value}.${it.kotlinMetaClass.simpleName}Entity.serializer(),
                        |${EntityName::class.java.canonicalName}("${it.shortName}"), 
                        |"${it.comment}")""".trimMargin() }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer

enum class $nameClass(
override val designClass: KClass<out Any>,
override val runtimeClass: KClass<out ${IEntity::class.java.canonicalName}<*>>,
override val serializer: KSerializer<out ${IEntity::class.java.canonicalName}<*>>,
override val entityName: ${EntityName::class.java.canonicalName},
override val comment: String

): ${IMetaEntity::class.java.canonicalName} {
$entities;

@Suppress("UNCHECKED_CAST")
inline fun <reified T> entitySerializer(): KSerializer<T> = this.serializer as KSerializer<T> 
}
"""
                log(Diagnostic.Kind.NOTE, "Create $nameClass")
                setOf(GeneratedFile(FileName("$nameClass"), GeneratedCode(trimIndent)))
            }
        }


    }

    companion object{
        val nameClassEntityEnumGenerator = "DataDictionaryEntityEnum"
    }

}

