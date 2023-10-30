package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.Comment
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Uk
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractAnnotatedClass
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import ru.vood.processor.datamodel.abstraction.model.abstraction.necessaryAnnotation
import javax.lang.model.element.Element

data class MetaEntity(val element: Element,
                      var node: Tree =None
) : AbstractAnnotatedClass<MetaEntityColumn>(element) {
    override fun elementToIGeneratedField(posicion: Int, e: Element): MetaEntityColumn =
        MetaEntityColumn(posicion, e)

    val flowEntityType = element.necessaryAnnotation<FlowEntity>().entityType

    val comment: String? = element.annotation<Comment>().map { it.comment }.orElse(null)

    val foreignKeysAnnotations = element.annotations<ForeignKey>()

    val uniqueKeysAnnotations = element.annotations<Uk>()

    val pkColumns by lazy {

        val pkCols = fields.filter { it.inPk }.map { it.name }.toSet()
        val notNullPkCols = fields.filter { it.inPk && !it.isNullable() }.map { it.name }.toSet()

        val minus = pkCols.minus(notNullPkCols.toSet())
        if (minus.isNotEmpty()){
            error("Entity ${kotlinMetaClass.toString()} contains nullable columns in PK ${minus}")
        }

        UkDto(UkName(shortName+"_PK"), pkCols) to fields.filter { it.inPk }
    }
    val uniqueKeysFields: Map<UkDto, List<MetaEntityColumn>> by lazy {
        val allUk = uniqueKeysAnnotations
            .map { anno ->
                UkDto(UkName(anno.name), anno.cols.map { ColumnName(it) }.toSet()) to anno.cols
                    .map { annoColName ->
                        annoColName to fields.filter { f -> f.name.value == annoColName }
                            .map { metaCol -> metaCol }
                            .firstOrNull()
                    }
            }.map { uk ->
                val ukCols = uk.second
                val map = ukCols.map { pair: Pair<String, MetaEntityColumn?> ->
                    val metaEntityColumn = pair.second
                    if (metaEntityColumn == null) {
                        error("for entity $name Uk annotation colum ${pair.first} not contains field class ")
                    } else pair.first to metaEntityColumn
                }
                uk.first to map
            }.map { uk ->
                val ukCols = uk.second
                ukCols.forEach { pair: Pair<String, MetaEntityColumn> ->
                    val metaEntityColumn = pair.second
                    if (metaEntityColumn.typeCollection != null) {
                        error("for entity $name Uk annotation colum ${pair.first} must not to be collection ${metaEntityColumn.typeCollection}")
                    }
                    if (metaEntityColumn.type !in ukTypes) {
                        error("for entity $name Uk annotation colum ${pair.first} must be on of next types $ukTypes, current type is ${metaEntityColumn.type}")
                    }
                }
                uk.first to uk.second.map { q -> q.second }
            }
//            .toMap()
            .plus(pkColumns)
        val dublicateUk = allUk.map { it.first.name.value }
            .groupBy {it}
            .filter { it.value.size>1 }
            .map { it.key }
            .distinct()

        if (dublicateUk.isNotEmpty()){
            error("for entity ${shortName}  dublicate UK Name ${dublicateUk} ")
        }

        allUk.toMap()
    }
    companion object {
        val ukTypes = listOf(
            "int",
            "java.lang.String",
            "long",
            "double",
            "float",
            "boolean",
            "java.time.Instant",
            "java.math.BigDecimal",
            "kotlin.Int",
            "Long",
            "Float",
            "Double",
            "String",
            "Boolean",
            "Float",
        )
    }
}