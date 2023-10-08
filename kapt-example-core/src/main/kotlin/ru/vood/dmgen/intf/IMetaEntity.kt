package ru.vood.dmgen.intf

import kotlin.reflect.KClass

interface IMetaEntity {
    val designClass: KClass<*>
}
