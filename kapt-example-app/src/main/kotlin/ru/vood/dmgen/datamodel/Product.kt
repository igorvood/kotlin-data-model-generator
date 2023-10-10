package ru.vood.dmgen.datamodel

import ru.vood.dmgen.annotation.*
import ru.vood.dmgen.datamodel.s.D
import java.math.BigDecimal
import java.time.Instant

@FlowEntity
@ForeignKey(
//    kClass = Deal::class.java.canonicalName,

    kClass = "ru.vood.dmgen.datamodel.Deal",
    "Product_FK_1",
    currentTypeCols = ["dealId"],
    outTypeCols = ["id"],
    cols = [ForeignKeyColumns("dealId", "id")]
)
@Uk("Product_UK_1",
    ["otherSystemProductId", "dealId"])
//@Uk("Product_UK_2",["dealId", "id"])
//@Uk("Product_UK_3",["l", "d", "f", "bd", "b", "t"])
data class Product(
    @Pk
    val id: String,
    @Pk
    val dealId: Int,

    val otherSystemProductId: String,

    val productName: String,

    val inn : Int?,

    val D: D,
    val Dn: D?,

    val ln: Long?,
    val dn: Double?,
    val fn: Float?,
//    val bd: BigDecimal,
    val bn: Boolean?,
//    val t: Instant,


    val l: Long,
    val d: Double,
    val f: Float,
//    val bd: BigDecimal,
    val b: Boolean,
//    val t: Instant,
)
