package ru.vood

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.vood.dmgen.annotation.RelationType
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryForeignKeyEnum

internal class Demo1123ApplicationTest {

    @Test
    fun relationType() {
        Assertions.assertEquals(7, DataDictionaryForeignKeyEnum.values().size)
        DataDictionaryForeignKeyEnum.values().forEach {
            val relationType = when (it) {
                DataDictionaryForeignKeyEnum.DealParamOneToOneOptional_Deal_FK -> RelationType.ONE_TO_ONE_OPTIONAL
                DataDictionaryForeignKeyEnum.DealParamOneToOne_Deal_FK -> RelationType.ONE_TO_ONE_MANDATORY
                DataDictionaryForeignKeyEnum.Deal_2_DealParamOneToOne_FK -> RelationType.ONE_TO_ONE_MANDATORY

                DataDictionaryForeignKeyEnum.DealParamSet_Deal_FK -> RelationType.UNNOWN
                DataDictionaryForeignKeyEnum.ProductPayments_FK_1 -> RelationType.UNNOWN
                DataDictionaryForeignKeyEnum.Product_FK_1 -> RelationType.UNNOWN
                DataDictionaryForeignKeyEnum.ProductPayments_FK_2 -> RelationType.UNNOWN

            }

            Assertions.assertEquals(relationType, it.relationType){"For enum ${it.name} wrong relationType"}
        }

    }
}