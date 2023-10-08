package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.intf.IContextOf
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityDataClassesGenerator(
    messager: Messager,
    filer: Filer,
    processingEnv: ProcessingEnvironment

) : AbstractGenerator<Set<MetaEntity>>(messager, filer, processingEnv) {

    private val commonPackage = "ru.vood.datamodel.meta.runtime.dataclasses"//calculatePackage(packages)

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        val map = generatedClassData
            .map { contextData ->
                val dataClass = contextData.name

                val columns = contextData.fields.sortedBy { it.name }

                val joinToString = columns.map { col ->
                    val kotlinMetaClass = col.kotlinMetaClass.toString()

                    "val ${col.name}: $kotlinMetaClass"
                }
                    .joinToString(",\n")

                val fullClassName = """${dataClass}Entity"""
                val code = """package $commonPackage
                    
@kotlinx.serialization.Serializable
data class $fullClassName (
$joinToString
): ${IContextOf::class.java.canonicalName}                    
                    
                """.trimIndent()

                log(Diagnostic.Kind.NOTE, "Create $fullClassName")
                GeneratedFile(FileName( fullClassName), GeneratedCode( code))



            }.toSet()


        return map
    }

    override val subDir: String
        get() = "runtime.dataclasses"
}