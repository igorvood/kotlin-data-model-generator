package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.ForeignKey
import ru.vood.dmgen.annotation.ForeignKeyColumns
import ru.vood.dmgen.annotation.Pk
import java.time.LocalDateTime

@FlowEntity
@ForeignKey(
    kClass = "ru.vood.dmgen.datamodel.Deal",
    "DealParamOneToOne_deal_FK",
    cols = [ForeignKeyColumns("dealId", "id")]
)
data class DealParamOneToOne(
    @Pk
    val dealId: Int,
    val paramDate: String
    )

@FlowEntity
@ForeignKey(
    kClass = "ru.vood.dmgen.datamodel.Deal",
    "DealParamSet_deal_FK",
    cols = [ForeignKeyColumns("dealId", "id")]
)
data class DealParamSet(
    @Pk
    val dealId: Int,
    @Pk
    val id: Int,

    val paramDate: String
)
