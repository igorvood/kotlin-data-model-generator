package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.*

@Comment("Это сущность Сделка")
@FlowEntity(FlowEntityType.AGGREGATE)
//@ForeignKey(
//    kClass = "ru.vood.dmgen.datamodel.DealParamOneToOne",
//    "Deal_2_DealParamOneToOne_FK",
//    cols = [ForeignKeyColumns("id", "dealId")]
//)

//@Uk(["paramsList"])
abstract class Deal(
    @Pk
    @Comment("Это ее идентификатор")
    val id: Int,


//    val paramsSet: DealParam,
//    val paramsList: List<DealParam>
) {


}
