package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk

@FlowEntity
//@Uk(["paramsList"])
data class Deal(
    @Pk
    val id: Int,

    val paramsSet: DialParam,
    val paramsList: List<DialParam>
)
