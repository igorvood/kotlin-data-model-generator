package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk

@FlowEntity
@ForeignKey(
//    kClass = Deal::class,
    kClass = "ru.vood.dmgen.datamodel.Deal",
    currentTypeCols = ["dealId"],
    outTypeCols = ["id"]
)
@ForeignKey(
//    Product::class,
    kClass = "ru.vood.dmgen.datamodel.Product",
    currentTypeCols = ["dealId", "productId"],
    outTypeCols = ["dealId", "id"]
)
data class ProductPayments(
    @Pk
    val productId: String,
    @Pk
    val dealId: Int,

    @Pk
    val id: String,

    val summa: Long

)