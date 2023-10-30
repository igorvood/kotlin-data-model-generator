package ru.vood.processor.datamodel.abstraction.model


data class MetaInformation(
    val metaForeignKeys: Set<MetaForeignKey>,
    val entities: Map<ModelClassName, MetaEntity>
) {

    fun aggregateInnerDep(): Tree {

        val filter =
            entities.filter { metaForeignKeys.filter { fk -> fk.fromEntity == it.value }.isEmpty() }
        if (filter.size != 1) {
            error("not found root entity, without ForeignKey on it")
        }
        val root = filter.entries.toList()[0].value
        return Dependency(
            modelClassName = root,
            collectInnerDependency(root, root),
            parent = null
        )
    }

    private fun collectInnerDependency(parentModelClassName: MetaEntity, root: MetaEntity): Set<Tree> {
        val filter = metaForeignKeys
            .filter { it.toEntity.modelClassName == parentModelClassName.modelClassName }
        val map1 = filter
            .map {
                val collectInnerDependency = collectInnerDependency(it.fromEntity, root)
                Dependency(
                    modelClassName = it.fromEntity,
                    children = collectInnerDependency,
                    parent = parentModelClassName
                )
//                collectInnerDependency
            }
        val map = map1
            .toSet()
        return map
    }


}

sealed interface Tree {
    val children: Set<Tree>
    val parent: MetaEntity?
    fun isRoot() = parent == null
    fun haveChildren() = children.isNotEmpty()

}
data class Dependency(
    val modelClassName: MetaEntity,
    override val children: Set<Tree>,
    override val parent: MetaEntity?
) : Tree

object None: Tree{
    override val children: Set<Tree>
        get() = TODO("Not yet implemented")
    override val parent: MetaEntity?
        get() = TODO("Not yet implemented")
}


private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

