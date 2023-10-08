package ru.vood.dmgen.intf

interface IMetaFkEntity {
    val fromEntity: IMetaEntity
    val toEntity: IMetaEntity
    val uk: IMetaUkEntity

}
