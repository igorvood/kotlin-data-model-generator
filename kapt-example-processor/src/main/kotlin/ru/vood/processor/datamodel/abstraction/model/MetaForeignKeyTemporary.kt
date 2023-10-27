package ru.vood.processor.datamodel.abstraction.model

data class MetaForeignKeyTemporary(
    val name: ForeignKeyName,
    val fromEntity: MetaEntity,
    val toEntity: MetaEntity,
    val fkCols: Set<FkCol>,
    val uk: UkDto
)

data class MetaForeignKey(
    val name: ForeignKeyName,
    val fromEntity: MetaEntity,
    val toEntity: MetaEntity,
    val fkCols: Set<FkCol>,
    val uk: UkDto,
    val relationType: RelationType
) {
    constructor(
        metaForeignKeyTemporary: MetaForeignKeyTemporary,
        relationType: RelationType
    ) : this(
        metaForeignKeyTemporary.name,
        metaForeignKeyTemporary.fromEntity,
        metaForeignKeyTemporary.toEntity,
        metaForeignKeyTemporary.fkCols,
        metaForeignKeyTemporary.uk,
        relationType
    )

}

@JvmInline
value class ForeignKeyName(val value: String)

enum class RelationType(val mandatory: Boolean) {
    ONE_TO_MANY(true),
    UNNOWN(true),
    ONE_TO_ONE_MANDATORY(true),
    ONE_TO_ONE_OPTIONAL(false);


}
