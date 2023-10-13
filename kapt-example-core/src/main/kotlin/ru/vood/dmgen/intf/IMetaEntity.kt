package ru.vood.dmgen.intf

import kotlinx.serialization.KSerializer
import ru.vood.dmgen.annotation.FlowEntityType
import kotlin.reflect.KClass

interface IMetaEntity {
    val designClass: KClass<*>
    val runtimeClass: KClass<*>
    val entityName: EntityName
    val comment: String
    val serializer: KSerializer<*>
    val entityType: FlowEntityType

}

@JvmInline
value class EntityName(val value: String)
