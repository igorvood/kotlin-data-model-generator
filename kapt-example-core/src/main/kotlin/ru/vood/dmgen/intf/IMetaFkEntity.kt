package ru.vood.dmgen.intf

import ru.vood.dmgen.annotation.RelationType

interface IMetaFkEntity {
    val fromEntity: IMetaEntity
    val toEntity: IMetaEntity
    val uk: IMetaUkEntity
    val relationType: RelationType

}
