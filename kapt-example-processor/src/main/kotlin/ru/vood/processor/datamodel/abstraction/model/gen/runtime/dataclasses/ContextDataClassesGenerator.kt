package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.intf.IContextOf
import ru.vood.dmgen.intf.IEntity
import ru.vood.dmgen.intf.IMetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.AbstractDataDictionaryGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.EntityEnumGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class ContextDataClassesGenerator(
    messager: Messager,
    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName

) : AbstractGenerator<Set<MetaEntity>>(messager, processingEnv,rootPackage) {

    override fun textGenerator(generatedClassData: Set<MetaEntity>): Set<GeneratedFile> {
        val map = generatedClassData
            .flatMap { metaEntity ->
                metaEntity.uniqueKeysFields
                    .map { ukData -> metaEntity to ukData }
            }
            .map { contextData ->
                val dataClass = contextData.first.name
                val entityName = """${dataClass}Entity"""
                val contextName = contextData.second.key.name
                val columns = contextData.second.value.sortedBy { it.name.value }

                val joinToString = columns.map { col ->
                    val kotlinMetaClass = col.type

                    "val ${col.name.value}: $kotlinMetaClass"
                }
                    .joinToString(",\n")

                val fullClassName = """${dataClass}Context${contextName.value}"""
                val code = """package ${packageName.value}
                    
@kotlinx.serialization.Serializable
data class $fullClassName (
$joinToString
): ${IContextOf::class.java.canonicalName}<${rootPackage.value}${EntityDataClassesGenerator.entityDataClassesGeneratorPackageName.value}.$entityName>//,
//${IEntity::class.java.canonicalName}<$fullClassName>
{
override val metaEntity: ${IMetaEntity::class.java.canonicalName}
        get() = ${rootPackage.value}${AbstractDataDictionaryGenerator.subPackageAbstractDataDictionaryGenerator.value}.${EntityEnumGenerator.nameClassEntityEnumGenerator}.$dataClass
        
override fun ktSerializer() = serializer()        
}          
                    
                """.trimIndent()

                log(Diagnostic.Kind.NOTE, "Create $fullClassName")
                GeneratedFile(FileName( fullClassName), GeneratedCode( code))



            }.toSet()


        return map
    }

    override val subPackage: PackageName
        get() = contextDataClassesGeneratorPackageName

    companion object{
        val contextDataClassesGeneratorPackageName = PackageName("runtime.dataclasses.context")
    }
}