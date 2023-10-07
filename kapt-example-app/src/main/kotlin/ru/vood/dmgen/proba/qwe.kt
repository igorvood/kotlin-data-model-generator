package ru.vood.dmgen.proba

import kotlinx.serialization.Serializable
import ru.vood.datamodel.meta.enums.DataDictionaryColumnEntityEnum
import ru.vood.datamodel.meta.enums.DataDictionaryEntityEnum
import ru.vood.datamodel.meta.enums.DataDictionaryForeignKeyEnum
import ru.vood.datamodel.meta.enums.DataDictionaryUniqueKeyEnum


fun main() {
    println(DataDictionaryEntityEnum.DealParam)
    println(DataDictionaryForeignKeyEnum.Product_FK_1)
    println(DataDictionaryUniqueKeyEnum.DealParam_PK)
    println(DataDictionaryColumnEntityEnum.DealParam_id)




//    val asd : Map<DataDictionaryEntityEnum, Set<MetaDependency>> = collectDependency( DataDictionaryForeignKeyEnum.values())
//
//    println(asd)

}

//fun collectDependency(values: Array<DataDictionaryForeignKeyEnum>): Map<DataDictionaryEntityEnum, Set<MetaDependency>> {
//
//    fun recursiveCollectDependency(values: Array<DataDictionaryForeignKeyEnum>, collector: Map<DataDictionaryEntityEnum, Set<MetaDependency>>): Map<DataDictionaryEntityEnum, Set<MetaDependency>>{
//      return  when(values.isEmpty()){
//            true -> collector
//            false -> {
//                val dataDictionaryForeignKeyEnum = values[0]
//                val let = collector[dataDictionaryForeignKeyEnum.fromEntity]?.let { metaDependencies ->
//                    metaDependencies.plus(MetaDependency(dataDictionaryForeignKeyEnum.toEntity))
//                } ?: setOf(MetaDependency(dataDictionaryForeignKeyEnum.toEntity))
//
//                val plus = collector.plus(dataDictionaryForeignKeyEnum.fromEntity to let)
//
//                val values1 = values.drop(1).toTypedArray()
//                recursiveCollectDependency(values1, plus)
//
//            }
//        }
//
//
//    }
//
//
//    return recursiveCollectDependency(values, mapOf())
//}


abstract sealed class EntityContext<ENTITY>{

//    inline fun <reified CONTEXT: EntityContext<ENTITY> > asdasd(context: (CONTEXT)->ENTITY): ENTITY {
//
//    }
}
@Serializable
data class DealParamEntityContext(val id: String): EntityContext<ru.vood.dmgen.datamodel.Deal>()