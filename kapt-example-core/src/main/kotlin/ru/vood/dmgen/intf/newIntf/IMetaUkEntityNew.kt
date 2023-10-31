package ru.vood.dmgen.intf.newIntf

import ru.vood.dmgen.intf.ColumnName
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IContextOf
import ru.vood.dmgen.intf.IEntity
import kotlin.reflect.KClass

interface IMetaUkEntityNew<T : IEntity<out T>> {
    val columns: Set<ColumnName>
    val entity: EntityName
    val extractContext: (T) -> IContextOf<T>
//    val contextOfClass: KClass<IContextOf<out T>>
}

data class UKEntityData<T : IEntity<out T>>(
    override val columns: Set<ColumnName>,
    override val entity: EntityName,
    override val extractContext: (T) -> IContextOf<T>
//    override val contextOfClass: KClass<IContextOf<out T>>
) : IMetaUkEntityNew<T> {
}