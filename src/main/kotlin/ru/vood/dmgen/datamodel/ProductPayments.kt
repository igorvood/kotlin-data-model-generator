package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk

@FlowEntity
@ForeignKey(Deal::class)
@ForeignKey(Product::class)
data class ProductPayments(
    @Pk
    val productId: String,
    @Pk
    val dealId: String,

    @Pk
    val id: String,

    val summa: Int

)
