package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk
import ru.vood.dmgen.annotation.Uk

@FlowEntity
//@Uk(["paramsList"])
data class Deal(
    @Pk
    val id: Int,

    val paramsSet: Set<DialParams>,
    val paramsList: List<DialParams>
)
