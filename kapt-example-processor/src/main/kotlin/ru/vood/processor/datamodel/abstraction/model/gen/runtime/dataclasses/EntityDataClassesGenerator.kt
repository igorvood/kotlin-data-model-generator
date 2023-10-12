package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.intf.IEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityDataClassesGenerator(
    messager: Messager,

    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractGenerator<Set<MetaEntity>>(messager, processingEnv,rootPackage) {

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        val map = generatedClassData
            .map { contextData ->
                val dataClass = contextData.name

                val columns = contextData.fields.sortedBy { it.position }

                val joinToString = columns.map { col ->
                    val kotlinMetaClass = col.type

                    col.element
                    val nullableSymbol = if (col.isNullable()) "?" else ""
                    """${col.comment?.let { """/**
*$it
*/
""".trimIndent() }?:""}     
val ${col.name}: $kotlinMetaClass$nullableSymbol""".trimIndent()
                }
                    .joinToString(",\n")

                val fullClassName = """${dataClass}Entity"""
                val code = """package ${packageName.value}
${contextData.comment?.let { """/**
*$it
*/
""".trimIndent() }?:""}                    
@kotlinx.serialization.Serializable
data class $fullClassName (
$joinToString
): ${IEntity::class.java.canonicalName}<$fullClassName>//, ${contextData.kotlinMetaClass.toString()}         
{
    override fun ktSerializer() = serializer()
}
                    
                """.trimIndent()

                log(Diagnostic.Kind.NOTE, "Create $fullClassName")
                GeneratedFile(FileName(fullClassName), GeneratedCode(code))


            }.toSet()


        return map
    }

    override val subPackage: PackageName
        get() = entityDataClassesGeneratorPackageName

    companion object{
        val entityDataClassesGeneratorPackageName = PackageName("runtime.dataclasses")
    }
}