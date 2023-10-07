package ru.vood.processor.datamodel.abstraction.model

data class MetaForeignKey(
    val name: ForeignKeyName,
    val fromEntity: MetaEntity,
    val toEntity: MetaEntity,
    val fkCols: Set<FkCol>
)

@JvmInline
value class ForeignKeyName(val value: String)
