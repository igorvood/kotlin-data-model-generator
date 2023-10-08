package ru.vood.dmgen.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class Comment(
    val comment: String,
)
