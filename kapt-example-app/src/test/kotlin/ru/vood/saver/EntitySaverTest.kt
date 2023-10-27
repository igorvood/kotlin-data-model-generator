package ru.vood.saver

import org.junit.jupiter.api.Test
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealEntity
import ru.vood.dmgen.datamodel.runtime.dataclasses.DealParamOneToOneOptionalEntity
import ru.vood.dmgen.datamodel.runtime.dataclasses.ParamOnParamEntity

internal class EntitySaverTest {

    @Test
    fun asd() {
        EntitySaver.save(
            DealEntity(
                id = 1,

                dealParamOneToOneOptional = DealParamOneToOneOptionalEntity(
                    dealId = 1,
                    paramDate = "asdasd",
                    paramOnParam = ParamOnParamEntity(1, "asd")
                ),

                dealParamSet = setOf()
            )
        )
//        val kProperty1 = DealParamOneToOneEntity::deal
    }
}