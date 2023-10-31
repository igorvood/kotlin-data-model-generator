package ru.vood.dmgen.intf

import kotlinx.serialization.KSerializer



interface Serializer<T>{

    fun ktSerializer() : KSerializer<out T>
}

interface IEntity<T: IEntity<T>>: Serializer<T>
interface IAggregate<T: IAggregate<T>>: IEntity<T>