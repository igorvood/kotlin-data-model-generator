package ru.vood.processor.datamodel.abstraction.model

data class UkDto(
    val cols: Set<ColumnName>
)

@JvmInline
value class ColumnName(val value: String)