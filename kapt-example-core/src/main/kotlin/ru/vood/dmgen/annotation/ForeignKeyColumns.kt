package ru.vood.dmgen.annotation

@Target()
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
@MustBeDocumented
annotation class ForeignKeyColumns(
    val currentTypeCol: String,
    val outTypeCol: String,
)
