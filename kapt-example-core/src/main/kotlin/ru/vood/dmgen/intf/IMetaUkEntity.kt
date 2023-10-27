package ru.vood.dmgen.intf

import kotlin.reflect.KClass

interface IMetaUkEntity {
    val columns: Set<IMetaColumnEntity>
    val entity: IMetaEntity
    val contextOfClass: KClass<*>
}
