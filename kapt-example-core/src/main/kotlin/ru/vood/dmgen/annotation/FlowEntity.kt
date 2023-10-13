package ru.vood.dmgen.annotation

@Target( AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FlowEntity(
    val entityType: FlowEntityType = FlowEntityType.INNER,
)
enum class FlowEntityType{
    INNER, AGGREGATE
}
