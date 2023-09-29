package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk

@FlowEntity
@ForeignKey(
    kClass = Deal::class,
    currentTypeCols = ["dealId"],
    outTypeCols = ["id"]
)
data class Product(
    @Pk
    val id: String,
    @Pk
    val dealId: Int,

    val productName: String,
)
