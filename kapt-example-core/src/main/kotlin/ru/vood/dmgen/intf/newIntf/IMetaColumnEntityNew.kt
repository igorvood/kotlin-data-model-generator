package ru.vood.dmgen.intf.newIntf

import ru.vood.dmgen.intf.ColumnName
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IEntity
import kotlin.reflect.KProperty1

interface IMetaColumnEntity<T : IEntity<out T>> {
    val entity: EntityName
    val kProperty1: KProperty1<T, *>
    val columnName: ColumnName
    val comment: String
}

data class ColumnEntityData<T : IEntity<out T>>(
    override val entity: EntityName,
    override val kProperty1: KProperty1<T, *>,
    override val columnName: ColumnName,
    override val comment: String
) : IMetaColumnEntity<T>