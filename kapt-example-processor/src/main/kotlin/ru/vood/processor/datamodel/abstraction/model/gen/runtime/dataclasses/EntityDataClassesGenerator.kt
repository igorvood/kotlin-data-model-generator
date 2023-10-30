package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.annotation.FlowEntityType
import ru.vood.dmgen.annotation.RelationType
import ru.vood.dmgen.intf.IAggregate
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
        val foreignKeyMap = generatedClassData.metaForeignKeys.groupBy { it.toEntity }
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
                val s = when (metaEntity.flowEntityType){
                    FlowEntityType.AGGREGATE -> """${IAggregate::class.java.canonicalName}<$fullClassName>//, ${metaEntity.kotlinMetaClass.toString()}"""
                    FlowEntityType.INNER_OPTIONAL, FlowEntityType.INNER_MANDATORY -> """${IEntity::class.java.canonicalName}<$fullClassName>//, ${metaEntity.kotlinMetaClass.toString()}"""
                }

                val code = """package ${packageName.value}
//import arrow.optics.*                    
                    
${
                    metaEntity.comment?.let {
                        """/**
*$it
*/
""".trimIndent()
                    } ?: ""
                }                    
@kotlinx.serialization.Serializable
//@optics([OpticsTarget.LENS])
data class $fullClassName (
$joinToString,

$fk
): $s         
{
    override fun ktSerializer() = serializer()
    
//    companion object
}
                    
                """.trimIndent()

                log(Diagnostic.Kind.NOTE, "Create $fullClassName")
                GeneratedFile(FileName(fullClassName), GeneratedCode(code))


            }.toSet()


        return map
    }

    enum class Relation {
        MANDATORY,
        OPTIONAL
    }

    private fun foreignKeyProcessor(
        toMetaEntity: MetaEntity,
        metaForeignKeysTemporary: Map<MetaEntity, List<MetaForeignKey>>
    ): String {
        val metaForeignKeysToEntityOptional =
            metaForeignKeysTemporary[toMetaEntity]?.filter { fk->fk.fromEntity.flowEntityType == FlowEntityType.INNER_OPTIONAL }?.associate { it to Relation.OPTIONAL }
            ?: mapOf()
        val metaForeignKeysToEntityMandatory =
            metaForeignKeysTemporary.values
                .flatMap { lfk -> lfk.filter { fk -> fk.fromEntity == toMetaEntity } }
                .map { it to Relation.MANDATORY }
                .toMap()

        val plus = metaForeignKeysToEntityMandatory.plus(metaForeignKeysToEntityOptional)

        val joinToString = plus.entries
            .map { entry ->
                val metaForeignKey = entry.key

                when (entry.value) {
                    Relation.MANDATORY -> genField(metaForeignKey.toEntity, "", metaForeignKey.relationType)
                    Relation.OPTIONAL ->
                        if (metaForeignKeysToEntityMandatory.keys.none { q -> q.toEntity == metaForeignKey.fromEntity }) {
                            genField(metaForeignKey.fromEntity, "?", metaForeignKey.relationType)
                        } else {
                            ""
                        }
                }
            }
            .filter { it != "" }
            .joinToString(",\n")

        return joinToString

    }

    private fun genField(toEntity: MetaEntity, question: String, relationType: RelationType) =
        when(relationType){
            RelationType.ONE_TO_ONE_MANDATORY, RelationType.ONE_TO_ONE_OPTIONAL -> "val ${toEntity.entityFileldName} : ${packageName.value}.${toEntity.name}Entity$question"
            RelationType.MANY_TO_ONE -> "val ${toEntity.entityFileldName} : Set<${packageName.value}.${toEntity.name}Entity>"
            RelationType.UNNOWN -> error("Не известный тип")
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
