package ru.vood.dmgen.proba

import kotlinx.serialization.Serializable
import ru.vood.datamodel.meta.enums.*


fun main() {
    println(DataDictionaryEntityEnum.DealParam)
    println(DataDictionaryForeignKeyEnum.Product_FK_1)
    println(DataDictionaryUniqueKeyEnum.DealParam_PK)
    println(DataDictionaryColumnEntityEnum.DealParam_id)
    println("============================")
//
    Dependency.entityDependency.entries.forEach { println(it) }

}


abstract sealed class EntityContext<ENTITY>{

//    inline fun <reified CONTEXT: EntityContext<ENTITY> > asdasd(context: (CONTEXT)->ENTITY): ENTITY {
//
//    }
}
@Serializable
data class DealParamEntityContext(val id: String): EntityContext<ru.vood.dmgen.datamodel.Deal>()