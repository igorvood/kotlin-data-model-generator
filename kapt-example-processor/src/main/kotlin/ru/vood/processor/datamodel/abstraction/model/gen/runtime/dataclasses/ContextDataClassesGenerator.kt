package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

class ContextDataClassesGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractGenerator<Set<MetaEntity>>(messager, filer, processingEnv) {

    private val commonPackage = "ru.vood.datamodel.meta.runtime.dataclasses"//calculatePackage(packages)

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        val map = generatedClassData
            .flatMap { metaEntity ->
                metaEntity.uniqueKeysFields
                    .map { ukData -> metaEntity to ukData }
            }
            .map { contextData ->
                val dataClass = contextData.first.name
                val contextName = contextData.second.key.name
                val columns = contextData.second.value.sortedBy { it.name }

                val joinToString = columns.map { col ->
                    val kotlinMetaClass = col.kotlinMetaClass.toString()

                    "val ${col.name}: $kotlinMetaClass"
                }
                    .joinToString(",\n")

                val fullClassName = """${dataClass}${contextName.value}"""
                val code = """package $commonPackage
                    
@kotlinx.serialization.Serializable
data class Context$fullClassName (
$joinToString
)                    
                    
                """.trimIndent()


                GeneratedFile(FileName( fullClassName), GeneratedCode( code))



            }.toSet()


        return map
    }

    override val subDir: String
        get() = "runtime.dataclasses"
}