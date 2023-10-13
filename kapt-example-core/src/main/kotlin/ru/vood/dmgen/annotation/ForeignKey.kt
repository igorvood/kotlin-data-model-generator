package ru.vood.dmgen.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
@MustBeDocumented
annotation class ForeignKey(
//    val kClass: KClass<*>,
    val kClass: String,
    val name: String,
    val cols: Array<ForeignKeyColumns> = []
)
