package ru.vood.dmgen.annotation

import java.util.Arrays

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class ForeignKey(
//    val kClass: KClass<*>,
    val kClass: String,
    val name: String,
    val currentTypeCols: Array<String>,
    val outTypeCols: Array<String>,
    val cols: Array<ForeignKeyColumns> = []
)
