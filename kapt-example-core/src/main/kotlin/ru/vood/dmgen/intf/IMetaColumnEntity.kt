package ru.vood.dmgen.intf

import kotlin.reflect.KProperty1

interface IMetaColumnEntity {
    val entity: IMetaEntity
    val kProperty1: KProperty1<*, *>
}
