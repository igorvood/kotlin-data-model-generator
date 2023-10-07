package ru.vood.dmgen.proba

import ru.vood.datamodel.meta.enums.DataDictionaryColumnEntityEnum
import ru.vood.datamodel.meta.enums.DataDictionaryEntityEnum
import ru.vood.datamodel.meta.enums.DataDictionaryForeignKeyEnum
import ru.vood.datamodel.meta.enums.DataDictionaryUniqueKeyEnum


fun asd(){
    println(DataDictionaryEntityEnum.DealParam)
    println(DataDictionaryForeignKeyEnum.Product_FK_1)
    println(DataDictionaryUniqueKeyEnum.DealParam_PK)
    println(DataDictionaryColumnEntityEnum.DealParam_id)
}