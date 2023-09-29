package ru.vood.processor.datamodel.abstraction.model

import ru.vood.processor.datamodel.abstraction.model.abstraction.mapKotlinType


data class PrimaryKetDataTypeDto(val javaDataType: String,
                                 val isScalarType: Boolean,
){


    val kotlinDataType: String = javaDataType.mapKotlinType()
}
