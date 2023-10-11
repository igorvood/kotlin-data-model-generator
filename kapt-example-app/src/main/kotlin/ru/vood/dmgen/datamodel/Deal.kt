package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.Comment
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk

@Comment("Это сущность Сделка")
@FlowEntity
//@Uk(["paramsList"])
interface Deal {
    @Pk
    @Comment("Это ее идентификатор")
    val id: Int


//    val paramsSet: DealParam,
//    val paramsList: List<DealParam>
}
