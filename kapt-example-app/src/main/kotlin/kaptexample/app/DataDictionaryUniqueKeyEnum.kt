package kaptexample.app
                        
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryColumnEntityEnum.*
import kotlin.reflect.KClass

enum class DataDictionaryUniqueKeyEnum(
    override val columns: Set<ru.vood.dmgen.intf.IMetaColumnEntity>,
    override val entity : ru.vood.dmgen.intf.IMetaEntity,
    override val contextOfClass: KClass<*>
): ru.vood.dmgen.intf.IMetaUkEntity{
DealParamOneToOneOptional_PK(
setOf(DealParamOneToOneOptional_dealId),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.DealParamOneToOneOptional,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.DealParamOneToOneOptionalContextDealParamOneToOneOptional_PK::class,
),
DealParamOneToOne_PK(
setOf(DealParamOneToOne_dealId),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.DealParamOneToOne,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.DealParamOneToOneContextDealParamOneToOne_PK::class,
),
DealParamSet_PK(
setOf(DealParamSet_dealId,DealParamSet_id),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.DealParamSet,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.DealParamSetContextDealParamSet_PK::class,
),
Deal_PK(
setOf(Deal_id),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Deal,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.DealContextDeal_PK::class,
),
ProductPayments_PK(
setOf(ProductPayments_dealId,ProductPayments_id,ProductPayments_productId),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.ProductPayments,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.ProductPaymentsContextProductPayments_PK::class,
),
Product_PK(
setOf(Product_dealId,Product_id),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Product,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.ProductContextProduct_PK::class,
),
Product_UK_1(
setOf(Product_dealId,Product_otherSystemProductId),
ru.vood.dmgen.datamodel.metaEnum.DataDictionaryEntityEnum.Product,
ru.vood.dmgen.datamodel.runtime.dataclasses.context.ProductContextProduct_UK_1::class,
)
}
