package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntityType

data class MetaInformation(
    val collectMetaForeignKey: Set<MetaForeignKey>,
    val entities: Map<ModelClassName, MetaEntity>
) {





}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

