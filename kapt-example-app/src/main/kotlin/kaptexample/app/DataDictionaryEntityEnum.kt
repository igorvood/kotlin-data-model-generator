package kaptexample.app
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import ru.vood.dmgen.annotation.FlowEntityType.*
import ru.vood.dmgen.annotation.FlowEntityType
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.intf.EntityName
import ru.vood.dmgen.intf.IEntity
import ru.vood.dmgen.intf.IMetaEntity


interface IMetaEntityNew {
    val designClass: KClass<*>
    val runtimeClass: KClass<*>
    val entityName: EntityName
    val comment: String
    val serializer: KSerializer<*>
    val entityType: FlowEntityType

}

data class EntityData< T: IEntity<out T>>(
    override val designClass: KClass<out Any>,
    override val runtimeClass: KClass<ru.vood.dmgen.intf.IEntity<out T>>,
    override val serializer: KSerializer<out ru.vood.dmgen.intf.IEntity<out T>>,
    override val entityName: ru.vood.dmgen.intf.EntityName,
    override val comment: String,
    override val entityType: FlowEntityType
): IMetaEntityNew


//val runtimeClass1 = ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity::class as IEntity<out DealEntity>
//val asdqqq: Map<ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum, EntityData<DealEntity>> = mapOf(ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Deal to EntityData(
//    ru.vood.dmgen.datamodel.Deal::class,
//    runtimeClass1,
//    ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity.serializer(),
//    ru.vood.dmgen.intf.EntityName("Deal"),
//    "Это сущность Сделка",
//    AGGREGATE
//))



enum class DataDictionaryEntityEnum(
override val designClass: KClass<out Any>,
override val runtimeClass: KClass<out ru.vood.dmgen.intf.IEntity<*>>,
override val serializer: KSerializer<out ru.vood.dmgen.intf.IEntity<*>>,
override val entityName: ru.vood.dmgen.intf.EntityName,
override val comment: String,
override val entityType: FlowEntityType

): ru.vood.dmgen.intf.IMetaEntity {
Deal(ru.vood.dmgen.datamodel.Deal::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity.serializer(),
ru.vood.dmgen.intf.EntityName("Deal"), 
"Это сущность Сделка",
AGGREGATE
),
DealParamOneToOne(ru.vood.dmgen.datamodel.DealParamOneToOne::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneEntity.serializer(),
ru.vood.dmgen.intf.EntityName("DealParamOneToOne"), 
"null",
INNER_MANDATORY
),
DealParamOneToOneOptional(ru.vood.dmgen.datamodel.DealParamOneToOneOptional::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity.serializer(),
ru.vood.dmgen.intf.EntityName("DealParamOneToOneOptional"), 
"null",
INNER_OPTIONAL
),
DealParamSet(ru.vood.dmgen.datamodel.DealParamSet::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamSetEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamSetEntity.serializer(),
ru.vood.dmgen.intf.EntityName("DealParamSet"), 
"null",
INNER_OPTIONAL
),
Product(ru.vood.dmgen.datamodel.Product::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.ProductEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.ProductEntity.serializer(),
ru.vood.dmgen.intf.EntityName("Product"), 
"null",
AGGREGATE
),
ProductPayments(ru.vood.dmgen.datamodel.ProductPayments::class, 
ru.vood.dmgen.datamodel.runtime.dataclasses.ProductPaymentsEntity::class,
ru.vood.dmgen.datamodel.runtime.dataclasses.ProductPaymentsEntity.serializer(),
ru.vood.dmgen.intf.EntityName("ProductPayments"), 
"null",
AGGREGATE
);

@Suppress("UNCHECKED_CAST")
inline fun <reified T> entitySerializer(): KSerializer<T> = this.serializer as KSerializer<T> 
}
