package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Uk
import ru.vood.processor.datamodel.abstraction.model.abstraction.metadto.AbstractAnnotatedClass
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import ru.vood.processor.datamodel.abstraction.model.abstraction.necessaryAnnotation
import javax.lang.model.element.Element

data class MetaEntity(val element: Element) : AbstractAnnotatedClass<MetaEntityColumn>(element) {
    override fun elementToIGeneratedField(e: Element): MetaEntityColumn =
        MetaEntityColumn(e)

    val flowEntity = element.necessaryAnnotation<FlowEntity>()

    val foreignKeysAnnotations = element.annotations<ForeignKey>()

    val uniqueKeysAnnotations = element.annotations<Uk>()

    val pkColumns =
        UkDto(UkName(shortName+"_PK"),fields.filter { it.inPk }.map { ColumnName(it.name) }.toSet()) to fields.filter { it.inPk }

    val uniqueKeysFields: Map<UkDto, List<MetaEntityColumn>> by lazy {
        val allUk = uniqueKeysAnnotations
            .map { anno ->
                UkDto(UkName(anno.name), anno.cols.map { ColumnName(it) }.toSet()) to anno.cols
                    .map { annoColName ->
                        annoColName to fields.filter { f -> f.name == annoColName }
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
        )
    }
}