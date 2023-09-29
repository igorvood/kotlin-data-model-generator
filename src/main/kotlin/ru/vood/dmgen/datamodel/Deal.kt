package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk

@FlowEntity
data class Deal(

    @Pk
    val id: String
)
