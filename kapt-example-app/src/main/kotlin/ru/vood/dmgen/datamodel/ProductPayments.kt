package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk

@FlowEntity
@ForeignKey(
//    kClass = Deal::class,
    kClass = "ru.vood.dmgen.datamodel.Deal",
    "ProductPayments_FK_1",
    cols = [ru.vood.dmgen.annotation.ForeignKeyColumns("dealId", "id")]
)
@ForeignKey(
//    Product::class,
    kClass = "ru.vood.dmgen.datamodel.Product",
    "ProductPayments_FK_2",
    cols = [ru.vood.dmgen.annotation.ForeignKeyColumns("dealId", "dealId"),
        ru.vood.dmgen.annotation.ForeignKeyColumns("productId", "id")
    ]
)
//@Uk("Product_UK_3",["summa"])
interface ProductPayments {
    @Pk
    val productId: String

    @Pk
    val dealId: Int

    @Pk
    val id: String

    val summa: Long

}