package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.Pk
import ru.vood.dmgen.annotation.Uk

@FlowEntity
@ForeignKey(
    kClass = Deal::class,
    currentTypeCols = ["dealId"],
    outTypeCols = ["id"]
)
@Uk(["otherSystemProductId", "dealId"])
@Uk(["dealId", "id"])
data class Product(
    @Pk
    val id: String,
    @Pk
    val dealId: Int,

    val otherSystemProductId: String,

    val productName: String,
)
