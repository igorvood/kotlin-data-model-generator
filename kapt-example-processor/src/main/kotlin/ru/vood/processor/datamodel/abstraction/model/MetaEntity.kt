package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Uk
import ru.vood.processor.datamodel.abstraction.model.abstraction.AbstractAnnotatedClass
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotation
import ru.vood.processor.datamodel.abstraction.model.abstraction.annotations
import javax.lang.model.element.Element

class MetaEntity(element: Element) : AbstractAnnotatedClass<MetaEntityColumn>(element) {
    override fun elementToIGeneratedField(e: Element): MetaEntityColumn =
        MetaEntityColumn(e)

    val flowEntity = element.annotation<FlowEntity>()

    val foreignKeysAnnotation = element.annotations<ForeignKey>()

    val uniqueKeysAnnotation = element.annotations<Uk>()

    val uniqueKeysFields: Map<Uk, List<MetaEntityColumn>> = uniqueKeysAnnotation
        .map { anno ->
            anno to anno.cols
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
        .toMap()

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