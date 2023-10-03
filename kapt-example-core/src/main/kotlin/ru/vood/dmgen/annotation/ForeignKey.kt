package ru.vood.dmgen.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ForeignKey(
    val kClass: KClass<*>,
    val currentTypeCols: Array<String>,
    val outTypeCols: Array<String>,
)