package ru.vood.dmgen.intf

interface IContextOf<ENT: IEntity<ENT>> {

    val metaEntity: IMetaEntity
}