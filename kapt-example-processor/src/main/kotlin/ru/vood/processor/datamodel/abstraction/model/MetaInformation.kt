package ru.vood.processor.datamodel.abstraction.model

import ru.vood.dmgen.annotation.FlowEntityType

data class MetaInformation(
    val collectMetaForeignKey: Set<MetaForeignKey>,
    val entities: Map<ModelClassName, MetaEntity>
) {

    val metaForeignKeys: Map<MetaEntity, List<MetaForeignKey>> = collectMetaForeignKey.groupBy { it.toEntity }
    fun fieldsFk() {
        val map1 = metaForeignKeys.entries
            .map { entry ->
                entry.key to entry.value
                    .filter { a -> a.fromEntity.flowEntity == FlowEntityType.INNER }
                    .map { metaFk -> metaFk.fromEntity to metaFk.fkCols }
            }
            .map { entry ->
                val toMetaEntity = entry.first
                val fromEntities = entry.second
                val map = fromEntities
                    .map { fromEnt ->
                        val fromMetaEntity = fromEnt.first
                        val fromEntityFkCols = fromEnt.second.map { qq -> qq.from.name }.toSet()
                        val fromEntityUKsCols = fromMetaEntity.uniqueKeysFields.keys.map { aas -> aas.cols }
                        val uksOneTOne = fromEntityUKsCols
                            .filter { ukCols -> ukCols.equalsAnyOrder(fromEntityFkCols) }


                        val relationType: String = if (uksOneTOne.size == 1) {
                            val metaForeignKeyMayBeCircle =
                                metaForeignKeys[fromMetaEntity]?.map { it.toEntity }?.filter { it == fromMetaEntity }
                                    ?.isNotEmpty()
                                    ?: false


                            val isOneToOneOptional = !metaForeignKeyMayBeCircle
                            val s = if (isOneToOneOptional) {
                                "isOneToOneOptional"
                            } else "OneToOne"
                            s
                        } else {
                            ""
                        }

                        fromMetaEntity
                    }



                toMetaEntity to ""
            }
            .toMap()




//        TODO("Not yet implemented")
    }


}

private inline fun <reified E> Set<E>.equalsAnyOrder(set: Set<E>): Boolean {
    return this.minus(set).isEmpty() && set.minus(this).isEmpty()
}

