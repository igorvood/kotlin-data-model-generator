package ru.vood.dmgen.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ForeignKeyColumns(
    val currentTypeCol: String,
    val outTypeCol: String,
)
