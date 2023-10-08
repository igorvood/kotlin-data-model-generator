package ru.vood.dmgen.intf

import kotlin.reflect.KClass

interface IMetaEntity {
    val designClass: KClass<*>
    val entityName: EntityName
    val comment: String
}

@JvmInline
value class EntityName(val value: String)
