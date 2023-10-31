package ru.vood.dmgen.intf

interface IContextOf<ENT : IEntity<ENT>> : Serializer<IContextOf<ENT>> {

    val metaEntity: IMetaEntity
}