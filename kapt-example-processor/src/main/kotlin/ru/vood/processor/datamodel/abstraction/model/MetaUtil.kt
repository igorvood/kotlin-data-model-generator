package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.RelationType
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element

tailrec fun collectMetaEntity(
    elementsAnnotatedWith: Set<Element>,
    collector: Map<Element, MetaEntity> = mapOf()
): Map<Element, MetaEntity> {
    return when (elementsAnnotatedWith.isEmpty()) {
        true -> collector
        false -> {
            val head = elementsAnnotatedWith.first()
            val tailElementsAnnotatedWith = elementsAnnotatedWith.drop(1).toSet()
            when (collector.contains(head)) {
                true -> collectMetaEntity(tailElementsAnnotatedWith, collector)
                false -> {
                    val plus = collector.plus(head to MetaEntity(head))
                    collectMetaEntity(tailElementsAnnotatedWith, plus)
                }
            }
        }
    }
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
        fromMetaEntity.fields.filter { field -> field.name.value == fkField }.firstOrNull()
            ?: error("Не найдено поле ${fkField}  внешнего ключа для сущности  ${entity.value}")
    }
    return fromCols
}

fun collectMetaForeignKey(
    elementsAnnotatedWith: List<Pair<ForeignKey, ModelClassName>>,
    entities: Map<ModelClassName, MetaEntity>,
    collector: Set<MetaForeignKeyTemporary> = setOf()
): Set<MetaForeignKeyTemporary> {

    val map = when (elementsAnnotatedWith.isEmpty()) {
        true -> collector
        false -> {
            val head = elementsAnnotatedWith.first()
            val foreignKey = head.first

            val fromMetaEntityClassName = head.second
            val toMetaEntityClassName = ModelClassName(foreignKey.kClass)
            val colsFromAnnotation = foreignKey.cols
                .map { a -> a.currentTypeCol }.toTypedArray()
//            val colsFrom = foreignKey.currentTypeCols
            val fromCols = metaEntityColumns(
                entities = entities,
                entity = fromMetaEntityClassName,
                cols = colsFromAnnotation,//foreignKey.cols.map { q -> q.currentTypeCol }.toTypedArray(),
                currentClass = fromMetaEntityClassName
            )

            val toCols = metaEntityColumns(
                entities = entities,
                entity = toMetaEntityClassName,
                currentClass = fromMetaEntityClassName,
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

            val foreignMetaEntity = entities[toMetaEntityClassName]!!
            val uks = foreignMetaEntity.uniqueKeysFields
                .filter { uksEntry ->

                    val ukCols = uksEntry.value.map { it.name }
                    val fkCols = toCols.map { it.name }
                    val b = ukCols.minus(fkCols).isEmpty() && fkCols.minus(ukCols).isEmpty()
                    b
                }
            val ukDto = if (uks.size != 1) {
                error(
                    """У сущности ${fromMetaEntityClassName.value} 
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
                MetaForeignKeyTemporary(
                    ForeignKeyName(foreignKey.name), entities[fromMetaEntityClassName]!!, foreignMetaEntity, fkCols,
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
    val entities: Map<ModelClassName, MetaEntity> =
        collectMetaEntity(elementsAnnotatedWithFlowEntity).map { ModelClassName(it.value.kotlinMetaClass.toString()) to it.value }.toMap()

    checkDublicateUk(entities)
    checkDublicateClassName(entities)

    val fks = entities.map { it.value }
        .flatMap { qw ->
            qw.foreignKeysAnnotations.map { fk -> fk to ModelClassName(qw.kotlinMetaClass.toString()) }
        }

    val collectMetaForeignKey = collectMetaForeignKey(fks, entities)

    checkDublicateFKName(collectMetaForeignKey)

    val fieldsFk = fieldsFk(collectMetaForeignKey)
    val metaInformation = MetaInformation(fieldsFk, entities)
    val message = metaInformation.aggregateInnerDep()
    println(message)
    return metaInformation
}

private fun checkDublicateFKName(collectMetaForeignKey: Set<MetaForeignKeyTemporary>) {
    val dublicatetdFkName =
        collectMetaForeignKey.map { it.name to it.fromEntity.name }
            .groupBy { it.first }
            .filter { it.value.size > 1 }
            .map { "dublicate FK name ${it.key.value} in enties: ${it.value.map { q -> q.second }}" }
            .joinToString("\n")

    if (dublicatetdFkName.isNotEmpty()) {
        error(dublicatetdFkName)
    }
}

private fun checkDublicateClassName(entities: Map<ModelClassName, MetaEntity>) {
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
}

private fun checkDublicateUk(entities: Map<ModelClassName, MetaEntity>) {
    val dublicatesUk = entities.flatMap { it.value.uniqueKeysFields.entries.map { w -> w.key.name to it.key } }
        .groupBy { it.first.value }
        .filter { it.value.size > 1 }
        .map { "dublicate uk name ${it.key} for entities ${it.value.map { w -> w.second.value }}" }

    if (dublicatesUk.isNotEmpty()) {
        error(dublicatesUk)
    }
}

private fun fieldsFk(
    collectMetaForeignKeyTemporary: Set<MetaForeignKeyTemporary>,

): Set<MetaForeignKey> {
    val metaForeignKeysTemporary: Map<MetaEntity, List<MetaForeignKeyTemporary>> =
        collectMetaForeignKeyTemporary.groupBy { it.toEntity }
    val map1 = metaForeignKeysTemporary.entries
        .map { entry ->
            entry.key to entry.value
//                .filter { a -> a.fromEntity.flowEntity == FlowEntityType.INNER }
                .map { metaFk -> metaFk.fromEntity to metaFk }
        }
        .flatMap { entry ->
            val fromEntities = entry.second
            val map = fromEntities
                .map { fromEnt ->
                    val fromMetaEntity = fromEnt.first
                    val metaForeignKeyTemporary = fromEnt.second
                    val fromEntityFkCols = metaForeignKeyTemporary.fkCols.map { qq -> qq.from.name }.toSet()
                    val fromEntityUKsCols = fromMetaEntity.uniqueKeysFields.keys.map { aas -> aas.cols }
                    val uksOneTOne = fromEntityUKsCols
                        .filter { ukCols -> ukCols.equalsAnyOrder(fromEntityFkCols) }

                    val relationType = if (uksOneTOne.size == 1) {
//                        val metaForeignKeyMayBeCircle =
//                            metaForeignKeysTemporary[fromMetaEntity]?.map { it.toEntity }
//                                ?.filter { it == fromMetaEntity }
//                                ?.isNotEmpty()
//                                ?: false


//                        val isOneToOneOptional = !metaForeignKeyMayBeCircle
//                        val s = if (isOneToOneOptional) {
//                            RelationType.ONE_TO_ONE_OPTIONAL
//                        } else RelationType.ONE_TO_ONE_MANDATORY
                        RelationType.ONE_TO_ONE_OPTIONAL
                    } else {
                        val fkCols = metaForeignKeyTemporary.fkCols.map { it.from.name }


                        val uksOneToMany = fromEntityUKsCols
                            .filter { ukCols ->
                                val minus = fkCols.minus(ukCols)
                                val minus1 = ukCols.minus(fkCols)
                                val notEmpty = minus.isEmpty()
                                val empty = minus1.isNotEmpty()
                                notEmpty && empty
//                                !ukCols.equalsAnyOrder(fromEntityFkCols) && fromEntityUKsCols.minus(ukCols).isEmpty()
                            }
                        if (uksOneToMany.isNotEmpty()) {
                            RelationType.MANY_TO_ONE
                        } else {
                            RelationType.UNNOWN
                        }

                    }
                    MetaForeignKey(metaForeignKeyTemporary, relationType)
                }

            map
        }
        .toSet()
    val minus = collectMetaForeignKeyTemporary.map { it.name }.minus(map1.map { it.name }.toSet())
    assert(minus.isEmpty()) { " Почему то не все обработаны $minus" }

    return map1

}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

