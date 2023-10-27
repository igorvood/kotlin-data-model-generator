package ru.vood.processor.datamodel.abstraction.model

data class MetaInformation(
    val collectMetaForeignKeyTemporary: Set<MetaForeignKeyTemporary>,
    val entities: Map<ModelClassName, MetaEntity>
) {





}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

