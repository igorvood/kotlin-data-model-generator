package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk
import java.time.LocalDateTime

@FlowEntity
data class DealParam(
    @Pk
    val id: Int,
    val paramDate: String
    )
