package ru.vood.dmgen.intf

interface IContextOf<ENT : IEntity<out ENT>> : Serializer<IContextOf<ENT>> {

    val metaEntity: IMetaEntity
}