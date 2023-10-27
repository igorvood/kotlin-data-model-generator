package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntityType

data class MetaInformation(
    val collectMetaForeignKeyTemporary: Set<MetaForeignKey>,
    val entities: Map<ModelClassName, MetaEntity>
) {

    fun aggregateInnerDep(): List<Tree> {
        val aggregate = entities.filter { it.value.flowEntityType == FlowEntityType.AGGREGATE }
        val map = aggregate.map { collectInnerDependency(it.value) }
        return map
    }


    private fun collectInnerDependency(rootModelClassName: MetaEntity): Tree {
        val map = collectMetaForeignKeyTemporary
            .filter { it.toEntity.modelClassName == rootModelClassName.modelClassName }
            .map { collectInnerDependency(it.fromEntity) }
            .toSet()
        return Dependency(rootModelClassName, map)
    }
}

sealed interface Tree

data class Dependency(
    val modelClassName: MetaEntity,

    val children: Set<Tree>
) : Tree {
    val haveChildren = children.isNotEmpty()
}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

