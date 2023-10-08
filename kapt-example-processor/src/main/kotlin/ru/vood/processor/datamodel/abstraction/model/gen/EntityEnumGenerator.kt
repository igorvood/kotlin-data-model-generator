package ru.vood.processor.datamodel.abstraction.model.gen

import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
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
                    .map { """${it.shortName}(${it.kotlinMetaClass.canonicalName}::class, ${EntityName::class.java.canonicalName}("${it.shortName}"), "${it.comment}")""" }
                    .sorted()
                    .joinToString(",\n")

                val trimIndent =
                    """package ${packageName.value}
import kotlin.reflect.KClass

enum class $nameClass(
override val designClass: KClass<*>,
override val entityName: ${EntityName::class.java.canonicalName},
override val comment: String

): ${IMetaEntity::class.java.canonicalName} {
$entities
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

