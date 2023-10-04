package ru.vood.dmgen.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
annotation class ForeignKey(
//    val kClass: KClass<*>,
    val kClass: String,
    val currentTypeCols: Array<String>,
    val outTypeCols: Array<String>,
)
