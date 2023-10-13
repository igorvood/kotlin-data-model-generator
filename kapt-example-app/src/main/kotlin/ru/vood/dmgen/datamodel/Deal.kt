package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.Comment
import ru.vood.dmgen.annotation.FKType
import ru.vood.dmgen.annotation.FlowEntity
import ru.vood.dmgen.annotation.Pk

@Comment("Это сущность Сделка")
@FlowEntity(FKType.AGGREGATE)
//@Uk(["paramsList"])
abstract class Deal(
    @Pk
    @Comment("Это ее идентификатор")
    val id: Int,


//    val paramsSet: DealParam,
//    val paramsList: List<DealParam>
){


}
