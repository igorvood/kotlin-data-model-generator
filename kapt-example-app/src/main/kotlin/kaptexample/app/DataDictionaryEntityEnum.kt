package kaptexample.app

import kotlinx.serialization.KSerializer
import ru.vood.dmgen.annotation.FlowEntityType
import ru.vood.dmgen.annotation.FlowEntityType.*
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IEntity
import kotlin.reflect.KClass


interface IMetaEntityNew<T : IEntity<out T>> {
    val designClass: KClass<out Any>
    val runtimeClass: KClass<out T>
    val entityName: EntityName
    val comment: String
    val serializer: KSerializer<out IEntity<out T>>
    val entityType: FlowEntityType

}

data class EntityData<T : IEntity<out T>>(
    override val designClass: KClass<out Any>,
    override val runtimeClass: KClass<out T>,
    override val serializer: KSerializer<out IEntity<out T>>,
    override val entityName: EntityName,
    override val comment: String,
    override val entityType: FlowEntityType
) : IMetaEntityNew<T>


//val runtimeClass1 = ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity::class as IEntity<out DealEntity>
val asdqqq = mapOf(
    ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Deal to EntityData(
        ru.vood.dmgen.datamodel.Deal::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity.serializer(),
        EntityName("Deal"),
        "Это сущность Сделка",
        AGGREGATE
    ),
    ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.DealParamOneToOne to EntityData(
        ru.vood.dmgen.datamodel.DealParamOneToOne::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity.serializer(),
        EntityName("DealParamOneToOne"),
        "null",
        INNER_MANDATORY
    )
)


enum class DataDictionaryEntityEnum(
    override val designClass: KClass<out Any>,
    override val runtimeClass: KClass<out ru.vood.dmgen.intf.IEntity<*>>,
    override val serializer: KSerializer<out ru.vood.dmgen.intf.IEntity<*>>,
    override val entityName: EntityName,
    override val comment: String,
    override val entityType: FlowEntityType

) : ru.vood.dmgen.intf.IMetaEntity {
    Deal(
        ru.vood.dmgen.datamodel.Deal::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity.serializer(),
        EntityName("Deal"),
        "Это сущность Сделка",
        AGGREGATE
    ),
    DealParamOneToOne(
        ru.vood.dmgen.datamodel.DealParamOneToOne::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity.serializer(),
        EntityName("DealParamOneToOne"),
        "null",
        INNER_MANDATORY
    ),
    DealParamOneToOneOptional(
        ru.vood.dmgen.datamodel.DealParamOneToOneOptional::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity.serializer(),
        EntityName("DealParamOneToOneOptional"),
        "null",
        INNER_OPTIONAL
    ),
    DealParamSet(
        ru.vood.dmgen.datamodel.DealParamSet::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamSetEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamSetEntity.serializer(),
        EntityName("DealParamSet"),
        "null",
        INNER_OPTIONAL
    ),
    Product(
        ru.vood.dmgen.datamodel.Product::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.ProductEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.ProductEntity.serializer(),
        EntityName("Product"),
        "null",
        AGGREGATE
    ),
    ProductPayments(
        ru.vood.dmgen.datamodel.ProductPayments::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.ProductPaymentsEntity::class,
        ru.vood.dmgen.datamodel.runtime.dataclasses.ProductPaymentsEntity.serializer(),
        EntityName("ProductPayments"),
        "null",
        AGGREGATE
    );

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> entitySerializer(): KSerializer<T> = this.serializer as KSerializer<T>
}
