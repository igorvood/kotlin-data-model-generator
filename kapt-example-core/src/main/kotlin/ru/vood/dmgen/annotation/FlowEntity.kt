package ru.vood.dmgen.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class FlowEntity(
    val entityType: FlowEntityType = FlowEntityType.INNER_OPTIONAL,
)

enum class FlowEntityType(val isOptional: Boolean) {
    INNER_OPTIONAL(true),
    INNER_MANDATORY(false),
    AGGREGATE(true)
}
