package ru.vood

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.vood.dmgen.annotation.RelationType
import ru.vood.dmgen.datamodel.metaEnum.DataDictionaryForeignKeyEnum
import ru.vood.dmgen.datamodel.metaEnum.entityDataMap
import ru.vood.dmgen.datamodel.metaEnum.uniqueKeyMap

internal class Demo1123ApplicationTest2 {

    @Test
    fun relationType() {

        val map1 = entityDataMap.values.map { entity ->
            entity.entityName.value to  uniqueKeyMap.entries.filter { uk -> uk.value.entity == entity.entityName }.map { uk->uk.key.value }.joinToString(",")
        }.map { it.first+"->"+it.second }
            .joinToString("\n")
        println(map1)



    }
}