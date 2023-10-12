package ru.vood.dmgen.intf

import kotlinx.serialization.KSerializer

interface IEntity<T: IEntity<T>> {

    fun ktSerializer() : KSerializer<T>
}