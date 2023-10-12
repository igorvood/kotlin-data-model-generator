package ru.vood.dmgen.meta

import kotlinx.serialization.KSerializer
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum
import ru.vood.dmgen.intf.IEntity
import ru.vood.spring.fabric.JsonSerializer


val entitySerializers by lazy {
    DataDictionaryEntityEnum.values().associate { it.runtimeClass to it.serializer }
}


inline fun <reified T : IEntity<T>> toDto(data: String): IEntity<T> {
    val kSerializer1 = entitySerializers[T::class]!!.let { it as KSerializer<IEntity<T>> }
        .let { JsonSerializer.json.decodeFromString(it, data) }


    return kSerializer1
}

inline fun <reified T : IEntity<T>> T.toJson(): String {
    val kSerializer1 = entitySerializers[T::class]!!.let { it as KSerializer<IEntity<T>> }
        .let { JsonSerializer.json.encodeToString(it, this) }


    return kSerializer1
}




