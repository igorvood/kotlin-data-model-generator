package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk

@FlowEntity
interface DealParam {
    @Pk
    val id: Int
    val paramDate: String
}
