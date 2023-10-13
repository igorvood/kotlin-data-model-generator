package ru.vood.dmgen.annotation

@Target( AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FlowEntity(
    val entityType: FKType = FKType.INNER,
)
enum class FKType{
    INNER, AGGREGATE
}
