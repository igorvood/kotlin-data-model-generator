package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element

fun collectMetaEntity(
    elementsAnnotatedWith: Set<Element>,
    collector: Map<Element, MetaEntity> = mapOf()
): Map<Element, MetaEntity> {


    val map = when (elementsAnnotatedWith.isEmpty()) {
        true -> collector
        false -> {
            val head = elementsAnnotatedWith.first()
            val tailElementsAnnotatedWith = elementsAnnotatedWith.drop(1).toSet()
            if (collector.contains(head)) {
                collectMetaEntity(tailElementsAnnotatedWith, collector)
            } else {
                val plus = collector.plus(head to MetaEntity(head))
                collectMetaEntity(tailElementsAnnotatedWith, plus)
            }
        }

    }


    return map
}

fun metaEntityColumns(
    entities: Map<ModelClassName, MetaEntity>,
    entity: ModelClassName,
    cols: Array<String>
): List<MetaEntityColumn> {
    val fromMetaEntity =
        entities[entity] ?: error("Не найдена сущность в контексте ${entity.value}")
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
            val fromCols = metaEntityColumns(entities, currentClass, foreignKey.currentTypeCols)

            val toCols = metaEntityColumns(
                entities = entities,
                entity = foreignClass,
                cols = foreignKey.outTypeCols
            )

            if (fromCols.size != toCols.size) {
                error("Не совпадают по кол-ву списки колонок currentTypeCols и outTypeCols во внешнем ключе $foreignKey")
            }

            val map = fromCols.indices
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
            if (uks.size != 1) {
                error("У сущности ${currentClass.value} для внешнего ключа $foreignKey во внешней таблице не найден уникальный ключ\nuks-> ${uks}")
            }
            val element = MetaForeignKey(entities[currentClass]!!, foreignMetaEntity)
            val plus = collector.plus(element)
            collectMetaForeignKey(elementsAnnotatedWith.drop(1), entities, plus)
        }
    }


    return map
}

fun RoundEnvironment.metaInformation(): MetaInformation {
    val elementsAnnotatedWithFlowEntity = this.getElementsAnnotatedWith(FlowEntity::class.java)

    val allMeta: Map<Element, MetaEntity> = collectMetaEntity(elementsAnnotatedWithFlowEntity)


    val entities: Map<ModelClassName, MetaEntity> =
        allMeta.map { ModelClassName(it.value.kotlinMetaClass.toString()) to it.value }.toMap()

    val elementsAnnotatedWith = this.getElementsAnnotatedWith(ForeignKey::class.java)


    val fks = entities.map { it.value }
        .flatMap { qw ->
            qw.foreignKeysAnnotations.map { fk -> fk to ModelClassName(qw.kotlinMetaClass.toString()) }
        }

    val collectMetaForeignKey = collectMetaForeignKey(fks, entities)

    return MetaInformation(collectMetaForeignKey, entities)
}