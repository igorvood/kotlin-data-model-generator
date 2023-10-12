package ru.vood.dmgen.meta

import kotlinx.serialization.KSerializer
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum
import ru.vood.dmgen.intf.IEntity
import ru.vood.spring.fabric.JsonSerializer
import kotlin.reflect.KClass


val entitySerializers: Map<KClass<out IEntity<*>>, KSerializer<out IEntity<*>>> =
    DataDictionaryEntityEnum.values().associate { it.runtimeClass to it.serializer }

@Suppress("UNCHECKED_CAST")
inline fun <reified T : IEntity<T>> toDto(data: String): IEntity<T> =
    entitySerializers[T::class]!!.let { it as KSerializer<IEntity<T>> }
        .let { JsonSerializer.json.decodeFromString(it, data) }


@Suppress("UNCHECKED_CAST")
inline fun <reified T : IEntity<T>> T.toJson(): String =
    entitySerializers[T::class]!!.let { it as KSerializer<IEntity<T>> }
        .let { JsonSerializer.json.encodeToString(it, this) }




