package ru.vood

import kotlinx.serialization.Serializable
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.vood.dmgen.datamodel.Deal
import ru.vood.dmgen.datamodel.metaEnum.*


@SpringBootApplication
class Demo1123Application

fun main(args: Array<String>) {
    println(DataDictionaryEntityEnum.DealParam)
    println(DataDictionaryForeignKeyEnum.Product_FK_1)
    println(DataDictionaryUniqueKeyEnum.DealParam_PK)
    println(DataDictionaryColumnEntityEnum.DealParam_id)
    println("============================")
//
    Dependency.entityDependency.entries.forEach { println(it) }
    runApplication<Demo1123Application>(*args)
}


abstract sealed class EntityContext<ENTITY> {

//    inline fun <reified CONTEXT: EntityContext<ENTITY> > asdasd(context: (CONTEXT)->ENTITY): ENTITY {
//
//    }
}

@Serializable
data class DealParamEntityContext(val id: String) : EntityContext<Deal>()