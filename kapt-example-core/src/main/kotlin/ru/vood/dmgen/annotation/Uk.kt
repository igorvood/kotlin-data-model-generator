package ru.vood.dmgen.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class Uk(
    val name: String,
    val cols: Array<String>,
)
