package ru.vood.dmgen.intf

interface IMetaUkEntity {
    val columns: Set<IMetaColumnEntity>
    val entity: IMetaEntity
}
