package ru.vood.dmgen.annotation

enum class RelationType(val mandatory: Boolean) {
    ONE_TO_MANY(true),
    UNNOWN(true),
    ONE_TO_ONE_MANDATORY(true),
    ONE_TO_ONE_OPTIONAL(false);


}