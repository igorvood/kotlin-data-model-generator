package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.intf.IEntity
import ru.vood.processor.datamodel.abstraction.model.MetaEntity
import ru.vood.processor.datamodel.abstraction.model.MetaForeignKey
import ru.vood.processor.datamodel.abstraction.model.MetaInformation
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
) : AbstractGenerator<MetaInformation>(messager, processingEnv, rootPackage) {

    override fun textGenerator(generatedClassData: MetaInformation): Set<GeneratedFile> {
        val metaEntitySet = generatedClassData.entities.map { it.value }.toSet()
        val foreignKeyMap = generatedClassData.collectMetaForeignKey.groupBy { it.toEntity }
        val map = metaEntitySet
            .map { metaEntity ->

                val metaForeignKeys = foreignKeyMap[metaEntity]
                val fk: String = foreignKeyProcessor(metaEntity, metaForeignKeys)

                val dataClass = metaEntity.name

                val columns = metaEntity.fields.sortedBy { it.position }

                val joinToString = columns.map { col ->
                    val kotlinMetaClass = col.type

                    col.element
                    val nullableSymbol = if (col.isNullable()) "?" else ""
                    """${
                        col.comment?.let {
                            """/**
*$it
*/
""".trimIndent()
                        } ?: ""
                    }     
val ${col.name.value}: $kotlinMetaClass$nullableSymbol""".trimIndent()
                }
                    .joinToString(",\n")

                val fullClassName = """${dataClass}Entity"""
                val code = """package ${packageName.value}
import arrow.optics.*                    
                    
${
                    metaEntity.comment?.let {
                        """/**
*$it
*/
""".trimIndent()
                    } ?: ""
                }                    
@kotlinx.serialization.Serializable
@optics([OpticsTarget.LENS])
data class $fullClassName (
$joinToString

$fk
): ${IEntity::class.java.canonicalName}<$fullClassName>//, ${metaEntity.kotlinMetaClass.toString()}         
{
    override fun ktSerializer() = serializer()
    
    companion object
}
                    
                """.trimIndent()

                log(Diagnostic.Kind.NOTE, "Create $fullClassName")
                GeneratedFile(FileName(fullClassName), GeneratedCode(code))


            }.toSet()


        return map
    }

    private fun foreignKeyProcessor(toMetaEntity: MetaEntity, metaForeignKeys: List<MetaForeignKey>?): String {
        if (metaForeignKeys != null && metaForeignKeys.isNotEmpty()) {
            metaForeignKeys.map { foreignKey ->
                val map = foreignKey.fkCols.map { it.from.name }
                val fromEntity = foreignKey.fromEntity
                val ukToMeta = foreignKey.uk
                val uniqueKeysFieldsFromEntity = fromEntity.uniqueKeysFields.keys.map { it.cols }
                val cols = ukToMeta.cols
                val contains = uniqueKeysFieldsFromEntity.contains(cols)
                val relationType = if (contains) {
                    "OneToOne"
                } else {
                    ""
                }








            }


        }
        return ""
    }

    override val subPackage: PackageName
        get() = entityDataClassesGeneratorPackageName

    companion object {
        val entityDataClassesGeneratorPackageName = PackageName("runtime.dataclasses")
    }
}