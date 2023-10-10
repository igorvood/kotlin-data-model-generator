package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk
import ru.vood.dmgen.annotation.Uk

@FlowEntity
@ForeignKey(
//    kClass = Deal::class,
    kClass = "ru.vood.dmgen.datamodel.Deal",
    "ProductPayments_FK_1",
    currentTypeCols = ["dealId"],
    outTypeCols = ["id"]
)
@ForeignKey(
//    Product::class,
    kClass = "ru.vood.dmgen.datamodel.Product",
    "ProductPayments_FK_2",
    currentTypeCols = ["dealId", "productId"],
    outTypeCols = ["dealId", "id"]
)
//@Uk("Product_UK_3",["summa"])
data class ProductPayments(
    @Pk
    val productId: String,
    @Pk
    val dealId: Int,

    @Pk
    val id: String?,

    val summa: Long

)