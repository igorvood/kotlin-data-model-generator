package ru.vood.processor.datamodel.abstraction.model

data class UkDto(
    val name: UkName,
    val cols: Set<ColumnName>
)

@JvmInline
value class ColumnName(val value: String)

@JvmInline
value class UkName(val value: String)