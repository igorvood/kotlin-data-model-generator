package ru.vood.processor.datamodel.abstraction.model.gen.dto

data class GeneratedFile(
    val fileName: FileName,
    val generatedCode: GeneratedCode,
)


@JvmInline
value class FileName(val value: String)

@JvmInline
value class GeneratedCode(val value: String)

@JvmInline
value class PackageName(val value: String)
