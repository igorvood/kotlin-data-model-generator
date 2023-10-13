package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.annotation.FKType
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
        val foreignKeyMap: Map<MetaEntity, List<MetaForeignKey>> = generatedClassData.collectMetaForeignKey.groupBy { it.toEntity }
        val map = metaEntitySet
            .map { metaEntity ->


                val fk: String = foreignKeyProcessor(metaEntity, foreignKeyMap)

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
$joinToString,

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


    private fun foreignKeyProcessor(toMetaEntity: MetaEntity, metaForeignKeys: Map<MetaEntity, List<MetaForeignKey>>): String {
        val metaForeignKeysToEntity = metaForeignKeys[toMetaEntity]
        return if (metaForeignKeysToEntity != null && metaForeignKeysToEntity.isNotEmpty()) {
            metaForeignKeysToEntity
                .filter { q-> q.fromEntity.flowEntity== FKType.INNER }
                .map { foreignKey ->
                val fromEntity = foreignKey.fromEntity
                val fromEntityFkCols = foreignKey.fkCols.map { it.from.name }.toSet()
                val fromEntityUKsCols = fromEntity.uniqueKeysFields.keys.map { aas -> aas.cols }
                val uksOneTOne = fromEntityUKsCols.filter { ukCols ->
                    ukCols.equalsAnyOrder(fromEntityFkCols)
                }
                val relationType = if (uksOneTOne.size==1) {
                    val metaForeignKeyMayBeCircle = metaForeignKeys[fromEntity]?.map { it.toEntity }?.filter { it == fromEntity }
                        ?.isNotEmpty()
                        ?:false


                    val isOneToOneOptional = !metaForeignKeyMayBeCircle
                    val s = if (isOneToOneOptional) {
                        "?"
                    } else ""
                    "val ${fromEntity.name} : ${packageName.value}.${fromEntity.name}Entity$s"
                } else {
                    ""
                }

                    relationType
            }.filter { ass->ass.isNotEmpty() }
                .joinToString(",\n")


        }else{""}

    }

    override val subPackage: PackageName
        get() = entityDataClassesGeneratorPackageName

    companion object {
        val entityDataClassesGeneratorPackageName = PackageName("runtime.dataclasses")
    }
}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}
