package ru.vood.processor.datamodel.abstraction.model.gen.runtime.dataclasses

import ru.vood.dmgen.annotation.FlowEntityType
import ru.vood.dmgen.annotation.RelationType
import ru.vood.dmgen.intf.IAggregate
import ru.vood.dmgen.intf.IEntity
import ru.vood.processor.datamodel.abstraction.model.*
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractGenerator
import ru.vood.processor.datamodel.abstraction.model.gen.dto.FileName
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedCode
import ru.vood.processor.datamodel.abstraction.model.gen.dto.GeneratedFile
import ru.vood.processor.datamodel.abstraction.model.gen.dto.PackageName
import java.util.Optional
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class EntityDataClassesGenerator(
    messager: Messager,
    processingEnv: ProcessingEnvironment,
    rootPackage: PackageName
) : AbstractGenerator<MetaInformation>(messager, processingEnv, rootPackage) {

    override fun textGenerator(generatedClassData: MetaInformation): Set<GeneratedFile> {
        val generatedFiles: Set<GeneratedFile> = collectEntityFile(generatedClassData.metaForeignKeys, generatedClassData.aggregateInnerDep())
        return generatedFiles
    }

    private fun collectEntityFile(metaForeignKeys: Set<MetaForeignKey>, aggregateInnerDep: Dependency , collector:Set<GeneratedFile> = setOf() ): Set<GeneratedFile> {

        val metaEntity = aggregateInnerDep.metaEntity

        val fk = aggregateInnerDep.children
            .map { child ->
                when (val fet = child.metaEntity.flowEntityType) {
                    FlowEntityType.AGGREGATE -> Optional.empty<String>()
                    FlowEntityType.INNER_MANDATORY, FlowEntityType.INNER_OPTIONAL -> {
                        val currentFks =
                            metaForeignKeys.filter { fk -> fk.toEntity == metaEntity && fk.fromEntity == child.metaEntity }
                        val metaForeignKey =
                            if (currentFks.size == 1) currentFks[0] else error("Found several fk from entity ${child.metaEntity.modelClassName.value} to   ${metaEntity.modelClassName.value}  ")


                        val s = if (fet.isOptional) "?" else ""
                        val genField = genField(child.metaEntity, s, metaForeignKey.relationType)
                        Optional.of(genField)
                    }

                }
            }
            .filter{!it.isEmpty}
            .map { it.get() }
            .joinToString(",\n")


//        val fk: String = foreignKeyProcessor(metaEntity, foreignKeyMap)

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
        val s = when (metaEntity.flowEntityType) {
            FlowEntityType.AGGREGATE -> """${IAggregate::class.java.canonicalName}<$fullClassName>//, ${metaEntity.kotlinMetaClass.toString()}"""
            FlowEntityType.INNER_OPTIONAL, FlowEntityType.INNER_MANDATORY -> """${IEntity::class.java.canonicalName}<$fullClassName>//, ${metaEntity.kotlinMetaClass.toString()}"""
        }

        val code = """package ${packageName.value}
                    
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
        val map = aggregateInnerDep.children
            .map { df -> collectEntityFile(metaForeignKeys, df, collector) }
            .flatten()
            .toSet()

        val plus = collector.plus(GeneratedFile(FileName(fullClassName), GeneratedCode(code))).plus(map)


        return plus
    }

    enum class Relation {
        MANDATORY,
        OPTIONAL
    }
    private fun genField(toEntity: MetaEntity, question: String, relationType: RelationType) =
        when (relationType) {
            RelationType.ONE_TO_ONE_OPTIONAL -> "val ${toEntity.entityFileldName} : ${packageName.value}.${toEntity.name}Entity$question"
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
