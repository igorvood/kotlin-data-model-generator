package ru.vood.processor.datamodel.abstraction.model

data class MetaForeignKey(
    val fromEntity: MetaEntity,
    val toEntity: MetaEntity,
    val fkCols: Set<FkCol>
)
