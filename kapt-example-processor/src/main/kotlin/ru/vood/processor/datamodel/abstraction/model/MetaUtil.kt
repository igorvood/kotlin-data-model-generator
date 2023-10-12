package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element

fun collectMetaEntity(
    elementsAnnotatedWith: Set<Element>,
    collector: Map<Element, MetaEntity> = mapOf(),
    roundEnvironment: RoundEnvironment
): Map<Element, MetaEntity> {


    val map = when (elementsAnnotatedWith.isEmpty()) {
        true -> collector
        false -> {
            val head = elementsAnnotatedWith.first()
            val tailElementsAnnotatedWith = elementsAnnotatedWith.drop(1).toSet()
            if (collector.contains(head)) {
                collectMetaEntity(tailElementsAnnotatedWith, collector, roundEnvironment)
            } else {

                val plus = collector.plus(head to MetaEntity(head, roundEnvironment))
                collectMetaEntity(tailElementsAnnotatedWith, plus, roundEnvironment)
            }
        }

    }


    return map
}

fun metaEntityColumns(
    entities: Map<ModelClassName, MetaEntity>,
    entity: ModelClassName,
    cols: Array<String>,
    currentClass: ModelClassName
): List<MetaEntityColumn> {
    val fromMetaEntity =
        entities[entity]
            ?: error("Для внешнего ключа сушности ${currentClass.value} Не найдена сущность в контексте ${entity.value}")
    val fromCols = cols.map { fkField ->
        fromMetaEntity.fields.filter { field -> field.name == fkField }.firstOrNull()
            ?: error("Не найдено поле ${fkField}  внешнего ключа для сущности  ${entity.value}")
    }
    return fromCols
}

fun collectMetaForeignKey(
    elementsAnnotatedWith: List<Pair<ForeignKey, ModelClassName>>,
    entities: Map<ModelClassName, MetaEntity>,
    collector: Set<MetaForeignKey> = setOf()
): Set<MetaForeignKey> {

    val map = when (elementsAnnotatedWith.isEmpty()) {
        true -> collector
        false -> {
            val head = elementsAnnotatedWith.first()
            val foreignKey = head.first

            val currentClass = head.second
            val foreignClass = ModelClassName(foreignKey.kClass)
            val colsFromAnnotation = foreignKey.cols
                .map { a -> a.currentTypeCol }.toTypedArray()
//            val colsFrom = foreignKey.currentTypeCols
            val fromCols = metaEntityColumns(
                entities = entities,
                entity = currentClass,
                cols = colsFromAnnotation,//foreignKey.cols.map { q -> q.currentTypeCol }.toTypedArray(),
                currentClass = currentClass
            )

            val toCols = metaEntityColumns(
                entities = entities,
                entity = foreignClass,
                currentClass = currentClass,
//                cols = foreignKey.outTypeCols//
                cols = foreignKey.cols.map { q -> q.outTypeCol }.toTypedArray()
            )

            if (fromCols.size != toCols.size) {
                error("Не совпадают по кол-ву списки колонок currentTypeCols и outTypeCols во внешнем ключе $foreignKey")
            }

            fromCols.indices
                .forEach { idx ->
                    val fromMetaEntityColumn = fromCols[idx]
                    val toMetaEntityColumn = toCols[idx]
                    if (fromMetaEntityColumn.type != toMetaEntityColumn.type) {
                        error("Для внешнего ключа $foreignKey не совпадают типы колонок  в текущей(${fromMetaEntityColumn.name}, ${fromMetaEntityColumn.type}) и внешней(${toMetaEntityColumn.name}, ${toMetaEntityColumn.type}) таблице")
                    }
                }

            val foreignMetaEntity = entities[foreignClass]!!
            val uks = foreignMetaEntity.uniqueKeysFields
                .filter { uksEntry ->

                    val ukCols = uksEntry.value.map { it.name }
                    val fkCols = toCols.map { it.name }
                    val b = ukCols.minus(fkCols).isEmpty() && fkCols.minus(ukCols).isEmpty()
                    b
                }
            val ukDto = if (uks.size != 1) {
                error(
                    """У сущности ${currentClass.value} 
                    для внешнего ключа $foreignKey 
                    во внешней таблице должен быть строго один уникальный ключ
                    список подходящих ключей-> ${uks.map { it.key.name.value }}"""
                )
            } else {
                uks.entries.first().key
            }

//                .size != toCols.size
            val fkCols = fromCols.withIndex()
                .map { from -> FkCol(from.value, toCols[from.index]) }
                .toSet()


            val element =
                MetaForeignKey(
                    ForeignKeyName(foreignKey.name), entities[currentClass]!!, foreignMetaEntity, fkCols,
                    ukDto
                )


            val plus = collector.plus(element)
            collectMetaForeignKey(elementsAnnotatedWith.drop(1), entities, plus)
        }
    }


    return map
}

fun RoundEnvironment.metaInformation(): MetaInformation {
    val elementsAnnotatedWithFlowEntity = this.getElementsAnnotatedWith(FlowEntity::class.java)

    val allMeta: Map<Element, MetaEntity> = collectMetaEntity(elementsAnnotatedWithFlowEntity, roundEnvironment = this)


    val entities: Map<ModelClassName, MetaEntity> =
        allMeta.map { ModelClassName(it.value.kotlinMetaClass.toString()) to it.value }.toMap()

    val map = entities.flatMap { it.value.uniqueKeysFields.entries.map { w -> w.key.name to it.key } }
        .groupBy { it.first.value }
        .filter { it.value.size > 1 }
        .map { "dublicate uk name ${it.key} for entities ${it.value.map { w -> w.second.value }}" }

    if (map.isNotEmpty()) {
        error(map)
    }

    val groupBy =
        entities.map { it.key to it.value.name }
            .groupBy { it.second }
            .filter { it.value.size > 1 }
            .flatMap { it.value.map { e -> e.first } }
            .distinct()
            .joinToString(",\n")
    if (groupBy.isNotEmpty()) {
        error("Class name without package must be unique. Duplicate names entity for next classes: \n$groupBy")
    }


    val fks = entities.map { it.value }
        .flatMap { qw ->
            qw.foreignKeysAnnotations.map { fk -> fk to ModelClassName(qw.kotlinMetaClass.toString()) }
        }

    val collectMetaForeignKey = collectMetaForeignKey(fks, entities)


    val dublicatetdFkName =
        collectMetaForeignKey.map { it.name to it.fromEntity.name }
            .groupBy { it.first }
            .filter { it.value.size > 1 }
            .map { "dublicate FK name ${it.key.value} in enties: ${it.value.map { q -> q.second }}" }
            .joinToString("\n")

    if (dublicatetdFkName.isNotEmpty()) {
        error(dublicatetdFkName)
    }
//    assert(dublicatetdFkName.isNotEmpty()) { dublicatetdFkName }


    return MetaInformation(collectMetaForeignKey, entities)
}